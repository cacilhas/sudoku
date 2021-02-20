type action = MoveLeft
            | MoveRight
            | MoveUp
            | MoveDown
            | MoveCenter
            | Undo
            | SetSettables
            | Toggle of int
            | SetValue of int
            | NewGame of Loader.level

