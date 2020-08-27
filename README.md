[appimage]: https://appimage.org/
[author]: https://github.com/cacilhas/
[awt]: https://docs.oracle.com/javase/7/docs/api/java/awt/
[license]: https://github.com/cacilhas/sudoku/blob/master/COPYING
[linux]: https://www.gnu.org/gnu/linux-and-gnu.en.html
[macos]: https://www.apple.com/macos/
[openjdk]: https://openjdk.java.net/
[releases]: https://github.com/cacilhas/sudoku/releases/latest
[sudoku]: https://packages.debian.org/buster/sudoku
[swing]: https://docs.oracle.com/javase/7/docs/api/javax/swing/

# Kodumaro Sudoku

This is a toy project, intending explore [Swing][swing]/[AWT][awt] a bit.

It’s a GUI interface for [Console-based Sudoku][sudoku] on
[**GNU/Linux**][linux] and [**macOS**][macos] (this last untested).

## Requirement

### For running

- Java JRE 1.8 or higher (suggested: [OpenJDK][openjdk])
- Sudoku package

### For compiling

- Java JDK 1.8 or higher (suggested: [OpenJDK][openjdk])
- Scala 2.12.12
- SBT 1.3.13
- Sudoku package

Compiling:

```
sbt clean assembly
```

After this, you can find the uberjar at `target/scala-2.12/kodumaro-sudoku.jar`.

## Releases

You can get the [latest release][releases].

There are the source code (tarball or ZIP file), the uberjar, and the
[AppImage package][appimage].

If you chose the source code, you must follow the steps in the topic above,
“For compiling”.

If you chose the uberjar, you must run:

```
java -Xmx512m -jar kodumaro-sudoku.jar
```

If you chose the AppImage package, you must make it executable and run:

```
chmod +x Kodumaro_Sudoku-x86_64.AppImage
./Kodumaro_Sudoku-x86_64.AppImage
```

## Copying

### License

- [The 3-Clause BSD License][license]

### Author

- [Arĥimedeς ℳontegasppα ℭacilhας][author]
