package com.recursivity.dynamo

import org.specs2.mutable.Specification
import com.amazonaws.services.dynamodb.model.AttributeValue
import transformers.CollectionTransformer

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 21/01/2012
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */

class CollectionTransformerSpec extends Specification{

  val ns = new AttributeValue()
  val numList = new java.util.ArrayList[String]()
  numList.add("2")
  ns.setNS(numList)

  val nums  = new java.util.ArrayList[Int]()
  nums.add(2)

  val ss = new AttributeValue()
  val sList = new java.util.ArrayList[String]()
  sList.add("hello")
  ss.setSS(sList)


  "a CollectionTransformerSpec" should{
    "transform a java collection of numbers into a NS" in{
      CollectionTransformer(nums) must be_==(ns);
    }
    "transform a java collection of strings into a SS" in{
      CollectionTransformer(sList) must be_==(ss);
    }
    "transform a collection of numbers into a N" in{
      CollectionTransformer(List(2)) must be_==(ns);
    }
    "transform a collection of strings into a S" in{
      CollectionTransformer(List("hello")) must be_==(ss);
    }
  }

}