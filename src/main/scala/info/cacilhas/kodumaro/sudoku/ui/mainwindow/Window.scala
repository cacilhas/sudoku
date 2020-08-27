package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.{Color, Dimension, Font, Graphics, GridBagLayout}
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}

import info.cacilhas.kodumaro.sudoku.game.{ClassLevel, Loader}
import info.cacilhas.kodumaro.sudoku.game.solver.Solver
import info.cacilhas.kodumaro.sudoku.ui.{Player, Sphere, Theme}
import javax.swing.KeyStroke

import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.swing.event.WindowActivated
import scala.util.Try
import swing._

final class Window extends Frame
                   with BoardMixin
                   with FileManagementMixin {
  window ⇒

  import ClassLevel._
  import Solver._

  val mustRender: AtomicBoolean = new AtomicBoolean(true)
  private val theme = Theme(
    font = new Font("Noto", Font.PLAIN, 24),
    fg = Color.lightGray,
    bg = Color.black,
  )
  private lazy val version = Option(getClass.getPackage.getImplementationVersion) getOrElse "1.0"

  private val player = new Player(this)

  title = "Kodumaro Sudoku"
  size = new Dimension(720, 720 + drawer.yOffset)
  minimumSize = size
  theme set window
  peer.getContentPane setLayout new GridBagLayout
  centerOnScreen()

  menuBar = new MenuBar {
    theme set this
    contents += new Menu("File") {
      theme set this
      contents += new MenuItem(new Action("Open") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl O")
        override def apply(): Unit = window openBoard ()
      }) {theme set this}
      contents += new MenuItem(new Action("Save") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl S")
        override def apply(): Unit = window saveBoard ()
      }) {theme set this}
      contents += new Separator
      contents += new MenuItem(new Action("Quit") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl Q")
        override def apply(): Unit = window close ()
      }) {theme set this}
    }

    contents += new Menu("Game") {
      theme set this
      contents += new Menu("New") {
        theme set this
        contents += buildNewAction(Easy)
        contents += buildNewAction(Medium)
        contents += buildNewAction(Hard)
        contents += buildNewAction(Pro)
      }
      contents += new Separator
      contents += buildSolveAction(FullHouse)
      contents += buildSolveAction(HiddenSingle)
    }

    contents += new Menu("Help") {
      theme set this
      contents += new MenuItem(new Action("About") {
        accelerator = Some(KeyStroke getKeyStroke "F1")
        override def apply(): Unit = window about ()
      }) {theme set this}
    }
  }

  reactions += {case _: WindowActivated ⇒ drawer start ()}

  protected def about(): Unit = {
    Dialog showMessage (
      window,
      s"""$title $version
         |
         |Swing UI for Console-based Sudoku using colours instead of numbers.
         |Arĥimedeς ℳontegasppα ℭacilhας <batalema@cacilhas.info>
         |""".stripMargin,
      title,
      Dialog.Message.Info,
    )
  }

  protected def solve(solver: Solver): Unit = {
    solver solve board
    mustRender set true
  }

  override def close(): Unit = {
    board = null
    super.close()
    dispose()
  }

  private def buildNewAction(level: ClassLevel): MenuItem = new MenuItem(new Action(level.toString.capitalize) {
    accelerator = Some(KeyStroke getKeyStroke (level match {
      case Medium ⇒ "ctrl N"
      case other  ⇒ s"ctrl ${other.toString.substring(0, 1).toUpperCase}"
    }))
    override def apply(): Unit = {
      board = Loader(level)
      mustRender set true
    }
  }) {theme set this}

  private def buildSolveAction(solver: Solver): MenuItem = new MenuItem(new Action(solver.toString) {
    override def apply(): Unit = {
      solver solve board
      mustRender set true
    }
  }) {theme set this}

  private object drawer {

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
        try while (board != null) {
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

    private def drawCircles(g: Graphics2D): Unit = if (board != null) {
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
