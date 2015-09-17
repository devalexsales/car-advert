package controllers

import java.util.UUID

import com.google.inject.Inject
import models.CarAdvert
import module.dao.CarAdvertDao
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json._
import play.api.mvc._
import models.Book._

class Application @Inject() (carAdvertDao: CarAdvertDao) extends Controller {

  def listCarAdverts = Action {
    Ok(Json.toJson(carAdvertDao.findAll()))
  }

  def getCarAdvertById(id: String) = Action {
    carAdvertDao.findById(id) match {
      case item => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

  val carAdvertForm = Form(
    mapping(
      "guid" -> text,
      "title" -> nonEmptyText,
      "fuel" -> nonEmptyText,
      "price" -> number,
      "isNew" -> boolean,
      "mileage" -> number,
      "firstRegistration" -> optional(date(pattern = "MM-dd-yyyy"))
    )(CarAdvert.apply)(CarAdvert.unapply)
  )

  def addCarAdvert = Action { implicit  request =>
    carAdvertForm.bindFromRequest.fold(
      formWithErrors => BadRequest(formWithErrors.errors.toString),
      carAdvert => {
        carAdvertDao.save(carAdvert.copy(guid = UUID.randomUUID().toString))
        Ok("")
      }
    )
  }

  def updateCarAdvert = Action { implicit  request =>
    carAdvertForm.bindFromRequest.fold(
      formWithErrors => BadRequest(formWithErrors.errors.toString),
      carAdvert => {
        if (carAdvertDao.update(carAdvert)) Ok("") else BadRequest("Unable to update carAdvert")
      }
    )
  }

  def removeCarAdvert(id: String) = Action {
    carAdvertDao.findById(id) match {
      case Some(item) => if (carAdvertDao.deleteBy(item.guid, item.title)) Ok("") else BadRequest("Unable to delete")
      case None => NotFound("")
    }
  }

  def listBooks = Action {
    Ok(Json.toJson(books))
  }

  def saveBook = Action(BodyParsers.parse.json) { request =>
    val b = request.body.validate[Book]
    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      book => {
        addBook(book)
        Ok(Json.obj("status" -> "OK"))
      }
    )
  }
}
