/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;

import occamsfuncer.Funcer;

/** nonleaf in avl-like treemap */
public class MapPair extends AbstractListmap<Object,Object>{
	
	//FIXME detect when size would exceed max integer size that fits in double and not allow that.
	
	public final ListmapPair map;
	
	public final ListPair list;
	
	public MapPair(ListPair list, ListmapPair map){
		this.list = list;
		this.map = map;
	}

	public long size(){
		return map.size()+list.size();
	}

	/** max possible list index < min possible map index */
	public Object lowKey(){
		return list.lowKey(); //not always 0L since could be or contain (leftmost) ListSkip
	}

	/** max possible list index < min possible map index */
	public Object highKey(){
		return map.highKey();
	}

	public Object get(Object key){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> put(Object key, Object val){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> rem(Object key){
		throw new Error("TODO");
	}

	public Funcer<Object,Object> cat(Funcer<Object, Object> list){
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
