package info.cacilhas.kodumaro.sudoku.ui

import java.awt._
import java.awt.event._
import java.io.{File, PrintWriter}
import javax.swing._

import info.cacilhas.kodumaro.sudoku.game.{ClassLevel, Loader}
import info.cacilhas.kodumaro.sudoku.model.Board

import io.Source

class Window extends JFrame {
  window ⇒

  import ClassLevel._

  private var _board: Board = _
  private lazy val homedir = System getProperty "user.home"
  private val theme = Theme(fg = Color.lightGray, bg = Color.black)
  private val dim = new Dimension(724, 800)

  // UI components
  private val frmBoard = new BoardCanvas(window, theme)
  private val mnFile = new Menu("File")
  private val mnNew = new Menu("New")
  private val mnHelp = new Menu("Help")
  private val itOpen = new MenuItem("Open")
  private val itSave = new MenuItem("Save")
  private val itQuit = new MenuItem("Quit")
  private val itAbout = new MenuItem("About")
  private val itEasy = new MenuItem(Easy.toString)
  private val itMedium = new MenuItem(Medium.toString)
  private val itHard = new MenuItem(Hard.toString)
  private val itPro = new MenuItem(Pro.toString)

  setTitle("Kodumaro Sudoku")
  setSize(dim)
  setMinimumSize(dim)
  setLocationRelativeTo(null)
  theme set window
  theme set getContentPane
  setFont(new Font("Noto", Font.PLAIN, 24))
  setMenuBar(new MenuBar)
  getContentPane setLayout new GridBagLayout
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

  start(itEasy, "newEasy", KeyEvent.VK_E)
  start(itMedium, "newMedium", KeyEvent.VK_N)
  start(itHard, "newHard", KeyEvent.VK_H)
  start(itPro, "newPro", KeyEvent.VK_P)
  start(itOpen, "open", KeyEvent.VK_O)
  start(itSave, "save", KeyEvent.VK_S)
  start(itQuit, "quit", KeyEvent.VK_Q)
  start(itAbout, "about")

  mnNew setFont getFont
  mnNew add itEasy
  mnNew add itMedium
  mnNew add itHard
  mnNew add itPro

  mnHelp add itAbout

  mnFile setFont getFont
  mnFile add mnNew
  mnFile add itOpen
  mnFile add itSave
  mnFile addSeparator ()
  mnFile add itQuit

  getMenuBar add mnFile
  getMenuBar add mnHelp

  board = Board()
  add(frmBoard)
  addWindowListener(_windowListener)
  addComponentListener(_componentListener)

  def board: Board = _board

  def close(): Unit = dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING))

  private lazy val version = Option(getClass.getPackage.getImplementationVersion) getOrElse "1.0"

  private def about(): Unit = {
    JOptionPane showMessageDialog (
      window,
      s"""$getTitle $version
         |
         |Swing UI for Console-based Sudoku using colours instead of numbers.
         |Arĥimedeς ℳontegasppα ℭacilhας <batalema@cacilhas.info>
         |""".stripMargin,
      getTitle,
      JOptionPane.INFORMATION_MESSAGE,
    )
  }

  private[this] def board_=(board: Board): Unit = {
    _board = board
    frmBoard.board = board
    // Force garbage collector to work
    val rt = Runtime.getRuntime
    rt gc ()
    rt runFinalization ()
  }

  private[this] def start(item: MenuItem, action: String, shortcut: Int = -1): Unit = {
    item setFont getFont
    item setActionCommand action
    item addActionListener actionListener
    if (shortcut != -1) item setShortcut new MenuShortcut(shortcut)
  }

  object fileManager {

    def save: Unit = try {
      if (board != null) {
        val chooser = getChooser
        if (chooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
          val file = chooser.getSelectedFile
          val writer = new PrintWriter(file)
          try writer write s"$board"
          finally writer close ()
          JOptionPane showMessageDialog (window, s"Board saved to $file", getTitle, JOptionPane.INFORMATION_MESSAGE)
        }
      } else
        JOptionPane showMessageDialog (window, s"No board to save", getTitle, JOptionPane.WARNING_MESSAGE)
    } catch {
      case exc: Throwable ⇒
        JOptionPane showMessageDialog (window, s"Could not save board: $exc", getTitle, JOptionPane.ERROR_MESSAGE)
    }

    def open: Unit = try {
      val chooser = getChooser
      if (chooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
        val source = Source fromFile chooser.getSelectedFile
        try board = Board(source.getLines mkString "\n")
        finally source.close
      }
    } catch {
      case exc: Throwable ⇒
        JOptionPane showMessageDialog (window, s"Could not open file: $exc", getTitle, JOptionPane.ERROR_MESSAGE)
    }

    private def getChooser: JFileChooser = {
      val chooser = new JFileChooser
      chooser setCurrentDirectory new File(homedir)
      chooser setDialogTitle getTitle
      chooser
    }
  }

  /*
   * Listeners
   */

  private object actionListener extends ActionListener {

    override def actionPerformed(event: ActionEvent): Unit = event.getActionCommand match {
      case "quit"      ⇒ close()
      case "open"      ⇒ fileManager.open
      case "save"      ⇒ fileManager.save
      case "about"     ⇒ about()
      case "newEasy"   ⇒ board = Loader(Easy)
      case "newMedium" ⇒ board = Loader(Medium)
      case "newHard"   ⇒ board = Loader(Hard)
      case "newPro"    ⇒ board = Loader(Pro)
      case _           ⇒ //
    }
  }

  private object _windowListener extends WindowListener {
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

  private object _componentListener extends ComponentAdapter {
    override def componentResized(e: ComponentEvent): Unit = frmBoard render ()
  }
}
