# Filters Module

This module provide a way to build global, and reusable request / response interceptors.
It's similar to JEE Filters ( Without XML :) ).

## Installation

1. add a repository resolver into your `Build.scala` or `build.sbt`.

```scala
	resolvers += "JTO snapshots" at "https://raw.github.com/jto/mvn-repo/master/snapshots"
```

1. add a dependency declaration into your `Build.scala` or `build.sbt`.
    1. current version

            "filters" %% "filters" % "1.0"

For example: `Build.scala`

```scala
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
```

## Usage

### Complete samples

Sample apps are available in the samples directory.

### Java

#### Creating a Filter

To create a Filter, you need to implement `import com.github.jto.Filter`
You're filter can either return a Result, or call `next.apply(context)` to continue the chaining.

```java
package filters;

import play.Logger;
import play.libs.F.*;
import play.mvc.*;
import play.mvc.Http.*;

import com.github.jto.*;

public class AccessLog implements Filter{
	public Result call(Function<Context, Result> next, Context ctx) throws Throwable{
		Result result = next.apply(ctx);
		Logger.info("[AccessLog]: " + ctx.request() + " => " + result + ", Headers: " + ctx.response().getHeaders());
		return result;
	}
}
```

#### Using filters

Simply call the `Filters` helper in your `Global.java`

```java
public class Global extends GlobalSettings {
	public Action onRequest(Request request, Method actionMethod) {
		Action parent = super.onRequest(request, actionMethod);
		return Filters.apply(parent, new AccessLog());
	}
}
```


### Scala

#### Creating a Filter

To create a Filter, you need to implement `import com.github.jto.scala.Filter` (make sure you're using the scala api)
Your filter can either return a Result, or call `next(context)` to continue the chaining.

```scala
object AccessLog extends Filter {
	override def apply[A](next: Request[A] => Result)(request: Request[A]): Result = {
		val result = next(request)
		play.Logger.info(request + " => " + result)
		result
	}
}
```

#### Using filters

Simply apply `Filters` on your Handler

```scala
object Global extends GlobalSettings {
	override def onRouteRequest(request: RequestHeader): Option[Handler] = {
		Filters(super.onRouteRequest(request), AccessLog)
	}
}
```
