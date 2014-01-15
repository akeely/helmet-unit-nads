package repository

import scala.util.Try
import api._

/**
 * Access the games.
 */
trait GameRepository {

  /**
   * Get all of the games.
   * 
   * @return All of the games.
   */
  def find(): Iterator[Game]
  
  /**
   * Get one game.
   * 
   * @param id The game identifier.
   * @return The game, or None if the game does not exist.
   */
  def findOne(id: String): Option[Game]
  
  /**
   * Add a drawing or sentence to the game.
   * 
   * @param id The game to add the entry to.
   * @param entry The drawing or sentence to add.
   * @retun The updated game.
   */
  def addEntry(id: String, entry: Entry): Try[Game]
}