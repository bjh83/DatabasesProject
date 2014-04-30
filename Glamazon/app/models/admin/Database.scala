package models.admin

import scala.slick.driver.MySQLDriver.simple._
import models.user.Products

case class Administrator(id: Option[Int], userName: String, password: String, lastName: String, firstName: String, emailAddress: String)

class Administrators(tag: Tag) extends Table[Administrator](tag, "ADMINISTRATORS") {
  def id = column[Int]("AID", O.PrimaryKey, O.AutoInc)
  def userName = column[String]("USER_NAME")
  def password = column[String]("PASSWORD")
  def lastName = column[String]("LAST_NAME")
  def firstName = column[String]("FIRST_NAME")
  def emailAddress = column[String]("EMAIL_ADDRESS")

  def * = (id.?, userName, password, lastName, firstName, emailAddress) <> (Administrator.tupled, Administrator.unapply)
}

case class Vendor(id: Option[Int], name: String)

class Vendors(tag: Tag) extends Table[Vendor](tag, "VENDORS") {
  def id = column[Int]("VID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("Name")

  def * = (id.?, name) <> (Vendor.tupled, Vendor.unapply)
}

case class VendingEntry(id: Option[Int], vendorId: Int, productId: Int)

class Vending(tag: Tag) extends Table[VendingEntry](tag, "VENDING") {
  def id = column[Int]("VENDINGID", O.PrimaryKey, O.AutoInc)
  def vendorId = column[Int]("VID")
  def productId = column[Int]("UPC")
  def product = foreignKey("P_FK", productId, TableQuery[Products])(_.upc)
  def vendor = foreignKey("V_FK", vendorId, TableQuery[Vendors])(_.id)

  def * = (id.?, vendorId, productId) <> (VendingEntry.tupled, VendingEntry.unapply)
}
