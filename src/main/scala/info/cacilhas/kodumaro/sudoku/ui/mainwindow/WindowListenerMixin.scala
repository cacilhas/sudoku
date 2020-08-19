package info.cacilhas.kodumaro.sudoku.ui.mainwindow

import java.awt.event.{ComponentAdapter, ComponentEvent, ComponentListener, WindowEvent, WindowListener}

import info.cacilhas.kodumaro.sudoku.ui.BoardCanvas

private[mainwindow] trait WindowListenerMixin {
  this: BoardMixin ⇒

  addWindowListener(windowListener)
  addComponentListener(componentListener)

  protected def frmBoard: BoardCanvas

  protected def addWindowListener(listener: WindowListener)

  protected def addComponentListener(listener: ComponentListener)

  private object windowListener extends WindowListener {
    override def windowOpened(windowEvent: WindowEvent): Unit = frmBoard start ()

    override def windowClosing(windowEvent: WindowEvent): Unit = board = null

    override def windowClosed(windowEvent: WindowEvent): Unit = ()

    override def windowIconified(windowEvent: WindowEvent): Unit = frmBoard.board = null

    override def windowDeiconified(windowEvent: WindowEvent): Unit = {
      frmBoard.board = board
      frmBoard start ()
    }

    override def windowActivated(windowEvent: WindowEvent): Unit = frmBoard render ()

    override def windowDeactivated(windowEvent: WindowEvent): Unit = ()
  }

  private object componentListener extends ComponentAdapter {
    override def componentResized(e: ComponentEvent): Unit = frmBoard render ()
  }
}
