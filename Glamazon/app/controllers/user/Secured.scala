package controllers.user

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._

trait Secured {
  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.user.User.displayLogin)

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }

  def withDBAuth(f: => String => DBSessionRequest[_] => SimpleResult) = {
      Security.Authenticated(username, onUnauthorized) { user =>
          DBAction(request => f(user)(request))
      }
  }
}
