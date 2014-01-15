package api

case class SentenceEntry(entry: String, author: User) extends Entry {
  val sentence = Some(entry)
  val isSentence = true
  val imageUrl = None
}