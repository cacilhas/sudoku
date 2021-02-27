type action =
            [ `MoveLeft
            | `MoveRight
            | `MoveUp
            | `MoveDown
            | `MoveCenter
            | `Undo
            | `Restart
            | `FullHouse of Solve.t
            | `NewGame of Loader.level
            | `SetValue of int
            | `Toggle of int
            ]

