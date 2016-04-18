package uk.org.lidalia.exampleapp.system.http.server

import org.mockito.BDDMockito.given
import org.scalatest.Outcome
import org.scalatest.fixture.FunSuite
import org.scalatest.mock.MockitoSugar
import uk.org.lidalia.http.client.SyncHttpClient
import uk.org.lidalia.http.core.{ByteEntity, Code, RequestUri, Method, Request, Response, Http}
import uk.org.lidalia.scalalang.ByteSeq

class JettyServerTests extends FunSuite with MockitoSugar {

  ignore("round trip") { case (client, mockServer) =>

    val request = Request(Method.GET, RequestUri("/path"), List())
    val response = Response(Code.OK, List(), new ByteEntity(ByteSeq()))
    given(mockServer.execute(request)).willReturn(response)

    assert(client.execute(request) == response)
  }

  override type FixtureParam = (SyncHttpClient[Response], Http[Response])

  override protected def withFixture(test: OneArgTest): Outcome = {
    ???
//    val mockServer = mock[Http[Response]]
//    new JettyServerDefinition(mockServer).using { server =>
//      val client = SyncHttpClient(server.baseUrl)
//      test((client, mockServer))
//    }
  }
}
