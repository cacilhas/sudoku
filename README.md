[appimage]: https://appimage.org/
[author]: https://github.com/cacilhas/
[awt]: https://docs.oracle.com/javase/7/docs/api/java/awt/
[dune]: https://dune.readthedocs.io/en/latest/
[license]: https://github.com/cacilhas/sudoku/blob/master/COPYING
[linux]: https://www.gnu.org/gnu/linux-and-gnu.en.html
[macos]: https://www.apple.com/macos/
[make]: https://www.gnu.org/software/make/manual/make.html
[ocaml]: https://ocaml.org/
[opam]: https://opam.ocaml.org/
[sudogo]: https://github.com/cacilhas/sudogo
[sudoku]: https://github.com/cinemast/sudoku
[sudoku-deb]: https://packages.debian.org/buster/sudoku

# Kodumaro Sudoku [DEPRECATED]

> **Deprecated message**
>
> Replaced by [Sudogo][sudogo].

This is a GUI interface for Michael Kennett’s [Console-based Sudoku][sudoku] on
[**GNU/Linux**][linux] and [**macOS**][macos] (this last untested).

## Requirement

### For running

Nothing.

### For compiling

- [GNU `make`][make]
- An [OCaml compiler][ocaml]
- [Opam][opam]
- [Dune][dune]
- [Sudoku package][sudoku-deb]

Compiling:

```sh
# In case you don’t get Dune installed yet:
opam install dune

# Install dependencies
opam install ocamlsdl ppx_inline_test

# Compile:
make
```

### Installing

Install only for the current user:

```sh
make install
```

Install for all users:

```sh
sudo make PREFIX=/usr/local install
```

## Playing

The help text is shown by the command:

```sh
kodumaro-sudoku -h
# or
kodumaro-sudoku --help
```

Keys:

- Keypad numbers (`KP1`, `KP2`…): change the candidate flag.
- Keypad numbers with Shift (`S-KP1`, `S-KP2`…): set the cell value.
- Arrows or WASD (`w`, `a`, `s`, `d`): move the player position.
- C-key (`c`): center the player.
- U-key (`u`): undo value set.
- R-key with Ctrl (`C-r`): restart current game.
- N-key with Ctrl (`C-n`): new hard game.
- 1-key with Ctrl (`C-1`): new easy game.
- 2-key with Ctrl (`C-2`): new medium game.
- 3-key with Ctrl (`C-3`): new hard game.
- 4-key with Ctrl (`C-4`): new fiendish game.
- Space (`space`): set current cell if it’s a single-candidate.
- Space with Shift (`S-space`): run Full House algorithm for the entire board.
- Space with Shift and Ctrl (`C-S-space`): run aggressive Full House algorithm.
- Esc (`escape`): quit.

## Copying

### License

- [The 3-Clause BSD License][license]

### Author

- [Arĥimedeς ℳontegasppα ℭacilhας][author]
