open Sdl

let get_bg_color x y = match ((x/3) + (y/3)) mod 2 with
| 0 -> 0x5c5c5cffl
| _ -> 0x9c9c9cffl


let get_fg_color i = match i with
| 1 -> 0xde0000ffl
| 2 -> 0xffa000ffl
| 3 -> 0xffff00ffl
| 4 -> 0x00ff00ffl
| 5 -> 0x00c0ffffl
| 6 -> 0x6000deffl
| 7 -> 0xc060ffffl
| 8 -> 0xc040c0ffl
| _ -> 0xb8b8b8ffl


let create_square_surface (w, h) =
  Surface.create_rgb ~width:w ~height:h ~depth:32


let coordinates (where : [`Top | `Center]) (tpe : [`Large | `Small]) (i : int)
: (int * int) * int =
  match (where, tpe) with
  | (`Top,    `Small) -> ((26*(i-1),      82), 26)
  | (`Top,    `Large) -> ((82*(i-1),       0), 82)
  | (`Center, `Small) -> ((26*(i-1) + 13, 95), 12)
  | (`Center, `Large) -> ((82*(i-1) + 41, 41), 40)


let circles =
  let surface = create_square_surface (756, 112) in
  for i = 1 to 9 do
    let color = get_fg_color i
    and ((lx, ly), lr) = coordinates `Center `Large i
    and ((sx, sy), sr) = coordinates `Center `Small i in
    Circle.fill_circle surface ~pos:(lx, ly) ~radius:lr ~color:color
  ; Circle.fill_circle surface ~pos:(sx, sy) ~radius:sr ~color:color
  done
; surface


let get_circle_rect tpe i =
  let ((x, y), sz) = coordinates `Top tpe i in
  Rect.make ~pos:(x, y) ~dims:(sz, sz)


let fill_circle tpe dst rect i =
  let dst_rect = rect ~dims:(0, 0)
  and src_rect = get_circle_rect tpe i in
  Surface.blit_surface
    ~src:circles ~src_rect:src_rect
    ~dst:dst ~dst_rect:dst_rect
  |> ignore
