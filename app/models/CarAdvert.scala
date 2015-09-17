package models

import java.util.Date

import awscala.dynamodbv2._
import play.api.libs.json.Json

object CarAdvert {
  implicit val carAdvertWrites = Json.writes[CarAdvert]
  implicit val carAdvertReads = Json.reads[CarAdvert]

  def toObject(item: Item): CarAdvert = {
    val map = ToEnhancedDynamoOps.toEnhancedItem(item).attributesMap
    CarAdvert(
      map.get("guid").get.getS,
      map.get("title").get.getS,
      map.get("fuel").get.getS,
      map.get("price").get.getN.toInt,
      if (map.get("new").get.getN.toInt == 1) true else false,
      map.get("mileage").get.getN.toInt,
      new Date(map.get("firstRegistration").get.getN.toLong)
    )
  }
}

case class CarAdvert(
                      guid: String,
                      title: String,
                      fuel: String,
                      price: Int,
                      isNew: Boolean,
                      mileage: Int,
                      firstRegistration: java.util.Date
                      )
