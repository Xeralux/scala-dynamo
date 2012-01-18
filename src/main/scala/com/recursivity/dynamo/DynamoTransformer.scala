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
        map.put(field.getName, CollectionTransformer(field.get(caseClass).asInstanceOf[TraversableLike[_ <: Any, _ <: Any]]))
      } else if (classOf[java.util.Collection[_ <: Any]].isAssignableFrom(cls)) {
        map.put(field.getName, CollectionTransformer(field.get(caseClass).asInstanceOf[java.util.Collection[_ <: Any]]))
      } else if (classOf[Option[_ <: Any]].isAssignableFrom(cls)) {
        field.get(caseClass).asInstanceOf[Option[_]] match {
          case Some(s) => {
            TransformerRegistry(s.getClass) match{
              case None => {
                val attr = new AttributeValue()
                attr.setS(s.toString)
                attr
                map.put(field.getName, attr)
              }
              case Some(transformer) => map.put(field.getName, transformer.transform(s))
            }           
          }
          case None => {}
        }
      } else {
        map.put(field.getName, getValue(field, caseClass))
      }
    })
    map
  }

  private def getValue(field: Field, caseClass: Any): AttributeValue = {
    TransformerRegistry(field.getType) match {
      case None => {
        val attr = new AttributeValue()
        attr.setS(field.get(caseClass).toString)
        attr
      }
      case Some(transformer) => {
        transformer.transform(field.get(caseClass))
      }
    }
  }

}