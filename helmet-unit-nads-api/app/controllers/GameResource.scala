package controllers

import play.api._
import play.api.mvc._

object GameResource extends Controller {

  def find(id: Int) = Action {
    Ok("This is a game.")
  }
  
  def findEntries(id: Int) = Action {
    Ok("These are entries for the game.")
  }
  
  def findCurrentEntry(id: Int) = Action {
    Ok("This is the current entry for the game.")
  }
  
  def saveEntry(id: Int, entryType: Option[String]) = Action {

    request =>
    
      def entryResult(x: Option[String]): Result = x match {
        case Some("img") => saveImage(id, request)
        case Some("txt") => saveSentence(id, request)
        case Some(_) => NotFound("Invalid entryType " + x + ". Must be 'img' or 'txt'.")
        case None => NotFound("entryType parameter required.")
      }
    
      entryResult(entryType)
  }
  
  def findPlayers(id: Int) = Action {
    Ok("These are the players in the game.")
  }
  
  def savePlayer(id: Int) = Action {
    Ok("The player has been added to the game.")
  }
  
  def updatePlayer(id: Int, playerId: Int) = Action {
    Ok("The player's status has been updated.")
  }
  
  def saveImage[A](id: Int, request: Request[A]): Result = {
    Ok("Image saved.")
  }
  
  def saveSentence[A](id: Int, request: Request[A]): Result = {
    Ok("Sentence saved.")
  }
}