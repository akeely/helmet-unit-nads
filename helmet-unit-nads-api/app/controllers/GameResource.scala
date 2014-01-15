package controllers

import repository.mongo.MongoGameRepository
import repository.mongo.MongoGameRepository

object GameResource extends TGameResource {
  val gameRepository = new MongoGameRepository("prod")
}