package com.infilos.relax

import com.fasterxml.jackson.databind.{Module, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.infilos.relax.json.{JsonFactory, JsonMappers}

/**
  * @author infilos on 2020-06-13.
  *
  */
trait Json4s extends JsonMappers with JsonFactory {

  override def underMapper(): ObjectMapper = JsonMappers.ScalaMapper

  def registerModule(module: Module): Unit = {
    JsonMappers.JavaMapper.registerModule(module)
    JsonMappers.ScalaMapper.registerModule(module)
  }

  def registerModules(modules: Module*): Unit = {
    JsonMappers.JavaMapper.registerModules(modules: _*)
    JsonMappers.ScalaMapper.registerModules(modules: _*)
  }
}

object Json4s extends Json4s {
  {
    JsonMappers.ScalaMapper.registerModule(DefaultScalaModule)
  }
}
