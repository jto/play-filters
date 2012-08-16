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

package jto.java.filters;

import java.util.List;

import play.*;
import play.libs.*;
import play.libs.F.*;
import play.mvc.*;
import play.mvc.Http.*;

/**
* Compose the action and the Filters to create a new Action
*
* {{{
*	public class JavaGlobal extends GlobalSettings {
*		public Action onRequest(Request request, Method actionMethod) {
*			Action parent = super.onRequest(request, actionMethod);
*			return Filters.apply(parent, new CustomFilter(), new AnotherFilter());
*		}
*	}
* }}}
*/
public class Filters {
	public static Action apply(Action a, final Filter... filters) {
		try{
			
			return new Action.Simple() {
				public Result call(Context ctx) throws Throwable {

					Function<Context, Result> n = new Function<Context, Result>(){
						public Result apply(Context c) throws Throwable{
							return delegate.call(c);
						}
					};

					for(final Filter f : filters){
						final Function<Context, Result> next = n;
						Function<Context, Result> chain = new Function<Context, Result>(){
							public Result apply(Context c) throws Throwable{
								return f.call(next, c);
							}
						};
						n = chain;
					}

					return n.apply(ctx);

				}
			
			};
		}	
		catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
}