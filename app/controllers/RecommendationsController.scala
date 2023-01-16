package controllers

import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import requests.SearchRecommendation
import service.{PipelinesService, RecommendationsService}

import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class RecommendationsController @Inject()(cc: ControllerComponents, pipelinesService: PipelinesService)
  extends AbstractController(cc) {

  def movieRecommendations(): Action[JsValue] = Action(parse.json) { implicit request =>

    request.body.validate[SearchRecommendation] match {
      case JsSuccess(searchRecommendation, _) => {
        val embeddingsPipelineModel = pipelinesService.loadEmbeddingsPipelineModel(searchRecommendation.embeddingsEngine)
        val embeddingsPipeline = pipelinesService.loadEmbeddingsPretrainedPipeline(searchRecommendation.embeddingsEngine)

        val recommendations = new RecommendationsService(embeddingsPipelineModel, embeddingsPipeline)
        val movieRecommendations = recommendations.inferMovieRecommendations(
          searchRecommendation.text,
          searchRecommendation.topK)

        Ok(Json.toJson(movieRecommendations))
      }
      case JsError(errors) => BadRequest
    }

  }

}
