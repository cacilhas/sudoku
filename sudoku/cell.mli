class cell : ?value:int -> int -> object

  method candidates_from : char array -> cell
  method clear           : int -> cell
  method is_set          : int -> bool
  method set             : int -> cell
  method settable        : int
  method toggle          : int -> cell
  method value           : int
  method string          : string
  method clone           : int -> cell
  method compare         : cell -> int
end
