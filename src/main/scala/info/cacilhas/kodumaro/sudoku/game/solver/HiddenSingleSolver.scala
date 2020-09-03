package info.cacilhas.kodumaro.sudoku.game.solver

import info.cacilhas.kodumaro.sudoku.model.{Board, Cell}

import collection.mutable.{Map => MutableMap}

private[solver] trait HiddenSingleSolver {
  this: Solver =>

  override def solve(board: Board): Unit = {
    solveLines(board)
    solveRows(board)
    solveGroups(board)
  }

  private def solveLines(board: Board): Unit =
    for (y <- 0 until 9) solveLine(board, y)

  private def solveRows(board: Board): Unit =
    for (x <- 0 until 9) solveRow(board, x)

  private def solveGroups(board: Board): Unit =
    for (y <- 0 until 3; x <- 0 until 3) solveGroup(board, x*3, y*3)

  private def solveLine(board: Board, y: Int): Unit =
    solveSeq(board, for (x <- 0 until 9; cell <- board(x, y)) yield CellWrap(x, y, cell))

  private def solveRow(board: Board, x: Int): Unit =
    solveSeq(board, for (y <- 0 until 9; cell <- board(x, y)) yield CellWrap(x, y, cell))

  private def solveGroup(board: Board, x: Int, y: Int): Unit =
    solveSeq(
      board,
      for {
        dy <- 0 until 3
        dx <- 0 until 3
        cell <- board(x+dx, y+dy)
      } yield CellWrap(x+dx, y+dy, cell),
    )

  private def solveSeq(board: Board, cells: Seq[CellWrap]): Unit = {
    val found = MutableMap[Int, List[CellWrap]]()
    for (i <- 1 to 9; wrap <- cells) wrap.cell match {
      case cell: Cell if cell.value == i    => found(i) = wrap :: found.getOrElse(i, Nil)
      case cell: Cell if cell.!? && cell(i) => found(i) = wrap :: found.getOrElse(i, Nil)
      case _                                => //
    }
    found foreach {
      case (i, cells) if cells.size == 1 => board(cells.head.x, cells.head.y) = i
      case _                             => //
    }
  }

  private case class CellWrap(x: Int, y: Int, cell: Cell)
}
