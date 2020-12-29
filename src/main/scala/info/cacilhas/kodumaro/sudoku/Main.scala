package info.cacilhas.kodumaro.sudoku

import java.io.IOException

import info.cacilhas.kodumaro.sudoku.ui.mainwindow.Window
import javax.swing.{JOptionPane, UIManager}

import util.{Failure, Success, Try}

object Main extends App {

  import sys.process._

  UIManager setLookAndFeel UIManager.getSystemLookAndFeelClassName

  Try("sudoku -g"!!) match {
    case Success(_) => new Window visible = true

    case Failure(exc: IOException) =>
      JOptionPane showMessageDialog (
        null,
        """Console-based Sudoku not found!
          |Please install the package.
          |""".stripMargin,
        "Kodumaro Sudoku",
        JOptionPane.ERROR_MESSAGE,
      )
      System exit 1

    case Failure(exc) => throw exc
  }
}
