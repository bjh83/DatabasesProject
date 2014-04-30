package controllers.user

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.user._

object Products extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session

  val products = TableQuery[Products]
  val shoppingCart = TableQuery[ShoppingCart]
  val users = TableQuery[Customers]

  def displayProducts = DBAction { implicit request =>
    Ok(views.html.products(products.list))
  }

  def product(productName: String) = DBAction { implicit request =>
    Ok(views.html.product(products.filter(_.productName === productName).first))
  }

  def addItem(productName: String) = withDBAuth { username => implicit request =>
      val upc = products.filter(_.productName === productName).first.upc
      val uid = users.filter(_.userName === productName).first.id.get
      shoppingCart += ShoppingCartEntry(None, uid, upc, 1)

      Ok(views.html.products(products.list))
  }
}
