open Action

let boards =
  let fst = Loader.create_board Loader.Hard in
  Array.init 729 (fun _ -> fst)

let index = ref 0

let player = ref (4, 4)

let the_player () = !player

let the_board () = boards.(!index)

let update_board new_board =
  let rec loop i =
    if (new_board#compare boards.(i)) = 0
    then index := i
    else if i = !index
    then begin
      incr index
    ; boards.(!index) <- new_board
    end
    else loop (i+1)
  in loop 0

let act the_action = match the_action with
  | MoveLeft      -> let (x, y) = !player in player := ((x + 8) mod 9, y)
  | MoveRight     -> let (x, y) = !player in player := ((x + 1) mod 9, y)
  | MoveUp        -> let (x, y) = !player in player := (x, (y + 8) mod 9)
  | MoveDown      -> let (x, y) = !player in player := (x, (y + 1) mod 9)
  | MoveCenter    -> player := (4, 4)
  | Restart       -> index := 0
  | Undo          -> index := max 0 (!index - 1)
  | FullHouse tpe -> Solve.Full_house.solve (the_board ()) tpe |> update_board
  | Toggle i      -> let (x, y) = !player in
                     (the_board ())#toggle x y i |> update_board
  | SetValue i    -> let (x, y) = !player in
                     (the_board ())#set x y i |> update_board
  | NewGame lv    -> index := 0
                   ; player := (4, 4)
                   ; let new_board = Loader.create_board lv in
                     for i = 0 to (Array.length boards) - 1 do
                       boards.(i) <- new_board
                     done
