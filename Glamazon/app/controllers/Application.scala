package controllers

import scala.collection.mutable.HashMap
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Product

object Application extends Controller with Secured {
  
  val sampleProducts: Map[String, Product] = Map("Soap" -> Product(0, "Soap", 20.0f), "Cheese" -> Product(1, "Cheese", 1.0f), "Phone" -> Product(2, "Phone", 100.0f))

  val users: HashMap[String, String] = HashMap()

  val shoppingCart: HashMap[String, List[Product]] = HashMap()

  val signUpForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
      ) verifying ("Invalid name or password", result => result match {
        case (name, password) => !users.contains(name)
      })
  )

  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
      ) verifying ("Invalid name or password", result => result match {
        case (name, password) => check(name, password)
      })
  )

  def check(username: String, password: String): Boolean = 
    users.contains(username) && users(username) == password

  def index = withAuth { username => implicit request =>
    Ok(views.html.products(sampleProducts.values.toList))
  }

  // Users
  def displayLogin = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def login = Action { implicit request =>
    println("Recieved login request")
    val populatedForm = loginForm.bindFromRequest
    println(populatedForm.errors)
    populatedForm.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => {
        println("Logging in")
        Redirect(routes.Application.index).withSession(Security.username -> user._1)
      }
    )
  }

  def logout = Action {
    Redirect(routes.Application.displayLogin).withNewSession.flashing(
      "success" -> "you are logged out"
    )
  }

  def displaySignUp = Action {
    Ok(views.html.signUp(signUpForm))
  }

  def signUp = Action { implicit request =>
    signUpForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.signUp(formWithErrors)),
      user => {
        users(user._1) = user._2
        Redirect(routes.Application.index).withSession(Security.username -> user._1)
      }
    )
  }

  // Products
  def displayProducts = Action {
    Ok(views.html.products(sampleProducts.values.toList))
  }

  def product(productName: String) = Action {
    Ok(views.html.product(sampleProducts(productName)))
  }

  def addItem(productName: String) = withAuth { username => implicit request =>
    if (shoppingCart.contains(username)) {
      shoppingCart(username) = shoppingCart(username) ++ List(sampleProducts(productName))
    } else {
      shoppingCart(username) = List(sampleProducts(productName))
    }
    Ok(views.html.products(sampleProducts.values.toList))
  }

  // Shopping Cart
  def displayShoppingCart = withAuth { username => implicit request =>
    Ok(views.html.shoppingCart((shoppingCart.get(username) ++ List()).flatten.map { case Product(_, name, _) => name }.toList))
  }


  def checkout = TODO

}

trait Secured {
  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.displayLogin)

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
}
