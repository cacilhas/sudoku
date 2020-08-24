package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.{Color, Dimension, Font, GridBagLayout}
import javax.swing.WindowConstants

import info.cacilhas.kodumaro.sudoku.game.solver.Solver
import info.cacilhas.kodumaro.sudoku.model.Board
import info.cacilhas.kodumaro.sudoku.ui.Theme

import swing.{Dialog, Frame, MenuBar}

final class Window extends Frame
                   with BoardMixin
                   with FileManagementMixin
                   with ComponentsMixin {
  window ⇒

  protected val theme: Theme = Theme(fg = Color.lightGray, bg = Color.black)
  private lazy val version = Option(getClass.getPackage.getImplementationVersion) getOrElse "1.0"

  title = "Kodumaro Sudoku"
  size = new Dimension(724, 800)
  minimumSize = size
  theme set window
  font = new Font("Noto", Font.PLAIN, 24)
  menuBar = new MenuBar
  peer.getContentPane setLayout new GridBagLayout
  peer setDefaultCloseOperation WindowConstants.EXIT_ON_CLOSE
  peer setLocationRelativeTo null
  board = Board()

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
    frmBoard.board = board // force rendering
  }

  override protected def onBoardUpdate(): Unit = frmBoard.board = board
}
