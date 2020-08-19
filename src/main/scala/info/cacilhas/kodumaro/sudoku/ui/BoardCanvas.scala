package info.cacilhas.kodumaro.sudoku.ui

import java.awt.event.{KeyEvent, KeyListener}
import java.awt.{Canvas, Color, Graphics2D}
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}

import info.cacilhas.kodumaro.sudoku.model.Board
import info.cacilhas.kodumaro.sudoku.ui.mainwindow.Window

import concurrent.{Future, blocking}
import concurrent.ExecutionContext.Implicits._
import ref.WeakReference
import util.Try

private[ui] class BoardCanvas(window: Window, theme: Theme) extends Canvas {

  private var _board: WeakReference[Board] = _
  private val colours = Seq(
    theme.bg,
    new Color(0xe00000), new Color(0xe08000), new Color(0xe0e000),
    new Color(0x00e000), new Color(0x0008e0), new Color(0x4b0082),
    new Color(0x9932cc), new Color(0xe000e0), new Color(0xc0c0c0),
  )
  private val backgrounds = Seq(new Color(0x808080), new Color(0x606060))

  setSize(720, 720)
  theme set this
  setIgnoreRepaint(true)

  def start(): Unit = renderer start ()

  def render(): Unit = renderer.changed = true

  def board: Board = _board()

  def board_=(board: Board): Unit = {
    Option(_board) foreach {_ clear ()}
    if (board != null) {
      _board = new WeakReference(board)
      render()
    }
  }

  private object renderer {

    private val mutex = new Semaphore(1)
    private val counter = new AtomicInteger(0)
    private val _changed = new AtomicBoolean(true)
    private val ticksBeforeStop = 2 // WORKAROUND

    def changed: Boolean = _changed.get
    def changed_=(value: Boolean): Unit = _changed set value

    def start(): Unit = {
      if (tryAcquire) Future(blocking {
        changed = true
        try while (board != null) {
          if (window.getKeyListeners.isEmpty) window addKeyListener _keyListener
          if (changed) render()
          Thread sleep 20 // almost 50 fps
        } finally release
      })
    }

    def tryAcquire: Boolean = mutex.tryAcquire

    def release: Boolean = {
      mutex release ()
      true
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
        case Some(cell) if cell ? ⇒
          sphere draw(x * 80 + 1, y * 80 + 1, 78, colours(cell.value))

        case Some(cell) ⇒
          for (iy ← 0 until 3; ix ← 0 until 3; i = 1 + ix + (2 - iy) * 3)
            if (cell(i))
              sphere draw(x * 80 + ix * 26 + 1, y * 80 + iy * 26 + 1, 26, colours(i))

        case None ⇒ //
      }
    }
  }

  private object player {
    private val _x = new AtomicInteger(4)
    private val _y = new AtomicInteger(4)
    private val mutex = new Semaphore(1)

    def x: Int = _x.get
    def y: Int = _y.get

    def tryAcquire: Boolean = mutex.tryAcquire

    def release(): Unit = mutex release ()

    def incX(value: Int): Unit = {
      mutex acquire ()
      try {
        _x set (9 + x + value) % 9
        render()
      } finally release()
    }

    def incY(value: Int): Unit = {
      mutex acquire ()
      try {
        _y set (9 + y + value) % 9
        render()
      } finally release()
    }
  }

  private object _keyListener extends KeyListener {

    override def keyTyped(event: KeyEvent): Unit = ()

    override def keyPressed(event: KeyEvent): Unit = event.getKeyCode match {
      case KeyEvent.VK_LEFT  ⇒ player incX -1
      case KeyEvent.VK_RIGHT ⇒ player incX 1
      case KeyEvent.VK_UP    ⇒ player incY -1
      case KeyEvent.VK_DOWN  ⇒ player incY 1
      case _ ⇒ //
    }

    override def keyReleased(event: KeyEvent): Unit = event.getKeyCode match {
      case KeyEvent.VK_ESCAPE  ⇒ window close ()

      case KeyEvent.VK_NUMPAD0 |
           KeyEvent.VK_0       ⇒
        board(player.x, player.y) foreach {_.value = 0}
        render()

      case keyCode             ⇒
        (keyCode match {
          case KeyEvent.VK_NUMPAD1 |
               KeyEvent.VK_NUMPAD2 |
               KeyEvent.VK_NUMPAD3 |
               KeyEvent.VK_NUMPAD4 |
               KeyEvent.VK_NUMPAD5 |
               KeyEvent.VK_NUMPAD6 |
               KeyEvent.VK_NUMPAD7 |
               KeyEvent.VK_NUMPAD8 |
               KeyEvent.VK_NUMPAD9 ⇒
            Option(keyCode - KeyEvent.VK_NUMPAD0)

          case KeyEvent.VK_1       |
               KeyEvent.VK_2       |
               KeyEvent.VK_3       |
               KeyEvent.VK_4       |
               KeyEvent.VK_5       |
               KeyEvent.VK_6       |
               KeyEvent.VK_7       |
               KeyEvent.VK_8       |
               KeyEvent.VK_9       ⇒
            Option(keyCode - KeyEvent.VK_0)

          case _                   ⇒ None

        }) match {
          case Some(num) ⇒
            board(player.x, player.y) match {
              case Some(cell) ⇒ unless (cell?) {
                if (event.isControlDown) cell toggle num
                else board(player.x, player.y) = num
                render()
              }
              case None ⇒ //
            }

          case None ⇒ //
        }
    }

    private def unless(condition: ⇒ Boolean)(block: ⇒ Any): Unit = if (!condition) block
  }
}
