package models

import awscala._, dynamodbv2._

/**
 * Enhanced dynamo item to search by map
 */
class EnhancedDynamoItem(item: Item) {
  def attributesMap: Map[String, AttributeValue] =
    item.attributes
      .foldLeft(Map.empty[String, AttributeValue])((m, a) =>
      m ++ Map(a.name -> a.value))
}

object ToEnhancedDynamoOps {
  implicit def toEnhancedItem(item: Item): EnhancedDynamoItem =
    new EnhancedDynamoItem(item)

}

