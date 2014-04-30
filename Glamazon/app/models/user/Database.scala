package models.user

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

case class Product(upc: Int, productName: String, sellingCost: Float)

class Products(tag: Tag) extends Table[Product](tag, "PRODUCTS") {
    def upc = column[Int]("UPC", O.PrimaryKey)
    def productName = column[String]("PRODUCT_NAME")
    def sellingCost = column[Float]("SELLING_COST")

    def * = (upc, productName, sellingCost) <> (Product.tupled, Product.unapply)
}

case class Customer(id: Option[Int], userName: String, password: String, lastName: String, firstName: String, emailAddress: String)

class Customers(tag: Tag) extends Table[Customer](tag, "CUSTOMERS") {
    def id = column[Int]("CID", O.PrimaryKey, O.AutoInc)
    def userName = column[String]("USER_NAME")
    def password = column[String]("PASSWORD")
    def lastName = column[String]("LAST_NAME")
    def firstName = column[String]("FIRST_NAME")
    def emailAddress = column[String]("EMAIL_ADDRESS")

    def * = (id.?, userName, password, lastName, firstName, emailAddress) <> (Customer.tupled, Customer.unapply)
}

case class ShoppingCartEntry(id: Option[Int], customerId: Int, productId: Int, quantity: Int)

class ShoppingCart(tag: Tag) extends Table[ShoppingCartEntry](tag, "SHOPPING_CART") {
  def id = column[Int]("SCID", O.PrimaryKey, O.AutoInc)
  def customerId = column[Int]("CID")
  def productId = column[Int]("UPC")
  def quantity = column[Int]("QUANTITY")
  def customer = foreignKey("C_FK", customerId, TableQuery[Customers])(_.id)
  def product = foreignKey("P_FK", productId, TableQuery[Products])(_.upc)

  def * = (id.?, customerId, productId, quantity) <> (ShoppingCartEntry.tupled, ShoppingCartEntry.unapply)
}

case class Purchase(id: Option[Int], date: Option[Timestamp], customerId: Int)

class Purchases(tag: Tag) extends Table[Purchase](tag, "PURCHASES") {
  def id = column[Int]("PID", O.PrimaryKey, O.AutoInc)
  def date = column[Timestamp]("DATE")
  def customerId = column[Int]("CID")
  def customer = foreignKey("C_FK", customerId, TableQuery[Customers])(_.id)

  def * = (id.?, date.?, customerId) <> (Purchase.tupled, Purchase.unapply)
}

case class PurchaseEntry(id: Option[Int], productId: Int, quantity: Int, purchaseId: Int)

class PurchaseEntries(tag: Tag) extends Table[PurchaseEntry](tag, "PURCHASE_ENTRIES") {
  def id = column[Int]("PEID", O.PrimaryKey, O.AutoInc)
  def productId = column[Int]("UPC")
  def quantity = column[Int]("QUANTITY")
  def purchaseId = column[Int]("PID")
  def product = foreignKey("P_FK", productId, TableQuery[Products])(_.upc)
  def purchase = foreignKey("PURCHASE_FK", purchaseId, TableQuery[Purchases])(_.id)
  
  def * = (id.?, productId, quantity, purchaseId) <> (PurchaseEntry.tupled, PurchaseEntry.unapply)
}
