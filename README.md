[appimage]: https://appimage.org/
[author]: https://github.com/cacilhas/
[awt]: https://docs.oracle.com/javase/7/docs/api/java/awt/
[dune]: https://dune.readthedocs.io/en/latest/
[license]: https://github.com/cacilhas/sudoku/blob/master/COPYING
[linux]: https://www.gnu.org/gnu/linux-and-gnu.en.html
[macos]: https://www.apple.com/macos/
[ocaml]: https://ocaml.org/
[opam]: https://opam.ocaml.org/
[sudoku]: https://packages.debian.org/buster/sudoku

# Kodumaro Sudoku

This is a toy project, intending explore [Swing][swing]/[AWT][awt] a bit.

It’s a GUI interface for [Console-based Sudoku][sudoku] on
[**GNU/Linux**][linux] and [**macOS**][macos] (this last untested).

## Requirement

### For running

Nothing.

### For compiling

- An [OCaml compiler][ocaml]
- [Opam][opam]
- [Dune][dune]
- [Sudoku package][sudoku]

Compiling:

```
# In case you don’t get Dune installed yet:
opam install dune

opam install ocamlsdl ppx_inline_test
dune build
```

### Installing

Move the executable (`_build/default/bin/sudoku.exe`) to your `PATH`.

## Copying

### License

- [The 3-Clause BSD License][license]

### Author

- [Arĥimedeς ℳontegasppα ℭacilhας][author]
