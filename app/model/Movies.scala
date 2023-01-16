package model

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, from_json, length}
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}
import utils.SparkUtil

object Movies {

  private val datasetPath = "/media/danilo/Data/Danilo/JSL/articles/SparkNLP_Semantic_Similarity/datasets"

  def loadMovies(): DataFrame = {
    val moviesDataset = SparkUtil.sparkSession.read
      .option("header", true)
      .csv(s"$datasetPath/movies_metadata.csv")
      .limit(500)
      .na.drop(Seq("genres", "original_title", "tagline", "overview"))
      .filter(length(col("overview")) > 20)

    val schema = new StructType(
      Array(
        StructField("id", StringType, nullable = true),
        StructField("name", StringType, nullable = true)
      )
    )

    val arraySchema = new ArrayType(schema, true)

    moviesDataset.select(
      from_json(col("genres"), arraySchema).as("genres"),
      col("original_title"),
      col("tagline"),
      col("overview"))
  }

}
