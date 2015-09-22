package controllers

import java.util.UUID

import com.google.inject.Inject
import models.{FuelType, CarAdvert}
import module.dao.CarAdvertDao
import org.slf4j.LoggerFactory
import play.api.libs.json._
import play.api.mvc._

class Application @Inject() (carAdvertDao: CarAdvertDao) extends Controller {
  val log = LoggerFactory.getLogger(this.getClass)

  def listCarAdverts = Action { implicit request =>
    val sortField = request.queryString.map { case (k,v) => k -> v.mkString }.get("sort") match {
      case None => "guid"
      case Some(str) => str
    }

    Ok(Json.prettyPrint(Json.toJson(carAdvertDao.findAll(sortField))))
  }

  def getCarAdvertById(id: String) = Action {
    carAdvertDao.findById(id) match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound(Json.obj("status" ->"KO", "message" -> (s"${id} not found.")))
    }
  }


  def addCarAdvert = Action(BodyParsers.parse.json) { implicit  request =>
    val carAdvertJson = request.body.validate[CarAdvert]
    carAdvertJson.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors)))
      },
      carAdvert => {
        if (FuelType.values.map(f => f.toString.toLowerCase()).contains(carAdvert.fuel)) {
          carAdvertDao.save(carAdvert.copy(guid = UUID.randomUUID().toString))
          Ok(Json.obj("status" ->"OK", "message" -> ("CarAdvert '"+carAdvert.title+"' saved.") ))
        } else {
          BadRequest(Json.obj("status" ->"KO", "message" -> s"Invalid fuel named ${carAdvert.fuel}"))
        }
      }
    )
  }

  def updateCarAdvert = Action(BodyParsers.parse.json) { implicit  request =>
    val carAdvertJson = request.body.validate[CarAdvert]
    carAdvertJson.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors)))
      },
      carAdvert => {
        if (FuelType.values.map(f => f.toString.toLowerCase()).contains(carAdvert.fuel)) {
          if (carAdvertDao.update(carAdvert)) Ok(s"${carAdvert.title} was updated.") else BadRequest("Unable to update carAdvert")
        } else {
          BadRequest(Json.obj("status" -> "KO", "message" -> s"Invalid fuel named ${carAdvert.fuel}"))
        }
      }
    )
  }

  def removeCarAdvert(id: String) = Action {
    carAdvertDao.findById(id) match {
      case Some(item) => if (carAdvertDao.deleteBy(item.guid, item.title)) Ok("") else BadRequest("Unable to delete")
      case None => NotFound(Json.obj("status" ->"KO", "message" -> (s"${id} not found.")))
    }
  }

}
