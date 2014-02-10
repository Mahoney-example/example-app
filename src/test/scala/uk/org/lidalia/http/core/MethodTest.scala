package uk.org.lidalia.http.core

import org.scalatest
import scalatest.PropSpec
import scalatest.prop.TableDrivenPropertyChecks
import scalatest.junit.JUnitRunner
import uk.org.lidalia.http.core.Method._
import org.junit.runner.RunWith
import scala.collection.immutable.Set
import java.lang.IllegalArgumentException

@RunWith(classOf[JUnitRunner])
class MethodTest extends PropSpec with TableDrivenPropertyChecks {

  val methodSpecifications =
    Table(
      ("method", "name",    "is safe", "is idempotent", "request may have entity", "response may have entity"),
      (GET,      "GET",     true,      true,            false,                     true                      ),
      (HEAD,     "HEAD",    true,      true,            false,                     false                     ),
      (PUT,      "PUT",     false,     true,            true,                      true                      ),
      (DELETE,   "DELETE",  false,     true,            false,                     true                      ),
      (POST,     "POST",    false,     false,           true,                      true                      ),
      (PATCH,    "PATCH",   false,     false,           true,                      true                      ),
      (OPTIONS,  "OPTIONS", true,      true,            true,                      true                      ),
      (TRACE,    "TRACE",   true,      true,            false,                     true                      )
    )
  
  property("a Method must have the correct specifications") {
    forAll(methodSpecifications) { (method, name, shouldBeSafe, shouldBeIdempotent, reqShouldBeAbleToHaveEntity, resShouldBeAbleToHaveEntity) =>
      assert(method.isSafe === shouldBeSafe)
      assert(method.isUnsafe === !shouldBeSafe)

      assert(method.isIdempotent === shouldBeIdempotent)
      assert(method.isNotIdempotent === !shouldBeIdempotent)

      assert(method.requestMayHaveEntity === reqShouldBeAbleToHaveEntity)
      assert(method.requestMayNotHaveEntity === !reqShouldBeAbleToHaveEntity)

      assert(method.responseMayHaveEntity === resShouldBeAbleToHaveEntity)
      assert(method.responseMayNotHaveEntity === !resShouldBeAbleToHaveEntity)

      assert(method.name === name)
      assert(method.toString === name)
    }
  }

  property("values returns all methods") {
    assert(Method.values() === Set(GET, HEAD, PUT, DELETE, POST, PATCH, OPTIONS, TRACE))
  }

  property("unable to re-register a method") {
    val exception = intercept[IllegalStateException] {
      Method.registerNonIdempotentMethod("GET", requestMayHaveEntity = true, responseMayHaveEntity = true)
    }
    assert(exception.getMessage === "Only one instance of a method may exist! Trying to create duplicate of GET")
  }

  property("apply returns existing method") {
    val method = Method("GET")
    assert(method === GET)
  }

  property("apply throws exception if unknown method") {
    val exception = intercept[UnknownMethodException] {
      Method("UNKNOWN")
    }
    assert(exception.getMessage === "Method not found: UNKNOWN; consider registering it.")
  }
}
