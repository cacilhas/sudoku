package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt._
import java.awt.event._

import info.cacilhas.kodumaro.sudoku.game.solver.Solver
import info.cacilhas.kodumaro.sudoku.model.Board
import info.cacilhas.kodumaro.sudoku.ui.{BoardCanvas, Theme}
import javax.swing._


final class Window extends JFrame
                   with BoardMixin
                   with ActionListenerMixin
                   with FileManagementMixin
                   with WindowListenerMixin
                   with ComponentsMixin {
  window ⇒

  private val theme = Theme(fg = Color.lightGray, bg = Color.black)
  private val dim = new Dimension(724, 800)
  private lazy val version = Option(getClass.getPackage.getImplementationVersion) getOrElse "1.0"
  override protected val frmBoard = new BoardCanvas(window, theme)

  setTitle("Kodumaro Sudoku")
  setSize(dim)
  setMinimumSize(dim)
  setLocationRelativeTo(null)
  theme set window
  theme set getContentPane
  setFont(new Font("Noto", Font.PLAIN, 24))
  setMenuBar(new MenuBar)
  getContentPane setLayout new GridBagLayout
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  board = Board()
  packComponents()

  override def close(): Unit = dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING))

  override protected def about(): Unit = {
    JOptionPane showMessageDialog (
      window,
      s"""$getTitle $version
         |
         |Swing UI for Console-based Sudoku using colours instead of numbers.
         |Arĥimedeς ℳontegasppα ℭacilhας <batalema@cacilhas.info>
         |""".stripMargin,
      getTitle,
      JOptionPane.INFORMATION_MESSAGE,
    )
  }

  override protected def solve(solver: Solver): Unit = {
    solver solve board
    frmBoard.board = board // force rendering
  }

  override protected def onBoardUpdate(): Unit = frmBoard.board = board
}
