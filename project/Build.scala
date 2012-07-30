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

    val appName         = "Filters"
    val appVersion      = "1.1-SNAPSHOT"

    object Repos {
      val artifactory = "http://artifactory.corp.linkedin.com:8081/artifactory/"
      val mavenLocal = Resolver.file("file",  new File(Path.userHome.absolutePath + "/Documents/mvn-repo/snapshots"))
      val sandbox = Resolver.url("Artifactory sandbox", url(artifactory + "ext-sandbox"))(Patterns("[organisation]/[module]/[revision]/[artifact]-[revision].[ext]"))

    }

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      organization := "jto",
      resolvers += Repos.sandbox,
      publishTo := Some(Repos.sandbox),
      credentials += Credentials(Path.userHome / ".sbt" / ".licredentials")
    )

}
