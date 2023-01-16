package utils

import com.johnsnowlabs.nlp.Annotation
import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
import model.Movies
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.functions.{col, flatten, isnan, lit}
import org.apache.spark.sql.{DataFrame, Dataset, Row}

class Similarity(embeddingsPipelineModel: PipelineModel, embeddingsPipeline: PretrainedPipeline) {

  def rankSimilarity(searchText: String, topK: Int): Array[Row] = {
    val moviesDataset = Movies.loadMovies()
    val similarityDataset = computeSimilarity(
      searchText,
      moviesDataset.withColumnRenamed("overview", "text")
    )

    similarityDataset
      .filter(!isnan(col("similarity")))
      .sort(col("similarity").desc)
      .select("text", "original_title", "tagline", "similarity")
      .take(topK)
  }

  private def computeSimilarity(searchText: String, dataset: Dataset[_]): DataFrame = {
    val targetEmbeddings = computeEmbeddings(searchText)

    val embeddingsDataset = embeddingsPipelineModel.transform(dataset)

    val similarityDataSet = embeddingsDataset.select(
      col("text"), col("original_title"), col("tagline"),
      flatten(col("sentence_embeddings.embeddings")).as("embeddings"),
      lit(targetEmbeddings).as("target_embeddings")
    ).withColumn("similarity",
      SparkUtil.computeSimilarityUDF(col("embeddings"), col("target_embeddings")))

    similarityDataSet
  }

  private def computeEmbeddings(text: String): Array[Float] = {

    val annotate = embeddingsPipeline.fullAnnotate(text)

    annotate("sentence_embeddings")
      .flatMap(annotation => annotation.asInstanceOf[Annotation].embeddings)
      .toArray
  }

}
