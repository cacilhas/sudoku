package info.cacilhas.kodumaro.sudoku.game.solver

import info.cacilhas.kodumaro.sudoku.model.Board

private[solver] trait FullHouseSolver {
  this: Solver ⇒

  override def solve(board: Board): Unit = while (board.hasSingle)
    for (y ← 0 until 9; x ← 0 until 9)
      board(x, y) match {
        case Some(cell) if cell!? ⇒ cell.singleValue match {
          case Some(value) ⇒ board(x, y) = value
          case None        ⇒ //
        }
        case _ ⇒ //
      }
}