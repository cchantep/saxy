name := "saxy"

organization := "cchantep"

version := "1.0.1"

scalaVersion := "2.11.3"

javacOptions in Test ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

autoScalaLibrary := false

scalacOptions += "-feature"

resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies += "net.sf.saxon" % "saxon" % "8.7"

crossPaths := false

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
