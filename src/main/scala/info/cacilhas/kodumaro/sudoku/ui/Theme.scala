package info.cacilhas.kodumaro.sudoku.ui

import java.awt.{Color, Component}

case class Theme(fg: Color, bg: Color) {

  def set(component: Component): Unit = {
    component setForeground fg
    component setBackground bg
  }
}
