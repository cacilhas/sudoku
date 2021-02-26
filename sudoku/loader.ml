open Board

type level = Easy | Medium | Hard | Fiendish

let string_of_level a_level = match a_level with
| Easy     -> "easy"
| Medium   -> "medium"
| Hard     -> "hard"
| Fiendish -> "fiendish"

let process_response res =
  let res = String.concat "" res
  and a_board = ref (new board ()) in
  for y = 0 to 8 do
    for x = 0 to 8 do
      let i = y*9 + x in
      let cur = (int_of_char res.[i]) - 48 in
      if cur > 0 && cur <= 9
      then a_board := (!a_board)#set x y cur
    done
  done
; !a_board

let create_board a_level =
  let command = "sudoku -g -fcompact -c" ^ (string_of_level a_level) in
  let chan = Unix.open_process_in command
  and ign = Str.regexp "%" in
  let rec loop acc =
    try let line = input_line chan in
      if not (Str.string_match ign line 0)
      then loop ((String.trim line) :: acc)
      else loop acc
    with End_of_file -> begin
      Unix.close_process_in chan |> ignore
    ; acc
    end
  in loop [] |> List.rev |> process_response
