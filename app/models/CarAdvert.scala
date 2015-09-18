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
      map.get("mileage") match {
        case Some(item) => Some(item.getN.toInt)
        case None => None
      },
      map.get("firstRegistration") match {
        case Some(item) => Some(new Date(item.getN.toLong))
        case None => None
      }
    )
  }


  def getField(carAdvert: CarAdvert, sortField: String) = sortField match {
    case "fuel" => carAdvert.fuel
    case "isNew" => carAdvert.isNew
    case "mileage" => carAdvert.mileage match {
      case None => -1
      case Some(item) => item
    }
    case "price" => carAdvert.price
    case "title" => carAdvert.title
    case "firstRegistration" => carAdvert.firstRegistration match {
      case None => -1
      case Some(item) => item.getTime
    }
    case _ => carAdvert.guid
  }
}

case class CarAdvert(
                      guid: String,
                      title: String,
                      fuel: String,
                      price: Int,
                      isNew: Boolean,
                      mileage: Option[Int],
                      firstRegistration: Option[java.util.Date]
                      )
