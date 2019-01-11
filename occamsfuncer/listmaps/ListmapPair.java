/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;
import java.util.Comparator;
import occamsfuncer.Funcer;

/** immutable. Normally will be used with comparator as any func
that takes {cons x y} param and returns T or F, which are all funcs.
See Op.maplist*.
*/
public final class ListmapPair<K,V> extends AbstractListmap<K,V>{
	
	//FIXME detect when size would exceed max integer size that fits in double and not allow that.
	
	/*TODO merge duplicate code between TreeMapList, TreeMap, and TreeList.
	TODO see ufnode.Ty's 5 types for merging maplist,
	leafmap leaflist mappair listpair maplist, or something like that.
	Still have to make it compatible with Funcall. See Op.maplist*.
	*/
	
	public final Comparator<K> sort;
	
	public final Funcer<K,V> lowChild, highChild;
	
	public final K lowKey, highKey;
	
	public final long size;
	
	public ListmapPair(Comparator<K> sort, Funcer<K,V> lowChild, Funcer<K,V> highChild){
		this.sort = sort;
		this.lowChild = lowChild;
		this.highChild = highChild;
		this.lowKey = lowChild.lowKey();
		this.highKey = highChild.highKey();
		this.size = lowChild.size()+highChild.size();
	}
	
	public boolean has(K key){
		return get(key)!=null;
	}
	
	public V get(K key){
		throw new Error("TODO");
	}
	
	public Funcer<K,V> put(K key, V value){
		throw new Error("TODO");
	}

	public long size(){
		return size;
	}

	public K lowKey(){
		return lowKey;
	}

	public K highKey(){
		return highKey;
	}

	public Funcer<K,V> rem(K key){
		throw new Error("TODO");
	}

	public Funcer<K,V> cat(Funcer<K,V> list){
		throw new Error("TODO");
	}

	public Funcer<K,V> preI(K key){
		throw new Error("TODO");
	}

	public Funcer<K,V> preE(K key){
		throw new Error("TODO");
	}

	public Funcer<K,V> sufI(K key){
		throw new Error("TODO");
	}

	public Funcer<K,V> sufE(K key){
		throw new Error("TODO");
	}

	public boolean equalsForest(Funcer m){
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