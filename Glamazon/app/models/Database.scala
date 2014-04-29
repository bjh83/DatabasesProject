package models

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

case class ShoppingCartEntry(id: Int, customerId: Int, productId: Int, quantity: Int)

class ShoppingCart(tag: Tag) extends Table[ShoppingCartEntry](tag, "SHOPPING_CART") {
  def id = column[Int]("SCID", O.PrimaryKey, O.AutoInc)
  def customerId = column[Int]("CID")
  def productId = column[Int]("UPC")
  def quantity = column[Int]("QUANTITY")
  def customer = foreignKey("C_FK", customerId, TableQuery[Customers])(_.id)
  def product = foreignKey("P_FK", productId, TableQuery[Products])(_.upc)

  def * = (id, customerId, productId, quantity) <> (ShoppingCartEntry.tupled, ShoppingCartEntry.unapply)
}
