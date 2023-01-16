package service

import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession
import play.api.inject.ApplicationLifecycle

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

// This creates an PipelinesService object once at start-up and registers hook for shut-down.
@Singleton
class PipelinesServiceImpl @Inject()(lifecycle: ApplicationLifecycle) extends PipelinesService {

  SparkSession.getActiveSession.getOrElse(
    SparkSession
      .builder()
      .appName("SparkNLP Default Session")
      .master("local[*]")
      .config("spark.driver.memory", "12G")
      .config("spark.driver.maxResultSize", "0")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .config("spark.kryoserializer.buffer.max", "1000m")
      .getOrCreate())

  private val modelsPath = "~/models"

  private var cacheEmbeddingsPipelineModel: Option[PipelineModel] = None
  private var cacheEmbeddingsPipeline: Option[PretrainedPipeline] = None
  private var cacheDistilBertEmbeddingsPipelineModel: Option[PipelineModel] = None
  private var cacheDistilBertEmbeddingsPipeline: Option[PretrainedPipeline] = None
  private var cacheBertEmbeddingsPipelineModel: Option[PipelineModel] = None
  private var cacheBertEmbeddingsPipeline: Option[PretrainedPipeline] = None

  loadEmbeddingsPipelineModel("glove")
  loadEmbeddingsPipelineModel("distil_bert")
  loadEmbeddingsPipelineModel("bert")
  loadEmbeddingsPretrainedPipeline("glove")
  loadEmbeddingsPretrainedPipeline("distil_bert")
  loadEmbeddingsPretrainedPipeline("bert")

  override def loadEmbeddingsPipelineModel(embeddings: String): PipelineModel = {
    if (cacheEmbeddingsPipelineModel.isDefined && embeddings == "glove") {
      println(s"Loading $embeddings embeddings  pipeline from memory")
      return cacheEmbeddingsPipelineModel.get
    }

    if (cacheDistilBertEmbeddingsPipelineModel.isDefined && embeddings == "distil_bert") {
      println(s"Loading $embeddings embeddings pipeline from memory")
      return cacheDistilBertEmbeddingsPipelineModel.get
    }

    if (cacheBertEmbeddingsPipelineModel.isDefined && embeddings == "bert") {
      println(s"Loading $embeddings embeddings pipeline from memory")
      return cacheBertEmbeddingsPipelineModel.get
    }

    val embeddingsPipelineModel = embeddings match {
      case "glove" => {
        println(s"Loading $embeddings embeddings pipeline from disk")
        cacheEmbeddingsPipelineModel = Some(PipelineModel.load(s"$modelsPath/embeddings_pipeline"))
        cacheEmbeddingsPipelineModel.get
      }
      case "distil_bert" => {
        println(s"Loading $embeddings embeddings pipeline from disk")
        cacheDistilBertEmbeddingsPipelineModel = Some(PipelineModel.load(s"$modelsPath/distilbert_embeddings_pipeline"))
        cacheDistilBertEmbeddingsPipelineModel.get
      }
      case "bert" => {
        println(s"Loading $embeddings embeddings pipeline from disk")
        cacheBertEmbeddingsPipelineModel = Some(PipelineModel.load(s"$modelsPath/bert_embeddings_pipeline"))
        cacheBertEmbeddingsPipelineModel.get
      }
      case _ => throw new UnsupportedOperationException(s"$embeddings embeddings not supported")
    }
    embeddingsPipelineModel


  }

  override def loadEmbeddingsPretrainedPipeline(embeddings: String): PretrainedPipeline = {

    if (cacheEmbeddingsPipeline.isDefined && embeddings == "glove") {
      println(s"Loading pretrained $embeddings embeddings pipeline from memory")
      return cacheEmbeddingsPipeline.get
    }

    if (cacheDistilBertEmbeddingsPipeline.isDefined && embeddings == "distil_bert") {
      println(s"Loading pretrained $embeddings embeddings pipeline from memory")
      return cacheDistilBertEmbeddingsPipeline.get
    }

    if (cacheBertEmbeddingsPipeline.isDefined && embeddings == "bert") {
      println(s"Loading pretrained $embeddings embeddings pipeline from memory")
      return cacheBertEmbeddingsPipeline.get
    }

    val embeddingsPipeline = embeddings match {
      case "glove" => {
        println(s"Loading pretrained $embeddings embeddings pipeline from disk")
        cacheEmbeddingsPipeline = Some(PretrainedPipeline.fromDisk(s"$modelsPath/embeddings_pipeline"))
        cacheEmbeddingsPipeline.get
      }
      case "distil_bert" => {
        println(s"Loading pretrained $embeddings embeddings pipeline from disk")
        cacheDistilBertEmbeddingsPipeline = Some(PretrainedPipeline.fromDisk(s"$modelsPath/distilbert_embeddings_pipeline"))
        cacheDistilBertEmbeddingsPipeline.get
      }
      case "bert" => {
        println(s"Loading pretrained $embeddings embeddings pipeline from disk")
        cacheBertEmbeddingsPipeline = Some(PretrainedPipeline.fromDisk(s"$modelsPath/bert_embeddings_pipeline"))
        cacheBertEmbeddingsPipeline.get
      }
      case _ => throw new UnsupportedOperationException(s"$embeddings embeddings not supported")
    }
    embeddingsPipeline

  }

  lifecycle.addStopHook { () =>
    Future.successful(())
  }

}
