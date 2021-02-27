open Sdl

val get_bg_color : int -> int -> int32
val get_fg_color : int -> int32
val fill_circle  : [`Small | `Large] -> Surface.t -> (dims:(int * int) -> Rect.t) -> int -> unit
