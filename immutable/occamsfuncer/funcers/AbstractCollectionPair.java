/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import immutable.occamsfuncer.Funcer;

public abstract class AbstractCollectionPair extends AbstractFuncer{
	
	public final long maplistSize;
	
	public final Funcer LDeep, RDeep;
	
	public AbstractCollectionPair(long maplistSize, Funcer minChild, Funcer maxChild){
		/*FIXME no more firstHeader except in Leaf (including Num, as abstract math)
		Theres no more firstHeader at all in Funcer.firstHeader(). Dont take it in constructor.
		All you need to know in most Funcer is whats their type letter, and each class only has
		1 of those, so just remove the param.
		This is part of application/x-occamsfuncer-..., especially the newer part where
		every data is type:value. In the case of MapPair, value is binary of 4 ids and a long size.
		Create a new function similar to Funcer.leftmostOp(), which in many cases equals leftmostOp
		(TODO find counterexamples)... It tells what coretype this object is.
		Remember bh(int) and LDeepIndex and LDeep and L etc.
		*/
		
		
		//super(firstHeader);
		this.maplistSize = maplistSize;
		this.LDeep = minChild;
		this.RDeep = maxChild;
	}
	
	public long maplistSize(){
		return maplistSize;
	}
	
	public Funcer LDeep(){
		return LDeep;
	}

	public Funcer RDeep(){
		return RDeep;
	}
	
	/*public Funcer L(){
		return new Call(todo, LDeep);
	}*/
	
	public Funcer R(){
		return RDeep;
	}
	
	/** LR in littleEndian of bits.
	This default implementation will work for any number of curries as long as LDeep() and RDeep() are the last 2.
	*/
	public int LDeepIndex(){
		return 0b101;
	}
	
	/** R in littleEndian of bits.
	This default implementation will work for any number of curries as long as LDeep() and RDeep() are the last 2.
	*/
	public int RDeepIndex(){
		return 0b11;
	}
	
	public boolean contentFitsInId(){
		return false;
	}

}
