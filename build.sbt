ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"
//ThisBuild / dev.zioVersion := "1.0.13"
lazy val root = (project in file("."))
  .settings(
    name := "lab_scala",
      libraryDependencies += "dev.zio" %% "zio" % "1.0.13"
  )
