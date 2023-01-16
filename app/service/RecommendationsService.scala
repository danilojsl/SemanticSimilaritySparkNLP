package service

import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
import org.apache.spark.ml.PipelineModel
import response.MovieRecommendation
import utils.Similarity

class RecommendationsService(embeddingsPipelineModel: PipelineModel, embeddingsPipeline: PretrainedPipeline) {

  def inferMovieRecommendations(searchText: String, topK: Int): Array[MovieRecommendation] = {
    val similarity = new Similarity(embeddingsPipelineModel, embeddingsPipeline)
    val recommendationsRows = similarity.rankSimilarity(searchText, topK)

    recommendationsRows.map{row =>
      val recommendedMovie = row.getAs[String]("original_title")
      val overviewMovie = row.getAs[String]("text")
      val similarity = row.getAs[Double]("similarity")

      MovieRecommendation(recommendedMovie, overviewMovie,
        BigDecimal(similarity).setScale(4, BigDecimal.RoundingMode.HALF_UP).toDouble)
    }
  }

}
