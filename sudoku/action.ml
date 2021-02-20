type action = MoveLeft
            | MoveRight
            | MoveUp
            | MoveDown
            | MoveCenter
            | Undo
            | FullHouse of Solve.solve_tpe
            | Toggle of int
            | SetValue of int
            | NewGame of Loader.level

