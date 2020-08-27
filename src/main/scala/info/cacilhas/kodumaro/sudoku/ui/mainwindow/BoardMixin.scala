package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.util.concurrent.atomic.AtomicReference

import info.cacilhas.kodumaro.sudoku.model.Board

trait BoardMixin {

  private val _board = new AtomicReference[Board](Board()) // start with an empty board

  def board: Board = _board.get

  def board_=(board: Board): Unit = _board set board
}
