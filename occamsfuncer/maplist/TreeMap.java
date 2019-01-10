/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.maplist;
import java.util.Comparator;

/** immutable. Normally will be used with comparator as any func
that takes {cons x y} param and returns T or F, which are all funcs.
See Op.maplist*.
*/
public final class TreeMap<K,V>{
	
	TODO merge duplicate code between TreeMapList, TreeMap, and TreeList.
	TODO see ufnode.Ty's 5 types for merging maplist,
	leafmap leaflist mappair listpair maplist, or something like that.
	Still have to make it compatible with Funcall. See Op.maplist*.
	
	public final Comparator<K> sort;
	
	public final TreeMap<K,V> lowChild, highChild;
	
	public final K lowKey, highKey;
	
	public final V value;
	
	public final double size;
	
	public TreeMap(Comparator<K> sort, TreeMap<K,V> lowChild, TreeMap<K,V> highChild, K lowKey, K highKey, V value, double size){
		this.sort = sort;
		this.lowChild = lowChild;
		this.highChild = highChild;
		this.lowKey = lowKey;
		this.highKey = highKey;
		this.value = value;
		this.size = size;
	}
	
	public boolean has(K key){
		return get(key)!=null;
	}
	
	public TreeMap<K,V> get(K key){
		throw new Error("TODO");
	}
	
	public TreeMap<K,V> put(K key, V value){
		throw new Error("TODO");
	}

}