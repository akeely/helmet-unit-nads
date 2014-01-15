package api

case class Game(owner: User, players: List[User], currentPlayer: Int, entries: List[Entry]) {
  
  def isOver(): Boolean = currentPlayer >= players.size
}