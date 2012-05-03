import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Filters"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      organization := "jto",
      publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath + "/Documents/mvn-repo/snapshots")))
    )

}
