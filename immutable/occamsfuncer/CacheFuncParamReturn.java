/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import java.util.HashMap;
import java.util.Map;
import immutable.occamsfuncer.Funcer;

/** Caches by ==, not by id(), cuz id() is slow and it works often enough on s lambdas
even though could be a duplicate s lambda that would have the same id() but not be ==,
the object forest wouldnt normally do that unless you generate them in strange ways,
and AI and Humans will learn to less often do those hard-to-optimize "strange ways"
when their code costs less HaltingDictator.topWallet the optimizable ways.
Calling id() triggers lazyEval of hashing everything below if not already hashed
so can be very slow on large arrays. Most objects never compute their id, but all can.
Being a map key or saving to harddrive or internet or loading from those calls id().
<br><br>
TODO bring in code from the older occamsfuncer
that uses java.lang.Object.hashCode() and ==
on func==func and param==param to get cached return
for EVERY call (needed cuz Op.s would exponentially duplicate)
EXCEPT those optimized (such as Op.kernel,
and acyclicFlow music tools optimizations, javassist, opencl, etc).
These map lookups wont be the bottleneck if you use the system well.
Cache will be cleared once per video frame, so about 40 times per second.
Also consider timing of jsoundcard (about 100 times per second)
and lwjgl opencl (about 70 times per second), but these will vary
in different computers, especially since mine is getting too old.
Linux has the lowest lag for sound (jsoundcard runs in windows and linux).
Havent tried lwjgl opencl in linux yet, works in windows, should work.
<br><br>
OLD COMMENT:
<br><br>
cache of [func param return] thats cleared about as often as screen repaints
such as 50x/sec. Needed for Sxyz calls to be bigO(1) instead of exponential.
<br><br>
Since this is mostly needed for forests of Sxyz (which get literal lambda
using {K literal} and get param flowing down that forest using identityFunc),
we use == instead of deduping them, and TODO maybe theres a faster way than HashMap.
*/
public class CacheFuncParamReturn{
	private CacheFuncParamReturn(){}
	
	/** dont need this since default hashcode (in java.lang.Object) is by System.identityHashcode
	//in case someone looks for a set of Funcers that all hash to the same bucket
	private static final int saltA = Rand.strongRand.nextInt();
	private static final int saltB = Rand.strongRand.nextInt();
	private static final int saltAMinusB = Rand.strongRand.nextInt();
	*/
	
	//TODO find a way to do this without using java.util.HashMap etc which is slow,
		//such as storing the last param and return in an Sxy object (not S, but the one with 2 curries)?
		//But for now just use a HashMap until its working better.
		
		private static final Map<FuncAndParam,Funcer> cache = new HashMap();
		
		/*static class CachedCall{
			Funcer func;
			Funcer param;
			Funcer ret;
			public CachedCall(Funcer func, Funcer param, Funcer ret){
				this.func = func;
				this.param = param;
				this.ret = ret;
			}
		}*/
		
		/** can only call equals on others of same class */
		private static final class FuncAndParam{
			public final Funcer func, param;
			//public final int hash;
			public FuncAndParam(Funcer func, Funcer param){
				this.func = func;
				this.param = param;
				//this would call id() since hashCode is derived from it: int a = func.hashCode(), b = param.hashCode();
				//hash = saltA*a + saltB*b + saltAMinusB*(a-b);
			}
			//public int hashCode(){ return hash; }
			public int hashCode(){
				//this would call id() since hashCode is derived from it:  return func.hashCode()+param.hashCode();
				return System.identityHashCode(func)+System.identityHashCode(param);
			}
			public boolean equals(Object o){
				FuncAndParam f = (FuncAndParam)o;
				return f.func==func && f.param==param;
			}
		}
		
		public static void clear(){
			cache.clear();
		}
		
		/** gets cached return value if any */
		public static Funcer get(Funcer func, Funcer param){
			return cache.get(new FuncAndParam(func,param));
		}
		
		public static void put(Funcer func, Funcer param, Funcer ret){
			cache.put(new FuncAndParam(func,param), ret);
		}

}
