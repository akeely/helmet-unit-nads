package app.mongo.converter

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import api._
import app.mongo.TestGames
import repository.mongo.converter.MongoGameConverter

class MongoGameConverterSpec extends Specification {
  
  "MongoGameConverter" should {
    
    "create inverse objects" in {
      
      testGameInversion(TestGames.testGame)
    }
    
    "handle empty entry lists" in {
      
      testGameInversion(TestGames.testEmptyGame)
    }
  }
  
  def testGameInversion(game: Game) = {
    
      val testMongoGame = MongoGameConverter.gameToMongo(game)
      val testInvertedGame = MongoGameConverter.mongoToGame(testMongoGame)
      
      testInvertedGame must equalTo(game)
  }
}