name := "ergo-luport-executor"

version := "0.1.0"
organization := "ergo.susy"
scalaVersion := "2.12.10"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "Typesafe maven releases" at "https://dl.bintray.com/typesafe/maven-releases/"
resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "org.ergoplatform" %% "ergo-appkit" % "develop-dd40e4e5-SNAPSHOT",
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "com.joefkelley" %% "argyle" % "1.0.0",
  "com.github.scopt" %% "scopt" % "4.0.1",
  "com.typesafe" % "config" % "1.4.1",
  "io.jvm.uuid" %% "scala-uuid" % "0.3.1"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:experimental.macros",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-unchecked",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ypartial-unification"
)

test in assembly := {}

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

assemblyMergeStrategy in assembly := {
  case "logback.xml" => MergeStrategy.first
  case "module-info.class" => MergeStrategy.discard
  case other => (assemblyMergeStrategy in assembly).value(other)
}
