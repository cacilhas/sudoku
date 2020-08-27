package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.event.{ComponentEvent, ComponentListener}
import java.awt.{Color, Dimension, Font}
import java.util.concurrent.atomic.AtomicBoolean

import info.cacilhas.kodumaro.sudoku.ui.Theme

import swing.event.{WindowActivated, WindowClosed, WindowClosing}
import swing._

final class Window extends Frame
                   with BoardMixin
                   with FileManagementMixin
                   with RendererMixin {
  window ⇒

  val mustRender: AtomicBoolean = new AtomicBoolean(true)
  val theme: Theme = Theme(
    font = new Font("Noto", Font.PLAIN, 24),
    fg = Color.lightGray,
    bg = Color.black,
  )
  private lazy val version = Option(getClass.getPackage.getImplementationVersion) getOrElse "1.0"

  title = "Kodumaro Sudoku"
  size = new Dimension(720, 784)
  minimumSize = size
  theme set window
  centerOnScreen()

  menuBar = MenuBuilder(window)

  contents = new GridBagPanel {
    layout(player) = new Constraints {fill = GridBagPanel.Fill.Both}
    theme set this
  }

  reactions += {
    case WindowActivated(`window`) ⇒ renderer start ()
    case WindowClosed(`window`)    ⇒ board = None
  }

  // FIXME: how to create a window-resized reaction?
  peer addComponentListener new ComponentListener {
    override def componentResized(componentEvent: ComponentEvent): Unit = mustRender set true
    override def componentMoved(componentEvent: ComponentEvent): Unit = ()
    override def componentShown(componentEvent: ComponentEvent): Unit = ()
    override def componentHidden(componentEvent: ComponentEvent): Unit = ()
  }

  def about(): Unit = {
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
    super.close()
    dispose()
  }

  type Offset = {def x: Int; def y: Int}

  def offset: Offset = renderer.offset
}
