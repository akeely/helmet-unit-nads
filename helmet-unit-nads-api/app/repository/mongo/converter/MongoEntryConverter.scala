package repository.mongo.converter

import scala.util.{Try,Success,Failure}
import com.mongodb.casbah.Imports._
import api._
import com.mongodb.casbah.commons.MongoDBList

object MongoEntryConverter {
    
  def entriesToMongo(entries: List[Entry]): MongoDBList = {
    val mongoEntries = entries map entryToMongo
    MongoDBList(mongoEntries:_*)
  }
  
  def entryToMongo(entry: Entry): MongoDBObject = {
    
    val builder = MongoDBObject.newBuilder
    
    builder += EntryMongoProperties.AUTHOR -> entry.author.name
    
    entry match {
      case e: ImageEntry => {
        builder += EntryMongoProperties.ENTRY_TYPE -> EntryMongoProperties.IMAGE_TYPE
        builder += EntryMongoProperties.ENTRY -> e.imageUrl
      }
      case e: SentenceEntry => {
        builder += EntryMongoProperties.ENTRY_TYPE -> EntryMongoProperties.SENTENCE_TYPE
        builder += EntryMongoProperties.ENTRY -> e.sentence
      }
      case _ => mongoFail("Unsupported entry type for ${entry}")
    }

    builder.result
  }
  
  // TODO: Figure out why both of these are needed
  def mongoToEntries(entries: MongoDBList): List[Entry] = {
    val e = entries map {
      case o: DBObject => mongoToEntry(o)
      case m: MongoDBObject => mongoToEntry(m)
    }
    
    e.toList
  }
  
  def mongoToEntry(entry: MongoDBObject): Entry = {
    
    val author = entry.getAsOrElse[String](EntryMongoProperties.AUTHOR, mongoFail("Entry must have an author"))
    val entryValue = entry.getAsOrElse[String](EntryMongoProperties.ENTRY, mongoFail("Entry must have a value"))
    val entryType = entry.getAsOrElse[String](EntryMongoProperties.ENTRY_TYPE, mongoFail("Entry must have a type"))

    if(entryType == EntryMongoProperties.SENTENCE_TYPE) SentenceEntry(entryValue,User(author))
    else if(entryType == EntryMongoProperties.IMAGE_TYPE) ImageEntry(entryValue,User(author))
    else mongoFail("Invalid entry type: ${entryType}")
  }
}

object EntryMongoProperties {
  val AUTHOR = "author"
  val ENTRY_TYPE = "type"
  val SENTENCE_TYPE = "sentence"
  val IMAGE_TYPE = "image"
  val ENTRY = "entry"
}