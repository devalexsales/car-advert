name := """car-advert"""

version := "1.0-SNAPSHOT"

scalacOptions ++= Seq("-encoding", "UTF-8")

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  specs2 % Test
)

lazy val root = project.in(file(".")).enablePlugins(PlayScala)


libraryDependencies ++= Seq(
  "com.github.seratch" %% "awscala" % "0.5.+",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalatestplus" %% "play" % "1.1.0" % "test"
)

fork in run := true