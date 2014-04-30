package controllers.admin

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
// import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.MySQLDriver.simple._
import controllers.user.Secured
import models.admin._
import models.user._

object AdminAccounts extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session

  val admins = TableQuery[Administrators]

  def adminForm(implicit session: MySession) = Form(
      mapping(
          "user name" -> text,
          "password" -> text,
          "last name" -> text,
          "first name" -> text,
          "email address" -> text
          )((userName, password, lastName, firstName, emailAddress) =>
              Administrator(None, userName, password, lastName, firstName, emailAddress))
          (admin => Some(admin.userName, admin.password, admin.lastName, admin.firstName, admin.emailAddress))
  )


  def accounts = withDBAuth { userName => implicit request =>
      Ok(views.html.admin.admin(admins.list, adminForm))
  }

  def newAccount = withDBAuth { userName => implicit request =>
      adminForm.bindFromRequest.fold(
          errors => BadRequest(views.html.admin.admin(admins.list, errors)),
          account => {
              admins += account
              Redirect(routes.AdminAccounts.accounts)
          }
      )
  }

  def deleteAccount(id: Int) = withDBAuth { userName => implicit request =>
      val q = admins.filter(_.id === id).delete
      Redirect(routes.AdminAccounts.accounts)
  }
}
