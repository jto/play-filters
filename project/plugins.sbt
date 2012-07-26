// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url("LinkedIn Sandbox (plugins.sbt)", url("http://artifactory.corp.linkedin.com:8081/artifactory/ext-sandbox"))(Patterns("[organisation]/[module]/[revision]/[module]-[revision].[artifact]","[organisation]/[module]/[revision]/[artifact]-[revision].[ext]","[organisation]/[module]/[revision]/[module]-[revision].[ext]"))

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2012.07.20.acdb719")