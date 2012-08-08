# Filters Module [![Build Status](https://secure.travis-ci.org/jto/play-filters.png?branch=li)](http://travis-ci.org/jto/play-filters)

This module provide a way to build global, and reusable request / response interceptors.
It's similar to JEE Filters ( Without XML :) ).

## Installation

1. add a repository resolver into your `Build.scala` or `build.sbt`.

```scala
	resolvers += "JTO snapshots" at "https://raw.github.com/jto/mvn-repo/master/snapshots"
```

2. add a dependency declaration into your `Build.scala` or `build.sbt`.

```scala    
	"jto" %% "filters" % "1.0"
```

Complete example: `Build.scala`

```scala
import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "MyFancyApp"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "jto" %% "filters" % "1.0"
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

To create a Filter, you need to implement `import jto.java.filters.Filter`
You're filter can either return a Result, or call `next.apply(context)` to continue the chaining.

```java
package filters;

import play.Logger;
import play.libs.F.*;
import play.mvc.*;
import play.mvc.Http.*;

import jto.java.filters.*;

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

To create a Filter, you need to implement `import jto.scala.filters.Filter` (make sure you're using the scala api)
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
## Licence

 Copyright 2012 Julien Tournay
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.