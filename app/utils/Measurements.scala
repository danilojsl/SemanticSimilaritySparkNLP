package utils

import scala.math.sqrt
import scala.language.postfixOps

object Measurements {

  // Calculate the cosine similarity of two vectors
  def cosineSimilarity(x: Array[Float], y: Array[Float]): Double = {
    dotProduct(x, y) / (euclideanDistance(x) * euclideanDistance(y))
  }

  // Calculate the Euclidean distance of a vector
  def euclideanDistance(x: Array[Float]): Double = {
    sqrt(x.map(a => a * a).sum)
  }

  // Calculate the dot product of two vectors
  def dotProduct(x: Array[Float], y: Array[Float]): Float = {
    (for ((a, b) <- x zip y) yield a * b) sum
  }

}
