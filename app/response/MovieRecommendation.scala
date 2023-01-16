package response

import play.api.libs.json.Json

case class MovieRecommendation(title: String, overview: String, score: Double)

object MovieRecommendation {
  implicit val writes = Json.writes[MovieRecommendation]
}