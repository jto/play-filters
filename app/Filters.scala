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
