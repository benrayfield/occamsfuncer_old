/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;

import occamsfuncer.Funcer;

/** in a sparse list, skip some indexs then my child.
This makes map and list compatible since they are both sparse, and list's keys are integers.
*/
public class ListSkip<V> extends AbstractListmap<Long,V>{
	
	public final long lowSkip;
	
	public final Funcer<Long,V> highChild;
	
	public ListSkip(long lowSkip, Funcer<Long,V> highChild){
		if(highChild instanceof ListSkip){
			ListSkip child = (ListSkip)highChild;
			this.lowSkip = lowSkip+child.lowSkip;
			this.highChild = child.highChild;
		}else{
			this.lowSkip = lowSkip;
			this.highChild = highChild;
		}
	}

	public long size(){
		return lowSkip+highChild.size(); //should only be 1 ListSkip deep so dont cache size
	}

	public Long lowKey(){
		return lowSkip+highChild.lowKey();
	}

	public Long highKey(){
		return lowSkip+highChild.highKey();
	}

	public V get(Long key){
		return highChild.get(key-lowSkip);
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

}