type action = MoveLeft
            | MoveRight
            | MoveUp
            | MoveDown
            | MoveCenter
            | Undo
            | Restart
            | FullHouse of Solve.solve_tpe
            | NewGame of Loader.level
            | SetValue of int
            | Toggle of int

