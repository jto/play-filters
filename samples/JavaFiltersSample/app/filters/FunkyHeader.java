package filters;

import play.Logger;
import play.libs.F.*;
import play.mvc.*;
import play.mvc.Http.*;

import jto.java.filters.*;


/**
*	Add a Funky Header to each Response
*/
public class FunkyHeader implements Filter{
	public Result call(Function<Context, Result> next, Context ctx) throws Throwable {
		Result result = next.apply(ctx);
		ctx.response().setHeader("X-FunkyHeader", "Funky");
		return result;
	}
}