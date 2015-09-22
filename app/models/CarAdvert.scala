package models

import java.util.Date

import awscala.dynamodbv2._
import models.CarAdvertField.CarAdvertField
import play.api.libs.json.Json

object CarAdvert {

  implicit val carAdvertWrites = Json.writes[CarAdvert]
  implicit val carAdvertReads = Json.reads[CarAdvert]

  val guidOrdering = new Ordering[CarAdvert] {
    override def compare(x: CarAdvert, y: CarAdvert): Int = { x.guid compare y.guid }
  }

  val titleOrdering = new Ordering[CarAdvert] {
    override def compare(x: CarAdvert, y: CarAdvert): Int = { x.title compare y.title }
  }

  val fuelOrdering = new Ordering[CarAdvert] {
    override def compare(x: CarAdvert, y: CarAdvert): Int = { x.fuel compare y.fuel }
  }

  val priceOrdering = new Ordering[CarAdvert] {
    override def compare(x: CarAdvert, y: CarAdvert): Int = { x.price compare y.price }
  }

  val newOrdering = new Ordering[CarAdvert] {
    override def compare(x: CarAdvert, y: CarAdvert): Int = {
      x.isNew compare y.isNew
    }
  }

    val mileageOrdering = new Ordering[CarAdvert] {
      override def compare(x: CarAdvert, y: CarAdvert): Int = {
        val a = x.mileage.getOrElse(-1)
        val b = y.mileage getOrElse(-1)

        a compare b
      }

    }

    val firstRegistrationOrdering = new Ordering[CarAdvert] {
      override def compare(x: CarAdvert, y: CarAdvert): Int = {
        val a = x.firstRegistration match {
          case None => -1
          case Some(item) => item.getTime
        }
        val b = y.firstRegistration match {
          case None => -1
          case Some(item) => item.getTime
        }

        a compare b
      }
    }


    def getField(carAdvert: CarAdvert, sortField: CarAdvertField) = sortField match {
      case CarAdvertField.Fuel => carAdvert.fuel
      case CarAdvertField.IsNew => carAdvert.isNew
      case CarAdvertField.Mileage => carAdvert.mileage.getOrElse(-1)
      case CarAdvertField.Price => carAdvert.price
      case CarAdvertField.Title => carAdvert.title
      case CarAdvertField.FirstRegistration => carAdvert.firstRegistration match {
        case None => -1
        case Some(item) => item.getTime
      }
      case _ => carAdvert.guid
    }

    def getOrdering(sortField: CarAdvertField) = sortField match {
      case CarAdvertField.Fuel => fuelOrdering
      case CarAdvertField.IsNew => newOrdering
      case CarAdvertField.Mileage => mileageOrdering
      case CarAdvertField.Price => priceOrdering
      case CarAdvertField.Title => titleOrdering
      case CarAdvertField.FirstRegistration => firstRegistrationOrdering
      case _ => guidOrdering
    }

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

object CarAdvertField {

  sealed trait CarAdvertField

  case object Guid extends CarAdvertField

  case object Title extends CarAdvertField

  case object Fuel extends CarAdvertField

  case object Price extends CarAdvertField

  case object IsNew extends CarAdvertField

  case object Mileage extends CarAdvertField

  case object FirstRegistration extends CarAdvertField

  def getCarAdvertField(s: String): Option[CarAdvertField] = s match {
    case "guid" => Some(Guid)
    case "title" => Some(Title)
    case "fuel" => Some(Fuel)
    case "price" => Some(Price)
    case "isNew" => Some(IsNew)
    case "mileage" => Some(Mileage)
    case "firstRegistration" => Some(FirstRegistration)
    case _ => None
  }
}
