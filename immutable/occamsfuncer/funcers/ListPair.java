/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;

import java.io.OutputStream;

import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.HaltingDictator;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;

/** avl treelist */
public class ListPair extends AbstractCollectionPair{
	
	todo mapAndListReturnValWhenCalledOnKeyAndLeafReturnsItselfWhenCalledOnAnything
	
	//Do I want every object to allow map keys, sparse list keys, and leaf value?
	//I could use a few classes for those combos.
	
	public ListPair(long maplistSize, Funcer minChild, Funcer maxChild){
		super(
			(short)Data.coretypeAvlListPair, //all mask bits 0
			maplistSize,
			minChild,
			maxChild
		);
	}
	
	public Funcer expand(){
		return Opcode.listPair.ob.f(maplistSize).f(LDeep).f(RDeep);
	}
	
	public Opcode leftmostOp(){
		return Opcode.listPair;
	}

	public Funcer minKey(){
		return HaltingDictator.evalInfiniteLoop();
		//return Const.zero;
	}

	public Funcer maxKey(){
		return HaltingDictator.evalInfiniteLoop();
		//throw new Error("TODO return list size minus 1");
	}

	public Object v(){
		return HaltingDictator.evalInfiniteLoop();
		//return Const.nil;
	}

	public Id id(){
		throw new Error("TODO");
	}

	public Funcer f(Funcer param){
		return get(param);
	}
	
	/** no fp ops */
	public Funcer fStrict(Funcer param){
		return f(param);
	}

	public Funcer prex(Funcer startExcl){
		throw new Error("TODO use param as list index");
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

}
