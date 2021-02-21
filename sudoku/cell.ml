open Errors

class cell ?value:(bound = 0) _ = object (self)

  val candidates = Array.init 9 succ |> Array.map char_of_int

  val validate_index =
    let valid = fun i -> i >= 1 && i <= 9 in
    fun i -> if not (valid i) then Out_of_range (i, (1, 9)) |> raise

  initializer
    if bound != 0
    then validate_index bound

  method candidates_from origin =
    for i = 0 to 8 do
      candidates.(i) <- origin.(i)
    done
    ; (self :> cell)

  method private set' i value =
    validate_index i
  ; if not (value = 0 || value = i)
    then Out_of_range (value, (0, i)) |> raise
  ; let new_candidates = Array.copy candidates in
    new_candidates.(i-1) <- char_of_int value
  ; (new cell ~value:bound 0)#candidates_from new_candidates

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
    else (new cell ~value:value 0)#candidates_from candidates

  method compare (other : cell) =
    match compare bound other#value with
      | 0    -> let rec loop i =
                  let this = (if self#is_set i  then i else 0)
                  and that = (if other#is_set i then i else 0) in
                  let diff = compare this that in
                  if diff != 0
                  then diff
                  else if i = 9
                  then 0
                  else loop (i+1)
                in
                loop 1
      | diff -> diff
end


(*******************************************************************************
 * Tests
 *)

let%test "Cell.cell should start zeroed" =
  (new cell 0)#value = 0

let%test "Cell.cell with supplied value" =
  (new cell ~value:2 0)#value = 2

let%test "Cell.cell should fail with invalid value" =
  try new cell ~value:~-1 0 |> ignore; false
  with Out_of_range (-1, (1, 9)) -> true

let%test_unit "Cell.cell#is_set i should return whether the index is set" =
  let a_cell = new cell 0 in
  for i = 1 to 9 do
    if not (a_cell#is_set i)
    then Failure (Printf.sprintf "expected index %d to be set" i) |> raise
  done

let%test "Cell.cell#is_set (negative number) should fail" =
  try (new cell 0)#is_set ~-1 && false
  with Out_of_range (-1, (1, 9)) -> true

let%test "Cell.cell#is_set (greater number) should fail" =
  try (new cell 0)#is_set 10 && false
  with Out_of_range (10, (1, 9)) -> true

let%test_unit "Cell.cell#clear i should clear index" =
  let a_cell = (new cell 0)#clear 4 in
  for i = 1 to 9 do
    if i = 4
    then begin
      if a_cell#is_set i
      then Failure (Printf.sprintf "expected index %d to be clear" i) |> raise
    end
    else if not (a_cell#is_set i)
         then Failure (Printf.sprintf "expected index %d to be set" i) |> raise
  done

let%test "Cell.cell#set i should set index" =
  (((new cell 0)#clear 5)#set 5)#is_set 5

let%test "Cell.cell#settable must return 0 for multiple candidates" =
  (new cell 0)#settable = 0

let%test "Cell.cell#settable must return 0 for set cell" =
  let a_cell = Array.map char_of_int [|0; 0; 0; 0; 5; 0; 0; 0; 0|]
               |> (new cell ~value:5 0)#candidates_from in
  a_cell#settable = 0

let%test "Cell.cell#settable must return the single candidate" =
  let a_cell = Array.map char_of_int [|0; 0; 0; 4; 0; 0; 0; 0; 0|]
               |> (new cell 0)#candidates_from in
  a_cell#settable = 4

let%test_unit "Cell.cell#toggle i should toggle index" =
  let a_cell = (new cell 0)#toggle 8 in
  for i = 1 to 9 do
    if i = 8
    then begin
      if a_cell#is_set i
      then Failure (Printf.sprintf "expected index %d to be clear" i) |> raise
    end
    else if not (a_cell#is_set i)
         then Failure (Printf.sprintf "expected index %d to be set" i) |> raise
  done
; let a_cell = a_cell#toggle 8 in
  if not (a_cell#is_set 8)
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

let%test "Cell.cell#clone value shouldn‘t clone a cell with clear value" =
  let origin = (new cell 0)#clear 6 in
  let clone = origin#clone 6 in
  origin#value = 0 && clone = origin

let%test "Cell.cell#compare same should return 0" =
  let fst = new cell 0
  and snd = new cell 0 in
  (fst#compare snd) = 0

let%test "Cell.cell#compare #2 should return -1" =
  let fst = new cell 0
  and snd = new cell ~value:2 0 in
  (fst#compare snd) = -1

let%test "Cell.cell#compare #0 should return 1" =
  let fst = new cell ~value:2 0
  and snd = new cell 0 in
  (fst#compare snd) = 1

let%test "Cell.cell#compare #3 should return -1" =
  let fst = new cell ~value:2 0
  and snd = new cell ~value:3 0 in
  (fst#compare snd) = -1

let%test "Cell.cell#compare same clear should return 0" =
  let fst = (new cell 0)#clear 2
  and snd = (new cell 0)#clear 2 in
  (fst#compare snd) = 0

let%test "Cell.cell#compare different clear should return 0" =
  let fst = (new cell 0)#clear 3
  and snd = (new cell 0)#clear 4 in
  (fst#compare snd) = -1

let%test "Cell.cell#compare same value, different clear should return 0" =
  let fst = (new cell ~value:5 0)#clear 3
  and snd = (new cell ~value:5 0)#clear 4 in
  (fst#compare snd) = -1
