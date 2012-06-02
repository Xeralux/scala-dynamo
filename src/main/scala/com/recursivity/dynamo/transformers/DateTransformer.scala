package com.recursivity.dynamo.transformers

import com.recursivity.dynamo.AttributeValueTransformer
import java.text.SimpleDateFormat
import com.amazonaws.services.dynamodb.model.AttributeValue
import org.joda.time._
import org.joda.time.format._

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 21:45
 * reads/writes in ISO date format
 */

class DateTransformer extends AttributeValueTransformer{
  val formatter = ISODateTimeFormat.dateTime

  def transform(any: Any) = {
    val attr = new AttributeValue()
    attr.setS(formatter.print(any.asInstanceOf[java.util.Date].getTime))
    attr
  }
}