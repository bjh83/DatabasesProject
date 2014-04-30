package controllers.user

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import scala.slick.driver.MySQLDriver.simple._
import models.user._

object ShoppingCart extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session

  val products = TableQuery[Products]
  val shoppingCart = TableQuery[ShoppingCart]
  val users = TableQuery[Customers]
  val purchases = TableQuery[Purchases]
  val purchaseEntries = TableQuery[PurchaseEntries]

  def displayShoppingCart = withDBAuth { username => implicit request =>
    val uid = users.filter(_.userName === username).first.id.get
    val shoppingCartContents = shoppingCart.filter(_.customerId === uid).flatMap(sc => products.filter(sc.productId === _.upc)).map(_.productName).list
    Ok(views.html.user.shoppingCart(shoppingCartContents))
  }

  def checkout = withDBAuth { username => implicit request =>
    val uid = users.filter(_.userName === username).first.id.get
    val shoppingCartProducts = products.flatMap(p => shoppingCart.filter(_.customerId === uid).filter(p.upc === _.productId))
    val purchaseId = (purchases returning purchases.map(_.id)) += Purchase(None, None, uid)
    // val purchaseId = purchases.filter(_.customerId === uid).sortBy(_.date).last.id.get
    purchaseEntries ++= shoppingCartProducts.list.map {sc => PurchaseEntry(None, sc.productId, sc.quantity, purchaseId)}
    shoppingCart.filter(_.customerId === uid).delete
    Redirect(routes.Products.displayProducts)
  }

  def empty = withDBAuth { username => implicit request =>
    val uid = users.filter(_.userName === username).first.id.get
    shoppingCart.filter(_.customerId === uid).flatMap(sc => products.filter(sc.productId === _.upc)).delete
    Redirect(routes.Products.displayProducts)
  }
}
