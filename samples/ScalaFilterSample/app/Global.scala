import jto.scala.filters._

import play.api.mvc._
import play.api._

/**
* Simple Filter exmample
* This Filter will Log the request and the associated Result
* output example: 
*		[info] application - GET / => SimpleResult(200, Map(Content-Type -> text/html; charset=utf-8, X-FunkyHeader -> Funky))
*/
object AccessLog extends Filter {
	def apply(next: RequestHeader => Result)(request: RequestHeader): Result = {
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
	def apply(next: RequestHeader => Result)(request: RequestHeader): Result = {
		next(request) match {
			case p: Result => p.withHeaders("X-FunkyHeader" -> "Funky")
			case r => r
		}
  }
}

object Global extends GlobalSettings {
	override def onRouteRequest(request: RequestHeader): Option[Handler] = {
		Filters(super.onRouteRequest(request), FunkyHeader, AccessLog)
	}
}
