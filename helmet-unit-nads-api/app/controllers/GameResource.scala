package controllers

import repository.mongo.MongoGameRepository
import repository.mongo.MongoGameRepository
import repository.mongo.GridFSImageRepository

object GameResource extends TGameResource {
  
  val dbName = "test"
  
  val gameRepository = new MongoGameRepository(dbName)
  val imageRepository = new GridFSImageRepository(dbName)
}