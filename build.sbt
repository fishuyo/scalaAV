
name := "Seer"

version := "1.0"

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "NativeLibs4Java Repository" at "http://nativelibs4java.sourceforge.net/maven/",
  "xuggle repo" at "http://xuggle.googlecode.com/svn/trunk/repo/share/java/",
  "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/",
  "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo"
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-swing" % "2.9.1",
  "com.nativelibs4java" % "scalacl" % "0.2",
  "xuggle" % "xuggle-xuggler" % "5.4"
  //"org.scalala" %% "scalala" % "1.0.0.RC3-SNAPSHOT"
)


autoCompilerPlugins := true

addCompilerPlugin("com.nativelibs4java" % "scalacl-compiler-plugin" % "0.2")

addSbtPlugin("com.github.philcali" % "sbt-lwjgl-plugin" % "3.1.1" )

mainClass := Some("com.fishuyo.Main")
