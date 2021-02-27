let help () =
  print_string "\027[37;1mKodumaro "
; print_string "\027[38;2;222;0;0mS"
; print_string "\027[38;2;255;160;0mu"
; print_string "\027[38;2;255;255;0md"
; print_string "\027[38;2;0;255;0mo"
; print_string "\027[38;2;0;192;255mk"
; print_string "\027[38;2;0;0;222mu"
; print_endline "\027[0m"
; print_endline "keys:"
; print_endline "  \027[33mEscape\027[0m – quit"
; print_endline "  \027[33mKP1\027[0m – toggle candidate 1"
; print_endline "  \027[33mKP2\027[0m – toggle candidate 2"
; print_endline "  ˙·."
; print_endline "  \027[33mKP9\027[0m – toggle candidate 9"
; print_endline "  \027[33mS-KP1\027[0m – set cell as 1"
; print_endline "  \027[33mS-KP2\027[0m – set cell as 2"
; print_endline "  ˙·."
; print_endline "  \027[33mS-KP9\027[0m – set cell as 9"
; print_endline "  \027[33mSpace\027[0m – set single-candidate cell"
; print_endline "  \027[33mS-Space\027[0m – run Full House algorithm"
; print_endline "  \027[33mC-S-Space\027[0m – run aggressive Full House algorithm"
; print_endline "  \027[33mu\027[0m – undo"
; print_endline "  \027[33ma\027[0m or \027[33mLeft\027[0m – move player left"
; print_endline "  \027[33md\027[0m or \027[33mRight\027[0m – move player right"
; print_endline "  \027[33ms\027[0m or \027[33mDown\027[0m – move player down"
; print_endline "  \027[33mw\027[0m or \027[33mUp\027[0m – move player up"
; print_endline "  \027[33mC-c\027[0m – center player"
; print_endline "  \027[33mC-r\027[0m – restart current game"
; print_endline "  \027[33mC-1\027[0m – new easy game"
; print_endline "  \027[33mC-2\027[0m – new medium game"
; print_endline "  \027[33mC-3\027[0m or \027[33mC-n\027[0m – new hard game"
; print_endline "  \027[33mC-4\027[0m – new fiendish game"


let () =
  if (Array.exists ((=) "-h") Sys.argv) || (Array.exists ((=) "--help") Sys.argv)
  then help ()
  else Ui.mainloop ()
