package controllers

import repository.mongo.GridFSImageRepository

object ImageResource extends TImageResource {

  val imageRepository = new GridFSImageRepository(GameResource.dbName)
}