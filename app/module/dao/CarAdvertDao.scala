package module.dao

import awscala.dynamodbv2._
import com.google.inject.ImplementedBy
import models.CarAdvert
import models.CarAdvert
import org.joda.time.DateTime
import org.slf4j.LoggerFactory


@ImplementedBy(classOf[CarAdvertDaoImpl])
trait CarAdvertDao {
  def deleteBy(id: String, title: String): Boolean

  def update(carAdvert: CarAdvert): Boolean

  def findAll(): List[CarAdvert]

  def findAll(sortField: String): List[CarAdvert]

  def findById(id: String): Option[CarAdvert]

  def save(carAdvert: CarAdvert): Unit

}

class CarAdvertDaoImpl extends CarAdvertDao {

  val CAR_ADVERTS: String = "car-adverts"
  implicit val dynamoDb: DynamoDB = DynamoDB.local()

  //init
  createTableIfMissing()

  def createTableIfMissing() = {
    dynamoDb.table(CAR_ADVERTS).getOrElse({
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
    })
  }

  override def save(carAdvert: CarAdvert): Unit = {
    saveOrUpdate(carAdvert)
  }

  override def findById(id: String): Option[CarAdvert] = dynamoDb.table(CAR_ADVERTS).get.query(Seq("guid" -> cond.eq(id))) match {
    case Nil => None
    case items => {
      Some(CarAdvert.toObject(items(0)))
    }
  }

  override def findAll(): List[CarAdvert] = {
    dynamoDb.table(CAR_ADVERTS).get.scan(Seq("guid" -> cond.ne("a"))) match {
      case items => items.map(item => CarAdvert.toObject(item)).toList
      case Nil => List()
    }
  }

  override def findAll(sortField: String): List[CarAdvert] = {
    dynamoDb.table(CAR_ADVERTS).get.scan(Seq("guid" -> cond.ne("a"))) match {
      case items => items.map(CarAdvert.toObject(_)).toList.sorted(CarAdvert.getOrdering(sortField))
      case Nil => List()
    }
  }

  override def update(carAdvert: CarAdvert): Boolean = findById(carAdvert.guid) match {
    case item => saveOrUpdate(carAdvert); true
    case None => false
  }

  override def deleteBy(id: String, title: String): Boolean = {
    dynamoDb.table(CAR_ADVERTS).get.delete(id, title);
    true
  }


  private def saveOrUpdate(carAdvert: CarAdvert): Unit = {
    val firstRegistration = carAdvert.firstRegistration match {
      case Some(item) => new DateTime(item).withTimeAtStartOfDay().getMillis // without time.
      case None => None
    }
    dynamoDb.table(CAR_ADVERTS).get.put(
      hashPK = carAdvert.guid,
      rangePK = carAdvert.title,
      "fuel" -> carAdvert.fuel,
      "price" -> carAdvert.price,
      "new" -> (if (carAdvert.isNew) 1 else 0),
      "mileage" -> carAdvert.mileage.getOrElse(-1),
      "firstRegistration" -> firstRegistration
    )
  }


}
