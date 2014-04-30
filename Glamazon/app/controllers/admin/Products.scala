package controllers.admin

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import controllers.user.Secured
import models.admin._
import models.user._

object Products extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session
  val products = TableQuery[Products]

  def productForm(implicit session: MySession) = Form(
      mapping(
          "upc" -> number,
          "product name" -> text,
          "selling cost" -> of[Double]
      )(Product.apply)(Product.unapply)
  )

  def products = withDBAuth { userName => implicit request =>
      OK(views.html.admin.products(products.list, productForm))
  }

  def newProduct = withDBAuth { userName => implicit request =>
      productForm.bindFromRequest.fold(
          errors => BadRequest(views.html.admin.products(products.list, errors)),
          product => {
              products += product
              Redirect(routes.Products.products)
          }
      )
  }

  def deleteProduct(id: Int) = withDBAuth { userName => implicit request =>
      products.filter(_.upc === id).deletei
      Redirect(routes.Products.products)
  }
}
