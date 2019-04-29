package immutable.occamsfuncer.funcers;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Mask;
import immutable.occamsfuncer.Opcode;
import immutable.util.BitsUtil;

public abstract class AbstractFuncer<T> implements Funcer<T>{
	
	public final int header;
	
	protected Id id;
	
	public final Funcer salt;
	
	//protected WeakReference<Funcer<T>> vmInternal_javaWeakReferenceToMe;
	
	public AbstractFuncer(int header, Funcer salt){
		this.header = header;
		this.salt = salt;
	}
	
	public Funcer salt(){
		return salt;
	}
	
	/*public WeakReference<Funcer<T>> vmInternal_javaWeakReferenceToMe(){
		if(vmInternal_javaWeakReferenceToMe == null){
			vmInternal_javaWeakReferenceToMe = new WeakReference(this);
		}
		return vmInternal_javaWeakReferenceToMe;
	}*/
	
	public int header(){
		return header;
	}

	public long maplistSize(){
		throw new Error("TODO");
	}
	
	
	public int compareTo(Funcer o){
		return id().compareTo(o.id());
	}
	
	public boolean equals(Object o){
		if(!(o instanceof Funcer)) return false;
		return id()==((Funcer)o).id(); //at most 1 instance of each Id per JVM (FIXME enforce that where? When create id, would have to dedup them)
	}
	
	public int hashCode(){
		return id().hashCode();
	}
	
	public Id id(){
		if(id == null) id = Id.id(this);
		return id;
	}

}
