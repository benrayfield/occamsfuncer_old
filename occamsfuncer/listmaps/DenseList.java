/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;
import java.lang.reflect.Array;

import occamsfuncer.Funcer;
import occamsfuncer.Op;

/** normally it would be double[] since occamsfuncer only supports functions and doubles,
but outside occamsfuncer, Maplist could be used for other types.
*/
public class DenseList extends AbstractListmap<Long,Object>{
	
	public final Object array;
	
	public DenseList(Object array){
		this.array = array;
		if(size() == 0) throw new Error("Use "+EmptyListmap.class);
	}

	public long size(){
		return Array.getLength(array);
	}

	public Long lowKey(){
		return 0L;
	}

	public Long highKey() {
		return size()-1;
	}

	/** Its more efficient than this func, to loop over array directly, but dont modify it. */
	public Object get(Long key){
		if(key < 0 || size() <= key) {
			throw new Error("TODO return what error object (like one for NotFound, one for NotEnoughWallet?");
		}
		return Array.get(array, key.intValue());
	}

	public Funcer<Long,Object> put(Long key, Object val){
		throw new Error("TODO");
	}

	public Funcer<Long,Object> rem(Long key){
		throw new Error("TODO");
	}

	public Funcer<Long,Object> cat(Funcer<Long, Object> list){
		throw new Error("TODO");
	}

	public Funcer<Long,Object> preI(Long key){
		throw new Error("TODO");
	}

	public Funcer<Long,Object> preE(Long key){
		throw new Error("TODO");
	}

	public Funcer<Long,Object> sufI(Long key){
		throw new Error("TODO");
	}

	public Funcer<Long,Object> sufE(Long key){
		throw new Error("TODO");
	}

	public boolean equalsForest(Funcer m){
		throw new Error("TODO");
	}

	@Override
	public Funcer l() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Funcer r() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Funcer minKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Funcer maxKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
