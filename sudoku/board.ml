open Cell
open Errors

let new_cell_list () = Array.init 81 (new cell)

class board ?cells:(cells = new_cell_list ()) _ = object (self)

  val index_from_xy = fun x y ->
    if x < 0 || x > 8 then Out_of_range (x, (0, 8)) |> raise
  ; if y < 0 || y > 8 then Out_of_range (y, (0, 8)) |> raise
  ; y*9 + x

  method private reset_row arr y value =
    for lx = 0 to 8 do
      let i = index_from_xy lx y in
      arr.(i) <- arr.(i)#clear value
    done

  method private reset_column arr x value =
    for ly = 0 to 8 do
      let i = index_from_xy x ly in
      arr.(i) <- arr.(i)#clear value
    done

  method private reset_group arr x y value =
    let gx = (x / 3) * 3
    and gy = (y / 3) * 3 in
    for dy = 0 to 2 do
      for dx = 0 to 2 do
        let i = index_from_xy (gx+dx) (gy+dy) in
        arr.(i) <- arr.(i)#clear value
      done
    done

  method get x y = cells.(index_from_xy x y)

  method set x y value =
    let the_cell = self#get x y in
    if (the_cell#is_set value) && the_cell#value = 0
    then begin
      let i = index_from_xy x y in
      let new_cells = Array.copy cells |> Array.mapi (fun j c ->
        c#clone (if j = i then value else c#value)
      ) in
      self#reset_row new_cells y value
    ; self#reset_column new_cells x value
    ; self#reset_group new_cells x y value
    ; new board ~cells:new_cells ()
    end
    else (self :> board)

  method toggle x y value =
    let i = index_from_xy x y in
    let new_cells = Array.copy cells |> Array.mapi (fun j c ->
      if j = i then c#toggle value else c#clone c#value
    ) in
    new board ~cells:new_cells ()

  method compare (other : board) =
    let rec loop i =
      let y = i / 9
      and x = i mod 9 in
      let this = self#get x y
      and that = other#get x y in
      match this#compare that with
        | 0    -> if i = 80
                  then 0
                  else loop (i+1)
        | diff -> diff
    in
    loop 0
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

let%test "Board.board#get ~-1 0 should raise an exception" =
  try (new board ())#get ~-1 0 |> ignore; false
  with Out_of_range (-1, (0, 8)) -> true

let%test "Board.board#get 9 0 should raise an exception" =
  try (new board ())#get 9 0 |> ignore; false
  with Out_of_range (9, (0, 8)) -> true

let%test "Board.board#get 0 ~-1 should raise an exception" =
  try (new board ())#get 0 ~-1 |> ignore; false
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
  let a_board = (new board ())#toggle 0 0 1 in
  let a_board = a_board#set 0 0 1 in
  for y = 0 to 8 do
    for x = 0 to 8 do
      let got = (a_board#get x y)#value in
      if got != 0
      then Failure (Printf.sprintf "cell %d,%d should be 0, got %d" x y got) |> raise
    done
  done

let%test_unit "Board.board#set_setables test to do" = ()
