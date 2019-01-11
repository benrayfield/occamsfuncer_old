/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;

import occamsfuncer.Funcer;

/** a list of size 1 */
public final class ListLeaf<V> extends AbstractListmap<Long,V>{
	
	//key is (double)0
	
	public final V val;
	
	public ListLeaf(V val){
		this.val = val;
	}
	
	public long size(){
		return 1;
	}

	public Long lowKey(){
		return 0L;
	}

	public Long highKey() {
		return 0L;
	}

	public V get(Long key){
		if(key == 0L) return val;
		throw new Error("TODO return what error object (like one for NotFound, one for NotEnoughWallet?");
	}

	public Funcer<Long,V> put(Long key, V val){
		throw new Error("TODO");
	}

	public Funcer<Long,V> rem(Long key){
		throw new Error("TODO");
	}

	public Funcer<Long,V> cat(Funcer<Long, V> list){
		throw new Error("TODO");
	}

	public Funcer<Long,V> preI(Long key){
		throw new Error("TODO");
	}

	public Funcer<Long,V> preE(Long key){
		throw new Error("TODO");
	}

	public Funcer<Long,V> sufI(Long key){
		throw new Error("TODO");
	}

	public Funcer<Long,V> sufE(Long key){
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
	
	/** leaf can be an array (maybe java.lang.String as array of char?),
	else is tree.
	*
	public final BlobType blob;
	
	public final double size;
	*/

}
