package repository.mongo

import com.mongodb.MongoException

package object converter {

  def mongoFail(message: String) = throw new MongoException(message)
}