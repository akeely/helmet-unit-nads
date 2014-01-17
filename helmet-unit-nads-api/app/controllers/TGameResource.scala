package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import repository.GameRepositoryComponent
import controllers.converter.JsonGameConverter
import scala.util.{Try,Success,Failure}
import api._
import repository.ImageRepositoryComponent
import controllers.converter.ImageConverter

trait TGameResource extends Controller with GameRepositoryComponent with ImageRepositoryComponent {

  def find(id: String) = Action {
    
    gameRepository.findOne(id) match {
      case Some(g) => Ok(JsonGameConverter.gameToJson(g))
      case None => NotFound(s"Game $id does not exist")
    }
  }
  
  def findEntries(id: String) = Action {
    gameRepository.findOne(id) match {
      case Some(g) => JsonGameConverter.entriesToJson(g.entries) match {
        case Success(e) => Ok(e)
        case Failure(e) => InternalServerError(e.getMessage())
      }
      case None => NotFound(s"Game $id does not exist")
    }
  }
  
  def findCurrentEntry(id: String) = Action {
    
    gameRepository.findOne(id) match {
      case Some(g) => JsonGameConverter.entryToJson(g.entries.last) match {
        case Success(j) => Ok(j)
        case Failure(e) => InternalServerError(e.getMessage())
      }
      case None => NotFound(s"Game $id does not exist")
    }
  }
  
  def saveEntry(id: String, entryType: Option[String]) = entryType match {
    case Some("img") => saveImageFile(id)
    case Some("imgdata") => saveImageData(id)
    case Some("txt") => saveSentence(id)
    case Some(e) => Action{BadRequest(s"Invalid entryType '$e'. Must be 'img', 'imgdata' or 'txt'.")}
    case None => Action{BadRequest("entryType parameter required.")}
  }
  
  def findPlayers(id: String) = Action {
    
    gameRepository.findOne(id) match {
      case None => Ok(s"Game $id does not exist")
      case Some(g) => Ok(Json.obj(
            "currentPlayer" -> g.players(g.currentPlayer).name,
            "players" -> Json.toJson(g.players.map{player => Json.obj("name" -> player.name)})
          ))
    }
  }
  
  def savePlayer(id: String) = Action(parse.text) { request =>
    val newPlayer = User(request.body)
    gameRepository.addPlayer(id, newPlayer) match {
      case Success(g) => Created(s"Added $newPlayer")
      case Failure(e) => NotFound(s"Game $id does not exist")
    }
  }
  
  def updatePlayer(id: String, playerId: String) = TODO
  
  def saveImageFile(id: String) = TODO
  
  def saveImageData(id: String) = Action(parse.text) { request =>
    gameRepository.findOne(id) match {
      case None => NotFound(s"Game $id does not exist")
      case Some(g) => ImageConverter.stringToImage(request.body) match {
        case None => BadRequest("'imgdata' request body must contain base64 encoded image data.")
        case Some(i) => saveImage(id,g.currentPlayer,i._2)
      }
    }
  }
  
  def saveImage(id: String, currentPlayer: Int, imageData: Array[Byte]): Result = {
    
    imageRepository.save(id, currentPlayer, imageData) match {
      case Failure(e) => InternalServerError(e.getMessage())
      case Success(filename) => {
      val newEntry = ImageEntry(filename,User("image author"))
        gameRepository.addEntry(id,newEntry) match {
          case Success(g) => Created("Added image.")
          case Failure(e) => InternalServerError("Failed to add image.")
        }
      }
    }
  }
  
  def saveSentence(id: String) = Action(parse.text) { request =>

    val sentence = request.body
    val entry = SentenceEntry(sentence,User("author"))
    gameRepository.addEntry(id, entry) match {
      case Success(g) => Created(s"Added sentence: $sentence")
      case Failure(e) => NotFound(s"Game $id does not exist")
    }
  }
}