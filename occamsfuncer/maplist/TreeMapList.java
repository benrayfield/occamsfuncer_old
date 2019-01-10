package occamsfuncer.maplist;

/** a maplist can have keys of a map (anything) and keys of a list (integers).
*/
public class TreeMapList{
	
	TODO merge duplicate code between TreeMapList, TreeMap, and TreeList.
	TODO see ufnode.Ty's 5 types for merging maplist,
	leafmap leaflist mappair listpair maplist, or something like that.
	Still have to make it compatible with Funcall. See Op.maplist*.
	
	public final TreeMap map;
	
	public final TreeList list;
	
	public TreeMapList(TreeMap map, TreeList list){
		this.map = map;
		this.list = list;
	}
	
	public Object has(Object key){
		return get(key)!=null;
	}
	
	public Object get(Object key){
		throw new Error("TODO");
	}
	
	public Object put(Object key, Object value){
		throw new Error("TODO");
	}

}
