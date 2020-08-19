package info.cacilhas.kodumaro.sudoku.game

import org.specs2.mutable.Specification

class LoaderTest extends Specification {

  import ClassLevel._

  "Loader" >> {

    "it should return an easy board" >> {Loader(Easy).header must beEqualTo("% randomly generated - easy")}
    "it should return a medium board" >> {Loader(Medium).header must beEqualTo("% randomly generated - medium")}
    "it should return a hard board" >> {Loader(Hard).header must beEqualTo("% randomly generated - hard")}
    "it should return a pro board" >> {Loader(Pro).header must beEqualTo("% randomly generated - fiendish")}
    "it should return a medium board by default" >> {Loader("").header must beEqualTo("% randomly generated - medium")}
  }
}
