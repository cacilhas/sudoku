package info.cacilhas.kodumaro.sudoku.game

import org.specs2.mutable.Specification

class ClassLevelTest extends Specification {

  import ClassLevel._

  "ClassLevel" >> {

    "easy" >> {s"$Easy" must beEqualTo("easy")}
    "medium" >> {s"$Medium" must beEqualTo("medium")}
    "hard" >> {s"$Hard" must beEqualTo("hard")}
    "pro" >> {s"$Pro" must beEqualTo("fiendish")}
    "fiendish" >> {Fiendish must be(Pro)}

    "from string" >> {
      "it should return medium for empty string" >> {ClassLevel from "" must be(Medium)}
      "it should be easy" >> {ClassLevel from "easy" must be(Easy)}
      "it should be medium" >> {ClassLevel from "medium" must be(Medium)}
      "it should be hard" >> {ClassLevel from "hard" must be(Hard)}
      "it should be pro" >> {ClassLevel from "pro" must be(Pro)}
      "it should be fiendish" >> {ClassLevel from "fiendish" must be(Fiendish)}
    }
  }
}
