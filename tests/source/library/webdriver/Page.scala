package uk.org.lidalia
package exampleapp
package tests
package library.webdriver

import uk.org.lidalia.net.UriReference

trait PageFactory[P <: Page[P]] {

  def url: UriReference
  def apply(reusableWebDriver: ReusableWebDriver): P
}

trait Page[P <: Page[P]] {

  val driver: ReusableWebDriver

  def title = driver.getTitle

  def isCurrentPage: Boolean

}
