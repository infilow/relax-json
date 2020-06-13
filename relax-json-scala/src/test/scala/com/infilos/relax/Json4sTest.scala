package com.infilos.relax

import java.io.IOException

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.node.ObjectNode
import org.junit.Assert._
import org.junit.Test

import scala.collection.JavaConverters._

/**
  * @author infilos on 2020-06-13.
  *
  */
object Json4sTest {
  case class User(name: String)
  case class Container[T](elements: List[T])
}

class Json4sTest {
  import Json4sTest._

  @Test
  def testValidJson(): Unit = {
    assertTrue(Json4s.isValidJsonString("{\"name\":\"Anna\"}"))
    assertFalse(Json4s.isValidJsonString("{\"name\"\"Anna\"}"))
    assertFalse(Json4s.isValidJsonString("{\"name\":\"Anna}"))
  }

  @Test
  def testString(): Unit = {
    val json = Json4s.from("{\"name\":\"Anna\"}")
    assertEquals("{\"name\":\"Anna\"}", json.asString)

    val json1 = Json4s.from("22")
    assertEquals("22", json1.asString)

    val json2 = Json4s.from("[1,2,3]")
    assertEquals("[1,2,3]", json2.asString)

    System.out.println(json2.asPrettyString)
  }

  @Test
  def testMap(): Unit = {
    val map = Map("1" -> "A", "2" -> "B", "3" -> "C")

    val json = Json4s.from(map)
    assertEquals(map, json.asMap().asScala)
  }

  @Test
  def testNestedMap(): Unit = {
    val inner = Map("A" -> 1, "B" -> 2)
    val outer = Map(1 -> inner, 2 -> inner)

    val originString = Json4s.from(outer).asString()

    val scalaMap = Json4s.from(outer).asMap().asScala
    val scalaString = Json4s.from(scalaMap).asString()

    assertEquals(originString, scalaString)
    assertEquals(originString, Json4s.from(scalaMap).asString())
  }

  @Test
  def testObject(): Unit = {
    val user = User("Anna")
    val json = Json4s.from(user)

    assertEquals(user, json.asObject(classOf[User]))
  }

  @Test
  @throws[IOException]
  def testJsonNode(): Unit = {
    val jsonNode = Json4s.underMapper().readTree("{\"name\":\"Anna\"}")

    val json = Json4s.from("{\"name\":\"Anna\"}")

    assertEquals(jsonNode, json.asJsonNode)
  }

  @Test
  def testJsonBytes(): Unit = {
    val bytes = Json4s
      .underMapper()
      .writeValueAsBytes(Json4s.underMapper().readTree("{\"name\":\"Anna\"}"))

    val json = Json4s.from("{\"name\":\"Anna\"}")

    assertArrayEquals(bytes, json.asBytes)
  }

  @Test
  def testEscape(): Unit = {
    val string = "{\"name\":\"Anna\"}"

    assertEquals(string, Json4s.unescape(Json.escape(string)))
  }

  @Test
  def testObjects(): Unit = {
    val users = List(User("A"), User("B"), User("C"))
    val json = Json4s.from(users)
    val jsonUsers = json.asType(new TypeReference[List[User]]() {})

    assertEquals(users, jsonUsers)
  }

  @Test
  def testType(): Unit = {
    val map: Map[Int, String] = Map(1 -> "A", 2 -> "B", 3 -> "C")

    val json = Json4s.from(map)
    val jsonMap: Map[Int, String] =
      json.asType(new TypeReference[Map[Int, String]] {})
    println(map)
    println(jsonMap)
  }

  @Test
  def testGeneric(): Unit = {
    val users = List(User("A"), User("B"), User("C"))
    val container = Container[User](users)

    val json = Json4s.from(container)
    val jsonContainer: Container[User] =
      json.asType(Json.typeOfGeneric(classOf[Container[_]], classOf[User]))

    assertEquals(container.elements, jsonContainer.elements)
  }

  @Test
  def testCopy(): Unit = {
    val string = "{\"name\":\"Anna\"}"

    val json = Json4s.from(string)

    val jsonNode1 = json.asJsonNode.asInstanceOf[ObjectNode]
    val jsonNode2 = json.asJsonNode.asInstanceOf[ObjectNode]

    jsonNode1.put("name", "Bala")
    jsonNode2.put("name", "Sala")

    assertEquals(string, json.asString)
    assertEquals("{\"name\":\"Bala\"}", Json4s.from(jsonNode1).asString)
    assertEquals("{\"name\":\"Sala\"}", Json4s.from(jsonNode2).asString)
  }

  @Test
  def testMerge(): Unit = {
    val string1 = "{\"name\":\"Anna\"}"
    val string2 = "{\"age\": 22}"

    val json1 = Json4s.from(string1)
    val json2 = Json4s.from(string2)

    assertEquals(1, json1.merge(json1).asMap.keySet.size)

    val map = json1.merge(json2).asMap

    assertEquals(2, json1.merge(json2).asMap.keySet.size)
    assertEquals(map.get("name"), "Anna")
    assertEquals(map.get("age"), 22)
  }

  @Test
  def testEqual(): Unit = {
    val string = "{\"name\":\"Anna\"}"

    assertEquals(Json4s.from(string), Json4s.from(string))

    assertNotEquals(Json4s.from(string), Json4s.from("{\"name\":\"Sala\"}"))
  }
}
