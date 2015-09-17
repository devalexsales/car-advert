name := """car-advert"""

version := "1.0-SNAPSHOT"

scalacOptions ++= Seq("-encoding", "UTF-8")

scalaVersion := "2.11.7"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  specs2 % Test
)

libraryDependencies ++= Seq(
  "org.webjars" 		%% "webjars-play" 		% "2.4.0-1",
  "com.github.seratch" %% "awscala" % "0.5.+",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalatestplus" %% "play" % "1.1.0" % "test"
)
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator

fork in run := true