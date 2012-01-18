import sbt._
import Keys._

object ScalaDynamo extends Build {
  val description = SettingKey[String]("description")

  resolvers ++= repos

  val parentSettings = Defaults.defaultSettings ++ Seq(
    parallelExecution := false,
    organization := "com.recursivity.dynamo",
    version := "1.0.0-SNAPSHOT",
    crossScalaVersions := Seq("2.9.1"),
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
    "Sonatype Nexus snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")

  val sonatypeSnapshots = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  object Dependencies {
    //val base64 = "net.iharder" % "base64" % "2.3.8"
    val specs2 = "org.specs2" %% "specs2" % "1.6.1" % "test"

    val recursivityCommons = "com.recursivity" %% "recursivity-commons" % "0.5.7"
  }

  import Dependencies._

  lazy val scalaDynamo = Project("scala-dynamo", file("."),
    settings = parentSettings)
    .settings(libraryDependencies := Seq(specs2, recursivityCommons),
    publishArtifact in Compile := false,
    description := "scala-dynamo",
	resolvers ++= repos)
}