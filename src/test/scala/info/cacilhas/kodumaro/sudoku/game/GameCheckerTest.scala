package info.cacilhas.kodumaro.sudoku.game

import info.cacilhas.kodumaro.sudoku.model.Board
import org.specs2.mutable.Specification

class GameCheckerTest extends Specification {

  import ClassLevel._

  "Game Checker" >> {

    "check" >> {
      "it should be OK" >> {
        val board = Loader(Medium)
        GameChecker check board must beTrue
      }

      "it should fail" >> {
        val board = Board()
        board(0, 0) = 1
        board(0, 1) = 1
        GameChecker check board must beFalse
      }
    }
  }
}
