package repository.mongo.converter

import scala.util.{Try,Success,Failure}
import com.mongodb.casbah.Imports._
import api._
import com.mongodb.casbah.commons.MongoDBList

object MongoGameConverter {
  
  def mongoToGame(mongoEntry: MongoDBObject): Game = {
    
    Game(
      owner = User(mongoEntry.getAsOrElse[String](GameMongoProperties.OWNER, mongoFail("Owner required for games."))),
      players = mongoEntry.getAs[MongoDBList](GameMongoProperties.PLAYERS) match {
        case Some(p) => {
          val t = p collect { 
            case s: String => User(s)
          }
          t.toList
        }
        case None => throw new MongoException("Game must have at least one player.")
      },
      currentPlayer = getCurrentPlayer(mongoEntry),
      entries = mongoEntry.getAs[MongoDBList](GameMongoProperties.ENTRIES) match {
        case Some(e) => MongoEntryConverter.mongoToEntries(e)
        case None => List.empty
      }
    )
  }

  def gameToMongo(game: Game): MongoDBObject = {
    
    val builder = MongoDBObject.newBuilder
    
    builder += GameMongoProperties.OWNER -> game.owner.name
    builder += GameMongoProperties.PLAYERS -> MongoDBList(game.players.map{_.name}:_*)
    builder += GameMongoProperties.CURRENT_PLAYER -> game.currentPlayer
    builder += GameMongoProperties.ENTRIES -> MongoEntryConverter.entriesToMongo(game.entries)
    
    builder.result
  }

  def idToMongoId(id: String): ObjectId = new ObjectId(id)
  
  def getCurrentPlayer(mongoEntry: MongoDBObject): Int = mongoEntry.getAsOrElse[Int](GameMongoProperties.CURRENT_PLAYER, mongoFail("Game must have a current player"))
}

object GameMongoProperties {

  val OWNER = "owner"
  val PLAYERS = "players"
  val CURRENT_PLAYER = "currentPlayer"
  val ENTRIES = "entries"
}