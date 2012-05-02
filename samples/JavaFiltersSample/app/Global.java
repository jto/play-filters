import java.lang.reflect.Method;
import java.util.*;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.*;

import com.github.jto.*;

import filters.*;

/**
* Example of a typical Global object using the Filter Module
* Note that Filters#apply is returning another Action.
* You still have the opportunity to compose this Action before sending the response back.
*/
public class Global extends GlobalSettings {
	public Action onRequest(Request request, Method actionMethod) {
			Action parent = super.onRequest(request, actionMethod);
			return Filters.apply(parent, new FunkyHeader(), new AccessLog());
	}
}
