package module.dao

import java.util.UUID

import models.CarAdvert
import org.slf4j.LoggerFactory
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class CarAdvertDaoITSpec  extends Specification with Mockito {
  val log = LoggerFactory.getLogger(this.getClass)

  val dao: CarAdvertDao = new CarAdvertDaoImpl()

  "CarAdvertDao" should {

    "a car advert integration tests" in {
      val id: String = UUID.randomUUID().toString

      //create a car advert
      dao.create(CarAdvert(
        id,
        title = "test",
        fuel = "gasoline",
        price = 10,
        isNew = true,
        mileage = -1,
        firstRegistration = None))

      //find by id
      val carAdvert: CarAdvert = dao.findById(id).get
      log.info(carAdvert.toString)
      carAdvert.guid === id

      //find all
      val carAdverts: List[CarAdvert] = dao.findAll()
      log.info(carAdverts.toString)
      carAdverts.size must_!=(0)
    }

//    "find all cars" in {
//
//    }
//
//    "find car by id" in {
//
//    }
//
//    "update a car advert by id" in {
//
//    }
//
//    "delete a car advert by id" in {
//
//    }

  }

}
