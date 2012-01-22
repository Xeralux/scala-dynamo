package com.recursivity.dynamo

import com.amazonaws.auth.AWSCredentials
import java.io.FileInputStream
import java.util.Properties

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 18/01/2012
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */

object Credentials {
  val file = System.getProperty("user.home") + System.getProperty("file.separator") + ".aws" +
    System.getProperty("file.separator") + "key.properties"
    val props = new Properties
    val in = new FileInputStream(file)
    props.load(in)
    in.close

    val accessKey = props.getProperty("access")
    val secretKey = props.getProperty("secret")

    def credentials = new Credentials(accessKey, secretKey)

  def apply() = credentials

}

case class Credentials(access: String, secret: String) extends AWSCredentials{
  def getAWSAccessKeyId = access

  def getAWSSecretKey = secret
}