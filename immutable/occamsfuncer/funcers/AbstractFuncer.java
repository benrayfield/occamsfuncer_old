/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.HaltingDictator;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;
import immutable.util.BitsUtil;
import mutable.occamsfuncer.memstat.MemStat;

public abstract class AbstractFuncer<T> implements Funcer<T>{
	
	//FIXME use short firstHeader correctly, along with multiformatsVarint, and content.
	//Get the datastruct finalized except for those starting with coretypeReserved.
	
	/** This is the first 16 bits, the part of header before the multiformatsVarints and content,
	as described in Data class comment.
	*/
	public final short firstHeader;
	
	protected Id id;
	
	protected MemStat mem;
	
	//protected WeakReference<Funcer<T>> vmInternal_javaWeakReferenceToMe;
	
	public AbstractFuncer(short firstHeader){
		this.firstHeader = firstHeader;
	}
	
	public MemStat mem(){
		if(mem == null) mem = HaltingDictator.newMemStat();
		return mem;
	}
	
	/*public WeakReference<Funcer<T>> vmInternal_javaWeakReferenceToMe(){
		if(vmInternal_javaWeakReferenceToMe == null){
			vmInternal_javaWeakReferenceToMe = new WeakReference(this);
		}
		return vmInternal_javaWeakReferenceToMe;
	}*/
	
	public short firstHeader(){
		return firstHeader;
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
	
	public int dimSize(int dimIndex){
		throw new Error("TODO");
	}
	
	/** MOVED TO WITHSALT.JAVA:
	in syntax the salt is after @
	such as f(?? "plus")@saltOfThisPlus#nameOfThisPlus
	or such as f(?? "plus")@#saltAndNameOfThisPlus when salt and name happen to be same whitespaceless string,
	and ?? is TheImportFunc.
	*
	public final Funcer salt;
	*/

}
