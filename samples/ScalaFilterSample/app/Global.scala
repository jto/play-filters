import com.github.jto.scala._

import play.api.mvc._
import play.api._

/**
* Simple Filter exmample
* This Filter will Log the request and the associated Result
* output example: 
*		[info] application - GET / => SimpleResult(200, Map(Content-Type -> text/html; charset=utf-8, X-FunkyHeader -> Funky))
*/
object AccessLog extends Filter {
	override def apply[A](next: Request[A] => Result)(request: Request[A]): Result = {
		val result = next(request)
		play.Logger.info(request + " => " + result)
		result
	}
}


/**
* Simple Filter exmample
* Add a Funky Header to every PlainResult
*/
object FunkyHeader extends Filter {
	override def apply[A](next: Request[A] => Result)(request: Request[A]): Result = {
		next(request) match {
			case p: PlainResult => p.withHeaders("X-FunkyHeader" -> "Funky")
			case r => r
		}
  }
}

object Global extends GlobalSettings {
	override def onRouteRequest(request: RequestHeader): Option[Handler] = {
		Filters(super.onRouteRequest(request), FunkyHeader, AccessLog)
	}
}