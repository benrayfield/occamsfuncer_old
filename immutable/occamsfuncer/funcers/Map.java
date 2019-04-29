package immutable.occamsfuncer.funcers;

import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.HaltingDictator;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;

/** treemap that lazyEvalHashes to same id for same set of key/value pairs
regardless of order of adds and removes. Trigges lazyEvalHash of keys but not values.
*/
public class Map extends AbstractCollection{
	
	/** min key is minKey.id(). max key is maxKey.id() */
	public final Funcer minKey, maxKey;
	
	public Map(int header, long maplistSize, Funcer minChild, Funcer maxChild, Funcer minKey, Funcer maxKey){
		super(header, maplistSize, minChild, maxChild);
		this.minKey = minKey;
		this.maxKey = maxKey;
	}
	
	public Funcer f(Funcer param){
		return get(param);
	}
	
	/** in this case theres no fp math so just copy f(param) */
	public Funcer fStrict(Funcer param){
		return f(param);
	}
	
	public Opcode leftmostOp(){
		return Opcode.i;
	}

	public Funcer minKey(){
		throw new Error("TODO");
	}

	public Funcer maxKey(){
		throw new Error("TODO");
	}

	public Object v(){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Id id(){
		throw new Error("TODO");
	}

	public Funcer prex(Funcer startExcl){
		throw new Error("TODO");
	}

	public Funcer sufx(Funcer endExcl){
		throw new Error("TODO");
	}

	public Funcer prei(Funcer endIncl){
		throw new Error("TODO");
	}

	public Funcer sufi(Funcer startIncl){
		throw new Error("TODO");
	}

	public Funcer prex(long endExcl){
		throw new Error("TODO");
	}

	public Funcer sufx(long endExcl){
		throw new Error("TODO");
	}

	public Funcer cato(Funcer itemSuffix){
		throw new Error("TODO");
	}

	public Funcer catn(Funcer listSuffix){
		throw new Error("TODO");
	}

	public Funcer flat(){
		throw new Error("TODO");
	}

	public Funcer put(Funcer key, Funcer value){
		throw new Error("TODO");
	}

	public Funcer get(Funcer key){
		throw new Error("TODO");
	}

	public Funcer get(long key){
		throw new Error("TODO");
	}

	public Funcer get(int key){
		throw new Error("TODO");
	}
	
	public boolean contentFitsInId(){
		return false;
	}
	
	public boolean isWeakref(){
		return false;
	}

	public Funcer setSalt(Funcer salt){
		throw new Error("TODO");
	}

	public boolean isTheImportFunc(){
		throw new Error("TODO");
	}

	public byte cur(){
		throw new Error("TODO");
	}

	public Funcer unstub(){
		throw new Error("TODO");
	}

	public int compareTo(Object o){
		throw new Error("TODO");
	}

}
