package com.recursivity.dynamo

import org.specs2.mutable.Specification
import java.util.{ArrayList, Date}

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 22:03
 * To change this template use File | Settings | File Templates.
 */

class DynamoTransformerSpec extends Specification{
  val bean = FlatBean("hello", true, new java.math.BigDecimal("23.05"), 1f, 1.5d, 5, 12l, new Date)
  val list = new java.util.ArrayList[Date]
  list.add(new Date)
  list.add(new Date)
  val bean2 = CollectionBean(Some("s"), List("hello", "world"), Set(1,2,3), Some(new Date), list, List(new Date, new Date))
  val dynamo = DynamoTransformer.toDynamo(bean2)
  
  "the transformer" should{
    "transform the flat bean to a map of attribute values and back again and still be identical" in{
      bean must_== DynamoTransformer.fromDynamo[FlatBean](DynamoTransformer.toDynamo(bean))
    }

    "transform the complex bean to a map of attribute values and back again and still be identical" in{
      bean2 must_== DynamoTransformer.fromDynamo[CollectionBean](DynamoTransformer.toDynamo(bean2))
    }
  }
  

}

case class FlatBean(name: String, bool: Boolean, decimal: BigDecimal, fl: Float, dbl: Double, int: Int, long: Long, date: Date)

case class CollectionBean(name: Option[String], strings: List[String], numbers: Set[Int], date: Option[Date], list: java.util.List[Date], dates: List[Date])