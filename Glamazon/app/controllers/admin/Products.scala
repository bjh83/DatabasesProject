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

object Products extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session
  val productAdmin = TableQuery[Products]

  def productForm(implicit session: MySession) = Form(
      mapping(
          "upc" -> number,
          "product name" -> text,
          "selling cost" -> of[Float]
      )(Product.apply)(Product.unapply)
  )

  def products = withDBAuth { userName => implicit request =>
    Ok(views.html.admin.products(productAdmin.list, productForm))
  }

  def newProduct = withDBAuth { userName => implicit request =>
      productForm.bindFromRequest.fold(
          errors => {
            println(errors)
            BadRequest(views.html.admin.products(productAdmin.list, errors))
          },
          product => {
            println("Adding product: " + product)
            productAdmin += product
            Redirect(routes.Products.products)
          }
      )
  }

  def deleteProduct(id: Int) = withDBAuth { userName => implicit request =>
      productAdmin.filter(_.upc === id).delete
      Redirect(routes.Products.products)
  }
}
