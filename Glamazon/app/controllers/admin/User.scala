package controllers.admin

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import scala.slick.driver.MySQLDriver.simple._
import controllers.user.Secured
import models.admin._
import models.user._

object Users extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session

  val userAccounts = TableQuery[Customers]

  def userForm(implicit session: MySession) = Form(
      mapping(
          "user name" -> text,
          "password" -> text,
          "last name" -> text,
          "first name" -> text,
          "email address" -> text
          )((userName, password, lastName, firstName, emailAddress) =>
              Customer(None, userName, password, lastName, firstName, emailAddress))
          (admin => Some(admin.userName, admin.password, admin.lastName, admin.firstName, admin.emailAddress))
  )


  def users = withDBAuth { userName => implicit request =>
      Ok(views.html.admin.users(userAccounts.list, userForm))
  }

  def newUser = withDBAuth { userName => implicit request =>
      userForm.bindFromRequest.fold(
          errors => BadRequest(views.html.admin.users(userAccounts.list, errors)),
          account => {
              userAccounts += account
              Redirect(controllers.admin.routes.Users.users)
          }
      )
  }

  def deleteUser(id: Int) = withDBAuth { userName => implicit request =>
      val q = userAccounts.filter(_.id === id).delete
      Redirect(routes.Users.users)
  }
}
