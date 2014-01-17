package repository.mongo

import scala.util.{Try,Success,Failure}
import com.mongodb.casbah.Imports._
import api._
import com.mongodb.casbah.commons.MongoDBList
import repository.GameRepository
import repository.mongo.converter._

class MongoGameRepository(dbName: String) extends GameRepository {

  val mongoClient = MongoClient()
  val db = mongoClient(dbName)
  val gameCollection = db("games")

  def find(): Iterator[Game] = {

    gameCollection.find.map(MongoGameConverter.mongoToGame(_))
  }

  def findOne(id: String): Option[Game] = {

    gameCollection.findOneByID(MongoGameConverter.idToMongoId(id)) match {
      case Some(g) => Some(MongoGameConverter.mongoToGame(g))
      case None => None
    }
  }

  def addEntry(id: String, entry: Entry): Try[Game] = {
    
    findOne(id) match {
      
      case None => Failure(new MongoException("Game ${id} does not exist"))
      case Some(game) => {
        val newEntries = game.entries :+ entry
        val newCurrentPlayer = game.currentPlayer + 1
        
        addEntryToMongo(id,entry) match {
          case Failure(e) => Failure(e)
          case Success(s) => {
            Success(Game(owner = game.owner, players = game.players, 
                currentPlayer = newCurrentPlayer, entries = newEntries))
          }
        }
      }
    }
  }
  
  def addPlayer(id: String, player: User): Try[Game] = {
    
    findOne(id) match {
      case None => Failure(new MongoException("Game ${id} does not exist"))
      case Some(game) => {
        val mongoId = MongoDBObject("_id" -> MongoGameConverter.idToMongoId(id))
        val newPlayers = game.players :+ player
        val result = gameCollection.update(mongoId, $push(GameMongoProperties.PLAYERS -> player.name))
        if(result.getLastError().ok()) Success(Game(game.owner,newPlayers, game.currentPlayer, game.entries))
        else Failure(result.getLastError().getException())
      }
    }
  }
  
  def addEntryToMongo(id: String, entry: Entry): Try[Unit] = {
    
    val mongoId = MongoDBObject("_id" -> MongoGameConverter.idToMongoId(id))
    val mongoEntry = MongoEntryConverter.entryToMongo(entry)
    val addEntryObject = $push(GameMongoProperties.ENTRIES -> mongoEntry)
    val incrementCurrentPlayerObject = $inc(GameMongoProperties.CURRENT_PLAYER -> 1)
    val allUpdatesObject = addEntryObject ++ incrementCurrentPlayerObject
    val result = gameCollection.update(mongoId, allUpdatesObject)
    if(result.getLastError().ok()) Success()
    else Failure(result.getLastError().getException())
  }
}