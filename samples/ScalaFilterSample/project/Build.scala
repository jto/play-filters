import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "ScalaFilter"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "jto" %% "filters" % "1.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += "JTO snapshots" at "https://raw.github.com/jto/mvn-repo/master/snapshots"
    )

}
