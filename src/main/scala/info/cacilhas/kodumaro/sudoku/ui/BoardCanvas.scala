package info.cacilhas.kodumaro.sudoku.ui

import java.awt.{Canvas, Color}

import info.cacilhas.kodumaro.sudoku.model.Board
import info.cacilhas.kodumaro.sudoku.ui.mainwindow.Window

import ref.WeakReference

class BoardCanvas(val window: Window, protected val theme: Theme) extends Canvas with BoardRenderer {

  private var _board: WeakReference[Board] = new WeakReference(null)
  protected val player = new Player(this)

  private object themeManager {
    def foreground: Color = getForeground
    def foreground_=(fg: Color): Unit = setForeground(fg)
    def background: Color = getBackground
    def background_=(bg: Color): Unit = setBackground(bg)
  }

  setSize(720, 720)
  theme set themeManager
  setIgnoreRepaint(true)

  def board: Board = _board()

  def board_=(board: Board): Unit = {
    _board clear ()
    _board = new WeakReference(board)
    renderBoard()
  }
}
