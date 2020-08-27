package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.{Color, Dimension, Font, GridBagLayout}
import java.util.concurrent.atomic.{AtomicBoolean}

import info.cacilhas.kodumaro.sudoku.game.{ClassLevel, Loader}
import info.cacilhas.kodumaro.sudoku.game.solver.Solver
import info.cacilhas.kodumaro.sudoku.ui.{Player, Theme}
import javax.swing.KeyStroke

import scala.swing.event.WindowActivated
import swing._

final class Window extends Frame
                   with BoardMixin
                   with FileManagementMixin
                   with RendererMixin {
  window ⇒

  import ClassLevel._
  import Solver._

  val mustRender: AtomicBoolean = new AtomicBoolean(true)
  protected val theme: Theme = Theme(
    font = new Font("Noto", Font.PLAIN, 24),
    fg = Color.lightGray,
    bg = Color.black,
  )
  private lazy val version = Option(getClass.getPackage.getImplementationVersion) getOrElse "1.0"

  title = "Kodumaro Sudoku"
  size = new Dimension(720, 720 + renderer.yOffset)
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

  reactions += {case _: WindowActivated ⇒ renderer start ()}

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

  override def close(): Unit = {
    board = None
    super.close()
    dispose()
  }

  private def buildNewAction(level: ClassLevel): MenuItem = new MenuItem(new Action(level.toString.capitalize) {
    accelerator = Some(KeyStroke getKeyStroke (level match {
      case Medium ⇒ "ctrl N"
      case other  ⇒ s"ctrl ${other.toString.substring(0, 1).toUpperCase}"
    }))
    override def apply(): Unit = {
      board = Option(Loader(level))
      mustRender set true
    }
  }) {theme set this}

  private def buildSolveAction(solver: Solver): MenuItem = new MenuItem(new Action(solver.toString) {
    override def apply(): Unit = {
      board foreach {solver solve}
      mustRender set true
    }
  }) {theme set this}
}
