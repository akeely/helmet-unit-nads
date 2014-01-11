package repository

import api._

trait GameRepository {

    def find(): Iterator[Game]
    def findOne(id: Int): Option[Game]
  	def addEntry(id: Int, entry: Entry): Unit
}