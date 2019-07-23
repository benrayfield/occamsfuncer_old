/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import immutable.occamsfuncer.Funcer;

public abstract class AbstractCollectionPair extends AbstractFuncer{
	
	public final long maplistSize;
	
	public final Funcer LDeep, RDeep;
	
	public AbstractCollectionPair(short firstHeader, long maplistSize, Funcer minChild, Funcer maxChild){
		super(firstHeader);
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
