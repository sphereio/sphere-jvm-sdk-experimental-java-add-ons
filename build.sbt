name := "sphere-jvm-sdk-experimental-java-add-ons"

organization := "io.sphere.sdk.jvm"

autoScalaLibrary in ThisBuild := false // no dependency to Scala standard library

crossPaths in ThisBuild := false

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += Resolver.sonatypeRepo("releases")

val jvmSdkVersion = "1.0.0-M15"

libraryDependencies ++=
  ("io.sphere.sdk.jvm" % "sphere-models" % jvmSdkVersion withSources()) ::
  ("io.sphere.sdk.jvm" % "sphere-java-client-core" % jvmSdkVersion withSources()) ::
  ("io.sphere.sdk.jvm" % "sphere-java-client" % jvmSdkVersion % "test,it" withSources()) ::
  "junit" % "junit-dep" % "4.11" % "test,it" ::
  "org.assertj" % "assertj-core" % "3.0.0" % "test,it" ::
  "com.novocode" % "junit-interface" % "0.11" % "test,it" ::
    Nil

lazy val `sphere-jvm-sdk-experimental-java-add-ons` = (project in file(".")).configs(IntegrationTest)

Defaults.itSettings