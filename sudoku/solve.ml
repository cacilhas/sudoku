type solve_tpe = Group of int * int | Normal | Hungry

module type FULL_HOUSE = sig
  val solve : Board.board -> solve_tpe -> Board.board
end

module Full_house : FULL_HOUSE = struct

  let xy_from_index idx = (idx mod 9, idx / 9)

  let solve_group board (gx, gy) =
    let res = ref board
    and x0 = (gx / 3) * 3
    and y0 = (gy / 3) * 3 in
    for y = y0 to y0+2 do
      for x = x0 to x0+2 do
        let value = (board#get x y)#settable in
        if value != 0
        then res := (!res)#set x y value
      done
    done
  ; !res

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
      begin
        match (cur#get x y)#settable with
          | 0     -> if idx = 80
                     then cur
                     else loop cur (idx + 1)
          | value -> loop (cur#set x y value) 0
      end
    in loop board 0

  let solve board tpe = match tpe with
    | Group (gx, gy) -> solve_group board (gx, gy)
    | Normal         -> solve_normal board
    | Hungry         -> solve_hungry board
end
