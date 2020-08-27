package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.io.{File, PrintWriter}

import info.cacilhas.kodumaro.sudoku.model.Board

import swing.{Dialog, FileChooser}
import io.Source

trait FileManagementMixin {
  window: Window ⇒

  def openBoard(): Unit = fileManager.open

  def saveBoard(): Unit = fileManager.save

  private lazy val homedir = System getProperty "user.home"

  private object fileManager {

    def save: Unit = try board match {
      case Some(board) ⇒
        val chooser = getChooser
        if (chooser.showSaveDialog(window) == FileChooser.Result.Approve) {
          val file = chooser.selectedFile
          val writer = new PrintWriter(file)
          try writer write s"$board"
          finally writer close ()
          Dialog showMessage (window, s"Board saved to $file", title, Dialog.Message.Info)
        }

      case None ⇒
        Dialog showMessage (window, s"No board to save", title, Dialog.Message.Warning)

    } catch {
      case exc: Throwable ⇒
        Dialog showMessage (window, s"Could not save board: $exc", title, Dialog.Message.Error)
    }

    def open: Unit = try {
      val chooser = getChooser
      if (chooser.showOpenDialog(window) == FileChooser.Result.Approve) {
        val source = Source fromFile chooser.selectedFile
        mustRender set true
        try board = Option(Board(source.getLines mkString "\n"))
        finally source.close
      }
    } catch {
      case exc: Throwable ⇒
        Dialog showMessage (window, s"Could not open file: $exc", title, Dialog.Message.Error)
    }

    private def getChooser: FileChooser = new FileChooser(new File(homedir)) {title = window.title}
  }
}
