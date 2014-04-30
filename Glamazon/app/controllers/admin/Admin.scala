package controllers.admin

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import controllers.user.Secured
import models.admin._

object Admin extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session

  val admins = TableQuery[Administrators]

  def loginForm(implicit session: MySession) = Form(
    tuple(
      "username" -> text,
      "password" -> text
      ) verifying ("Invalid name or password", result => result match {
        case (name, password) => check(name, password)
      })
  )

  def check(userName: String, password: String)(implicit session: MySession): Boolean =
    admins.filter(_.userName === userName).first.password == password

  def index = withDBAuth { userName => implicit request =>
    Ok(views.html.admin.index())
  }

  def displayLogin = DBAction { implicit request =>
    Ok(views.html.admin.login(loginForm))
  }

  def login = DBAction { implicit request =>
    val populatedForm = loginForm.bindFromRequest
    println(populatedForm.errors)
    populatedForm.fold(
      formWithErrors => BadRequest(views.html.admin.login(formWithErrors)),
      userForm => {
        val admin = admins.filter(_.userName === userForm._1).first
        Redirect(routes.Admin.index).withSession(Security.username -> admin.userName)
      }
    )
  }

  def logout = Action {
    Redirect(routes.Admin.displayLogin).withNewSession.flashing(
      "success" -> "you are logged out"
    )
  }
}
