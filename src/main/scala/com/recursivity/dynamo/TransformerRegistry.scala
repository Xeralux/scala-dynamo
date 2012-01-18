package com.recursivity.dynamo

import collection.mutable.HashMap
import transformers._

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */

object TransformerRegistry {
  private val registry = new HashMap[Class[_], Class[_ <: AttributeValueTransformer]]
  registry += classOf[String] -> classOf[StringTransformer]
  registry += classOf[BigDecimal] -> classOf[NumberValueTransformer]
  registry += classOf[Boolean] -> classOf[StringTransformer]
  registry += classOf[java.util.Date] -> classOf[DateTransformer]
  registry += classOf[Int] -> classOf[NumberValueTransformer]
  registry += classOf[java.math.BigDecimal] -> classOf[NumberValueTransformer]
  registry += classOf[java.lang.Boolean] -> classOf[StringTransformer]
  registry += classOf[java.lang.Integer] -> classOf[NumberValueTransformer]
  registry += classOf[java.lang.Long] -> classOf[NumberValueTransformer]
  registry += classOf[java.lang.Double] -> classOf[NumberValueTransformer]
  registry += classOf[java.lang.Float] -> classOf[NumberValueTransformer]
  registry += classOf[Long] -> classOf[NumberValueTransformer]
  registry += classOf[Short] -> classOf[NumberValueTransformer]
  registry += classOf[java.lang.Short] -> classOf[NumberValueTransformer]


  def apply(clazz: Class[_]): Option[AttributeValueTransformer] = {
    registry.get(clazz).map(cls => cls.newInstance())
  }

  def registerTransformer(clazz: Class[_], transformerClass: Class[_<: AttributeValueTransformer]){
    registry += clazz -> transformerClass
  }
}