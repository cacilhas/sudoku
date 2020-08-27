package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.{Color, Graphics}
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger

import info.cacilhas.kodumaro.sudoku.ui.{Player, Sphere}

import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.swing.Graphics2D
import scala.util.Try

trait RendererMixin {
  window: Window ⇒

  private val player = new Player(this)

  protected object renderer {

    import ExecutionContext.Implicits.global

    val yOffset: Int = 64
    private val mutex = new Semaphore(1)
    private val counter = new AtomicInteger(0)
    private val ticksBeforeStop = 2 // WORKAROUND
    private lazy val backgrounds = Seq(new Color(0x808080), new Color(0x606060))
    private lazy val colours = Seq(
      theme.bg,
      new Color(0xe00000), new Color(0xe08000), new Color(0xe0e000),
      new Color(0x00e000), new Color(0x0008e0), new Color(0x4b0082),
      new Color(0x9932cc), new Color(0xe000e0), new Color(0xc0c0c0),
      )

    def start(): Unit = {
      if (mutex.tryAcquire) Future(blocking {
        mustRender set true
        try while (board.isDefined) {
          if (peer.getKeyListeners.isEmpty)
            peer addKeyListener player.keyListener
          if (mustRender.get) render()
          Thread sleep 20 // almost 50 fps
        } finally mutex release ()
      })
    }

    def render(): Unit = Try {
      peer.getBufferStrategy match {
        case null ⇒ window.peer.createBufferStrategy(3)

        case strategy ⇒
          if (player.tryAcquire) try {
            val g = strategy.getDrawGraphics.asInstanceOf[Graphics2D]
            draw(g)
            g dispose()
            strategy show()
            counter set (counter.get + 1) % ticksBeforeStop
            if (counter.get == 0) mustRender set false
          } finally player release ()
      }
    }

    private def draw(g: Graphics): Unit = {
      peer paint g
      val g2d = g.asInstanceOf[Graphics2D]
      drawBackground(g2d)
      drawCircles(g2d)
      drawPlayer(g2d)
    }

    private def drawPlayer(g: Graphics2D): Unit = {
      g setColor Color.white
      g drawRect(player.x*80, player.y*80 + yOffset, 80, 80)
    }

    private def drawBackground(g: Graphics2D): Unit =
      for (y ← 0 until 9; x ← 0 until 9) {
        g setColor backgrounds((x / 3 + y / 3) % 2)
        g fillRect(x*80, y*80 + yOffset, 80, 80)
        g setColor theme.bg
        g drawRect(x*80, y*80 + yOffset, 80, 80)
      }

    private def drawCircles(g: Graphics2D): Unit = board foreach { board ⇒
      val sphere = new Sphere(g)
      for (y ← 0 until 9; x ← 0 until 9) board(x, y) match {
        case Some(cell) if cell? ⇒
          sphere draw(x * 80 + 1, y * 80 + yOffset + 1, 78, colours(cell.value))

        case Some(cell) ⇒
          for (iy ← 0 until 3; ix ← 0 until 3; i = 1 + ix + (2 - iy) * 3)
            if (cell(i))
              sphere draw(x * 80 + ix * 26 + 1, y * 80 + iy * 26 + yOffset + 1, 26, colours(i))

        case None ⇒ //
      }
    }
  }
}
