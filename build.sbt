name := """play-pet-store-java"""
organization := "com.gtaylor"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  guice,
  javaJdbc,
  caffeine,
  "org.postgresql" % "postgresql" % "42.2.12",
  "io.ebean" % "ebean-querybean" % "13.9.2",
  "io.ebean" % "ebean-agent" % "13.9.2",
  "io.ebean" % "ebean-test" % "13.9.2" % Test
)
