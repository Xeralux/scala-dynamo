package com.recursivity.dynamo

import com.amazonaws.services.dynamodb.model.AttributeValue
import collection.mutable.{MutableList, HashMap}

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 21:09
 * To change this template use File | Settings | File Templates.
 */

object GetAttributeValue {

  def apply(attr: AttributeValue): Any = {
    if(attr.getN != null) attr.getN
    else if(attr.getS != null) attr.getS
    else if(attr.getSS != null) attr.getSS
    else attr.getNS
  }

}

object DynamoMap{
  def apply(map: java.util.Map[String,  AttributeValue]): Map[String, Any] = {
    val iterator = map.keySet().iterator()
    val result = new HashMap[String, Any]
    while(iterator.hasNext){
      val key = iterator.next()
      val value = {
        val temp = GetAttributeValue(map.get(key))
        if(temp.isInstanceOf[java.util.List[_]]){
          val iter = temp.asInstanceOf[java.util.List[String]].iterator
          val list = new MutableList[String]
          while(iter.hasNext){
            list += iter.next()
          }
          list.toList
        }else temp
      }
      result += key -> value
    }
    result.toMap
  }
}

object DynamoType{
  def apply(attr: AttributeValue): DynamoType = {
    if(attr.getN != null) DynamoNumber
    else DynamoString
  }
}

sealed trait DynamoType
case object DynamoNumber extends DynamoType
case object DynamoString extends DynamoType