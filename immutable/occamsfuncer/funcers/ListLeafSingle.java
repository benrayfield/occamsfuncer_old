/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import java.io.OutputStream;

import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;

/** Interprets 1 funcer as a list item, instead of interpreting its contents as list items */
public class ListLeafSingle extends AbstractFuncer{
	
	todo mapAndListReturnValWhenCalledOnKeyAndLeafReturnsItselfWhenCalledOnAnything
	
	public final Funcer val;

	public ListLeafSingle(Funcer val){
		super((short)Data.coretypeAvlListLeafSingle); //all mask bits 0
		this.val = val;
	}

	public Funcer f(Funcer param){
		throw new Error("TODO");
	}

	public Funcer fStrict(Funcer param){
		throw new Error("TODO");
	}
	
	public Funcer expand(){
		return this; //cuz L and R have all my parts
	}

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}

	public Funcer L(){
		return Opcode.listLeafSingle.ob;
	}

	public Funcer R(){
		return val;
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
		throw new Error("TODO");
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
	
	/*
	public final Funcer val;
	
	public ListLeafSingle(Funcer val){
		this.val = val;
	}
	
	public long size(){
		return 1;
	}
	
	public Funcer cat(Funcer list, double walletLimit){
		throw new Error("TODO use ListPair but make sure to balance");
	}
	
	public Funcer pre(long endExcl, double walletLimit){
		return endExcl==1 ? this : ListEmpty.instance;
	}
	
	public Funcer suf(long start, double walletLimit){
		return start==0 ? this : ListEmpty.instance;
	}
	
	public Funcer listGet(long i, double walletLimit){
		return i==0 ? val : Opcode.nil.funcer;
	}
	
	public CoreType coreType(){
		return CoreType.listLeafSingle;
	}

	public Funcer mapGet(Id key, double walletLimit){
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
