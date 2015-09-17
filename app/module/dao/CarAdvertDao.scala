package module.dao

import awscala.dynamodbv2._
import com.google.inject.ImplementedBy
import models.CarAdvert


@ImplementedBy(classOf[CarAdvertDaoImpl])
trait CarAdvertDao {
  def deleteBy(id: String, title: String): Boolean

  def update(carAdvert: CarAdvert): Boolean

  def findAll(): List[CarAdvert]

  def findById(id: String): Option[CarAdvert]

  def save(carAdvert: CarAdvert): Unit

}

class CarAdvertDaoImpl extends CarAdvertDao {

  val CAR_ADVERTS: String = "car-adverts"
  implicit val dynamoDb: DynamoDB = DynamoDB.local()

  def createTable() = {
    val tableMeta: TableMeta = dynamoDb.createTable(
      name = CAR_ADVERTS, hashPK = "guid" -> AttributeType.String, rangePK = "title" -> AttributeType.String,
    otherAttributes = Seq(),
    indexes = Seq(LocalSecondaryIndex(
      name = "car-adverts-index",
      keySchema = Seq(
        KeySchema("guid", KeyType.Hash),
        KeySchema("title", KeyType.Range)
      ),
      projection = Projection(ProjectionType.Include, Seq("title"))
    ))
    )
  }

  override def save(carAdvert: CarAdvert): Unit = {
    dynamoDb.table(CAR_ADVERTS) match {
      case Some(table) => {
          saveOrUpdate(carAdvert, table)
      }
      case None => {
        createTable()
        save(carAdvert)
      }
    }
  }

  override def findById(id: String): Option[CarAdvert] = {
    dynamoDb.table(CAR_ADVERTS) match {
      case Some(table) => {
        table.query(Seq("guid" -> cond.eq(id))) match {
          case Nil => None
          case items => {
            Some(CarAdvert.toObject(items(0)))
          }
        }
      }
      case None => {
        createTable()
        findById(id)
      }
    }
  }

  override def findAll(): List[CarAdvert] = {
    dynamoDb.table(CAR_ADVERTS) match {
      case Some(table) => {
        table.scan(Seq("guid" -> cond.ne("a"))) match {
          case items => items.map(item => CarAdvert.toObject(item)).toList
          case Nil => List()
        }
      }
      case None => {
        createTable()
        findAll()
      }
    }
  }

  override def update(carAdvert: CarAdvert): Boolean = {
    dynamoDb.table(CAR_ADVERTS) match {
      case Some(table) => {
        findById(carAdvert.guid) match {
          case item => saveOrUpdate(carAdvert, table); true
          case None => false
        }
      }
      case None => {
        createTable()
        update(carAdvert)
      }
    }
  }

  override def deleteBy(id: String, title: String): Boolean = {
    dynamoDb.table(CAR_ADVERTS) match {
      case Some(table) => table.delete(id, title); true
      case None => {
        createTable()
        deleteBy(id, title)
      }
    }
  }

  private def saveOrUpdate(carAdvert: CarAdvert, table: Table): Unit = {
    table.put(
      hashPK = carAdvert.guid,
      rangePK = carAdvert.title,
      "fuel" -> carAdvert.fuel,
      "price" -> carAdvert.price,
      "new" -> (if (carAdvert.isNew) 1 else 0),
      "mileage" -> carAdvert.mileage,
      "firstRegistration" -> (if (carAdvert.isNew) -1 else carAdvert.firstRegistration.get.getTime)
    )
  }


}
