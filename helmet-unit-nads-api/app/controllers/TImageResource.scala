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
import play.api.libs.iteratee.Enumerator
import scala.concurrent.ExecutionContext.Implicits.global

trait TImageResource extends Controller with ImageRepositoryComponent {

  
  def findOne(filename: String) = Action {
    
    imageRepository.findOne(filename) match {
      case None => NotFound(s"Image $filename does not exist.")
      case Some(f) => {
        val content: Enumerator[Array[Byte]] = Enumerator.fromStream(f)
        Ok.chunked(content).withHeaders(CONTENT_TYPE->"image/png")
      }
    }
  }
}