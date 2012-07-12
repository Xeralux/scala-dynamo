package com.recursivity.dynamo

import com.recursivity.commons.bean.{BeanUtils, GenericTypeDefinition}
import com.amazonaws.services.dynamodb.model.AttributeValue
import collection.TraversableLike
import java.lang.reflect.Field
import transformers.CollectionTransformer

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 21:20
 * To change this template use File | Settings | File Templates.
 */

object DynamoTransformer {

  def fromDynamo[T](obj: java.util.Map[String, AttributeValue])(implicit m: Manifest[T]): T = {
    val typeString = m.toString.replace("[", "<").replace("]", ">")
    val typeDef = GenericTypeDefinition(typeString)
    val map = DynamoMap(obj)
    BeanUtils.instantiate(typeDef.definedClass, map)
  }

  def fromDynamo[T](obj: Map[String, AttributeValue])(implicit m: Manifest[T]): T = {
    val typeString = m.toString.replace("[", "<").replace("]", ">")
    val typeDef = GenericTypeDefinition(typeString)
    BeanUtils.instantiate(typeDef.definedClass, obj)
  }

  def toDynamo(caseClass: AnyRef): java.util.Map[String, AttributeValue] = {
    val map = new java.util.HashMap[String, AttributeValue]()
    caseClass.getClass.getDeclaredFields.foreach(field => {
      field.setAccessible(true)
      val cls = field.getType
      if (classOf[TraversableLike[_ <: Any, _ <: Any]].isAssignableFrom(cls)) {
        val coll = field.get(caseClass).asInstanceOf[TraversableLike[_ <: Any, _ <: Any]]
        // Dynamo doesn't allow putting empty collections into attributes.  So if it is empty we just
	// leave it out.
	if(!coll.isEmpty)
	  map.put(field.getName, CollectionTransformer(field.get(caseClass).asInstanceOf[TraversableLike[_ <: Any, _ <: Any]]))
      } else if (classOf[java.util.Collection[_ <: Any]].isAssignableFrom(cls)) {
        val coll = field.get(caseClass).asInstanceOf[java.util.Collection[_ <: Any]]
        if(!coll.isEmpty)
          map.put(field.getName, CollectionTransformer(coll))
      } else if (classOf[Option[_ <: Any]].isAssignableFrom(cls)) {
        field.get(caseClass).asInstanceOf[Option[_]].foreach(s => {
          map.put(field.getName, getValue(s.getClass, s))
        })
      } else {
        map.put(field.getName, getValue(field.getType, field.get(caseClass)))
      }
    })
    map
  }

  def getValue(valueType: Class[_], fieldValue: Any): AttributeValue = {
    TransformerRegistry(valueType) match {
      case None => {
        val attr = new AttributeValue()
        attr.setS(fieldValue.toString)
        attr
      }
      case Some(transformer) => transformer.transform(fieldValue)
    }
  }
}