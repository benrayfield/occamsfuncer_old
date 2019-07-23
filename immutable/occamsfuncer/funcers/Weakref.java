/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import static immutable.occamsfuncer.ImportStatic.*;

import java.io.OutputStream;

import static immutable.occamsfuncer.HaltingDictator.*;
import immutable.occamsfuncer.*;

/** A func that returns 1/true if its param is a certain Funcer, else returns 0/false.
This allows you to refer to a specific id without needing a copy of it,
so you can delete your copy of it or not (or never had it) without breaking the forest,
since a recognizer-function does not have a dependency of the thing it recognizes.
The id of a weakref differs from the id of the thing it recognizes by 1 bit.
That is bit index 1 of a 192 bit id. Its 1 for weakref, 0 for literal.
Bit index 0 is 1 for a hash, 0 for leaf. Leaf can be any primitive array of up to 15 dimensions
and has a few bits of other metadata such as
is it generalText vs semanticText(such as urn, url, contentType).
<br><br>
You cant have a weakref to another weakref since theres only 1 weakref bit in ids,
and I dont expect you'd ever want one, but if you do you can at user level
make a function which does it by reading the id into a leaf
and doing logic on that id compared to the param's id. See Opcode.idAsWeakref.
*/
public class Weakref extends AbstractFuncer{
	
	/** Param is either my id or my target's id.
	Creates weakref id (id of a recognizer-func of it) if its a non-weakref.
	*/
	public Weakref(Id myIdOrTargetsId){
		super((short)((myIdOrTargetsId.idA>>>48)|Data.maskIsWeakref)); //high 16 bits of this.id
		this.id = myIdOrTargetsId.setWeakref(true);
	}
	
	public Weakref(Funcer target){
		this(target.id());
		if(target.id().isWeakref()) throw new Error(
			"Cant have a weakref to a weakref (see comment in Weakref.java"
			+" how to make such a func at user level if you need one): "+target);
	}
	
	public Funcer f(Funcer param){
		$();
		Id p = param.id();
		return id.equalsIgnoringWeakrefBit(p) && !p.isWeakref() ? one : zero;
	}
	
	/** no fp ops */
	public Funcer fStrict(Funcer param){
		return f(param);
	}
	
	public Funcer setSalt(Funcer salt){
		throw new Error("TODO since Weakref.java is an optimization of a funcer that returns 1 if its param is a certain funcer else returns 0, but only if that funcer is not a weakref and this weakref has nil salt, then there needs to either be a GeneralWeakref.java to handle those which doesnt fit in id or this would return a combo of funcers that does the same thing BUT that wouldnt be this weakref with salt that would be some other funcer with that salt so the setSalt func maybe should work with another func canSalt() which would be false for leafs and weakrefs and true everywhere else.");
	}
	
	public Funcer expand(){
		return this;
	}

	public Funcer L(){
		throw new Error("TODO");
	}

	public Funcer R(){
		throw new Error("TODO");
	}

	public Funcer minKey(){
		throw new Error("TODO");
	}

	public Funcer maxKey(){
		throw new Error("TODO");
	}

	public Object v(){
		throw new Error("TODO");
	}

	public Opcode leftmostOp(){
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

	public int compareTo(Object o){
		throw new Error("TODO");
	}

	public boolean contentFitsInId(){
		return true;
	}

	public boolean isWeakref(){
		return true;
	}

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}

}
