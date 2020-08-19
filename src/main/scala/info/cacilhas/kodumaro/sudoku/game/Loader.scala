package info.cacilhas.kodumaro.sudoku.game

import info.cacilhas.kodumaro.sudoku.model.Board

object Loader {

  import sys.process._

  private val command = "sudoku -g -fcompact"

  def apply(level: ClassLevel): Board = try Board(s"$command -c$level".!!)
  catch {
    case err: RuntimeException ⇒
      throw err initCause new RuntimeException(s"$command -c$level")
  }

  def apply(level: String): Board = apply(ClassLevel from level)
}
