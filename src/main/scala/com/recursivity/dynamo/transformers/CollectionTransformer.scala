package com.recursivity.dynamo.transformers

import collection.TraversableLike
import com.amazonaws.services.dynamodb.model.AttributeValue
import com.recursivity.dynamo._

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */

object CollectionTransformer {

  def apply(collection: java.util.Collection[_]): AttributeValue = {
    val iterator = collection.iterator()
    val list = new java.util.ArrayList[String]
    var dType: DynamoType = DynamoString
    while (iterator.hasNext) {
      val f = iterator.next()
      val attr = getAttribute(f.getClass, f)
      dType = DynamoType(attr)
      list.add(GetAttributeValue(attr).toString)
    }
    createAttribute(dType, list)
  }

  private def getAttribute(cls: Class[_], any: Any): AttributeValue = TransformerRegistry(cls) match {
    case None => {
      val attr = new AttributeValue()
      attr.setS(any.toString)
      attr
    }
    case Some(transformer) => transformer.transform(any)
  }
  
  private def createAttribute(dType: DynamoType, list: java.util.List[String]): AttributeValue = {
    val attribute = new AttributeValue()
    dType match {
      case DynamoString => attribute.setSS(list)
      case DynamoNumber => attribute.setNS(list)
    }
    attribute
  }

  def apply(collection: TraversableLike[_ <: Any, _ <: Any]): AttributeValue = {
    val list = new java.util.ArrayList[String]
    var dType: DynamoType = DynamoString
    collection.foreach(f => {
      val attr = getAttribute(f.getClass, f)
      dType = DynamoType(attr)
      list.add(GetAttributeValue(attr).toString)
    })
    createAttribute(dType, list)
  }
}

