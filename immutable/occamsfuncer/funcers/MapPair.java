/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;

import java.io.OutputStream;

import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.HaltingDictator;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.ImportStatic;
import immutable.occamsfuncer.Opcode;

/** treemap that lazyEvalHashes to same id for same set of key/value pairs
regardless of order of adds and removes. Trigges lazyEvalHash of keys but not values.
<br><br>
f(?? ;mapPair size minKey maxKey minChild maxChild)
where minChild and maxChild are LDeep() and RDeep(). 
*/
public class MapPair extends AbstractCollectionPair{
	
	/** min key is minKey.id(). max key is maxKey.id() */
	public final Funcer minKey, maxKey;
	
	public MapPair(Funcer minChild, Funcer maxChild){
		super(
			(short)Data.coretypeMapPair, //all mask bits 0
			minChild.maplistSize()+maxChild.maplistSize(),
			minChild,
			maxChild
		);
		minKey = minChild.minKey();
		maxKey = maxChild.maxKey();
		if(maplistSize < 0) throw new Error(
			"size doesnt fit in long: "+minChild.maplistSize()+" + "+maxChild.maplistSize());
	}
	
	public Funcer expand(){
		return Opcode.mapPair.ob.f(maplistSize).f(minKey).f(maxKey).f(LDeep).f(RDeep);
	}
	
	/** occamsfuncerRedesignToMakeSLinkedListEtcConsistentAndUrbitlike */
	public Funcer L(){
		return new Call(Opcode.mapPair.ob, RDeep);
	}
		
	/** map is func of key to value */
	public Funcer f(Funcer param){
		return get(param);
	}
	
	/** in this case theres no fp math so just copy f(param) */
	public Funcer fStrict(Funcer param){
		return f(param);
	}
	
	public Opcode leftmostOp(){
		return Opcode.identityFunc;
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

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}
	
	todo mapAndListReturnValWhenCalledOnKeyAndLeafReturnsItselfWhenCalledOnAnything
	
	
	TODO do this in MapPair, MapSingle, AvlTreelistPair, listsingle, etc...
	2019-7-14 chooseBhDesignOfMappairMapsingleMinkeyEtc QUOTE
	UPDATE: I want it to be f(mapPair size mapPair minKey maxKey minChild maxChild)
	(or some order of those params) cuz there must not be any hidden data (such as minKey
			and size. Make it return nil if those params dont obey the allowed relations
					of size and key order.
			...
			NO TO THIS... SOLUTION, from below QUOTE
			mapPair is represented as: f(mapPair minChild maxChild).
			mapSingle is represented as: f(mapSingle key value).
			That way, the size and minKey and maxKey are technically derivable from the bh(int),
			including that you have to read the f(?? "mapPair") etc, but in practice it will almost
			always be done using minKey and maxKey and size funcs which get it directly from the mapPair
			and mapSingle and mapEmpty datastructs instead of deriving it the slow way from the whole 
			tree at once (which those datastructs cache).
			UNQUOTE.
			UNQUOTE.


}
