package repository

import scala.util.{Try,Success,Failure}
import java.io.File
import java.io.InputStream

trait ImageRepository {

  def save(id:String,currentPlayer:Int,image:Array[Byte]): Try[String]
  def save(id:String,currentPlayer:Int,image:File): Try[String]
  def findOne(filename:String): Option[InputStream]
}