/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import static immutable.occamsfuncer.ImportStatic.*;

import java.io.OutputStream;

import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.HaltingDictator;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;
import mutable.occamsfuncer.memstat.MemStat;

/** optimization of Leaf when value is a double and salt is ImportStatic.nil. */
public final class Num implements Funcer<Double>{
	
	public final double val;
	
	private Id id;
	
	private MemStat mem;
	
	//public static final short firstHeaderOfNum =
	//	(short)(Data.maskIsScalar | Data.maskIsSigned | Data.coretypeLeaf);
	
	public Num(double val){
		this.val = val;
	}
	
	public Funcer f(Funcer param){
		return this;
	}
	
	public strictfp Funcer fStrict(Funcer param){
		return this;
	}
	
	public Funcer expand(){
		return this;
	}

	/** same as in AbstractFuncer */
	public int compareTo(Funcer o){
		return id().compareTo(o.id());
	}
	
	/** same as in AbstractFuncer */
	public boolean equals(Object o){
		if(!(o instanceof Funcer)) return false;
		return id()==((Funcer)o).id(); //at most 1 instance of each Id per JVM (FIXME enforce that where? When create id, would have to dedup them)
	}
	
	/** same as in AbstractFuncer */
	public int hashCode(){
		return id().hashCode();
	}

	/*public short firstHeader(){
		return firstHeaderOfNum;
	}*/

	public long maplistSize(){
		return 0;
	}

	public Funcer L(){
		return nil;
	}

	public Funcer R(){
		return nil;
	}

	public Funcer minKey(){
		throw HaltingDictator.throwMe;
	}

	public Funcer maxKey(){
		throw HaltingDictator.throwMe;
	}

	public Double v(){
		return val;
	}

	public Opcode leftmostOp(){
		return Opcode.data;
	}

	public boolean isTheImportFunc(){
		return false;
	}

	/** calling me with any param returns me */
	public byte cur(){
		return 1;
	}

	public Id id(){
		if(id == null) id = Id.id(this);
		return id;
	}

	public boolean contentFitsInId(){
		return true;
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

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}
	
	/** Num is an optimization of Leaf of bit[64] which isScalar, isSigned, etc, aka a double */
	public int dims(){
		return 1;
	}

	public int dimSize(int dimIndex){
		if(dimIndex==0) return 64; //double is bit[64]
		throw new IndexOutOfBoundsException("dim"+dimIndex);
	}
	
	public MemStat mem(){
		if(mem == null) mem = HaltingDictator.newMemStat();
		return mem;
	}

}
