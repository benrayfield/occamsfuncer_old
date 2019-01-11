/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;
import occamsfuncer.Funcer;

/** nonleaf in avl-like treelist */
public final class ListPair<V> extends AbstractListmap<Long,V>{
	
	//FIXME detect when size would exceed max integer size that fits in double and not allow that.
	
	public final long size, lowKey, highKey;
	
	public final Funcer<Long,V> lowChild;
	
	public final Funcer<Long,V> highChild;
	
	public ListPair(Funcer<Long,V> lowChild, Funcer<Long,V> highChild){
		this.lowChild = lowChild;
		this.highChild = highChild;
		this.size = lowChild.size()+highChild.size();
		this.lowKey = lowChild.lowKey();
		this.highKey = lowChild.highKey()+1+highChild.highKey();
	}
	
	public V get(Long key){
		long midKey = lowChild.highKey();
		return key<=midKey ? lowChild.get(key) : highChild.get(midKey+1+key);
	}

	public long size(){
		return size;
	}

	public Long lowKey(){
		return lowKey;
	}

	public Long highKey(){
		return highKey;
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
	*/

}
