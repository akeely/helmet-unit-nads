package api

trait Entry {
    val sentence: Option[String]
    val imageUrl: Option[String]
    val isSentence: Boolean
    val author: User
}