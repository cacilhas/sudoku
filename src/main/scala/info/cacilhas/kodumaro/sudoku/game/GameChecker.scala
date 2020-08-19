package info.cacilhas.kodumaro.sudoku.game

import java.io.{File, PrintWriter}
import java.lang.management.ManagementFactory

import info.cacilhas.kodumaro.sudoku.model.Board

import util.control.Breaks
import util.{Random, Try}

object GameChecker {

  import sys.process._

  private lazy val pid = ManagementFactory.getRuntimeMXBean.getName.split("@").head
  private val rnd = Random

  rnd setSeed System.currentTimeMillis

  def check(board: Board): Boolean = {
    val file = new File(s"/dev/shm/kodumaro-sudoku.$pid-${rnd.nextLong}")
    try {
      saveFile(file, board)
      checkBoard(file)
    } finally file delete ()
  }

  private def checkBoard(file: File): Boolean = Try(s"sudoku -v -fcompact $file".!!).isSuccess

  private def saveFile(file: File, board: Board): Unit = {
    val loop = new Breaks
    loop breakable {
      for (i ← 5 to 1 by -1) {
        try {
          val writer = new PrintWriter(file)
          try writer write s"$board"
          finally writer close()
          loop.break

        } catch {
          case exc: Throwable ⇒ if (i <= 1) throw exc
        }
      }
    }
  }
}
