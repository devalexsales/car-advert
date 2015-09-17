package module.dao

import awscala.dynamodbv2.DynamoDB
import org.slf4j.LoggerFactory
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import awscala._, dynamodbv2._

class DynamoDBITSpec extends Specification {
  val log = LoggerFactory.getLogger(this.getClass)

  implicit val dynamoDB = DynamoDB.local()

  "DynamoDb " should {
    "work" in {
      val tableMeta: TableMeta = dynamoDB.createTable(
        name = "Members",
        hashPK =  "Id" -> AttributeType.Number,
        rangePK = "Country" -> AttributeType.String,
        otherAttributes = Seq("Company" -> AttributeType.String),
        indexes = Seq(LocalSecondaryIndex(
          name = "CompanyIndex",
          keySchema = Seq(KeySchema("Id", KeyType.Hash), KeySchema("Company", KeyType.Range)),
          projection = Projection(ProjectionType.Include, Seq("Company"))
        ))
      )

      val table: Table = dynamoDB.table("Members").get

      table.put(1, "Japan", "Name" -> "Alice", "Age" -> 23, "Company" -> "Google")
      table.put(2, "U.S.",  "Name" -> "Bob",   "Age" -> 36, "Company" -> "Google")
      table.put(3, "Japan", "Name" -> "Chris", "Age" -> 29, "Company" -> "Amazon")

      val googlers: Seq[Item] = table.scan(Seq("Company" -> cond.gt("Google")))

      table.destroy()

      1 + 1 === 2
    }
  }

}
