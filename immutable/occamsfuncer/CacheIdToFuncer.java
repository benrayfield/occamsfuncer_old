/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import java.util.HashMap;
import java.util.Map;
import immutable.occamsfuncer.Funcer;

/** map of Id to Funcer where id exists.
Id is lazyEval and is never created for most funcers.
*/
public class CacheIdToFuncer{
	
	private static final Map<Id,Funcer> map = new HashMap();
	
	/** else null */
	public static synchronized Funcer get(Id id){
		return map.get(id);
	}
	
	/** Throws if f.id(double walletLimit) has not yet created its id *
	public static synchronized void put(Funcer f){
		map.put(f.idElseThrow(), f);
	}*/
	
	public static synchronized void put(Funcer f, double walletLimit){
		map.put(f.id(), f);
	}
	
	public static synchronized void rem(Id id){
		map.remove(id);
	}
	
	public static synchronized void clear(){
		map.clear();
	}

}
