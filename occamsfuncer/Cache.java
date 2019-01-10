package occamsfuncer;

import java.util.HashMap;
import java.util.Map;

/** cache of [func param return] thats cleared about as often as screen repaints
such as 50x/sec. Needed for Sxyz calls to be bigO(1) instead of exponential.
<br><br>
Since this is mostly needed for forests of Sxyz (which get literal lambda
using {K literal} and get param flowing down that forest using identityFunc),
we use == instead of deduping them, and TODO maybe theres a faster way than HashMap.
*/
public class Cache{
	
	//TODO find a way to do this without using java.util.HashMap etc which is slow,
	//such as storing the last param and return in an Sxy object (not S, but the one with 2 curries)?
	//But for now just use a HashMap until its working better.
	
	private static final Map cache = new HashMap();
	
	static class CachedCall{
		Funcall func;
		Funcall param;
		Funcall ret;
		public CachedCall(Funcall func, Funcall param, Funcall ret){
			this.func = func;
			this.param = param;
			this.ret = ret;
		}
	}
	
	public static void clear(){
		cache.clear();
	}
	
	/** gets cached return value if any */
	public static Funcall get(Funcall func, Funcall param){
		throw new Error("TODO");
	}
	
	public static void put(Funcall func, Funcall param, Funcall ret){
		throw new Error("TODO");
	}

}
