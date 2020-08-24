package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.io.{File, PrintWriter}

import info.cacilhas.kodumaro.sudoku.model.Board
import javax.swing.{JFileChooser, JFrame, JOptionPane}

import scala.io.Source

trait FileManagementMixin {
  window: Window ⇒

  protected def open(): Unit = fileManager.open

  protected def save(): Unit = fileManager.save

  private lazy val homedir = System getProperty "user.home"

  private object fileManager {

    def save: Unit = try {
      if (board != null) {
        val chooser = getChooser
        if (chooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
          val file = chooser.getSelectedFile
          val writer = new PrintWriter(file)
          try writer write s"$board"
          finally writer close ()
          JOptionPane showMessageDialog (window, s"Board saved to $file", getTitle, JOptionPane.INFORMATION_MESSAGE)
        }
      } else
        JOptionPane showMessageDialog (window, s"No board to save", getTitle, JOptionPane.WARNING_MESSAGE)
    } catch {
      case exc: Throwable ⇒
        JOptionPane showMessageDialog (window, s"Could not save board: $exc", getTitle, JOptionPane.ERROR_MESSAGE)
    }

    def open: Unit = try {
      val chooser = getChooser
      if (chooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
        val source = Source fromFile chooser.getSelectedFile
        try board = Board(source.getLines mkString "\n")
        finally source.close
      }
    } catch {
      case exc: Throwable ⇒
        JOptionPane showMessageDialog (window, s"Could not open file: $exc", getTitle, JOptionPane.ERROR_MESSAGE)
    }

    private def getChooser: JFileChooser = {
      val chooser = new JFileChooser
      chooser setCurrentDirectory new File(homedir)
      chooser setDialogTitle getTitle
      chooser
    }
  }
}
