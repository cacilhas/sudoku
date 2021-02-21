open Cell

class board : ?cells:(cell array) -> unit -> object

  method get     : int -> int -> cell
  method set     : int -> int -> int -> board
  method toggle  : int -> int -> int -> board
  method compare : board -> int
end
