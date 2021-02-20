open Cell
open Errors

type full_house = Group of int * int | Normal | Hungry

let new_cell_list () = List.init 81 (new cell)

class board ?cells:(cells = new_cell_list ()) _ = object (self)

  val index_from_xy = fun x y -> begin
    if x < 0 || x > 8 then Out_of_range (x, (0, 8)) |> raise
  ; if y < 0 || y > 8 then Out_of_range (y, (0, 8)) |> raise
  ; y*9 + x
  end

  val xy_from_index = fun idx -> (idx mod 9, idx / 9)

  method get x y = index_from_xy x y |> List.nth cells

  method set x y value =
    if (self#get x y)#is_set value
    then begin
      let i = index_from_xy x y in
      let new_cells = cells |> List.mapi (fun j c ->
        if j = i
        then c#clone value
        else c#clone c#value
      ) in
      for lx = 0 to 8 do
        let cur = index_from_xy lx y |> List.nth new_cells in
        cur#clear value
      done
    ; for ly = 0 to 8 do
        let cur = index_from_xy x ly |> List.nth new_cells in
        cur#clear value
      done
    ; let gx = (x / 3) * 3
      and gy = (y / 3) * 3 in
      for dy = 0 to 2 do
        for dx = 0 to 2 do
          let cur = index_from_xy (gx+dx) (gy+dy) |> List.nth new_cells in
          cur#clear value
        done
      done
    ; new board ~cells:new_cells ()

    end
    else (self :> board)

  method set_settables tpe =
    match tpe with

      | Group (px, py) ->
        let res = ref (self :> board)
        and x0 = (px / 3) * 3
        and y0 = (py / 3) * 3 in
        for y = y0 to y0+2 do
          for x = x0 to x0+2 do
            let value = (self#get x y)#settable in
            if value != 0
            then res := (!res)#set x y value
          done
        done
      ; !res

      | Normal ->
        let res = ref (self :> board) in
        for y = 0 to 8 do
          for x = 0 to 8 do
            let value = (self#get x y)#settable in
            if value != 0
            then res := (!res)#set x y value
          done
        done
      ; !res

      | Hungry ->
        let rec loop cur idx =
          let (x, y) = xy_from_index idx in
          begin
            match (cur#get x y)#settable with
              | 0     -> if idx = 80
                         then cur
                         else loop cur (idx + 1)
              | value -> loop (cur#set x y value) 0
          end
        in loop (self :> board) 0

end


(*******************************************************************************
 * Tests
 *)

let%test_unit "Board.board should start zeroed" =
  let a_board = new board () in
  for y = 0 to 8 do
    for x = 0 to 8 do
      let got = (a_board#get x y)#value in
      if got != 0
      then Failure (Printf.sprintf "cell %d,%d expected to be zero, got %d" x y got) |> raise
    done
  done

let%test "Board.board#get (-1) 0 should raise an exception" =
  try (new board ())#get (-1) 0 |> ignore; false
  with Out_of_range (-1, (0, 8)) -> true

let%test "Board.board#get 9 0 should raise an exception" =
  try (new board ())#get 9 0 |> ignore; false
  with Out_of_range (9, (0, 8)) -> true

let%test "Board.board#get 0 (-1) should raise an exception" =
  try (new board ())#get 0 (-1) |> ignore; false
  with Out_of_range (-1, (0, 8)) -> true

let%test "Board.board#get 0 9 should raise an exception" =
  try (new board ())#get 0 9 |> ignore; false
  with Out_of_range (9, (0, 8)) -> true

let%test_unit "Board.board#set 3 4 5 should return a new board" =
  let a_board = (new board ())#set 3 4 5 in
  for y = 0 to 8 do
    for x = 0 to 8 do
      let the_cell = a_board#get x y in
      if x = 3 || y = 4 || (x/3 = 1 && y/3 = 1)
      then begin
        if the_cell#is_set 5
        then Failure (Printf.sprintf "cell %d,%d should be unset" x y) |> raise
      end
      else if not (the_cell#is_set 5)
           then Failure (Printf.sprintf "cell %d,%d should be set" x y) |> raise
    done
  done
; if (a_board#get 3 4)#value != 5
  then Failure "cell 3,4 should have value 5" |> raise

let%test_unit "Board.board#set 0 0 1 should fail if cell is unset" =
  let a_board = new board () in
  (a_board#get 0 0)#clear 1
; let a_board = a_board#set 0 0 1 in
  for y = 0 to 8 do
    for x = 0 to 8 do
      let got = (a_board#get x y)#value in
      if got != 0
      then Failure (Printf.sprintf "cell %d,%d should be 0, got %d" x y got) |> raise
    done
  done

let%test_unit "Board.board#set_setables test to do" = ()
