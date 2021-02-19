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


let create_square_surface (size:int) _ =
  Sdlvideo.create_RGB_surface
    [`HWSURFACE; `SRCALPHA; `SRCCOLORKEY]
    ~w:size ~h:size ~bpp:32
    ~rmask:Pixel_info.rmask
    ~gmask:Pixel_info.gmask
    ~bmask:Pixel_info.bmask
    ~amask:Pixel_info.amask


let large_circles =
  let surfaces = Array.init 9 (create_square_surface 84) in
  for i = 1 to 9 do
    let surface = surfaces.(i-1) in
    get_fg_color i
    |> Sdlvideo.map_RGB surface
    |> Circle.draw_circle surface ~x:42 ~y:42 ~radius:40
  done
; surfaces


let small_circles =
  let surfaces = Array.init 9 (create_square_surface 28) in
  for i = 1 to 9 do
    let surface = surfaces.(i-1) in
    get_fg_color i
    |> Sdlvideo.map_RGB surface
    |> Circle.draw_circle surface ~x:14 ~y:14 ~radius:12
  done
; surfaces


let get_circle tpe i = match tpe with
  | `Small -> small_circles.(i-1)
  | `Large -> large_circles.(i-1)


let draw_circle tpe dst rect i =
  let rect = rect ~w:0 ~h:0
  and src = get_circle tpe i in
  Sdlvideo.blit_surface ~src:src ~dst:dst ~dst_rect:rect ()
