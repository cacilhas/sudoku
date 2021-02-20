type position = w:int -> h:int -> Sdlvideo.rect


module type PIXEL_INFO = sig
  val rmask : int32
  val gmask : int32
  val bmask : int32
  val amask : int32
end


module Pixel_info : PIXEL_INFO = struct
  let rmask = 255l       (*16711680l*)
  and gmask = 65280l     (*65280l*)
  and bmask = 16711680l  (*255l*)
  and amask = -16777216l (*0l*)
end

let get_bg_color x y = match ((x/3) + (y/3)) mod 2 with
  | 0 -> (92, 92, 92)
  | _ -> (156, 156, 156)


let get_fg_color i = match i with
  | 1 -> (222,   0,   0)
  | 2 -> (255, 160,   0)
  | 3 -> (255, 255,   0)
  | 4 -> (  0, 255,   0)
  | 5 -> (  0, 192, 255)
  | 6 -> ( 96,   0, 222)
  | 7 -> (192,  96, 255)
  | 8 -> (192,  64, 192)
  | _ -> (184, 184, 184)


let create_square_surface (w, h) =
  Sdlvideo.create_RGB_surface
    [`HWSURFACE; `SRCALPHA; `SRCCOLORKEY]
    ~w:w ~h:h ~bpp:32
    ~rmask:Pixel_info.rmask
    ~gmask:Pixel_info.gmask
    ~bmask:Pixel_info.bmask
    ~amask:Pixel_info.amask


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
    let color = get_fg_color i |> Sdlvideo.map_RGB surface
    and ((lx, ly), lr) = coordinates `Center `Large i
    and ((sx, sy), sr) = coordinates `Center `Small i in
    Circle.fill_circle surface ~x:lx ~y:ly ~radius:lr color
  ; Circle.fill_circle surface ~x:sx ~y:sy ~radius:sr color
  done
; surface


let get_circle_rect tpe i =
  let ((x, y), sz) = coordinates `Top tpe i in
  Sdlvideo.rect ~x:x ~y:y ~w:sz ~h:sz


let fill_circle tpe dst rect i =
  let dst_rect = rect ~w:0 ~h:0
  and src_rect = get_circle_rect tpe i in
  Sdlvideo.blit_surface
    ~src:circles ~src_rect:src_rect
    ~dst:dst ~dst_rect:dst_rect     ()
