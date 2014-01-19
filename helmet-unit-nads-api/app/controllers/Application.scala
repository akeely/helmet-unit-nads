package controllers

import play.api._
import play.api.mvc._
import play.api.templates.Html
import repository.mongo.MongoGameRepository
import api._

object Application extends Controller {
  
  val gameRepository = new MongoGameRepository("test")
  
  def index = Action {
    Ok(views.html.main("Your new application is ready.")(Html("Welcome to Helmet Unit Nads")))
  }
  
  def imageEntry(gameId: String) = Action {
    
    gameRepository.findOne(gameId) match {
      case None => NotFound(s"Game $gameId does not exist")
      case Some(g) => if(g.isOver()) Ok(views.html.gameover(gameId,g.entries))
        else getLastEntry(g) match {
          case Some(e) => e match {
            case SentenceEntry(s, a) => Ok(views.html.image(gameId, s, a.name))
            case ImageEntry(i,a) => Ok(views.html.sentence(gameId,i,a.name))
            case _ => InternalServerError("Invalid entry type")
          }
          case None => Ok(views.html.newgame(gameId,g.players.map(_.name)))
        }
    }
  }
  
  def getLastEntry(game: Game): Option[Entry] = {
    if(game.entries.isEmpty) None
    else Some(game.entries.last)
  }
}