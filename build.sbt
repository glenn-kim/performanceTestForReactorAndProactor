name := "performanceTestForReactorAndProactor"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9"
)

javaOptions in run += "-Xms5G -Xmx8G"
mainClass in Compile := Some("org.nhnnext.Main")

enablePlugins(JavaServerAppPackaging)