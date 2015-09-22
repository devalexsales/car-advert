package models

import java.util.Date

import awscala.dynamodbv2._
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


    def getField(carAdvert: CarAdvert, sortField: String) = sortField match {
      case "fuel" => carAdvert.fuel
      case "isNew" => carAdvert.isNew
      case "mileage" => carAdvert.mileage.getOrElse(-1)
      case "price" => carAdvert.price
      case "title" => carAdvert.title
      case "firstRegistration" => carAdvert.firstRegistration match {
        case None => -1
        case Some(item) => item.getTime
      }
      case _ => carAdvert.guid
    }

    def getOrdering(sortField: String) = sortField match {
      case "fuel" => fuelOrdering
      case "isNew" => newOrdering
      case "mileage" => mileageOrdering
      case "price" => priceOrdering
      case "title" => titleOrdering
      case "firstRegistration" => firstRegistrationOrdering
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
