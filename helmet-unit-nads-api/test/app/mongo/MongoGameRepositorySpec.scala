package app.mongo

import com.mongodb.casbah.Imports._

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import api._
import repository.mongo.converter.MongoGameConverter
import repository.mongo.MongoGameRepository

class MongoGameRepositorySpec extends Specification {

  val gameRepository = new MongoGameRepository("test")
  
  "MongoGameRepository" should {
    
    "retrieve the test gameby ID" in {
      
      val game = gameRepository.findOne(TestGames.testGameId)
      
      game must equalTo(Some(TestGames.testGame))
    }
    
    "retrieve the test game with all games" in {
      
      val games = gameRepository.find()
      
      games must contain(TestGames.testGame)
    }
    
    "add the correct entry" in {
      
      resetTestGame()
      
      gameRepository.addEntry(TestGames.newTestGameId.toString(), TestGames.testNewEntry)
      
      val updatedGame = gameRepository.findOne(TestGames.newTestGameId.toString())
      
      updatedGame must beSome[Game].which(_.isOver)
      updatedGame must beSome[Game].which(_.entries.size must equalTo(2))
      updatedGame must beSome[Game].which(_.currentPlayer must equalTo(2))
      updatedGame must beSome[Game].which(_.entries must equalTo(List(TestGames.testGame.entries.head,TestGames.testNewEntry)))
    }
    
    "add the correct player" in {
      
      resetTestGame()
      
      val newPlayer = User("new player")
      
      val gameResponse = gameRepository.addPlayer(TestGames.newTestGameId.toString(), newPlayer)
      val updatedGame = gameRepository.findOne(TestGames.newTestGameId.toString())
      val expectedGame = Game(TestGames.testGame.owner,
          TestGames.testGame.players:+newPlayer,
          TestGames.testGame.currentPlayer,
          TestGames.testGame.entries)
      
      updatedGame must beSome[Game].which(_ == expectedGame)
      gameResponse must beSuccessfulTry[Game].which(_ == expectedGame)
    }
  }
  
  def resetTestGame(): Unit = {
      
    val idObject = MongoDBObject("_id" -> TestGames.newTestGameId)
    val mongoGame = MongoGameConverter.gameToMongo(TestGames.testGame) ++ idObject
      
    gameRepository.gameCollection.remove(idObject, WriteConcern.None)
    gameRepository.gameCollection.save(mongoGame, WriteConcern.Normal)
  }
}