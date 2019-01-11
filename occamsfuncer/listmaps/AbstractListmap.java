package occamsfuncer.listmaps;
import occamsfuncer.Funcer;
import occamsfuncer.Op;

public abstract class AbstractListmap<K,V> implements Funcer<K,V>{
	
	public boolean isErr(){
		return false;
	}

	public boolean isErrWal(){
		return false;
	}

	public boolean isErrGet(){
		return false;
	}
	
	public Op leftmostOp(){
		return Op.listmap;
	}
	
	public int curries(){
		throw new Error("TODO");
	}
	
	public double d(){
		return 0;
	}

	public boolean isFloat(){
		return false;
	}
	
	public Funcer f(Funcer p, double walletLimit){
		throw new Error("TODO see the func behaviors defined in comment of Op.listmap");
	}


}
