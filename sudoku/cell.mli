class cell : ?value:int -> int -> object

  method clear  : int -> unit
  method is_set : int -> bool
  method set    : int -> unit
  method toggle : int -> unit
  method value  : int
  method string : string
  method clone  : int -> cell
end
