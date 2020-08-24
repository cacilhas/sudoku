package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import javax.swing.KeyStroke

import info.cacilhas.kodumaro.sudoku.game.{ClassLevel, Loader}
import info.cacilhas.kodumaro.sudoku.game.solver.Solver
import info.cacilhas.kodumaro.sudoku.ui.BoardCanvas

import swing.{Action, Menu, MenuBar, MenuItem, Separator}

trait ComponentsMixin {
  window: Window ⇒

  protected lazy val frmBoard = new BoardCanvas(window, window.theme)

  menuBar = new MenuBar {
    contents += new Menu("File") {
      font = window.font

      contents += new MenuItem(new Action("Open") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl O")

        override def apply(): Unit = window openBoard ()
      }) {font = window.font}

      contents += new MenuItem(new Action("Save") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl S")

        override def apply(): Unit = window saveBoard ()
      }) {font = window.font}

      contents += new Separator

      contents += new MenuItem(new Action("Quit") {
        accelerator = Some(KeyStroke getKeyStroke "ctrl Q")

        override def apply(): Unit = window close ()
      }) {font = window.font}
    }

    contents += new Menu("Game") {
      font = window.font

      contents += new Menu("New") {
        import ClassLevel._

        font = window.font

        contents += new MenuItem(new Action(Easy.toString.capitalize) {
          accelerator = Some(KeyStroke getKeyStroke "ctrl E")
          font = window.font

          override def apply(): Unit = board = Loader(Easy)
        }) {font = window.font}

        contents += new MenuItem(new Action(Medium.toString.capitalize) {
          accelerator = Some(KeyStroke getKeyStroke "ctrl N")
          font = window.font

          override def apply(): Unit = board = Loader(Medium)
        }) {font = window.font}

        contents += new MenuItem(new Action(Hard.toString.capitalize) {
          accelerator = Some(KeyStroke getKeyStroke "ctrl H")
          font = window.font

          override def apply(): Unit = board = Loader(Hard)
        }) {font = window.font}

        contents += new MenuItem(new Action(Pro.toString.capitalize) {
          accelerator = Some(KeyStroke getKeyStroke "ctrl P")
          font = window.font

          override def apply(): Unit = board = Loader(Pro)
        }) {font = window.font}
      }

      contents += new Separator

      contents += new MenuItem(new Action("Solve Full House") {
        override def apply(): Unit = solve(Solver.FullHouse)
      }) {font = window.font}

      contents += new MenuItem(new Action("Solve Hidden Single") {
        override def apply(): Unit = solve(Solver.HiddenSingle)
      }) {font = window.font}
    }

    contents += new Menu("Help") {
      font = window.font

      contents += new MenuItem(new Action("About") {
        font = window.font

        override def apply(): Unit = window about ()
      }) {font = window.font}
    }
  }
}
