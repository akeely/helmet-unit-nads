package api

case class ImageEntry(url: String, author: User) extends Entry {
    
    val imageUrl = Some(url)
    val isSentence = false
    val sentence = None
}