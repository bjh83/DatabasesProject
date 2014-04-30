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

  def displayShoppingCart = withDBAuth { username => implicit request =>
    val uid = users.filter(_.userName === username).first
    val shoppingCartContents = shoppingCart.filter(_.customerId === uid).list
    Ok(views.html.shoppingCart(shoppingCartContents))
  }

  def checkout = TODO
}
