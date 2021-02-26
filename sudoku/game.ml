open Action

type game_t = {
  mutable index : int
; mutable player : int * int
}

let g = {
  index = 0
; player = (4, 4)
}

let boards =
  let fst = Loader.create_board Loader.Hard in
  Array.init 729 (fun _ -> fst)

let the_player () = g.player

let the_board () = boards.(g.index)

let update_board new_board =
  let rec loop i =
    if (new_board#compare boards.(i)) = 0
    then g.index <- i
    else if i = g.index
    then begin
      g.index <- g.index + 1
    ; boards.(g.index) <- new_board
    end
    else loop (i+1)
  in loop 0

let act the_action = match the_action with
| MoveLeft      -> let (x, y) = g.player in g.player <- ((x + 8) mod 9, y)
| MoveRight     -> let (x, y) = g.player in g.player <- ((x + 1) mod 9, y)
| MoveUp        -> let (x, y) = g.player in g.player <- (x, (y + 8) mod 9)
| MoveDown      -> let (x, y) = g.player in g.player <- (x, (y + 1) mod 9)
| MoveCenter    -> g.player <- (4, 4)
| Restart       -> g.index <- 0
| Undo          -> g.index <- max 0 (g.index - 1)
| FullHouse tpe -> Solve.Full_house.solve (the_board ()) tpe |> update_board
| Toggle i      -> let (x, y) = g.player in
                    (the_board ())#toggle x y i |> update_board
| SetValue i    -> let (x, y) = g.player in
                    (the_board ())#set x y i |> update_board
| NewGame lv    -> g.index <- 0
                  ; g.player <- (4, 4)
                  ; let new_board = Loader.create_board lv in
                    for i = 0 to (Array.length boards) - 1 do
                      boards.(i) <- new_board
                    done
