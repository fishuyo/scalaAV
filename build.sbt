
name := "what"

version := "1.0"

scalaVersion := "2.9.1"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.9.1"

resolvers += "NativeLibs4Java Repository" at "http://nativelibs4java.sourceforge.net/maven/"

libraryDependencies += "com.nativelibs4java" % "scalacl" % "0.2"

autoCompilerPlugins := true

addCompilerPlugin("com.nativelibs4java" % "scalacl-compiler-plugin" % "0.2")

mainClass := Some("com.fishuyo.Main")
