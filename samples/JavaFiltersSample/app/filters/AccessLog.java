package filters;

import play.Logger;
import play.libs.F.*;
import play.mvc.*;
import play.mvc.Http.*;

import com.github.jto.*;

/**
* Simple Filter exmample
* This Filter will Log the request and the associated Results, with the response headers
* output example: 
*		[info] application - [AccessLog]: GET / => SimpleResult(200, Map(Content-Type -> text/html; charset=utf-8)), Headers: {X-FunkyHeader=Funky}
*/
public class AccessLog implements Filter{
	public Result call(Function<Context, Result> next, Context ctx) throws Throwable{
		Result result = next.apply(ctx);
		Logger.info("[AccessLog]: " + ctx.request() + " => " + result + ", Headers: " + ctx.response().getHeaders());
		return result;
	}
}