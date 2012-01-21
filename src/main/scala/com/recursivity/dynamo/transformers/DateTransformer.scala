package com.recursivity.dynamo.transformers

import com.recursivity.dynamo.AttributeValueTransformer
import java.text.SimpleDateFormat
import com.amazonaws.services.dynamodb.model.AttributeValue

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */

class DateTransformer extends AttributeValueTransformer{
  def transform(any: Any) = {
    val attr = new AttributeValue()
    attr.setS(new SimpleDateFormat("yyyyyMMddHHmmssSSSZ").format(any.asInstanceOf[java.util.Date]))
    attr
  }
}