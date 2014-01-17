package app.mongo

import scala.collection.Seq
import org.specs2.mutable.Specification
import org.specs2.specification.Fragments
import org.specs2.main.ArgProperty
import repository.mongo.GridFSImageRepository
import java.io.File

class GridFSImageRepositorySpec extends Specification {

  val imageRepository = new GridFSImageRepository("test")
  
  val game = "game"
  val player = 1
  val filename = imageRepository.getFilename(game, player)
  
  val imagePath = "/Users/andrew_keely/Downloads/canvasTest.png"
  val imageFile = new File(imagePath)
  
  "GridFSImageRepository" should {
    
    "add an image correctly" in {
      
      imageRepository.gridFS.remove(filename)
      
      val addedFilename = imageRepository.save(game, player, imageFile)
      
      addedFilename should beSuccessfulTry[String].which(f => f == filename)
    }
  }
  
}