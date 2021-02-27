type t = [`SetCell of (int * int) | `Normal | `Hungry]

module type FULL_HOUSE = sig
  val solve : Board.board -> t -> Board.board
end

module Full_house : FULL_HOUSE
