import utils.Similarity

object SimilarityTest {

  def main(args: Array[String]): Unit = {

    val similarity = new Similarity()
    val recommendationsDataset = similarity.rankSimilarity("beautiful woman is involved in a crime")
    recommendationsDataset.show(false)

    recommendationsDataset.toJSON.show(false)
    recommendationsDataset.printSchema()


    val recommendationsArray = recommendationsDataset.collect().map { row =>
      val recommendedMovie = row.getAs[String]("original_title")
      val overviewMovie = row.getAs[String]("text")
      val similarity = row.getAs[Double]("similarity")

      Map("recommendedMovie" -> recommendedMovie,
          "overviewMovie" -> overviewMovie,
          "similarity" -> similarity.toString
      )
    }

    println("WIP")

  }

}
