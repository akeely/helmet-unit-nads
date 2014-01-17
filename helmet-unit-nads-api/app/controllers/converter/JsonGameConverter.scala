package controllers.converter

import play.api.libs.json._
import api._
import scala.util.{Try,Success,Failure}

object JsonGameConverter {

  def gameToJson(game: Game): JsObject = Json.obj(
      "owner" -> game.owner.name,
      "currentPlayer" -> game.players(game.currentPlayer).name,
      "numEntries" -> game.entries.size
    )
  
  def jsonToGame(json: JsObject): Try[Game] = ???
  
  def entryToJson(entry: Entry): Try[JsObject] = entry match {
    
    case s: SentenceEntry => Success(Json.obj("sentence" -> s.sentence,
              "author" -> s.author.name))
    case i: ImageEntry => Success(Json.obj("imgUrl" -> s"/img/${i.imageUrl}",
              "author" -> i.author.name))
    case e => Failure(new IllegalArgumentException(s"Unsupported entry type $e"))
  }
  
  def entriesToJson(entries: List[Entry]): Try[JsValue] = {
    Try(entries.map{entryToJson(_).get}) match {
      case Success(e) => Success(Json.toJson(e))
      case Failure(e) => Failure(e)
    }
  }

  
}