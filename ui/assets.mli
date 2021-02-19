val get_bg_color : int -> int -> Sdlvideo.color
val get_fg_color : int -> Sdlvideo.color
val draw_circle  : [`Small | `Large] -> Sdlvideo.surface -> (w:int -> h:int -> Sdlvideo.rect) -> int -> unit
