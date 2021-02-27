open Sdl

let get_bg_color x y =
  if ((x/3) + (y/3)) mod 2 = 0
  then Surface.rgb_color ~rgb:(0x5c, 0x5c, 0x5c)
  else Surface.rgb_color ~rgb:(0x9c, 0x9c, 0x9c)


let get_fg_color i = match i with
| 1 -> Surface.rgb_color ~rgb:(0xde, 0x00, 0x00)
| 2 -> Surface.rgb_color ~rgb:(0xff, 0xa0, 0x00)
| 3 -> Surface.rgb_color ~rgb:(0xff, 0xff, 0x00)
| 4 -> Surface.rgb_color ~rgb:(0x00, 0xff, 0x00)
| 5 -> Surface.rgb_color ~rgb:(0x00, 0xc0, 0xff)
| 6 -> Surface.rgb_color ~rgb:(0x60, 0x00, 0xde)
| 7 -> Surface.rgb_color ~rgb:(0xc0, 0x06, 0xff)
| 8 -> Surface.rgb_color ~rgb:(0xc0, 0x40, 0xc0)
| _ -> Surface.rgb_color ~rgb:(0xb8, 0xb8, 0xb8)


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
