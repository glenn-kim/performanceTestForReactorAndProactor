logLevel := Level.Warn
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.2")
dependencyOverrides += "org.scala-sbt" % "sbt" % "0.13.8"
