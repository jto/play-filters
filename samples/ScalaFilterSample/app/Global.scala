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

import play.api.libs.concurrent.execution.defaultContext
case class Stats(rh:RequestHeader,requestTime:Long)

object StatsFilter {
   def apply(st:Stats => Unit) =  Filter { case (next,rh) =>
     import scala.concurrent.stm._
     import play.api.libs.iteratee._
     val timeBefore = System.currentTimeMillis()
     def doneDeliveringBytes[E] = Enumeratee.onIterateeDone[E](() => {st(Stats(rh,System.currentTimeMillis() - timeBefore))})
     val result = next(rh)
     def eventuallyResult(r:Result):Result = {
       r match {
         case r@SimpleResult(h,body) => SimpleResult(h, body &> doneDeliveringBytes)(r.writeable)
         case AsyncResult(p) => AsyncResult(p.map(eventuallyResult))
         case r => println("oops"); r
       }
     }
     eventuallyResult(result)
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

object Global extends WithFilters(StatsFilter(println)) with GlobalSettings

/*
object Global extends /*GlobalSettings */{
	override def doFilter(a:EssentialAction): EssentialAction = {
		Filters(a,StatsFilter(println))
	}
}
*
  */
