package com.recursivity.dynamo

import org.specs2.mutable.Specification
import java.lang.Short
import java.math.BigDecimal
import java.util.Date
import java.text.SimpleDateFormat

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 21/01/2012
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */

class TransformerSpec extends Specification{
  // test all supported transformations

  val date = new Date()
  val dateString = new SimpleDateFormat("yyyyyMMddHHmmssSSSZ").format(date)

  "The transformer registry" should{
    "transform a string into an AttributeValue" in{
      TransformerRegistry.apply(classOf[String]).get.transform("Hello").getS must be_==("Hello")
    }
    "transform a boolean into an AttributeValue" in{
      TransformerRegistry.apply(classOf[Boolean]).get.transform(true).getS must be_==("true")
    }
    "transform a long into an AttributeValue" in{
      TransformerRegistry.apply(classOf[Long]).get.transform(new java.lang.Long("2")).getN must be_==("2")
    }
    "transform a int into an AttributeValue" in{
      TransformerRegistry.apply(classOf[Int]).get.transform(new java.lang.Integer("3")).getN must be_==("3")
    }
    "transform a float into an AttributeValue" in{
      TransformerRegistry.apply(classOf[Float]).get.transform(new java.lang.Float("4")).getN must be_==("4.0")
    }
    "transform a double into an AttributeValue" in{
      TransformerRegistry.apply(classOf[Double]).get.transform(new java.lang.Double("5")).getN must be_==("5.0")
    }
    "transform a short into an AttributeValue" in{
      TransformerRegistry.apply(classOf[Short]).get.transform(new Short("23")).getN must be_==("23")
    }
    "transform a bigdecimal into an AttributeValue" in{
      TransformerRegistry.apply(classOf[BigDecimal]).get.transform(new BigDecimal(("23.4"))).getN must be_==("23.4")
    }
    "transform a date into an AttributeValue" in{
      TransformerRegistry.apply(classOf[Date]).get.transform(date).getS must be_==(dateString)
    }
  }

}