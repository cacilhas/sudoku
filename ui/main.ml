open Sdlkey
open Sudoku



let deal_with_press (sym, kmod) = match sym with
  | KEY_ESCAPE -> exit 0

  | KEY_UP
  | KEY_w     -> Game.act Action.MoveUp
  | KEY_DOWN
  | KEY_s     -> Game.act Action.MoveDown
  | KEY_LEFT
  | KEY_a     -> Game.act Action.MoveLeft
  | KEY_RIGHT
  | KEY_d     -> Game.act Action.MoveRight
  | KEY_c     -> Game.act Action.MoveCenter

  | KEY_u -> Game.act Action.Undo
  | KEY_1 -> if kmod land (kmod_lctrl lor kmod_rctrl) != 0
             then Game.act (Action.NewGame Loader.Easy)
  | KEY_2 -> if kmod land (kmod_lctrl lor kmod_rctrl) != 0
             then Game.act (Action.NewGame Loader.Medium)
  | KEY_3
  | KEY_n -> if kmod land (kmod_lctrl lor kmod_rctrl) != 0
             then Game.act (Action.NewGame Loader.Hard)
  | KEY_4 -> if kmod land (kmod_lctrl lor kmod_rctrl) != 0
             then Game.act (Action.NewGame Loader.Fiendish)

  | KEY_KP1 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 1)
               else Game.act (Action.SetValue 1)
  | KEY_KP2 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 2)
               else Game.act (Action.SetValue 2)
  | KEY_KP3 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 3)
               else Game.act (Action.SetValue 3)
  | KEY_KP4 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 4)
               else Game.act (Action.SetValue 4)
  | KEY_KP5 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 5)
               else Game.act (Action.SetValue 5)
  | KEY_KP6 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 6)
               else Game.act (Action.SetValue 6)
  | KEY_KP7 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 7)
               else Game.act (Action.SetValue 7)
  | KEY_KP8 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 8)
               else Game.act (Action.SetValue 8)
  | KEY_KP9 -> if kmod land (kmod_lshift lor kmod_rshift) = 0
               then Game.act (Action.Toggle 9)
               else Game.act (Action.SetValue 9)

  | _ -> ()


let draw_cell screen x y (cell : Cell.cell) =
  if cell#value = 0
  then for dy = 0 to 2 do
         for dx = 0 to 2 do
           let i = dy*3 + dx + 1 in
           if cell#is_set i
           then let pos = Sdlvideo.rect ~x:(x*84 + dx*28) ~y:(y*84 + (2-dy)*28)
                in Assets.draw_circle `Small screen pos i
         done
       done
  else let pos = Sdlvideo.rect ~x:(x*84) ~y:(y*84) in
       Assets.draw_circle `Large screen pos cell#value


let draw_table screen =
  for y = 0 to 8 do
    for x = 0 to 8 do
      let rect = Sdlvideo.rect ~x:(x*84+1) ~y:(y*84+1) ~w:82 ~h:82
      and color = (Sdlvideo.map_RGB screen (Assets.get_bg_color x y)) in
      Sdlvideo.fill_rect ~rect:rect screen color
    done
  done


let draw_board screen (board : Board.board) =
  draw_table screen
; for y = 0 to 8 do
    for x = 0 to 8 do
      board#get x y |> draw_cell screen x y
    done
  done


let draw_player screen (x, y) =
  let rect = Sdlvideo.rect ~x:(x*84) ~y:(y*84) ~w:84 ~h:84
  and color = (Sdlvideo.map_RGB screen Sdlvideo.white) in
  Sdlvideo.fill_rect ~rect:rect screen color


let rec loop screen =
  Sdlvideo.fill_rect screen (Sdlvideo.map_RGB screen Sdlvideo.black)
; Game.the_player () |> draw_player screen
; Game.the_board  () |> draw_board screen
; Sdlvideo.flip screen
; begin
    match Sdlevent.wait_event () with
      | Sdlevent.KEYDOWN evt -> deal_with_press (evt.keysym, evt.keymod)
      | _                    -> ()
  end
; loop screen


let mainloop () =
  Sdl.init [`VIDEO]
; at_exit Sdl.quit
; Sdlwm.set_caption ~title:"Kodumaro Sudoku" ~icon:"sudoku"
; Sdlevent.enable_events Sdlevent.keydown_mask
; Sdlvideo.set_video_mode ~w:756 ~h:756 [`DOUBLEBUF] |> loop
