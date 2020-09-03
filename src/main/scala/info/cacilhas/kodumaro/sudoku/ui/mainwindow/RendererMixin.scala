package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.{Color, Graphics}
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger

import info.cacilhas.kodumaro.sudoku.ui.{Player, Sphere}

import concurrent.{ExecutionContext, Future, blocking}
import swing.Graphics2D
import util.chaining._
import util.{Try, Using}

trait RendererMixin { window: Window =>

  protected lazy val player = new Player(this)
  implicit private val releasablePlayer: Using.Releasable[Player] =
    (resource: Player) => resource.release()
  implicit private val releasableSemaphore: Using.Releasable[Semaphore] =
    (resource: Semaphore) => resource.release()

  protected object renderer {

    import ExecutionContext.Implicits.global

    object offset {
      def x: Int = (window.size.width - 720) / 2
      def y: Int = (window.size.height - 784) / 2 + 64
    }

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
        Using(mutex) { _ =>
          mustRender set true
          while (board.isDefined) {
            if (mustRender.get) render()
            Thread sleep 20 // almost 50 fps
          }
        }
      })
    }

    def render(): Unit = Try {
      peer.getBufferStrategy match {
        case null => window.peer.createBufferStrategy(3)

        case strategy =>
          if (player.tryAcquire) Using(player) { _ =>
            strategy
              .getDrawGraphics
              .asInstanceOf[Graphics2D]
              .tap{draw(_)}
              .tap{_.dispose()}
            strategy.show()
            counter set (counter.get + 1) % ticksBeforeStop
            if (counter.get == 0) mustRender set false
          }
      }
    }

    private def draw(g: Graphics): Unit = g
      .tap{peer paint}
      .asInstanceOf[Graphics2D]
      .tap{drawBackground}
      .tap{drawCircles}
      .tap{player paint}

    private def drawBackground(g: Graphics2D): Unit =
      for (y <- 0 until 9; x <- 0 until 9) g
        .tap{_ setColor backgrounds((x / 3 + y / 3) % 2)}
        .tap{_ fillRect(x*80 + offset.x, y*80 + offset.y, 80, 80)}
        .tap{_ setColor theme.bg}
        .tap{_ drawRect(x*80 + offset.x, y*80 + offset.y, 80, 80)}

    private def drawCircles(g: Graphics2D): Unit = board foreach { board =>
      val sphere = new Sphere(g)
      for (y <- 0 until 9; x <- 0 until 9) board(x, y) match {
        case Some(cell) if cell? =>
          sphere draw(x * 80 + offset.x + 1, y * 80 + offset.y + 1, 78, colours(cell.value))

        case Some(cell) =>
          for (iy <- 0 until 3; ix <- 0 until 3; i = 1 + ix + (2 - iy) * 3)
            if (cell(i))
              sphere draw(x * 80 + ix * 26 + offset.x + 1, y * 80 + iy * 26 + offset.y + 1, 26, colours(i))

        case None => //
      }
    }
  }
}
