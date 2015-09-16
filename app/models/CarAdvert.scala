package models

import play.api.libs.json.Json

object CarAdvert {
  implicit val carAdvertWrites = Json.writes[CarAdvert]
  implicit val carAdvertReads = Json.reads[CarAdvert]

  case class CarAdvert(
                        guid: String,
                       title: String,
                       fuel: String,
                       price: Int,
                       isNew: Boolean,
                       mileage: Int,
                       firstRegistration: java.util.Date
                        )
}
