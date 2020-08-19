package info.cacilhas.kodumaro.sudoku.game.solver

import info.cacilhas.kodumaro.sudoku.model.Board

sealed trait Solver {def solve(board: Board): Unit}

object Solver {

  object FullHouse extends Solver with FullHouseSolver
  object HiddenSingle extends Solver with HiddenSingleSolver
}
