package immutable.occamsfuncer;
import static mutable.util.Lg.*;
import static immutable.occamsfuncer.ImportStatic.*;
import static immutable.occamsfuncer.HaltingDictator.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.UnaryOperator;

/** plugins must obey the same design constraints as OccamsFuncer,
especially being immutable/stateless and limiting their compute resources by HaltingDictator.
<br><br>
Its easier to make core Opcodes deterministic.
Plugins are meant to have the same behavior in all implementations,
and once a behavior is defined it doesnt change,
except that a behavior may change from costing infinnite resources (in HaltingDictator)
to existing and running in sometimes finite resources,
which is consistent with the design since its still deterministic just faster.
I dont expect this will actually happen perfectly so occamsfuncer user level code
which calls the plugins will have to change every time a plugin is redesigned,
so eventually I want there to be no plugins and instead derive them
from occamsfuncer user level code and auto optimize by opencl and javassist.
<br><br>
Any call of a plugin on the same param may be cached and reused
since its meant to be immutable/stateless.
<br><br>
Plugins cant do stateful things such as web calls except if the content is constant such as by hash,
and they cant write to graphics or sound card etc, but external code which calls occamsfuncer can,
and occamsfuncer can read external code's message inside the occamsfuncer (by forkEdit)
then returna a funcer containing a response (like request/response).
<br><br>
TODO explain that better and give an example.
<br><br>
Example occamsfuncer code:
f(?? ;plugin ;immutable.occamsfuncer.Plugins.ocfnplugExamplePlusOne 100) evals to 101.
*/
public class Plugins{
	
	private static final Map<String,UnaryOperator<Funcer>> plugins =
		Collections.synchronizedMap(new HashMap());
	
	/** any static java func whose name starts with this,
	and takes 1 param a Funcer and returns a Funcer, is automatically in the whitelist,
	and nothing else in the whitelist (TODO verify security).
	*/
	public static final String whitelistPrefix = "ocfnplug";
	
	/** throws HaltingDictator.throwMe if plugin not found */
	public static UnaryOperator<Funcer> plugin(String javaFuncName){
		UnaryOperator<Funcer> func = plugins.get(javaFuncName);
		if(func != null) return func;
		
		$(10000);
		javaFuncName = javaFuncName.trim();
		int i = javaFuncName.lastIndexOf('.');
		if(i == -1) throw HaltingDictator.throwMe; //caught at innermost Opcode.spend
		String className = javaFuncName.substring(0,i);
		try{
			Class c = Class.forName(className);
			String funcName = javaFuncName.substring(i+1);
			if(!funcName.startsWith(whitelistPrefix)) throw HaltingDictator.throwMe; //caught at innermost Opcode.spend
			final Method m = c.getMethod(funcName, Funcer.class);
			int modifiers = m.getModifiers();
			if(!Modifier.isStatic(m.getModifiers())) throw HaltingDictator.throwMe; //caught at innermost Opcode.spend
			Class retType = m.getReturnType();
			if(retType != Funcer.class) throw HaltingDictator.throwMe; //caught at innermost Opcode.spend
			UnaryOperator<Funcer> plugin = (Funcer p)->{
				try{
					return (Funcer) m.invoke(null, p);
				}catch(IllegalAccessException | IllegalArgumentException x){
					//this should never happen since its verified before making this lambda
					throw new Error(x);
				}catch(InvocationTargetException x){
					//caught either HaltingDictator.throwMe or something wrapping it or plugin is badly designed
					throw HaltingDictator.throwMe; //caught at innermost Opcode.spend
				}
			};
			plugins.put(javaFuncName, plugin);
			lg("Loaded plugin: "+javaFuncName);
			return plugin;
		}catch(ClassNotFoundException | NoSuchMethodException | SecurityException e){
			//TODO if its a SecurityException, log it the first time.
			//Else its easy to figure out from params and which java funcs exist and their names. 
			throw HaltingDictator.throwMe; //caught at innermost Opcode.spend
		}
	}
	
	/** an example occamsfuncer plugin that returns its param plus one */
	public static Funcer ocfnplugExamplePlusOne(Funcer p){
		$();
		return wr(p.d()+1);
	}
	
	/** a counterexample thats not an occamsfuncer plugin cuz of its name, for security */
	public static Funcer notanocfnplugExamplePlusOne(Funcer p){
		lgErr("This should not be callable from occamsfuncer code, but can be called from java code.");
		return ocfnplugExamplePlusOne(p);
	}
	
	/** 
	*
	public static void put(String pluginName, UnaryOperator<Funcer> func){
		plugins.put(pluginName, func);
	}
	
	public static Funcer call(String pluginName, Funcer param){
		TODO how to auto add plugins? Static java code doesnt always run
		but can be forced by Class.forName etc.
		Should plugins be javaClass that implements UnaryOperator<Funcer>?
		Do I want custom classloading?
		Make an example plugin called ;example .
		
		UnaryOperator<Funcer> func = plugins.get(pluginName);
		if(func == null) HaltingDictator.evalInfiniteLoop(); //caught at innermost Opcode.spend
		return func.apply(param);
	}*/

}
