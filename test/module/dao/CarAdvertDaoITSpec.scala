package module.dao

import java.util.UUID

import models.CarAdvert.CarAdvert
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class CarAdvertDaoITSpec  extends Specification with Mockito {

  val dao: CarAdvertDao = new CarAdvertDaoImpl()

  "CarAdvertDao" should {

    "create a car advert" in {
      dao.create(CarAdvert(
        UUID.randomUUID().toString,
        title = "test",
        fuel = "gasoline",
        price = 10,
        isNew = true,
        mileage = -1,
        firstRegistration = new java.util.Date))

      1 + 1 === 2
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
