package controllers.converter

import org.apache.commons.codec.binary.Base64

object ImageConverter {

  val base64regex = "data:([a-z/]+);base64,(.*)".r
  
  /**
   * Convert a base64 URI string to an image byte array.
   * 
   * @param src The base64 encoded URI string representing the image.
   * @return A pair containing the mime type as a String, and the image as a byte array. Return
   * None if the input string is not a base64 encoded image.
   */
  def stringToImage(src: String): Option[(String,Array[Byte])] = src match {
    case base64regex(mimetype,data) => Some((mimetype, convertImageData(data)))
    case _ => None
  }
  
  def convertImageData(data: String): Array[Byte] = Base64.decodeBase64(data.getBytes("utf-8"))
}