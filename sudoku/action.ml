type action = MoveLeft
            | MoveRight
            | MoveUp
            | MoveDown
            | MoveCenter
            | Undo
            | FullHouse of Board.full_house
            | Toggle of int
            | SetValue of int
            | NewGame of Loader.level

