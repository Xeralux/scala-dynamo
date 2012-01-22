package com.recursivity.dynamo

import org.specs2.mutable.Specification
import com.amazonaws.services.dynamodb.model.AttributeValue
import java.util.ArrayList


/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 21/01/2012
 * Time: 23:52
 * To change this template use File | Settings | File Templates.
 */

class AttributeSpec extends Specification{
  val num = new AttributeValue()
  num.setN("324")
  val string = new AttributeValue()
  string.setS("hello")
  
  val ns = new AttributeValue()
  val numList = new java.util.ArrayList[String]()
  numList.add("1")
  ns.setNS(numList)

  val ss = new AttributeValue()
  val sList = new java.util.ArrayList[String]()
  sList.add("hello")
  ss.setSS(sList)

  "DynamoType" should{
    "resolve a number to DynamoNumber" in{
      DynamoType(num) must be_==(DynamoNumber)
    }
    "resolve a string to DynamoString" in{
      DynamoType(string) must be_==(DynamoString)
    }
  }

  "GetAttributeValue" should {
    "retrieve a number from a set N" in{
      GetAttributeValue(num) must be_==("324")
    }
    "retrieve a string from a set S" in{
      GetAttributeValue(string) must be_==("hello")
    }
    "retrieve a java collection of strings from a set SS" in{
      GetAttributeValue(ns) must be_==(numList)
    }
    "retrieve a java collection of strings from a set NS" in{
      GetAttributeValue(ss) must be_==(sList)
    }
  }

}