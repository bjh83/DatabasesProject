package models

import scala.slick.driver.MySQLDriver.simple._

case class Product(upc: Int, productName: String, sellingCost: Double)

class Products(tag: Tag) extends Table[(Int, String, Double)](tag, "PRODUCTS") {
    def upc = column[Int]("UPC", O.PrimaryKey)
    def productName = column[String]("PRODUCT_NAME")
    def sellingCost = column[Double]("SELLING_COST")

    def * = upc ~ productName ~ sellingCost <> (Product.tupled, Product.unapply)
}

case class Customer(id: Int, userName: String, password: String, lastName: String, firstName: String, emailAddress: String)

class Customers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "CUSTOMERS") {
    def id = column[Int]("CID", O.PrimaryKey)
    def userName = column[String]("USER_NAME")
    def password = column[String]("PASSWORD")
    def lastName = column[String]("LAST_NAME")
    def firstName = column[String]("FIRST_NAME")
    def emailAddress = column[String]("EMAIL_ADDRESS")

    def * = id ~ userName ~ password ~ lastName ~ firstName ~ emailAddress <> (Customer.tupled, Customer.unapply)
}

case class ShoppingCart
