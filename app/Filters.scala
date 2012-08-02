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

package jto.scala.filters

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.Iteratee

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
trait EssentialFilter {
  def apply(next: EssentialAction): EssentialAction
}

trait Filter extends EssentialFilter {

  self =>

  def apply(f:RequestHeader => Result)(rh:RequestHeader):Result

  def apply(next: EssentialAction): EssentialAction  = {

    val p = scala.concurrent.Promise[Result]()

    new EssentialAction {

      def apply(rh:RequestHeader):Iteratee[Array[Byte],Result] = {
        val it = scala.concurrent.Promise[Iteratee[Array[Byte],Result]]()
        val result = self.apply({(rh:RequestHeader) => it.success(next(rh)) ; AsyncResult(p.future)})(rh)
        Iteratee.flatten(it.future).map{r => p.success(r); result}
      }
    }

  }
}

object Filter {

}

/**
* Compose the action and the Filters to create a new Action
*/
object Filters {
	def apply(h: Option[Handler], filters: EssentialFilter*) = h.map{ _ match {
			case a: EssentialAction => FilterChain(a, filters.toList)
			case h => h
		}
	}
}

/**
* Compose the action and the Filters to create a new Action
*/

object FilterChain{
  def apply[A](action:EssentialAction, filters: List[EssentialFilter]): EssentialAction = new EssentialAction {
    def apply(rh:RequestHeader):Iteratee[Array[Byte],Result] = {
      val chain = filters.foldLeft(action){ (a, i) => i(a) }
      chain(rh)
    }
  }
}
