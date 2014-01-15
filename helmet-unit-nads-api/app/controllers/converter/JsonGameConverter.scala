package controllers.converter

import play.api.libs.json._
import api.Game
import scala.util.{Try,Success,Failure}

object JsonGameConverter {

  def gameToJson(game: Game): JsObject = Json.obj(
      "owner" -> game.owner.name,
      "currentPlayer" -> game.players(game.currentPlayer).name,
      "numEntries" -> game.entries.size
    )
  
  def jsonToGame(json: JsObject): Try[Game] = ???
  
}