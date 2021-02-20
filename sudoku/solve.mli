type solve_tpe = SetCell of int * int | Normal | Hungry

module type FULL_HOUSE = sig
  val solve : Board.board -> solve_tpe -> Board.board
end

module Full_house : FULL_HOUSE
