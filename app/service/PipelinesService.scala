package service

import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
import org.apache.spark.ml.PipelineModel

trait PipelinesService {

  def loadEmbeddingsPipelineModel(embeddings: String): PipelineModel

  def loadEmbeddingsPretrainedPipeline(embeddings: String): PretrainedPipeline

}
