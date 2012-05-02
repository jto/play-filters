import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "JavaFilters"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "filters" %% "filters" % "1.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
       resolvers += "JTO snapshots" at "https://raw.github.com/jto/mvn-repo/master/snapshots"
    )

}