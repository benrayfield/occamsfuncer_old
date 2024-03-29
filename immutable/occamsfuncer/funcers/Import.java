/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import static immutable.occamsfuncer.ImportStatic.*;

import java.io.OutputStream;

import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;
import mutable.occamsfuncer.memstat.MemStat;
import immutable.occamsfuncer.HaltingDictator;

public class Import implements Funcer{
	
	public static Import instance = new Import();
	
	public final Id id = Id.id(this);
	
	public final MemStat mem = HaltingDictator.newMemStat();
	
	//public static final short firstHeaderOfImport = (short)(Data.coretypeImport); //all mask bits 0
	
	private Import(){}
	
	public Funcer f(Funcer param){
		//Call.java checks for TheImportFunc and caches Opcode leftmostOp and curries.
		//TheImportFunc takes 1 param before knowing how many curries.
		return new Call(this, param);
	}
	
	/** no fp ops */
	public strictfp Funcer fStrict(Funcer param){
		return f(param);
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

	public short firstHeader(){
		return 0; //FIXME what constant header should a Leaf of double and salt=nil have? Its not 0.
	}

	public long maplistSize(){
		return 0;
	}

	public Funcer L(){
		return nil;
	}

	public Funcer R(){
		return nil;
	}

	public Funcer salt(){
		return nil;
	}
	
	public boolean canSalt(){
		return false;
	}

	public Funcer<Double> setSalt(Funcer salt){
		return evalInfiniteLoop();
	}

	public Funcer minKey(){
		return evalInfiniteLoop();
	}

	public Funcer maxKey(){
		return evalInfiniteLoop();
	}

	public Object v(){
		return evalInfiniteLoop(); //FIXME return nil?
	}

	public Opcode leftmostOp(){
		return Opcode.a_coretypeImport;
	}

	/*public boolean isTheImportFunc(){
		return false;
	}*/

	/** calling me with any param returns me *
	public byte cur(){
		FIXME shouldnt this be done in Opcode? except maybe in Call its different number of curries than Opcode.
		return 1;
	}*/

	public Id id(){ return id; }

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

	public int compareTo(Object o){
		throw new Error("TODO");
	}

	public int dimSize(int dimIndex){
		throw new Error("TODO");
	}

	public MemStat mem(){
		return mem;
	}
}
