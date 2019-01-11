/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;
import occamsfuncer.Funcer;

public class EmptyListmap extends AbstractListmap<Object,Object>{
	
	public static final EmptyListmap instance = new EmptyListmap();

	public long size(){
		return 0;
	}

	public Object lowKey(){
		throw new Error("TODO return what error object (like one for NotFound, one for NotEnoughWallet?");
	}

	public Object highKey(){
		throw new Error("TODO return what error object (like one for NotFound, one for NotEnoughWallet?");
	}

	public Object get(Object key){
		throw new Error("TODO return what error object (like one for NotFound, one for NotEnoughWallet?");
	}
	
	public Funcer<Object,Object> put(Long key, Object val){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> rem(Object key){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> cat(Funcer<Object,Object> list){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> preI(Object key){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> preE(Object key){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> sufI(Object key){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> sufE(Object key){
		throw new Error("TODO");
	}

	public boolean equalsForest(Funcer m){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> put(Object key, Object val){
		throw new Error("TODO");
	}
	
	public Funcer l(){
		throw new Error("TODO");
	}

	public Funcer r(){
		throw new Error("TODO");
	}

	public Funcer minKey(){
		throw new Error("TODO");
	}

	public Funcer maxKey(){
		throw new Error("TODO");
	}

}