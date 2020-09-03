package info.cacilhas.kodumaro.sudoku.ui

import java.awt.Color
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger

import info.cacilhas.kodumaro.sudoku.ui.mainwindow.Window

import util.chaining._
import util.Using
import swing.{Component, Graphics2D, Reactions}
import swing.event.Key.Modifier
import swing.event.{Key, KeyPressed, KeyReleased}

class Player(window: Window) extends Component {

  private val _x = new AtomicInteger(4)
  private val _y = new AtomicInteger(4)
  private val mutex = new Semaphore(1)
  implicit private val releasable: Using.Releasable[Semaphore] = (resource: Semaphore) => resource.release()

  focusable = true
  listenTo(keys)

  def x: Int = _x.get
  def y: Int = _y.get

  override def paint(g: Graphics2D): Unit =
    g.tap {_ setColor Color.white}
     .tap {_ drawRect(x*80 + window.offset.x, y*80 + window.offset.y, 80, 80)}

  def tryAcquire: Boolean = mutex.tryAcquire

  def release(): Unit = mutex.release()

  def incX(value: Int): Unit = {
    _x set (9 + x + value) % 9
    window.mustRender set true
  }

  def incY(value: Int): Unit = {
    _y set (9 + y + value) % 9
    window.mustRender set true
  }

  reactions += {
    val reactions = new Reactions.Impl
    reactions += {
      case KeyPressed(_, Key.Left, _, _)     => incX(-1)
      case KeyPressed(_, Key.Right, _, _)    => incX(1)
      case KeyPressed(_, Key.Up, _, _)       => incY(-1)
      case KeyPressed(_, Key.Down, _, _)     => incY(1)
      case KeyReleased(_, Key.Escape, _, _)  => window.close()
      case KeyReleased(_, Key.Numpad0, _, _) |
           KeyReleased(_, Key.Key0, _, _)    =>
        window.board foreach {_ (x, y) foreach {_.value = 0}}
        window.mustRender set true

      case KeyReleased(_, key, modifiers, _) => key match {
        case key =>
          (key match {
            case Key.Numpad1 |
                 Key.Numpad2 |
                 Key.Numpad3 |
                 Key.Numpad4 |
                 Key.Numpad5 |
                 Key.Numpad6 |
                 Key.Numpad7 |
                 Key.Numpad8 |
                 Key.Numpad9 =>
              Option(key.id - Key.Numpad0.id)

            case Key.Key1 |
                 Key.Key2 |
                 Key.Key3 |
                 Key.Key4 |
                 Key.Key5 |
                 Key.Key6 |
                 Key.Key7 |
                 Key.Key8 |
                 Key.Key9 =>
              Option(key.id - Key.Key0.id)

            case _ => None

          }) match {
            case Some(num) =>
              window.board match {
                case Some(board) =>
                  board(x, y) match {
                    case Some(cell) => unless(cell?) {
                      modifiers & Modifier.Control match {
                        case 0 => board(x, y) = num
                        case _ => cell toggle num
                      }
                      window.mustRender set true
                    }
                    case None => //
                  }
                case None => //
              }
            case None => //
          }
      }
    }
    reactions
  }

  private def unless(condition: => Boolean)(block: => Any): Unit = if (!condition) block
}
