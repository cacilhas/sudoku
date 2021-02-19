let get_bg_color x y = match ((x/3) + (y/3)) mod 2 with
  | 0 -> (64, 64, 64)
  | _ -> (192, 192, 192)


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
    ~rmask:255l ~gmask:65280l ~bmask:16711680l ~amask:(-16777216l)


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
