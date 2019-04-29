package immutable.occamsfuncer.funcers;
import static immutable.occamsfuncer.ImportStatic.*;

import java.io.OutputStream;

import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.HaltingDictator;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;

/** optimization of Leaf when value is a double and salt is ImportStatic.nil. */
public final class Num implements Funcer<Double>{
	
	public final double v;
	
	private Id id;
	
	public Num(double v){
		this.v = v;
	}
	
	public Funcer f(Funcer param){
		return this;
	}
	
	public strictfp Funcer fStrict(Funcer param){
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

	public int header(){
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

	public Funcer<Double> setSalt(Funcer salt){
		return salt==nil ? this : new Leaf(header(), salt, v);
	}

	public Funcer minKey(){
		throw HaltingDictator.throwMe;
	}

	public Funcer maxKey(){
		throw HaltingDictator.throwMe;
	}

	public Double v(){
		return v;
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

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}

}
