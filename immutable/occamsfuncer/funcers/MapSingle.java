/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import static immutable.occamsfuncer.ImportStatic.evalInfiniteLoop;

import java.io.OutputStream;

import immutable.ids_fork7128543112795615.ob;
import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;

public class MapSingle extends AbstractFuncer{
	
	//todo mapAndListReturnValWhenCalledOnKeyAndLeafReturnsItselfWhenCalledOnAnything
	
	public final Funcer key, val;

	public MapSingle(Funcer key, Funcer val){
		//super((short)Data.coretypeMapSingle); //all mask bits 0
		this.key = key;
		this.val = val;
	}

	public Funcer f(Funcer param){
		throw new Error("TODO");
	}

	public Funcer fStrict(Funcer param){
		throw new Error("TODO");
	}
	
	public Funcer expand(){
		return Opcode.j_coretypeMapSingle.ob.f(1.).f(key).f(val);
	}

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}

	public Funcer L(){
		throw new Error("TODO");
	}

	public Funcer R(){
		throw new Error("TODO");
	}

	public Funcer setSalt(Funcer salt){
		throw new Error("TODO");
	}

	public Funcer minKey(){
		throw new Error("TODO");
	}

	public Funcer maxKey(){
		throw new Error("TODO");
	}

	public Object v(){
		throw new Error("TODO");
	}

	public Opcode leftmostOp(){
		throw new Error("TODO");
	}

	public byte cur(){
		throw new Error("TODO");
	}

	public boolean contentFitsInId(){
		return false;
	}

	public boolean isWeakref(){
		throw new Error("TODO");
	}

	public Funcer unstub(){
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

	public int compareTo(Object o){
		throw new Error("TODO");
	}
	
	public int childs(){
		return 2;
	}

	public Funcer child(int index){
		switch(index){
		case 0: return key;
		case 1: return val;
		default: return evalInfiniteLoop();
		}
	}
	
	/*
	//public final boolean isExec;
	
	/** 192 bit keys are forked at most 192 times, usually just a few,
	like a trie per bit but with the optimization that when theres only 1 path
	it doesnt fork there it just keeps going to wherever it does fork.
	*
	public final Id key;
	
	public final Funcer val;
	
	public MapSingle(byte[] content){
		throw new Error("TODO get Funcer pointers from CacheIdToFuncer, and put self there");
	}
	
	public MapSingle(Id key, Funcer val){
		this.key = key;
		this.val = val;
	}
	
	public long size(){ return 1; }
	
	public CoreType coreType(){
		return CoreType.mapSingle;
	}

	public Funcer cat(Funcer list, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer pre(long endExcl, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer suf(long start, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer mapGet(Id key, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer listGet(long listIndex, double walletLimit){
		throw new Error("TODO");
	}

	public boolean isMap(){
		throw new Error("TODO");
	}

	public boolean isList(){
		throw new Error("TODO");
	}

	public boolean isArray(){
		throw new Error("TODO");
	}

	public Object unwrap(){
		throw new Error("TODO");
	}

	public byte[] content(double walletLimit){
		throw new Error("TODO");
	}
	*/
}
