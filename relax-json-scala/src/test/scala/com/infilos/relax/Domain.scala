package com.infilos.relax

import com.fasterxml.jackson.core._
import com.fasterxml.jackson.databind._
import com.infilos.relax.json.JsonSerdes

/**
  * @author zhiguang.zhang on 2020-11-05.
  *
  */
case class Domain(name: String, value: String)

object Domain {

  class Serdes extends JsonSerdes[Domain] {
    override def onClass: Class[Domain] = classOf[Domain]

    override def serializer: JsonSerializer[Domain] =
      new JsonSerializer[Domain]() {
        override def serialize(domain: Domain,
                               gen: JsonGenerator,
                               provider: SerializerProvider): Unit =
          gen.writeString(s"${domain.name}#${domain.value}")
      }

    override def deserializer: JsonDeserializer[Domain] =
      new JsonDeserializer[Domain] {
        override def deserialize(p: JsonParser,
                                 ctxt: DeserializationContext): Domain =
          Domain(p.getText.split("#").head, p.getText.split("#").last)
      }
  }
}
