package models

object FuelType {
  sealed trait FuelTypes

  case object Diesel extends FuelTypes

  case object Gasoline extends FuelTypes

  def getFuelType(s: String): Option[FuelTypes] = s match {
    case "gasoline" => Some(Gasoline)
    case "diesel" => Some(Diesel)
    case _ => None
  }
}
