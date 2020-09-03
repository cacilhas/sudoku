package info.cacilhas.kodumaro.sudoku.model

import org.specs2.mutable.Specification

class BoardTest extends Specification {

  "Board" >> {

    "new board" >> {
      val board = Board()

      "it should start empty" >> {
        "head" >> {board.header must beEqualTo("% started empty")}
        for (y <- 0 until 9; x <- 0 until 9) s"board($x, $y)" >> {(board(x, y).get?) must beFalse}
        "board(10, 2) should not exist" >> {board(10, 2) must beNone}
      }

      "it should render board" >> {
        s"$board" must beEqualTo(
          """% started empty
            |.........
            |.........
            |.........
            |.........
            |.........
            |.........
            |.........
            |.........
            |.........
            |""".stripMargin)
      }
    }

    "set a cell" >> {
      val board = Board()
      board(0, 0) = 9

      "it should be set" >> {
        for (y <- 0 until 9; x <- 0 until 9 if x != 0 || y != 0) s"board($x, $y)" >> {(board(x, y).get?) must beFalse}
        "board(0, 0) should be set" >> {board(0, 0).get.value must beEqualTo(9)}
      }

      "it should render board" >> {
        s"$board" must beEqualTo(
          """% started empty
            |9........
            |.........
            |.........
            |.........
            |.........
            |.........
            |.........
            |.........
            |.........
            |""".stripMargin)
      }
    }

    "reset a cell" >> {
      val board = Board()
      board(0, 0) = 9

      for (y <- 0 until 9; x <- 0 until 9 if x > 0 || y > 0)
        if (x < 3 && y < 3) s"board($x, $y)(9) == false" >> {board(x, y).get apply 9 must beFalse}
        else if (x == 0 || y == 0) s"board($x, $y)(9) == false" >> {board(x, y).get apply 9 must beFalse}
        else s"board($x, $y)(9) == true" >> {board(x, y).get apply 9 must beTrue}
      "board(0, 0) == 9" >> {board(0, 0).get.value must beEqualTo(9)}
    }

    "load board" >> {
      val expected =
        """% randomly generated - easy
          |23...7..6
          |....34.2.
          |4619...3.
          |..2.8.6.3
          |9..4.3..1
          |3.5.6.4..
          |.2...6578
          |.7.81....
          |6..7...12
          |""".stripMargin
      val board = Board(expected)
      "it should serialise back" >> {s"$board" must beEqualTo(expected)}
    }
  }
}
