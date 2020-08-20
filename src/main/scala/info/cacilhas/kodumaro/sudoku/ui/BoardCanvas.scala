package info.cacilhas.kodumaro.sudoku.ui

import java.awt.Canvas

import info.cacilhas.kodumaro.sudoku.model.Board
import info.cacilhas.kodumaro.sudoku.ui.mainwindow.Window

import ref.WeakReference

private[ui] class BoardCanvas(val window: Window, protected val theme: Theme) extends Canvas with BoardRenderer {

  private var _board: WeakReference[Board] = _
  protected val player = new Player(this)

  setSize(720, 720)
  theme set this
  setIgnoreRepaint(true)

  def board: Board = _board()

  def board_=(board: Board): Unit = {
    Option(_board) foreach {_ clear ()}
    if (board != null) {
      _board = new WeakReference(board)
      render()
    }
  }
}
