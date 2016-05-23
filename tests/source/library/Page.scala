package uk.org.lidalia
package exampleapp.tests
package library

import uk.org.lidalia.net.{UriReference, Url}

trait PageFactory[P <: Page[P]] {

  def url: UriReference
  def apply(reusableWebDriver: ReusableWebDriver): P
}

trait Page[P <: Page[P]] {

}
