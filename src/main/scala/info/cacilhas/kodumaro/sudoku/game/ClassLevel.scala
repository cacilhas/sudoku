package info.cacilhas.kodumaro.sudoku.game

trait ClassLevel

object ClassLevel extends Enumeration {
  private case class build(name: String) extends Val(name) with ClassLevel {
    override def toString(): String = name
  }

  // val VeryEasy: ClassLevel = build("very easy") // FIXME: broken
  val Easy: ClassLevel     = build("easy")
  val Medium: ClassLevel   = build("medium")
  val Hard: ClassLevel     = build("hard")
  val Pro: ClassLevel      = build("fiendish")
  val Fiendish: ClassLevel = Pro

  def from(value: String): ClassLevel = value.toLowerCase match {
    case ""    => Medium
    case "pro" => Pro
    case value => values find {_.toString == value} map {_.asInstanceOf[ClassLevel]} getOrElse Medium
  }
}
