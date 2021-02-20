open Action

let boards =
  let fst = Loader.create_board Loader.Hard in
  Array.init 81 (fun _ -> fst)

let index = ref 0

let player = ref (4, 4)

let the_player () = !player

let the_board () = boards.(!index)

let act the_action = match the_action with
  | MoveLeft      -> let (x, y) = !player in player := ((x + 8) mod 9, y)
  | MoveRight     -> let (x, y) = !player in player := ((x + 1) mod 9, y)
  | MoveUp        -> let (x, y) = !player in player := (x, (y + 8) mod 9)
  | MoveDown      -> let (x, y) = !player in player := (x, (y + 1) mod 9)
  | MoveCenter    -> player := (4, 4)
  | Undo          -> index := max 0 (!index - 1)
  | FullHouse tpe ->
      let cur = the_board () in
      let new_board = Solve.Full_house.solve cur tpe in
      if new_board != cur
      then begin
        index := !index + 1
      ; boards.(!index) <- new_board
      end
  | Toggle i      ->
      let (x, y) = !player
      and cur = the_board () in
      (cur#get x y)#toggle i
  | SetValue i    ->
      let (x, y) = !player
      and cur = the_board () in
      if (cur#get x y)#is_set i && (cur#get x y)#value = 0
      then begin
        index := !index + 1
      ; boards.(!index) <- cur#set x y i
      end
  | NewGame lv    ->
      index := 0
    ; player := (4, 4)
    ; let new_board = Loader.create_board lv in
      for i = 0 to 80 do
        boards.(i) <- new_board
      done
