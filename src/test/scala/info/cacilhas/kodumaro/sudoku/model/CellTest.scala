package info.cacilhas.kodumaro.sudoku.model

import org.specs2.mutable.Specification

class CellTest extends Specification {

  "Cell" >> {

    "value" >> {
      val cell = new Cell

      "value must be 0" >> {cell.value must beEqualTo(0)}
      "done must be false" >> {(cell?) must beFalse}
    }

    "set value" >> {
      val cell = new Cell
      cell.value = 3

      "value must be 3" >> {cell.value must beEqualTo(3)}
      "done must be true" >> {(cell?) must beTrue}
    }

    "sef value failed" >> {
      val cell = new Cell
      cell.value = 10

      "value must be 0" >> {cell.value must beEqualTo(0)}
      "done must be false" >> {(cell?) must beFalse}
    }

    "index" >> {
      val cell = new Cell

      "it should start all set" >> {
        for (i ← 1 to 9) s"cell($i) should be true" >> {cell(i) must beTrue}
        "cell(0) should be false" >> {cell(0) must beFalse}
      }
    }

    "set index" >> {
      val cell = new Cell
      cell(2) = false

      "it should be set to false" >> {
        for (i ← 1 to 9 if i != 2) s"cell($i) should be true" >> {cell(i) must beTrue}
        "cell(2) should be false" >> {cell(2) must beFalse}
      }
    }

    "set index failed" >> {
      val cell = new Cell
      cell(10) = true

      "it should start all set" >> {
        for (i ← 1 to 9) s"cell($i) should be true" >> {cell(i) must beTrue}
        "cell(10) should be false" >> {cell(10) must beFalse}
      }
    }

    "string" >> {

      "it should be dot when undone" >> {s"${new Cell}" must beEqualTo(".")}

      "it should be the value when done" >> {
        val cell = new Cell
        cell.value = 5
        s"$cell" must beEqualTo("5")
      }
    }
  }
}
