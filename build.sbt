name := "MagnoliaUtils"

version := "0.1"

scalaVersion := "2.13.5"

lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.14.0"

lazy val cats = "org.typelevel" %% "cats-core" % "2.3.0"
lazy val fs2 = "co.fs2" %% "fs2-core" % "2.5.10"
lazy val catsEffect = "org.typelevel" %% "cats-effect" % "3.3.0"
lazy val magnolia = "com.softwaremill.magnolia1_2" %% "magnolia" % "1.1.0"

libraryDependencies += scalacheck % Test
libraryDependencies += fs2
libraryDependencies += magnolia
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided