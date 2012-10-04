import sbt._
import Keys._

object ScalaDynamo extends Build {
  val description = SettingKey[String]("description")

  resolvers ++= repos

  val parentSettings = Defaults.defaultSettings ++ Seq(
    parallelExecution := false,
    organization := "com.recursivity.dynamo",
    version := "1.0.0-SNAPSHOT",
    crossScalaVersions := Seq("2.9.2"),
    scalaVersion <<= (crossScalaVersions) {
      versions => versions.head
    },
    packageOptions <<= (packageOptions, name, version, organization) map {
      (opts, title, version, vendor) =>
        opts :+ Package.ManifestAttributes(
          "Created-By" -> "Simple Build Tool",
          "Built-By" -> System.getProperty("user.name"),
          "Build-Jdk" -> System.getProperty("java.version"),
          "Specification-Title" -> title,
          "Specification-Version" -> version,
          "Specification-Vendor" -> vendor,
          "Implementation-Title" -> title,
          "Implementation-Version" -> version,
          "Implementation-Vendor-Id" -> vendor,
          "Implementation-Vendor" -> vendor
        )
    }
  )



  val repos = Seq("Sonatype Nexus releases" at "https://oss.sonatype.org/content/repositories/releases",
    "Sonatype Nexus snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
	"maven central" at "http://repo1.maven.org/maven2/")

  val sonatypeSnapshots = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  object Dependencies {
    //val base64 = "net.iharder" % "base64" % "2.3.8"
    val specs2 = "org.specs2" %% "specs2" % "1.12.1" % "test"

    val httpClient = "org.apache.httpcomponents" % "httpclient" % "4.1.2"

    val commonsLogging = "commons-logging" % "commons-logging" % "1.1.1"
	
    val jodaTime = "joda-time" % "joda-time" % "2.1"	
    val jodaConvert = "org.joda" % "joda-convert" % "1.2"	

    val jacksonMapper = "org.codehaus.jackson" % "jackson-mapper-asl" % "1.5.0"
	
    val staxRi = "stax" % "stax" % "1.2.0"

    val recursivityCommons = "com.recursivity" %% "recursivity-commons" % "0.6"
  }

  import Dependencies._

  lazy val scalaDynamo = Project("scala-dynamo", file("."),
    settings = parentSettings)
    .settings(libraryDependencies := Seq(specs2, recursivityCommons, httpClient, commonsLogging, jacksonMapper, staxRi, jodaTime, jodaConvert),
    //publishArtifact in Compile := false,
    description := "scala-dynamo",
	resolvers ++= repos)
}
