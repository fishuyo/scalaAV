
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
  "org.scala-lang" % "scala-compiler" % "2.9.1",
  "org.scala-lang" % "scala-swing" % "2.9.1",
  "com.nativelibs4java" % "scalacl" % "0.2",
  "xuggle" % "xuggle-xuggler" % "5.4",
  "org.scalala" % "scalala_2.9.0" % "1.0.0.RC2-SNAPSHOT",
  "log4j" % "log4j" % "1.2.16",
  "net.sf.bluecove" % "bluecove" % "2.1.0",
  "net.sf.bluecove" % "bluecove-gpl" % "2.1.0"
)


autoCompilerPlugins := true

addCompilerPlugin("com.nativelibs4java" % "scalacl-compiler-plugin" % "0.2")

addSbtPlugin("com.github.philcali" % "sbt-lwjgl-plugin" % "3.1.1" )

mainClass := Some("com.fishuyo.Main")

//fork in run := true

//javaOptions in run += "-Dbluecove.native.path=/usr/lib/java/"

//javaOptions in run += "-d32"
