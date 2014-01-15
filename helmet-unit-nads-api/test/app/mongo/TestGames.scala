package app.mongo

import api._

import com.mongodb.casbah.Imports.ObjectId

object TestGames {
  val testOwner = User("owner")
  val testPlayers = List(testOwner,User("otherPlayer"))
  val testCurrentPlayer = 1
  val testEntries = List(SentenceEntry("test sentence", testOwner))
  val testGame = Game(testOwner,testPlayers,testCurrentPlayer,testEntries)
  val testGameId = "52d36e3a03644b81d50caf2c"
  val testEmptyGame = Game(testOwner,testPlayers,testCurrentPlayer,List())
  
  val newTestGameId = new ObjectId("52d4bae30364751d6e4c4ac4")
  val testNewEntry = ImageEntry("someImage",User("otherPlayer"))
}