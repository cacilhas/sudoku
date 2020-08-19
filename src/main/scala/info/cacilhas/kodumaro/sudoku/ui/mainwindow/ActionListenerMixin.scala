package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.MenuItem
import java.awt.event.{ActionEvent, ActionListener}

import info.cacilhas.kodumaro.sudoku.game.ClassLevel
import info.cacilhas.kodumaro.sudoku.game.Loader

private[mainwindow] trait ActionListenerMixin {
  this: BoardMixin ⇒

  protected def addActionListenerTo(component: MenuItem): Unit =
    component addActionListener actionListener

  def close(): Unit
  protected def open(): Unit
  protected def save(): Unit
  protected def about(): Unit

  private object actionListener extends ActionListener {

    import ClassLevel._

    override def actionPerformed(event: ActionEvent): Unit = event.getActionCommand match {
      case "quit"      ⇒ close()
      case "open"      ⇒ open()
      case "save"      ⇒ save()
      case "about"     ⇒ about()
      case "newEasy"   ⇒ board = Loader(Easy)
      case "newMedium" ⇒ board = Loader(Medium)
      case "newHard"   ⇒ board = Loader(Hard)
      case "newPro"    ⇒ board = Loader(Pro)
      case _           ⇒ //
    }
  }
}
