package info.cacilhas.kodumaro.sudoku.ui

import java.awt.{Color, Font}

case class Theme(font: Font, fg: Color, bg: Color) {

  private type Component = {
    var font: Font
    var foreground: Color
    var background: Color
  }

  def set(component: Component): Unit = {
    component.font = font
    component.foreground = fg
    component.background = bg
  }
}
