package controllers

import scala.collection.mutable.HashMap
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models._

object User extends Controller with Secured {

  type MySession = scala.slick.jdbc.JdbcBackend#Session
  
  val sampleProducts: Map[String, Product] = Map("Soap" -> Product(0, "Soap", 20.0f), "Cheese" -> Product(1, "Cheese", 1.0f), "Phone" -> Product(2, "Phone", 100.0f))

  val users = TableQuery[Customers]
  val products = TableQuery[Products]

  val shoppingCart: HashMap[String, List[Product]] = HashMap()

  val signUpForm = Form(
    mapping(
      "username" -> text,
      "password" -> text,
      "last name" -> text,
      "first name" -> text,
      "email address" -> text
    )((userName, password, lastName, firstName, emailAddress) => 
      Customer(None, userName, password, lastName, firstName, emailAddress))
    (customer => Some(customer.userName, customer.password, customer.lastName, customer.firstName, customer.emailAddress))
  )

  def loginForm(implicit session: MySession) = Form(
    tuple(
      "username" -> text,
      "password" -> text
      ) verifying ("Invalid name or password", result => result match {
        case (name, password) => check(name, password)
      })
  )

  def check(userName: String, password: String)(implicit session: MySession): Boolean =
    users.filter(_.userName === userName).first.password == password

  def index = DBAction { implicit request =>
    Ok(views.html.products(products.list))
  }

  def displayLogin = DBAction { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def login = DBAction { implicit request =>
    println("Recieved login request")
    val populatedForm = loginForm.bindFromRequest
    println(populatedForm.errors)
    populatedForm.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      userForm => {
        val user = users.filter(_.userName === userForm._1).first
        println("Logging in")
        Redirect(routes.User.index).withSession(Security.username -> user.userName)
      }
    )
  }

  def logout = Action {
    Redirect(routes.User.displayLogin).withNewSession.flashing(
      "success" -> "you are logged out"
    )
  }

  def displaySignUp = Action {
    Ok(views.html.signUp(signUpForm))
  }

  def signUp = DBAction { implicit request =>
    signUpForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.signUp(formWithErrors)),
      user => {
        users += user
        Redirect(routes.User.index).withSession(Security.username -> users.filter(_.userName === user.userName).first.userName)
      }
    )
  }
}
