/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;

import occamsfuncer.Funcer;

/** a map of 1 key and 1 value */
public class MapLeaf<K,V> extends AbstractListmap<K,V>{
	
	public final K key;
	
	public final V val;
	
	public MapLeaf(K key, V val){
		this.key = key;
		this.val = val;
	}
	
	public boolean has(K key){
		throw new Error("there will be error objects, never null: return get(key)!=null;");
	}
	
	public V get(K key){
		throw new Error("TODO");
	}
	
	public Funcer<K,V> put(K key, V value){
		throw new Error("TODO");
	}
	
	public long size(){
		return 1;
	}

	public K lowKey(){
		return key;
	}

	public K highKey(){
		return key;
	}

	public Funcer<K,V> rem(K key){
		throw new Error("TODO");
	}

	public Funcer<K,V> cat(Funcer<K, V> list){
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
