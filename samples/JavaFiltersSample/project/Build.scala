import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "JavaFilters"
    val appVersion      = "2012.07.30.77b960b"

    object Repos {
      val pattern = Patterns(
        Seq("[organisation]/[module]/[revision]/[module]-[revision](-[classifier]).ivy"),
        Seq("[organisation]/[module]/[revision]/[module]-[revision](-[classifier]).[ext]"),
        true
      )

      val artifactory = "http://artifactory.corp.linkedin.com:8081/artifactory/"
      val mavenLocal = Resolver.file("file",  new File(Path.userHome.absolutePath + "/Documents/mvn-repo/snapshots"))
      val sandbox = Resolver.url("Artifactory sandbox", url(artifactory + "ext-sandbox"))(pattern)
      val local = Resolver.file("file",  new File(Path.userHome.absolutePath + "/Desktop"))(pattern) // debug
    }

    val appDependencies = Seq(
      "jto" %% "filters" % "2012.07.30.77b960b"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      organization := "jto",
      resolvers += Repos.sandbox,
      publishTo := Some(Repos.sandbox),
      credentials += Credentials(Path.userHome / ".sbt" / ".licredentials"),
      publishMavenStyle :=  false
    )

}