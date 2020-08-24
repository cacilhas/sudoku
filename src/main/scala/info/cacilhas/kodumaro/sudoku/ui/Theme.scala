package info.cacilhas.kodumaro.sudoku.ui

import java.awt.Color

case class Theme(fg: Color, bg: Color) {

  private type Component = {
    var foreground: Color
    var background: Color
  }

  def set(component: Component): Unit = {
    component.foreground = fg
    component.background = bg
  }
}
