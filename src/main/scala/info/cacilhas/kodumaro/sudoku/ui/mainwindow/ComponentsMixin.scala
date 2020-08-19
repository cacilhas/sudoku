package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.event.KeyEvent
import java.awt.{Frame, Menu, MenuItem, MenuShortcut}

import info.cacilhas.kodumaro.sudoku.game.ClassLevel
import info.cacilhas.kodumaro.sudoku.ui.BoardCanvas

private[mainwindow] trait ComponentsMixin {
  this: Frame with ActionListenerMixin ⇒

  import ClassLevel._
  import KeyEvent._

  protected def frmBoard: BoardCanvas
  private val mnFile = new Menu("File")
  private val mnGame = new Menu("Game")
  private val mnNew = new Menu("New")
  private val mnHelp = new Menu("Help")
  private val itSolveFullHouse = new MenuItem("Solve Full House")
  private val itOpen = new MenuItem("Open")
  private val itSave = new MenuItem("Save")
  private val itQuit = new MenuItem("Quit")
  private val itAbout = new MenuItem("About")
  private val itEasy = new MenuItem(Easy.toString.capitalize)
  private val itMedium = new MenuItem(Medium.toString.capitalize)
  private val itHard = new MenuItem(Hard.toString.capitalize)
  private val itPro = new MenuItem(Pro.toString.capitalize)

  start(itEasy, "newEasy", VK_E)
  start(itMedium, "newMedium", VK_N)
  start(itHard, "newHard", VK_H)
  start(itPro, "newPro", VK_P)
  start(itOpen, "open", VK_O)
  start(itSave, "save", VK_S)
  start(itQuit, "quit", VK_Q)
  start(itSolveFullHouse, "solve(FullHouse)")
  start(itAbout, "about")

  protected def packComponents(): Unit = {
    mnNew setFont getFont
    mnNew add itEasy
    mnNew add itMedium
    mnNew add itHard
    mnNew add itPro

    mnHelp add itAbout

    mnFile setFont getFont
    mnFile add itOpen
    mnFile add itSave
    mnFile addSeparator()
    mnFile add itQuit

    mnGame setFont getFont
    mnGame add mnNew
    mnGame add itSolveFullHouse

    getMenuBar add mnFile
    getMenuBar add mnGame
    getMenuBar add mnHelp
    add(frmBoard)
  }

  private def start(item: MenuItem, action: String, shortcut: Int = -1): Unit = {
    item setFont getFont
    item setActionCommand action
    addActionListenerTo(item)
    if (shortcut != -1) item setShortcut new MenuShortcut(shortcut)
  }
}
