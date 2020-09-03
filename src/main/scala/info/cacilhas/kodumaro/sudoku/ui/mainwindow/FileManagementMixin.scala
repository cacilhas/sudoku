package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.io.{File, PrintWriter}

import info.cacilhas.kodumaro.sudoku.model.Board

import io.Source
import swing.{Dialog, FileChooser}
import util.Using

trait FileManagementMixin {
  window: Window =>

  def openBoard(): Unit = fileManager.open

  def saveBoard(): Unit = fileManager.save

  private lazy val homedir = System getProperty "user.home"

  private object fileManager {

    def save: Unit = try board match {
      case Some(board) =>
        val chooser = getChooser
        if (chooser.showSaveDialog(window) == FileChooser.Result.Approve) {
          val file = chooser.selectedFile
          Using(new PrintWriter(file)) { writer =>
            writer write s"$board"
          }
          Dialog showMessage (window, s"Board saved to $file", title, Dialog.Message.Info)
        }

      case None =>
        Dialog showMessage (window, s"No board to save", title, Dialog.Message.Warning)

    } catch {
      case exc: Throwable =>
        Dialog showMessage (window, s"Could not save board: $exc", title, Dialog.Message.Error)
    }

    def open: Unit = try {
      val chooser = getChooser
      if (chooser.showOpenDialog(window) == FileChooser.Result.Approve)
        Using(Source fromFile chooser.selectedFile) { source =>
          mustRender set true
          board = Option(Board(source.getLines mkString "\n"))
        }
    } catch {
      case exc: Throwable =>
        Dialog showMessage (window, s"Could not open file: $exc", title, Dialog.Message.Error)
    }

    private def getChooser: FileChooser = new FileChooser(new File(homedir)) {title = window.title}
  }
}
