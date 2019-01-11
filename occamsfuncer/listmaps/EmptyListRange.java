/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.listmaps;
import occamsfuncer.Funcer;

/** this is an alternative to ListSkip(someNumberToSkip,EmptyMaplist),
FIXME but I'm unsure if this is consistent with the rest of the listmap design
cuz it has no minKey or maxKey so in that way should equal an empty list.
*/
public class EmptyListRange extends AbstractListmap<Long,Object>{
	
	public final long size;
	
	public EmptyListRange(long size){
		this.size = size;
	}

	public long size(){
		return size;
	}

	public Long lowKey(){
		throw new Error("TODO return what error object (like one for NotFound, one for NotEnoughWallet?");
	}

	public Long highKey(){
		throw new Error("TODO return what error object (like one for NotFound, one for NotEnoughWallet?");
	}

	public Object get(Long key){
		throw new Error("TODO return what error object (like one for NotFound, one for NotEnoughWallet?");
	}

	public Funcer<Long,Object> put(Long key,Object val){
		Funcer m = new ListLeaf(val);
		return key==0L ? m : new ListSkip(key,m);
	}

	public Funcer<Long,Object> rem(Long key){
		return this;
	}

	public Funcer<Long,Object> cat(Funcer list) {
		throw new Error("TODO");
	}

	public Funcer<Long,Object> preI(Long key){
		// TODO Auto-generated method stub
		return null;
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
