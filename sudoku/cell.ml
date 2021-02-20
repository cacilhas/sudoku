open Errors

class cell ?value:(bound = 0) _ = object (self)

  val candidates = Array.init 9 succ |> Array.map char_of_int

  val validate_index =
    let valid = fun i -> i >= 1 && i <= 9 in
    fun i -> if not (valid i) then Out_of_range (i, (1, 9)) |> raise

  initializer
    if bound != 0
    then validate_index bound

  method private set' i value =
    validate_index i
  ; if not (value = 0 || value = i)
    then Out_of_range (value, (0, i)) |> raise
  ; candidates.(i-1) <- char_of_int value

  method clear i = self#set' i 0

  method is_set i =
    validate_index i
  ; int_of_char candidates.(i-1) = i

  method set i = self#set' i i

  method settable =
    if bound = 0
    then begin
      let valids = Array.to_list candidates
                   |> List.map int_of_char
                   |> List.filter ((!=) 0) in
      if (List.length valids) = 1
      then List.nth valids 0
      else 0
    end
    else 0

  method toggle i =
    if self#is_set i
    then self#clear i
    else self#set i

  method value = bound

  method string = match bound with
    | 0     -> "."
    | bound -> string_of_int bound

  method clone value =
    if not (value > 0 && self#is_set value)
    then (self :> cell)
    else begin
      let the_clone = new cell ~value:value 0 in
      for i = 1 to 9 do
        if not (self#is_set i)
        then the_clone#clear i
      done
    ; the_clone
    end

end

(*******************************************************************************
 * Tests
 *)

let%test "Cell.cell should start zeroed" =
  (new cell 0)#value = 0

let%test "Cell.cell with supplied value" =
  (new cell ~value:2 0)#value = 2

let%test "Cell.cell should fail with invalid value" =
  try
    new cell ~value:(-1) 0 |> ignore
  ; false
  with Out_of_range (-1, (1, 9)) -> true

let%test_unit "Cell.cell#is_set i should return whether the index is set" =
  let a_cell = new cell 0 in
  for i = 1 to 9 do
    if not (a_cell#is_set i)
    then Failure (Printf.sprintf "expected index %d to be set" i) |> raise
  done

let%test "Cell.cell#is_set (negative number) should fail" =
  try (new cell 0)#is_set (-1) && false
  with Out_of_range (-1, (1, 9)) -> true

let%test "Cell.cell#is_set (greater number) should fail" =
  try (new cell 0)#is_set 10 && false
  with Out_of_range (10, (1, 9)) -> true

let%test_unit "Cell.cell#clear i should clear index" =
  let a_cell = new cell 0 in
  a_cell#clear 4
; for i = 1 to 9 do
    if i = 4
    then begin
      if a_cell#is_set i
      then Failure (Printf.sprintf "expected index %d to be clear" i) |> raise
    end
    else if not (a_cell#is_set i)
         then Failure (Printf.sprintf "expected index %d to be set" i) |> raise
  done

let%test "Cell.cell#set i should set index" =
  let a_cell = new cell 0 in
  a_cell#clear 5
; a_cell#set 5
; a_cell#is_set 5

let%test "Cell.cell#settable must return 0 for multiple candidates" =
  (new cell 0)#settable = 0

let%test "Cell.cell#settable must return 0 for set cell" =
  let a_cell = new cell 5 in
  for i = 1 to 9 do
    a_cell#clear i|> ignore
  done
; a_cell#settable = 0

let%test "Cell.cell#settable must return the single candidate" =
  let a_cell = new cell 0 in
  for i = 1 to 9 do
    a_cell#clear i|> ignore
  done
; a_cell#set 4
; a_cell#settable = 4

let%test_unit "Cell.cell#toggle i should toggle index" =
  let a_cell = new cell 0 in
  a_cell#toggle 8
; for i = 1 to 9 do
    if i = 8
    then begin
      if a_cell#is_set i
      then Failure (Printf.sprintf "expected index %d to be clear" i) |> raise
    end
    else if not (a_cell#is_set i)
         then Failure (Printf.sprintf "expected index %d to be set" i) |> raise
  done
; a_cell#toggle 8
; if not (a_cell#is_set 8)
  then Failure "expected index 8 to be set back" |> raise

let%test "Cell.cell#string should return dot for an unset cell" =
  (new cell 0)#string = "."

let%test "Cell.cell#string should return the digit for a set cell" =
  (new cell ~value:2 0)#string = "2"
  && (new cell ~value:5 0)#string = "5"

let%test "Cell.cell#clone value should return a new cell with the new value" =
  let origin = new cell 0 in
  let clone = origin#clone 6 in
  origin#value = 0 && clone#value = 6

let%test "Cell.cell.#clone value shouldn‘t clone a cell with clear value" =
  let origin = new cell 0 in
  origin#clear 6
; let clone = origin#clone 6 in
  origin#value = 0 && clone = origin
