package immutable.occamsfuncer.funcers;
import immutable.occamsfuncer.Funcer;

public abstract class AbstractCollection extends AbstractFuncer{
	
	public final long maplistSize;
	
	public final Funcer minChild, maxChild;
	
	public AbstractCollection(int header, Funcer salt, long maplistSize, Funcer minChild, Funcer maxChild){
		super(header,salt);
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

}
