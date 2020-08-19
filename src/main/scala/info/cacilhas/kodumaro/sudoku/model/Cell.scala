package info.cacilhas.kodumaro.sudoku.model

import collection.mutable.{Map ⇒ MutableMap}

class Cell {

  private var _value: Int = 0
  private val values: Int MutableMap Boolean = MutableMap((for (i ← 1 to 9) yield (i, true)).toArray :_*)

  def value: Int = _value
  def value_=(value: Int): Unit = if (value == 0 || apply(value)) this._value = value

  def apply(index: Int): Boolean = values getOrElse (index, false)
  def update(index: Int, value: Boolean): Unit =
    if ((0 < index && index <= 9) && (index != this.value)) values(index) = value

  def single: Boolean = if (`!?`) values.count {case (_, v) ⇒ v} == 1 else false

  def singleValue: Option[Int] =
    if (?) Option(value)
    else if (single)
      Option(values.filter {case (_, v) ⇒ v}.map {case (k, _) ⇒ k}.head)
    else None

  def toggle(index: Int): Unit = if (`!?`) update(index, !apply(index))

  def `?`: Boolean = _value > 0

  def `!?`: Boolean = !`?`

  override def equals(that: Any): Boolean = that match {
    case that: Cell ⇒ (this%) == (that%)
    case that: Int  ⇒ this._value > 0 && this._value == that
    case that       ⇒ s"$this" == s"$that"
  }

  override def toString: String = if (?) s"${_value}" else "."

  // For comparison purposes
  private def `%`: String = s"${_value}" + (for (i ← 1 to 9) yield if (values(i)) s"$i" else "") mkString ""
}
