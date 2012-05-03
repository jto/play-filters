package jto.scala.filters;

import play.api._
import play.api.mvc._

/**
* Implement this interface if you want to add a Filter to your application
* {{{
* object AccessLog extends Filter {
*		 override def apply[A](next: Request[A] => Result)(request: Request[A]): Result = {
*			 val result = next(request)
*			 play.Logger.info(request + "\n\t => " + result)
*			 result
*		 }
*	 }
** }}}
*/
trait Filter {
	def apply[A](next: Request[A] => Result)(request: Request[A]): Result
}

/**
* Compose the action and the Filters to create a new Action
*/
object Filters {
	def apply(h: Option[Handler], filters: Filter*) = h.map{ _ match {
			case a: Action[_] => FilterChain(a, filters.toList)
			case h => h
		}
	}
}

/**
* Compose the action and the Filters to create a new Action
*/
case class FilterChain[A](action: Action[A], filters: List[Filter]) extends Action[A] {
	val chain = filters.foldLeft(action: Request[A] => Result){ (a, i) => i.apply(a) _ }
	override def apply(request: Request[A]): Result = chain(request)
	override def parser = action.parser
}
