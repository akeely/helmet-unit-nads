/**
 *
 */
package controllers

import play.api._
import play.api.mvc._

/**
 * @author andrew_keely
 *
 */
object GameCollection extends Controller {

  def find = Action {
    Ok("Hello world")
  }
  
  def save = Action(parse.json) { implicit request =>
    Ok("Game created")
  }
}