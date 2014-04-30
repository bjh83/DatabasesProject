package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models._

object ShoppingCart extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session

  val products = TableQuery[Products]
  val shoppingCart = TableQuery[ShoppingCart]
  val users = TableQuery[Customers]

  def displayShoppingCart = withDBAuth { username => implicit request =>
    val uid = users.filter(_.userName === username).first.id.get
    val shoppingCartContents = shoppingCart.filter(_.customerId === uid).flatMap(sc => products.filter(sc.productId === _.upc)).map(_.productName).list
    Ok(views.html.shoppingCart(shoppingCartContents))
  }

  def checkout = TODO
}
