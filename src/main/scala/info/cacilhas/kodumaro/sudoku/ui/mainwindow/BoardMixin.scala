package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import info.cacilhas.kodumaro.sudoku.model.Board

trait BoardMixin {

  private var _board: Board = _

  protected def onBoardUpdate(): Unit

  def board: Board = _board

  protected def board_=(board: Board): Unit = {
    _board = board
    onBoardUpdate()
    collectGarbage()
  }

  private def collectGarbage(): Unit = Runtime.getRuntime match {
    case rt ⇒
      rt gc ()
      rt runFinalization ()
  }
}
