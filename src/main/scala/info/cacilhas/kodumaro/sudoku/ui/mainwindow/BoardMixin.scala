package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.util.concurrent.atomic.AtomicReference

import info.cacilhas.kodumaro.sudoku.model.Board

trait BoardMixin {

  private val _board = new AtomicReference[Option[Board]](Option(Board())) // start with an empty board

  def board: Option[Board] = _board.get

  def board_=(board: Option[Board]): Unit = _board set board
}
