open Cell

type full_house = Group of int * int | Normal | Hungry

class board : ?cells:(cell list) -> unit -> object

  method get           : int -> int -> cell
  method set           : int -> int -> int -> board
  method set_settables : full_house -> board
end
