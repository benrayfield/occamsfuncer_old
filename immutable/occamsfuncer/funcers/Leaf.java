/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import static mutable.util.Lg.*;
import java.io.OutputStream;

import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;
import immutable.occamsfuncer.HaltingDictator;

public class Leaf<T> extends AbstractFuncer<T>{
	
	static{
		todo("TODO multiformats varint for up to some small limit of dims (maybe 14 or 15?)"
			+" of hyper-rect array (such as float[43][4][55] but not triangleArray),"
			+" and get that data format finalized, along with the headers for weakref,"
			+" hash, literalInId, etc. TODO https://github.com/multiformats/multihash/issues/87");
	}
	
	/*TODO merge Funcer.java with Tensor.java, where every leaf is a Tensor,
	though I may want less abstraction such as viewing a set of tensors
	as the outerjoin of all their dims and 1 more dim to say which tensor it is
	such as AB multiply BC is a 5d tensor a,b0,b1,c,whichOfThose2Tensors
	and the result is derived from mat[a][b][b][c][0]*mat[a][b][b][c][1];
	But I will keep the Num optimization of a 0 dimensional tensor
	(or in the byte[] content view by multiformats_varint
	its a 1 dimensional tensor of 64 bits, always 1 more dim).
	*/
	
	/** value, such as Double or float[][][][][] */
	public final T v;
	
	public Leaf(short firstHeader, Funcer salt, T v){
		super(firstHeader, salt);
		this.v = v;
	}
	
	public Opcode leftmostOp(){
		return Opcode.identityFunc;
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

	public Funcer f(Funcer param){
		return this;
	}

	public Funcer prex(Funcer startExcl){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Funcer sufx(Funcer endExcl){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Funcer prei(Funcer endIncl){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Funcer sufi(Funcer startIncl){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Funcer prex(long endExcl){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Funcer sufx(long endExcl){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Funcer cato(Funcer itemSuffix){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Funcer catn(Funcer listSuffix){
		return HaltingDictator.evalInfiniteLoop();
	}

	public Funcer flat(){
		return this;
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
		return new Leaf(firstHeader, salt, v);
	}

	public Funcer fStrict(Funcer param){
		throw new Error("TODO");
	}

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}

}
