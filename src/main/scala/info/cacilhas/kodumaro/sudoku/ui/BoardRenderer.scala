package info.cacilhas.kodumaro.sudoku.ui

import java.awt.{Color, Graphics2D}
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}

import concurrent.{Future, blocking}
import concurrent.ExecutionContext.Implicits._
import util.Try

trait BoardRenderer {
  this: BoardCanvas ⇒

  private lazy val colours = Seq(
    theme.bg,
    new Color(0xe00000), new Color(0xe08000), new Color(0xe0e000),
    new Color(0x00e000), new Color(0x0008e0), new Color(0x4b0082),
    new Color(0x9932cc), new Color(0xe000e0), new Color(0xc0c0c0),
  )
  private lazy val backgrounds = Seq(new Color(0x808080), new Color(0x606060))

  def start(): Unit = renderer start ()

  def renderBoard(): Unit = renderer.changed = true

  private object renderer {

    private val mutex = new Semaphore(1)
    private val counter = new AtomicInteger(0)
    private val _changed = new AtomicBoolean(true)
    private val ticksBeforeStop = 2 // WORKAROUND

    def changed: Boolean = _changed.get
    def changed_=(value: Boolean): Unit = _changed set value

    def start(): Unit = {
      if (mutex.tryAcquire) Future(blocking {
        changed = true
        try while (board != null) {
          if (window.peer.getKeyListeners.isEmpty)
            window.peer addKeyListener player.keyListener
          if (changed) render()
          Thread sleep 20 // almost 50 fps
        } finally mutex release ()
      })
    }

    def render(): Unit = Try {
      getBufferStrategy match {
        case null ⇒ createBufferStrategy(3)

        case strategy ⇒
          if (player.tryAcquire) try {
            val g = strategy.getDrawGraphics.asInstanceOf[Graphics2D]
            paint(g) // clear buffer
            drawBackground(g)
            drawCircles(g)
            drawPlayer(g)
            g dispose()
            strategy show()
            counter set (counter.get + 1) % ticksBeforeStop
            if (counter.get == 0) changed = false
          } finally player release ()
      }
    }

    private def bgColor: Color = theme.bg

    private def drawPlayer(g: Graphics2D): Unit = {
      g setColor Color.white
      g drawRect(player.x*80, player.y*80, 80, 80)
    }

    private def drawBackground(g: Graphics2D): Unit =
      for (y ← 0 until 9; x ← 0 until 9) {
        g setColor backgrounds((x / 3 + y / 3) % 2)
        g fillRect(x * 80, y * 80, 80, 80)
        g setColor bgColor
        g drawRect(x * 80, y * 80, 80, 80)
      }

    private def drawCircles(g: Graphics2D): Unit = if (board != null) {
      val sphere = new Sphere(g)
      for (y ← 0 until 9; x ← 0 until 9) board(x, y) match {
        case Some(cell) if cell? ⇒
          sphere draw(x * 80 + 1, y * 80 + 1, 78, colours(cell.value))

        case Some(cell) ⇒
          for (iy ← 0 until 3; ix ← 0 until 3; i = 1 + ix + (2 - iy) * 3)
            if (cell(i))
              sphere draw(x * 80 + ix * 26 + 1, y * 80 + iy * 26 + 1, 26, colours(i))

        case None ⇒ //
      }
    }
  }
}
