package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.{Color, Dimension, Font, GridBagLayout}
import java.util.concurrent.atomic.AtomicBoolean

import info.cacilhas.kodumaro.sudoku.ui.Theme

import swing.event.{WindowActivated, WindowDeactivated}
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
  size = new Dimension(720, 720 + renderer.yOffset)
  minimumSize = size
  theme set window
  peer.getContentPane setLayout new GridBagLayout
  centerOnScreen()

  menuBar = MenuBuilder(window)

  contents = player
  reactions += {
    case WindowActivated(`window`) ⇒
      renderer start ()

    case WindowDeactivated(`window`) ⇒
      board = None
      dispose()
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

  def yOffset: Int = renderer.yOffset
}
