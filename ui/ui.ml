open Sdl
open Sudoku

type size_t = { w : int ; h : int }
let tsize   = { w = 756 ; h = 756 }


let deal_with_press kmod = function
| Scancode.ESCAPE -> exit 0
| Scancode.UP
| Scancode.W     -> Game.act `MoveUp
| Scancode.DOWN
| Scancode.S     -> Game.act `MoveDown
| Scancode.LEFT
| Scancode.A     -> Game.act `MoveLeft
| Scancode.RIGHT
| Scancode.D     -> Game.act `MoveRight
| Scancode.C     -> Game.act `MoveCenter
| Scancode.U     -> Game.act `Undo
| Scancode.R     -> if List.mem Keymod.LCtrl kmod
                    || List.mem Keymod.RCtrl kmod
                    then Game.act `Restart
| Scancode.Num1  -> if List.mem Keymod.LCtrl kmod
                    || List.mem Keymod.RCtrl kmod
                    then Game.act (`NewGame `Easy)
| Scancode.Num2  -> if List.mem Keymod.LCtrl kmod
                    || List.mem Keymod.RCtrl kmod
                    then Game.act (`NewGame `Medium)
| Scancode.Num3
| Scancode.N     -> if List.mem Keymod.LCtrl kmod
                    || List.mem Keymod.RCtrl kmod
                    then Game.act (`NewGame `Hard)
| Scancode.Num4  -> if List.mem Keymod.LCtrl kmod
                    || List.mem Keymod.RCtrl kmod
                    then Game.act (`NewGame `Fiendish)
| Scancode.SPACE -> `FullHouse (
                      if List.mem Keymod.LCtrl kmod
                      || List.mem Keymod.RCtrl kmod
                      then `Hungry
                      else if List.mem Keymod.LShift kmod
                           || List.mem Keymod.RShift kmod
                      then `Normal
                      else let (x, y) = Game.the_player () in
                      `SetCell (x, y)
                    ) |> Game.act
| Scancode.KP_1  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 1)
                    else Game.act (`SetValue 1)
| Scancode.KP_2  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 2)
                    else Game.act (`SetValue 2)
| Scancode.KP_3  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 3)
                    else Game.act (`SetValue 3)
| Scancode.KP_4  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 4)
                    else Game.act (`SetValue 4)
| Scancode.KP_5  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 5)
                    else Game.act (`SetValue 5)
| Scancode.KP_6  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 6)
                    else Game.act (`SetValue 6)
| Scancode.KP_7  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 7)
                    else Game.act (`SetValue 7)
| Scancode.KP_8  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 8)
                    else Game.act (`SetValue 8)
| Scancode.KP_9  -> if List.mem Keymod.LShift kmod
                    || List.mem Keymod.RShift kmod
                    then Game.act (`Toggle 9)
                    else Game.act (`SetValue 9)
| _ -> ()


let render_cell screen x y (cell : Cell.cell) =
  if cell#value = 0
  then for dy = 0 to 2 do
         for dx = 0 to 2 do
           let i = dy*3 + dx + 1 in
           if cell#is_set i
           then let pos = Rect.make ~pos:(x*84 + dx*28, y*84 + (2-dy)*28)
                in Assets.fill_circle `Small screen pos i
         done
       done
  else let pos = Rect.make ~pos:(x*84, y*84) in
       Assets.fill_circle `Large screen pos cell#value

let render_bg surface =
  for y = 0 to 8 do
    for x = 0 to 8 do
      let rect = Rect.make ~pos:(x*84+1, y*84+1) ~dims:(82, 82) in
      Surface.fill_rect ~dst:surface ~rect:rect ~color:(Assets.get_bg_color x y)
    done
  done

let render_board surface (board : Board.board) =
  render_bg surface
; for y = 0 to 8 do
    for x = 0 to 8 do
      board#get x y |> render_cell surface x y
    done
  done

let render_player screen (x, y) =
  let rect = Rect.make ~pos:(x*84, y*84) ~dims:(84, 84) in
  Surface.fill_rect ~dst:screen ~rect:rect ~color:0xffffffffl

let render_table screen table =
  let width = Surface.get_width screen
  and height = Surface.get_height screen in
  let x = (width - tsize.w) / 2
  and y = (height - tsize.h) / 2 in
  let dst_rect = Rect.make ~pos:(x, y) ~dims:(tsize.w, tsize.h)
  and src_rect = Rect.make ~pos:(0, 0) ~dims:(tsize.w, tsize.h) in
  Surface.blit_surface ~src:table  ~src_rect:src_rect
                       ~dst:screen ~dst_rect:dst_rect
  |> ignore


let rec loop window table =
  let screen = Window.get_surface window in
  let bg_rect = Rect.make ~pos:(0, 0) ~dims:(tsize.w, tsize.h) in
  Surface.fill_rect ~dst:screen ~rect:bg_rect ~color:0xff000000l
; Surface.fill_rect ~dst:table  ~rect:bg_rect ~color:0xff000000l
; Game.the_player () |> render_player table
; Game.the_board  () |> render_board table
; render_table screen table
; Window.update_surface window
; begin
    match Event.poll_event () with
    | Some (Event.KeyDown evt) -> deal_with_press evt.keymod evt.scancode
    | _                        -> ()
  end
; loop window table


let mainloop () =
  Init.init [`VIDEO]
; at_exit Quit.quit
; let window = Window.create
               ~title:"Kodumaro Sudoku"
               ~pos:(`centered, `centered) ~dims:(tsize.w, tsize.h)
               ~flags:[Window.Resizable; Window.Shown] in
  at_exit (fun () -> Window.destroy window)
; let table = Surface.create_rgb ~width:tsize.w ~height:tsize.h ~depth:32 in
  loop window table
