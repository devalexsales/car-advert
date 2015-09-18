package module.dao

import java.util.UUID

import models.CarAdvert
import org.slf4j.LoggerFactory
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class CarAdvertDaoITSpec  extends Specification {
  val log = LoggerFactory.getLogger(this.getClass)

  val dao: CarAdvertDao = new CarAdvertDaoImpl()

  "CarAdvertDao" should {

    "a car advert integration tests" in {
      val id: String = UUID.randomUUID().toString
      val id2: String = UUID.randomUUID().toString

      //create a car advert
      dao.save(CarAdvert(
        id,
        title = "test",
        fuel = "gasoline",
        price = 10,
        isNew = false,
        mileage = Some(100),
        firstRegistration = Some(new java.util.Date())))

      dao.save(CarAdvert(
        id2,
        title = "test",
        fuel = "gasoline",
        price = 10,
        isNew = true,
        mileage = None,
        firstRegistration = None))

      //find by id
      var carAdvert: CarAdvert = dao.findById(id).get
      log.info(carAdvert.toString)
      carAdvert.guid === id
      carAdvert.fuel === "gasoline"
      carAdvert.isNew === false

      var carAdvert2: CarAdvert = dao.findById(id2).get
      log.info(carAdvert2.toString)
      carAdvert2.guid === id2
      carAdvert2.fuel === "gasoline"
      carAdvert2.isNew === true

      //find all
      val carAdverts: List[CarAdvert] = dao.findAll()
      log.info(carAdverts.toString)
      carAdverts.size must_!=(0)

      //update a car advert by id
      val isUpdated = dao.update(carAdvert.copy(fuel = "diesel"))
      isUpdated === true
      carAdvert = dao.findById(id).get
      carAdvert.fuel === "diesel"

      //delete a car advert by id
      val isDeleted = dao.deleteBy(id, carAdvert.title)
      isDeleted === true
      dao.findById(id) match {
        case None => success
        case _ => failure
      }
    }

  }

}
