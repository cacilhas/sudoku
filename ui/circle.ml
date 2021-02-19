let alter_color surface color factor =
  let r = Int32.unsigned_rem color 256l
          |> Int32.to_float |> ( *.) factor |> int_of_float
  and g = Int32.unsigned_rem (Int32.unsigned_div color 256l) 256l
          |> Int32.to_float |> ( *.) factor |> int_of_float
  and b = Int32.unsigned_rem (Int32.unsigned_div color 65536l) 256l
          |> Int32.to_float |> ( *.) factor |> int_of_float in
  Sdlvideo.map_RGB surface (r, g, b)


let rotate (x, y) rad =
  let rad = (Float.atan2 y x) +. rad
  and radius = (x *. x) +. (y *. y) |> sqrt in
  ((cos rad) *. radius, (sin rad) *. radius)


let shade surface (x, y) xradius color =
  for yradius = (xradius/3) to xradius do
    let xradius = float_of_int xradius
    and yradius = float_of_int yradius in
    let shadow = alter_color surface color ((xradius -. (yradius /. 2.0)) /. xradius) in
    for degree = 0 to 180 do
      let rad = (float_of_int degree) *. Float.pi /. 180.0 in
      let xf = (cos rad) *. xradius
      and yf = (sin rad) *. yradius in
      let (xf, yf) = rotate (xf, yf) (Float.pi /. 6. |> Float.neg) in
      let sx = x + (int_of_float xf)
      and sy = y + (int_of_float yf) in
      let rect = Sdlvideo.rect ~x:sx ~y:sy ~w:1 ~h:1 in
      Sdlvideo.fill_rect ~rect:rect surface shadow
    done
  done


let draw_circle surface ~x ~y ~radius color =
  for degree = 0 to 90 do
    let rad = (float_of_int degree) *. Float.pi /. 180.0
    and radius = float_of_int radius in
    let xf = (cos rad) *. radius
    and yf = (sin rad) *. radius
    and x0 = (cos (Float.pi +. rad)) *. radius
    and y0 = (sin (Float.pi +. rad)) *. radius in
    let sx = x + (int_of_float x0)
    and sy = y + (int_of_float y0)
    and wi = int_of_float (xf -. x0)
    and hei = int_of_float (yf -. y0) in
    let rect = Sdlvideo.rect ~x:sx ~y:sy ~w:wi ~h:hei in
    Sdlvideo.fill_rect ~rect:rect surface color
  done
; shade surface (x, y) radius color
