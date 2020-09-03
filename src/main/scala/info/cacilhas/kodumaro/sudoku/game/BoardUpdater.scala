package info.cacilhas.kodumaro.sudoku.game

import info.cacilhas.kodumaro.sudoku.model.{Board, Cell}

private[sudoku] class BoardUpdater(val board: Board) {

  def upgrade(x: Int, y: Int, value: Int): Unit = if (validParameters(x, y, value)) {
    group(x, y)  foreach {_(value) = false}
    row(x, y)    foreach {_(value) = false}
    column(x, y) foreach {_(value) = false}
  }

  private def group(x: Int, y: Int): Seq[Cell] = {
    val gx = (x / 3) * 3
    val gy = (y / 3) * 3
    for {
      ix <- gx until gx + 3
      iy <- gy until gy + 3 if ix != x || iy != y
    } yield board(ix, iy).get
  }

  private def row(x: Int, y: Int): Seq[Cell] =
    for (ix <- 0 until 9 if ix != x) yield board(ix, y).get

  private def column(x: Int, y: Int): Seq[Cell] =
    for (iy <- 0 until 9 if iy != y) yield board(x, iy).get

  // Guard validators
  private def validParameters(x: Int, y: Int, value: Int): Boolean = validIndex(x) && validIndex(y) && validValue(value)
  private def validIndex(i: Int): Boolean = 0 <= i && i < 9
  private def validValue(value: Int): Boolean = 0 < value && value <= 9
}
