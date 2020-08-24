package info.cacilhas.kodumaro.sudoku.ui

import java.awt.geom.Point2D
import java.awt.{Color, Graphics2D,  RadialGradientPaint, RenderingHints}

private class Sphere(val g: Graphics2D) {

  g setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

  def draw(x: Int, y: Int, r: Int, colour: Color): Unit = {
    val darkEdge = new Color(
      (colour.getRed*.8).toInt,
      (colour.getGreen*.8).toInt,
      (colour.getBlue*.8).toInt,
      0x80,
    )
    val shade3D = new Color(colour.getRed/5, colour.getGreen/5, colour.getBlue/5, 0xcd)

    val oldPaint = g.getPaint
    g setColor colour
    g fillOval (x, y, r, r)

    g setPaint new RadialGradientPaint(
      new Point2D.Float(x + r/2f, y + r/2f),
      r/2f,
      Seq[Float](0f, 1f).toArray,
      Seq[Color](darkEdge, shade3D).toArray,
    )
    g fillOval (x, y, r, r)

    g setPaint oldPaint
  }
}
