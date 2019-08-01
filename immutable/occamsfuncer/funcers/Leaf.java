/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import static mutable.util.Lg.todo;

import java.io.OutputStream;

import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.HaltingDictator;
import immutable.occamsfuncer.Opcode;
import static immutable.occamsfuncer.ImportStatic.*;
public class Leaf<T> extends AbstractFuncer<T>{
	
	//todo mapAndListReturnValWhenCalledOnKeyAndLeafReturnsItselfWhenCalledOnAnything
	
	
	//Only Leaf and Num have these:
	/** the first n bits of headerBitsForMask are hash prefix, and the rest of the header is not part of that id which is a hash. */
	//public static final int firstNMaskHeaderBitsAreHashPrefix = 4;
	
	//TODO there will be more mask bits or something else, for
	
	
	//public static final int headerBitsForMask = 9 + UNDECIDED HOW MANY MORE CUZ NEED TO KNOW IF AVLLIST IS ALL FLOATS, ALL DOUBLES, ALL INT4s, ETC, AND MIGHT NEED SIMILAR FOR ARE MAP KEYS ALL LIST INDEXS IN DENSE RANGE, AND ARE MAP KEYS ALL THE SAME TYPE, AND ARE MAP VALUES ALL THE SAME TYPE, AND POSSIBLY OTHER BITS;
	//use a varint for number of dims: public static final int headerBitsForHowManyDims = 4;
	/** Opcode starts with 2^thisMany coretypes that are also opcodes, and rest are just opcodes,
	so for Enum.ordinal() to fit in byte, this can be at most 7 bits, 128 possible coretypes.
	*/
	//public static final int headerBitsForCoreType = 7;
	
	/*FIXME I need sCurryList and sLinkedList and sMap and an s and non-s form for almost every datastruct,
	as in the f(x y) vs F(x y) syntax in recent *.occamsfuncer files,
	and the short firstHeader needs to include that. I wont store the k's separately
	even though in syntax its written as commas(maybe multiple k depth?),
	but I do need at least a bit for is it the s vs non-s form.
	And I want it to be in the second byte, so maybe need to have 0-7 dims instead of 0-15,
	cuz I dont want to use maskindReserved0 for it in first byte.
	BUT I'LL PROBABLY PUT IT STARTING AT THE LAST BIT OF THE FIRST BYTE ANYWAYS
	SINCE IM USING SHORT/16 FIRSTHEADER.
	*/
	
	public static final int isCoretypeOrHashElseContenttypethencolonthencontent = 1<<31;
	
	/** If true, an object that either is a localId or would generate a localId.
	If true then when id is generated it may be generated nondeterministicly and does not trigger lazyHash,
	is not part of a merkle forest but is something more like a forest of pointers within 1 system,
	which is used for efficiency of having a funcer as a map key without triggering lazyHash.
	If false then when id is generated that id will either have the maskIsHash bit or is a literal that fits in an id.
	*/
	public static final int maskIsLocal = 1<<30;
	
	/** True if this is an id thats a hash of something bigger, else might be a literal that fits in an id or the bigger content.
	If 1/true then the whole datastruct is: bit isHash, bit isWeakref, bit[190] hash,
	and see func in Id for what the hash algorithm is (can fork the opensource and start your own
	p2p net to change the hash algorithm, and can copy data except minKey and maxKey etc need rehashing.
	If !isHash, then the normal datastruct as described in the comment of this Data class.
	<br><br>
	maskIsHash and maskIsWeakref must be the high 2 bits cuz if isHash
	then the next 190 bits are hash instead of header16. 
	*/
	public static final int maskIsHash = 1<<29;
	/** maskIsHash and maskIsWeakref must be the high 2 bits cuz if isHash
	then the next 190 bits are hash instead of header16.
	*/
	public static final int maskIsWeakref = 1<<28;
	
	public static final int maskIsScalar = 1<<27;
	public static final int maskIsInteger = 1<<26;
	public static final int maskIsSigned = 1<<25;
	public static final int masksIsText = 1<<24;
	/** URL, URN, contentType, etc, normally used with isText but could also be isInteger, such as "image/jpeg". */
	public static final int maskIsSemantic = 1<<23;
	
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
	
	/** Be careful not to break things by generating header wrong */
	public Leaf(T v){
		//super(firstHeader);
		this.v = v;
	}
	
	public Leaf(boolean isText, boolean isSemantic, T v){
		super((short)(
			(isText ? masksIsText : 0)
			| (isSemantic ? maskIsSemantic : 0)
			//removed cuz its now type:content, and type tells which coretype if its a 1 ascii type: | coretypeLeaf
		));
		this.v = v;
	}
	
	public Funcer expand(){
		return this;
	}
	
	/** like Double or float[30][50] */
	public static Leaf wrap(Object v){
		return new Leaf(false,false,v);
	}
	
	/** like "image/jpeg" but interpreted as a string */
	public static Leaf wrapText(Object v){
		return new Leaf(true,false,v);
	}
	
	/** like "image/jpeg" */
	public static Leaf wrapSemanticText(Object v){
		return new Leaf(true,true,v);
	}
	
	/** 2019-6-25 I dont know any examples of this, but its here for completeness of the masks */
	public static Leaf wrapSemanticNontext(Object v){
		return new Leaf(false,true,v);
	}
	
	public Opcode leftmostOp(){
		return Opcode.identityFunc;
	}
	
	public T v(){
		return v;
	}

	public Funcer L(){
		return evalInfiniteLoop();
		//return Const.identityFunc;
	}

	public Funcer R(){
		return evalInfiniteLoop();
		//return this;
	}

	public Funcer minKey(){
		return evalInfiniteLoop();
		//return Const.nil;
	}

	public Funcer maxKey(){
		return evalInfiniteLoop();
		//return Const.nil;
	}

	public Funcer f(Funcer param){
		return this;
	}

	public Funcer prex(Funcer startExcl){
		return evalInfiniteLoop();
	}

	public Funcer sufx(Funcer endExcl){
		return evalInfiniteLoop();
	}

	public Funcer prei(Funcer endIncl){
		return evalInfiniteLoop();
	}

	public Funcer sufi(Funcer startIncl){
		return evalInfiniteLoop();
	}

	public Funcer prex(long endExcl){
		return evalInfiniteLoop();
	}

	public Funcer sufx(long endExcl){
		return evalInfiniteLoop();
	}

	public Funcer cato(Funcer itemSuffix){
		return evalInfiniteLoop();
	}

	public Funcer catn(Funcer listSuffix){
		return evalInfiniteLoop();
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

	public Funcer fStrict(Funcer param){
		throw new Error("TODO");
	}

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}
	
	public int dims(){
		throw new Error("TODO multiformats-varint for number of dims followed by varint for each dim size forllowed by that many bits padded with zeros to next byte");
	}
	
	public int dimSize(int dimIndex){
		throw new Error("TODO multiformats-varint for number of dims followed by varint for each dim size forllowed by that many bits padded with zeros to next byte");
	}

	public Funcer fNonstrict(Funcer param){
		throw new Error("TODO");
	}

	public Funcer LDeep(){
		throw new Error("TODO");
	}

	public Funcer RDeep(){
		throw new Error("TODO");
	}

	public Opcode coretype(){
		throw new Error("TODO");
	}

	/*public Funcer<T> id(){
		throw new Error("TODO");
	}*/

	public int childs(){
		throw new Error("TODO");
	}

	public Funcer<T> child(int index){
		throw new Error("TODO");
	}

	public int firstBits(){
		throw new Error("TODO");
	}

}
