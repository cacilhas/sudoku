package info.cacilhas.kodumaro.sudoku.game

case class Step(x: Int, y: Int, value: Int) {
  require(0 <= x && x < 9, s"invalid x value $x")
  require(0 <= y && y < 9, s"invalid y value $y")
  require(0 < value && value <= 9, s"invalid value $value")

  override def toString: String = s"$value -> ($x,$y)"
}
