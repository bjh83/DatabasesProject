package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models._

object Products extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session

  val products = TableQuery[Products]
  val shoppingCart = TableQuery[ShoppingCart]
  val users = TableQuery[Users]

  def displayProducts = DBAction {
    Ok(views.html.products(products.list))
  }

  def product(productName: String) = DBAction {
    Ok(views.html.product(products.filter(_.productName === productName).first))
  }

  def addItem(productName: String) = withDBAuth { username => implicit request =>
      val upc = products.filter(_.productName === productName).first
      val uid = users.filter(_.userName === productName).first
      shoppingCart += ShoppingCartEntry(None, uid, upc, 1)

      Ok(views.html.products(sampleProducts.values.toList))
  }
}
