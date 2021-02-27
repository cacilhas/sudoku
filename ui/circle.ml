open Sdl

let shadow_color color factor =
  let factor = Float.pi *. factor /. 2.0 |> sin in
  let r = Int32.unsigned_rem color 256l
          |> Int32.to_float
          |> ( *.) factor
          |> Int32.of_float
          |> fun r -> Int32.shift_left r 24
  and g = Int32.unsigned_rem (Int32.unsigned_div color 256l) 256l
          |> Int32.to_float
          |> ( *.) factor
          |> Int32.of_float
          |> fun g -> Int32.shift_left g 16
  and b = Int32.unsigned_rem (Int32.unsigned_div color 65536l) 256l
          |> Int32.to_float
          |> ( *.) factor
          |> Int32.of_float
          |> fun b -> Int32.shift_left b 8 in
  Int32.logor r g |> Int32.logor b |> Int32.logor 0xffl


let rad_of_degree degree = (float_of_int degree) *. Float.pi /. 180.0

let rotation = Float.pi *. 11.0 /. 6.0

let rotate (x, y) rad =
  let rad = (Float.atan2 y x) +. rad
  and radius = (x *. x) +. (y *. y) |> sqrt in
  ((cos rad) *. radius, (sin rad) *. radius)


let shade surface (x, y) xradius color =
  for yradius = (xradius*2/5) to xradius do
    let xradius = float_of_int xradius
    and yradius = float_of_int yradius in
    let shadow = shadow_color color
                   ((xradius -. (yradius /. 2.0)) /. xradius) in
    for degree = 0 to 180 do
      let rad = rad_of_degree degree in
      let xf = (cos rad) *. xradius
      and yf = (sin rad) *. yradius in
      let (xf, yf) = rotate (xf, yf) rotation in
      let sx = x + (int_of_float xf)
      and sy = y + (int_of_float yf) in
      let rect = Rect.make ~pos:(sx, sy) ~dims:(1, 1) in
      Surface.fill_rect ~dst:surface ~rect:rect ~color:shadow
    done
  done


let fill_circle surface ~pos:(x, y) ~radius ~color =
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
    let rect = Rect.make ~pos:(sx, sy) ~dims:(wi, hei) in
    Surface.fill_rect ~dst:surface ~rect:rect ~color:color
  done
; shade surface (x, y) radius color
