package models.admin

import scala.slick.driver.MySQLDriver.simple._

case class Administrator(id: Option[Int], userName: String, password: String, lastName: String, firstName: String, emailAddress: String)

class Administrators(tag: Tag) extends Table[Administrator](tag, "ADMINISTRATORS") {
  def id = column[Int]("AID", O.PrimaryKey, O.AutoInc)
  def userName = column[String]("USER_NAME")
  def password = column[String]("PASSWORD")
  def lastName = column[String]("LAST_NAME")
  def firstName = column[String]("FIRST_NAME")
  def emailAddress = column[String]("EMAIL_ADDRESS")

  def * = (id.?, userName, password, lastName, firstName, emailAddress) <> (Administrator.tupled, Administrator.unapply)
}
