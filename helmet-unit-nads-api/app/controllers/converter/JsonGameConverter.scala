package controllers.converter

import play.api.libs.json._
import api.Game
import scala.util.{Try,Success,Failure}

object JsonGameConverter {

  def gameToJson(game: Game): JsObject = ???
  
  def jsonToGame(json: JsObject): Try[Game] = ???
  
}