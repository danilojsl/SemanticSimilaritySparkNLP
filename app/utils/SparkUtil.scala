package utils

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

object SparkUtil {

  val sparkSession = SparkSession.getActiveSession.getOrElse(
    SparkSession
      .builder()
      .appName("SparkNLP Default Session")
      .master("local[*]")
      .config("spark.driver.memory", "22G")
      .config("spark.driver.maxResultSize", "0")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .config("spark.kryoserializer.buffer.max", "1000m")
      .getOrCreate())

  def computeSimilarityUDF: UserDefinedFunction = udf { (vector1: Array[Float], vector2: Array[Float]) =>
    Measurements.cosineSimilarity(vector1, vector2)
  }

}
