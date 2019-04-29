package immutable.occamsfuncer.funcers;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;
import immutable.occamsfuncer.HaltingDictator;

public class Leaf<T> extends AbstractFuncer<T>{
	
	/** value, such as Double or float[][][][][] */
	public final T v;
	
	public Leaf(int header, Funcer salt, T v){
		super(header, salt);
		this.v = v;
	}
	
	public Opcode leftmostOp(){
		return Opcode.i;
	}
	
	public T v(){
		return v;
	}

	public Funcer L(){
		return HaltingDictator.evalInfiniteLoop();
		//return Const.identityFunc;
	}

	public Funcer R(){
		return HaltingDictator.evalInfiniteLoop();
		//return this;
	}

	public Funcer minKey(){
		return HaltingDictator.evalInfiniteLoop();
		//return Const.nil;
	}

	public Funcer maxKey(){
		return HaltingDictator.evalInfiniteLoop();
		//return Const.nil;
	}

	public Id id(){
		throw new Error("TODO");
	}

	public Funcer f(Funcer param){
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

	public double d(){
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
	
	public boolean contentFitsInId(){
		throw new Error("TODO");
	}
	
	public boolean isWeakref(){
		return false;
	}

	public Funcer<T> setSalt(Funcer salt){
		return new Leaf(header, salt, v);
	}

}
