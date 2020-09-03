package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import info.cacilhas.kodumaro.sudoku.game.{ClassLevel, Loader}
import info.cacilhas.kodumaro.sudoku.game.solver.Solver
import javax.swing.KeyStroke

import swing.{Action, Menu, MenuBar, MenuItem, Separator}

private object MenuBuilder {

  import ClassLevel._
  import Solver._

  def apply(window: Window): MenuBar = new MenuBar {
    window.theme set this
    contents += new Menu("File") {
      window.theme set this
      contents += new MenuItem(new Action("Open") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl O")
        override def apply(): Unit = window.openBoard()
      }) {window.theme set this}
      contents += new MenuItem(new Action("Save") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl S")
        override def apply(): Unit = window.saveBoard()
      }) {window.theme set this}
      contents += new Separator
      contents += new MenuItem(new Action("Quit") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl Q")
        override def apply(): Unit = window.close()
      }) {window.theme set this}
    }

    contents += new Menu("Game") {
      window.theme set this
      contents += new Menu("New") {
        window.theme set this
        contents += buildNewAction(Easy, window)
        contents += buildNewAction(Medium, window)
        contents += buildNewAction(Hard, window)
        contents += buildNewAction(Pro, window)
      }
      contents += new Separator
      contents += buildSolveAction(FullHouse, window)
      contents += buildSolveAction(HiddenSingle, window)
    }

    contents += new Menu("Help") {
      window.theme set this
      contents += new MenuItem(new Action("About") {
        accelerator = Some(KeyStroke getKeyStroke "F1")
        override def apply(): Unit = window.about()
      }) {window.theme set this}
    }
  }

  private def buildNewAction(level: ClassLevel, window: Window): MenuItem =
    new MenuItem(new Action(level.toString.capitalize) {
      accelerator = Some(KeyStroke getKeyStroke (level match {
        case Medium => "ctrl N"
        case other  => s"ctrl ${other.toString.substring(0, 1).toUpperCase}"
      }))
      override def apply(): Unit = {
        window.board = Option(Loader(level))
        window.mustRender set true
      }
    }) {window.theme set this}

  private def buildSolveAction(solver: Solver, window: Window): MenuItem =
    new MenuItem(new Action(solver.toString) {
      override def apply(): Unit = {
        window.board foreach {solver solve}
        window.mustRender set true
      }
    }) {window.theme set this}
}
