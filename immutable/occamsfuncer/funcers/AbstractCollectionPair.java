/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import immutable.occamsfuncer.Funcer;

public abstract class AbstractCollectionPair extends AbstractFuncer{
	
	public final long maplistSize;
	
	public final Funcer minChild, maxChild;
	
	public AbstractCollectionPair(short firstHeader, Funcer salt, long maplistSize, Funcer minChild, Funcer maxChild){
		super(firstHeader,salt);
		this.maplistSize = maplistSize;
		this.minChild = minChild;
		this.maxChild = maxChild;
	}
	
	public long maplistSize(){
		return maplistSize;
	}
	
	public Funcer L(){
		return minChild;
	}

	public Funcer R(){
		return maxChild;
	}
	
	public boolean contentFitsInId(){
		return false;
	}

}
