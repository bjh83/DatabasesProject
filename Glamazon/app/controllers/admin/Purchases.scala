package controllers.admin

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.db.slick._
import scala.slick.driver.MySQLDriver.simple._
import controllers.user.Secured
import models.admin._
import models.user._

object Purchases extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session
  val purchases = TableQuery[Purchases]
  val purchaseEntries = TableQuery[PurchaseEntries]
  val products = TableQuery[Products]

  def transactions = withDBAuth { userName => implicit request =>
    Ok(views.html.admin.transactions(purchases.list))
  }

  def transaction(id: Int) = withDBAuth { userName => implicit request =>
    val purchasedProducts = purchaseEntries.filter(_.purchaseId === id).flatMap(p => products.filter(p.productId === _.upc))
    Ok(views.html.admin.transaction(purchasedProducts.list))
  }

}
