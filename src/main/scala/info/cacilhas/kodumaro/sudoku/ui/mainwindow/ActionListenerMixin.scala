package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.MenuItem
import java.awt.event.{ActionEvent, ActionListener}

import info.cacilhas.kodumaro.sudoku.game.{ClassLevel, Loader, Solver}

private[mainwindow] trait ActionListenerMixin {
  this: BoardMixin with FileManagementMixin ⇒

  def close(): Unit
  protected def about(): Unit
  protected def solve(solver: Solver): Unit

  protected def addActionListenerTo(component: MenuItem): Unit =
    component addActionListener actionListener

  private object actionListener extends ActionListener {

    import ClassLevel._

    override def actionPerformed(event: ActionEvent): Unit = event.getActionCommand match {
      case "quit"                ⇒ close()
      case "open"                ⇒ open()
      case "save"                ⇒ save()
      case "about"               ⇒ about()
      case "solve(FullHouse)"    ⇒ solve(Solver.FullHouse)
      case "solve(HiddenSingle)" ⇒ solve(Solver.HiddenSingle)
      case "newEasy"             ⇒ board = Loader(Easy)
      case "newMedium"           ⇒ board = Loader(Medium)
      case "newHard"             ⇒ board = Loader(Hard)
      case "newPro"              ⇒ board = Loader(Pro)
      case _                     ⇒ //
    }
  }
}
