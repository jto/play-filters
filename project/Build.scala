// Copyright 2012 Julien Tournay
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "filters"
    val appVersion      = "2012.09.20.1886ca6.v3"

    object Repos {
      val pattern = Patterns(
        Seq("[organisation]/[module]/[revision]/[module]-[revision](-[classifier]).ivy"),
        Seq("[organisation]/[module]/[revision]/[module]-[revision](-[classifier]).[ext]"),
        true
      )

      val artifactory = "http://artifactory.corp.linkedin.com:8081/artifactory/"
      val mavenLocal = Resolver.file("file",  new File(Path.userHome.absolutePath + "/Documents/mvn-repo/snapshots"))(Resolver.ivyStylePatterns)
      val sandbox = Resolver.url("Artifactory sandbox", url(artifactory + "ext-sandbox"))(pattern)
      val jtoSnaps = Resolver.url("JTO ivy snapshots", url("https://raw.github.com/jto/mvn-repo/master/snapshots"))(Resolver.ivyStylePatterns)
      val local = Resolver.file("file",  new File(Path.userHome.absolutePath + "/Desktop"))(pattern) // debug
    }

    lazy val plugin = PlayProject(appName, appVersion, Nil, mainLang = SCALA, path = file("plugin")).settings(
      playPlugin := true,
      organization := "jto",
      scalaVersion := "2.9.2",
      licenses := Seq("Apache License v2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
      homepage := Some(url("https://github.com/jto/play-filters")),
      publishTo := Some(Repos.sandbox),
      credentials += Credentials(Path.userHome / ".sbt" / ".licredentials"),
      resolvers ++= Seq(Repos.sandbox, Repos.jtoSnaps),
      publishMavenStyle :=  false
    )


    lazy val javaSample = PlayProject("filters-sample-java", appVersion, Nil, mainLang = JAVA, path = file("samples/JavaFiltersSample")).settings(
      organization := "jto",
      resolvers += Repos.sandbox
    ).dependsOn(plugin)

    lazy val scalaSample = PlayProject("filters-sample-scala", appVersion, Nil, mainLang = SCALA, path = file("samples/ScalaFiltersSample")).settings(
      organization := "jto",
      resolvers += Repos.sandbox
    ).dependsOn(plugin)

}
