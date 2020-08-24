package info.cacilhas.kodumaro.sudoku.ui

import java.awt.event.{KeyEvent, KeyListener}
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger

class Player(parent: BoardCanvas) {
  player ⇒

  private val _x = new AtomicInteger(4)
  private val _y = new AtomicInteger(4)
  private val mutex = new Semaphore(1)

  def x: Int = _x.get
  def y: Int = _y.get

  def tryAcquire: Boolean = mutex.tryAcquire

  def release(): Unit = mutex release ()

  def incX(value: Int): Unit = {
    mutex acquire ()
    try {
      _x set (9 + x + value) % 9
      parent renderBoard ()
    } finally release()
  }

  def incY(value: Int): Unit = {
    mutex acquire ()
    try {
      _y set (9 + y + value) % 9
      parent renderBoard ()
    } finally release()
  }

  object keyListener extends KeyListener {

    import KeyEvent._

    override def keyTyped(event: KeyEvent): Unit = ()

    override def keyPressed(event: KeyEvent): Unit = event.getKeyCode match {
      case VK_LEFT  ⇒ player incX -1
      case VK_RIGHT ⇒ player incX 1
      case VK_UP    ⇒ player incY -1
      case VK_DOWN  ⇒ player incY 1
      case _ ⇒ //
    }

    override def keyReleased(event: KeyEvent): Unit = event.getKeyCode match {
      case VK_ESCAPE  ⇒ parent.window close ()

      case VK_NUMPAD0 |
           VK_0       ⇒
        parent board (player.x, player.y) foreach {_.value = 0}
        parent renderBoard ()

      case keyCode ⇒
        (keyCode match {
          case VK_NUMPAD1 |
               VK_NUMPAD2 |
               VK_NUMPAD3 |
               VK_NUMPAD4 |
               VK_NUMPAD5 |
               VK_NUMPAD6 |
               VK_NUMPAD7 |
               VK_NUMPAD8 |
               VK_NUMPAD9 ⇒
            Option(keyCode - VK_NUMPAD0)

          case VK_1 |
               VK_2 |
               VK_3 |
               VK_4 |
               VK_5 |
               VK_6 |
               VK_7 |
               VK_8 |
               VK_9 ⇒
            Option(keyCode - VK_0)

          case _ ⇒ None

        }) match {
          case Some(num) ⇒
            parent board (player.x, player.y) match {
              case Some(cell) ⇒ unless (cell?) {
                if (event.isControlDown) cell toggle num
                else parent board (player.x, player.y) = num
                parent renderBoard ()
              }
              case None ⇒ //
            }

          case None ⇒ //
        }
    }

    private def unless(condition: ⇒ Boolean)(block: ⇒ Any): Unit = if (!condition) block
  }
}
