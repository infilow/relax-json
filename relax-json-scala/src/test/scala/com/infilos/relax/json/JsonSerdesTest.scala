package com.infilos.relax.json

import com.infilos.relax.{Domain, Json4s}
import org.junit.Test

/**
  * @author infilos on 2020-11-05.
  *
  */
class JsonSerdesTest {

  @Test
  def test(): Unit = {
    assert(Json4s.from(Domain("name", "value")).asString() == "name#value")
    assert(
      Json4s.from("name#value").asObject(classOf[Domain]) == Domain(
        "name",
        "value"
      )
    )
  }
}
