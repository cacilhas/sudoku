open Cell

class board : ?cells:(cell list) -> unit -> object

  method get : int -> int -> cell
  method set : int -> int -> int -> board
end