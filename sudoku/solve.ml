type t = [%import: Solve.t]
module type FULL_HOUSE = [%import: (module Solve.FULL_HOUSE)]

module Full_house : FULL_HOUSE = struct

  let xy_from_index idx = (idx mod 9, idx / 9)

  let solve_cell board (x, y) =
    let value = (board#get x y)#settable in
    if value = 0
    then board
    else board#set x y value

  let solve_normal board =
    let res = ref board in
    for y = 0 to 8 do
      for x = 0 to 8 do
        let value = (board#get x y)#settable in
        if value != 0
        then res := (!res)#set x y value
      done
    done
  ; !res

  let solve_hungry board =
    let rec loop cur idx =
      let (x, y) = xy_from_index idx in
      let value = (cur#get x y)#settable in
      if value = 0
      then begin
        if idx = 80
        then cur
        else loop cur (idx + 1)
      end
      else loop (cur#set x y value) 0
    in loop board 0

  let solve board tpe = match tpe with
  | `SetCell (x, y) -> solve_cell board (x, y)
  | `Normal         -> solve_normal board
  | `Hungry         -> solve_hungry board
end
