package info.cacilhas.kodumaro.sudoku.model

import java.util.concurrent.Semaphore

import info.cacilhas.kodumaro.sudoku.game.BoardUpdater

import concurrent.{Future, blocking}
import concurrent.ExecutionContext.Implicits._

class Board private(val header: String = "% started empty") {

  private val cells: Seq[Cell] = for (_ ← 0 until 81) yield new Cell
  private val updater = new BoardUpdater(this)
  private val updateMutex = new Semaphore(1)

  def apply(x: Int, y: Int): Option[Cell] =
    if (0 <= x && x < 9 && 0 <= y && y < 9) Option(cells(x + y*9)) else None

  def update(x: Int, y: Int, value: Int): Unit = apply(x, y) filter {_.value != value} foreach { cell ⇒
    cell.value = value
    if (cell.value == value) updater upgrade (x, y, value)
    if (updateMutex.tryAcquire) Future(blocking {
      updateMutex release ()
    })
  }

  def hasSingle: Boolean = cells exists {_.single}

  override def toString: String = {
    val res = new StringBuilder(header)
    res ++= "\n"
    for (y ← 0 until 9) {
      for (x ← 0 until 9)
        res ++= s"${apply(x, y).get}"
      res ++= "\n"
    }
    res.mkString
  }
}

object Board {

  def apply(): Board = new Board

  def apply(value: String): Board = {
    val res = value.trim split "\n"
    val board = new Board(res.head)
    for (y ← 0 until 9) {
      val current = res.tail(y)
      for (x ← 0 until 9) {
        current substring (x, x+1) match {
          case "." ⇒ //
          case v   ⇒ board(x, y) = v.toInt
        }
      }
    }
    board
  }
}
