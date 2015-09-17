package module.dao

import awscala.dynamodbv2._
import com.google.inject.ImplementedBy
import models.CarAdvert


@ImplementedBy(classOf[CarAdvertDaoImpl])
trait CarAdvertDao {
  def findAll(): List[CarAdvert]

  def findById(id: String): Option[CarAdvert]

  def create(carAdvert: CarAdvert): Unit

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

  override def create(carAdvert: CarAdvert): Unit = {
    dynamoDb.table(CAR_ADVERTS) match {
      case Some(table) => {
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
      case None => {
        createTable()
        create(carAdvert)
      }
    }
  }

  override def findById(id: String): Option[CarAdvert] = {
    dynamoDb.table(CAR_ADVERTS) match {
      case Some(table) => {
        table.query(Seq("guid" -> cond.eq(id))) match {
          case items => {
            Some(CarAdvert.toObject(items(0)))
          }
          case Nil => None
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
}
