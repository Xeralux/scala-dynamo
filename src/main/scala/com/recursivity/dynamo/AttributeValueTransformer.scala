package com.recursivity.dynamo

import com.amazonaws.services.dynamodb.model.AttributeValue

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 21:31
 * To change this template use File | Settings | File Templates.
 */

trait AttributeValueTransformer {
  
  def transform(any: Any): AttributeValue

}