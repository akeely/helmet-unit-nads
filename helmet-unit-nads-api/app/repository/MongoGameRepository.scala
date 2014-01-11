package repository

import com.mongodb.casbah.Imports._
import api._

class MongoGameRepository extends GameRepository {

	val mongoClient = MongoClient()
	val db = mongoClient("db")
	val gameCollection = db("games")
	
    def find(): Iterator[Game] = {
	  
	  gameCollection.find.map(mongoToGame(_))
	}
	
    def findOne(id: Int): Option[Game] = {
      
      gameCollection.findOneByID(idToMongoId(id)) match {
        case Some(g) => Some(mongoToGame(g))
        case None => None
      }
    }
    
  	def addEntry(id: Int, entry: Entry): Unit = ???
  	
  	def mongoToGame(mongoEntry: MongoDBObject): Game = ???
  	
  	def idToMongoId(id: Int): ObjectId = ???
}