package repository.mongo

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.gridfs.Imports._
import scala.util.{Try,Success,Failure}
import repository.ImageRepository
import java.io.File
import java.io.InputStream

class GridFSImageRepository(dbName: String) extends ImageRepository {

  val mongoClient = MongoClient()
  val db = mongoClient(dbName)
  val gridFS = GridFS(db)
  
  def save(id:String,currentPlayer:Int,image:Array[Byte]): Try[String] = {
    
    val filename = getFilename(id, currentPlayer)
    
    gridFS(image){ fh =>
      fh.filename = filename
      fh.contentType = "image/png"
    } match {
      case Some(id) => Success(filename)
      case None => Failure(new MongoException(s"Failed to save file $filename"))
    }
  }
  
  def save(id:String,currentPlayer:Int,image:File): Try[String] = {
    
    val filename = getFilename(id, currentPlayer)
    
    gridFS(image){ fh =>
      fh.filename = filename
      fh.contentType = "image/png"
    } match {
      case Some(id) => Success(filename)
      case None => Failure(new MongoException(s"Failed to save file $filename"))
    }
  }
  
  def findOne(filename:String): Option[InputStream] = gridFS.findOne(filename) match {
    case Some(i) => Some(i.inputStream)
    case None => None
  }
  
  def getFilename(id:String,currentPlayer:Int): String = s"$id-$currentPlayer.png"
}