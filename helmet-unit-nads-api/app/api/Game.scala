package api

trait Game {
  
  val owner: User
  val players: List[User]
  val currentPlayer: Int
  val entries: List[Entry]
}