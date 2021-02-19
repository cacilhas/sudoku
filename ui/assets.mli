open Sdlvideo

type position = w:int -> h:int -> rect

module type PIXEL_INFO = sig
  val rmask : int32
  val gmask : int32
  val bmask : int32
  val amask : int32
end

module Pixel_info : PIXEL_INFO

val get_bg_color : int -> int -> color
val get_fg_color : int -> color
val fill_circle  : [`Small | `Large] -> surface -> position -> int -> unit
