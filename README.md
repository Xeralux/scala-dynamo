# Scala Dynamo

A simple Scala library intended to make working with Amazon Web Services Dynamo NoSQL database a lot simpler and less verbose. Currently supports transforming Scala case classes to- and from Dynamo's Map of key/AttributeValues. It supports basic data types like Strings, booleans, numerical values and java.util.Date, plus Scala collections, Java Collections and Scala Options. Adding additional types can be easily done, as described below.

This is still a _very_ early version, some code is ugly, duplicated, it needs more tests etc, but it should at least work for the basic use case of case class mapping, given it is built on top of the relatively solid foundations of [the Bowler web framework's](http://bowlerframework.org/) request mapping from HTTP into case class objects.
We will continue adding functionality and tests as we go along.

Built with sbt 0.11.x, using Scala 2.9.1, available under a BSD license.

## Basic Usage

Transforming to a Dynamo structured Map[String, AttributeValue] that you can shoot at Dynamo with it's [Java API](http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/GettingStartedJavaQuery.html):

	// required import
	import com.recursivity.dynamo.DynamoTransformer._
	
	// example case class structure
	case class DomainObject(name: String, date: Option[Date], list: List[Int])
	
	// code to transform
	
	val myObject = DomainObject("name", Some(new Date), List(1,2,3))
	
	// use the toDynamo function we imported, and voila, we're done!
	val dynamoMap = toDynamo(myObject)
	
Transforming it back from a Dynamo map to a case class (assuming same import and case class as above):

	// as easy as this!
	val myCaseClassObject = fromDynamo[DomainObject](dynamoMap)
	
## Adding support for additional types to store in Dynamo

Storage: implement the com.recursivity.dynamo.AttributeValueTransformer trait, and register the new implementation by calling:

	com.recursivity.dynamo.TransformerRegistry.register(classOf[ClassBeingTransformed], classOf[MyNewShinyAttributeValueTransformer])
	
Here is an example of the DateTransformer implementation for inbound (storage) transformation:

	class DateTransformer extends AttributeValueTransformer{
	  def transform(any: Any) = {
	    val df = new SimpleDateFormat("yyyyyMMddHHmmssSSSZ")
	    val string = df.format(any.asInstanceOf[java.util.Date])
	    val attr = new AttributeValue()
	    attr.setS(string)
	    attr
	  }
	}
	
To transform objects back (loading from Dynamo), you need a transformer in the other direction that implements com.recursivity.commons.bean.StringValueTransformer, register it by calling: 
	
	com.recursivity.commons.bean.TransformerRegistry.register(classOf[ClassBeingTransformed], classOf[MyNewShinyStringValueTransformer])
	
Here is an example of a StringValueTransformer for dates:

	class DateTransformer extends StringValueTransformer[java.util.Date]{
	  def toValue(from: String): Option[java.util.Date] = {
	    try{
	      val df = new SimpleDateFormat("yyyyyMMddHHmmssSSSZ")
	      return Some(df.parse(from))
	    }catch{
	      case e: Exception => return None
	    }
	  }

	}
	
	