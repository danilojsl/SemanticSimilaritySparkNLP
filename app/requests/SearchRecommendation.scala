package requests

import play.api.libs.json.Json

case class SearchRecommendation(text: String, embeddingsEngine: String, topK: Int)

object SearchRecommendation {
  implicit val reads = Json.reads[SearchRecommendation]
}