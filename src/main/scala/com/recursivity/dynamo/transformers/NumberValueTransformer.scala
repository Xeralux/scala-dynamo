package com.recursivity.dynamo.transformers

import com.recursivity.dynamo.AttributeValueTransformer
import com.amazonaws.services.dynamodb.model.AttributeValue

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */

class NumberValueTransformer extends AttributeValueTransformer{
  def transform(any: Any) = {
    val str = any.toString
    val value = new AttributeValue
    value.setN(if(str == "NaN") "-1" else str)
    value
  }
}