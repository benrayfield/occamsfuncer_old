/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;

/** The main merkle datastruct all kinds of Funcer are made of
(which includes the Id datastruct processed in class Id):
<br><br>
If the first bit is 1, then its bit isHash, bit isWeakref, bit[190] hashContent, else it is:
<br><br>
bit[7] masks;
	isHash isWeakref isScalar isInteger isSigned isText isSemantic
	//semantic: URL, URN, contentType, etc, normally used with isText but could also be isInteger, such as "image/jpeg".
bit[5] coreType;
	Examples: Import Nil Call Leaf MapEmpty MapSingle MapPair ListEmpty ListSingle ListPair
		TypedObject Tlapp P2pStreamChunkLikeForMmgmouseai (the rest are reserved for future expansion)
bit[4] numOfDims; //dim means dimension, 0-15 dimensions
multiformatsVarint[numOfDims] dims;
	//TODO finish writing in https://github.com/multiformats/multihash/issues/87
bit[multiply all numbers in dims] content;
<br><br>
Example: float[11] has numOfDims=2 and content is a bit[11][32] since float has 32 bits,
and isHash of its id is true (and isWeakref may be true or false),
and isHash of the data in the float[11] is false,
and (the rest of this is about the float[11]) isWeakref is false,
isScalar true, isInteger false, isSigned true, isText false, isSemantic false, (reserved bit false).
<br><br>
If that fits in 192 bits then its a literal in an id, else its an id that refers to bigger content.
An example of the merkle datastruct is MapPair which has 5 ids, each 192 bits:
minKey, minChild, maxChild, maxKey, salt, and it has long size.
For example, a double is 96 bits so fits in an id, and a double[100] is about 6432 bits
and a double[1000000] would be about 64000048  bits (TODO verify),
so its very binary efficient for all but the smallest structures and efficient enough for those small.
<br><br>
Most contentTypes are constant size. Its just the leafs, such as double[50][99953][12],
that need the varints.
FIXME??? I'm undecided if coreType and array should be combined this way,
since most coreTypes are a specific size dont need numOfDims but since numOfDims is only 4 bits
its ok, but its more wasteful to have 2 bytes of "multiformatsVarint[numOfDims] dims;"
telling the size of something thats constant size but it does make it more consistent
so I'm willing to pay that cost.
*/
public class Data{
	private Data(){}
	
	public static final int headerBitsForMask = 7;
	public static final int headerBitsForCoreType = 5;
	public static final int headerBitsForHowManyDims = 4;
	
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
	
	/** If 1 then the whole datastruct is: bit isHash, bit isWeakref, bit[190] hash,
	and see func in Id for what the hash algorithm is (can fork the opensource and start your own
	p2p net to change the hash algorithm, and can copy data except minKey and maxKey etc need rehashing.
	If !isHash, then the normal datastruct as described in the comment of this Data class.
	<br><br>
	maskIsHash and maskIsWeakref must be the high 2 bits cuz if isHash
	then the next 190 bits are hash instead of header16. 
	*/
	public static final short maskIsHash = (short)(1<<15);
	/** maskIsHash and maskIsWeakref must be the high 2 bits cuz if isHash
	then the next 190 bits are hash instead of header16.
	*/
	public static final short maskIsWeakref = (short)(1<<14);
	public static final short maskIsScalar = (short)(1<<13);
	public static final short maskIsInteger = (short)(1<<12);
	public static final short maskIsSigned = (short)(1<<11);
	public static final short masksIsText = (short)(1<<10);
	/** URL, URN, contentType, etc, normally used with isText but could also be isInteger, such as "image/jpeg". */
	public static final short maskindIsSemantic = (short)(1<<9);
	
	TODO get sCall working asap to make sure the logic is consistent, even though it probably is.
	
	/** the func that statelessly imports funcs, written as ??,
	like f(?? "plus" 3 4) returns 7, and f(f(?? "plus")#+ 3 f(+ 4 1)) returns 8.
	*/
	public static final int coretypeImport = 0;
	/** TODO find churchEncoding of nil as a lambda, which this is an optimization of */
	public static final int coretypeNil = 1;
	/** Example: float[50][99937][20], or Double, or Long, or Integer, etc, with the type info
	in the short/16 firstHeader such as to say it isSemantic or not like semantic "image/jpeg".
	*/
	public static final int coretypeLeaf = 2;
	public static final int coretypeCall = 3;
	public static final int coretypeSCall = 4;
	public static final int coretypeConsPair = 5;
	public static final int coretypeMapEmpty = 6;
	public static final int coretypeMapSingle = 7;
	public static final int coretypeSMapSingle = 8;
	public static final int coretypeMapPair = 9;
	public static final int coretypeSMapPair = 10;
	public static final int coretypeAvlListEmpty = 11;
	public static final int coretypeAvlListLeafSingle = 12;
	public static final int coretypeAvlListLeafArray = 13;
	//FIXME do I need coretypeSAvlListLeafSingle and coreTypeSAvlListLeafArray? Probably not, but verify.
	public static final int coretypeSListSingle = 14;
	public static final int coretypeAvlListPair = 15;
	public static final int coretypeSAvlListPair = 16;
	/** 2 pointers (TODO also a salt?): type and value,
	though technically the whole thing is a Funcer like anythning else so is normally viewed
	as a value, it may be useful forExample to have a typedObject
	where the type is an isSemantic of "image/jpeg" and value to be an array of unsigned int1 or signed bytes etc.
	*/
	public static final int coretypeTypedObject = 17;
	public static final int coretypeSTypedObject = 18;
	/** tlapp is Torrent Like Acyc Part Packet (in mindmap, basically a kind of compression (incomplete design)
	for merkle forest where can refer to other tlapps or leafs by noncompressed id at some crossSection).
	*/
	public static final int coretypeReserved19ForTapp = 19;
	/** like mmgMouseai p2p streams, a chunk of data from it or from multiple of them,
	and part of that being a pointer at the name of the stream which may be any Funcer
	especially a publickey such as ed25519 but I dont want to hardcode the kind of digsig algorithm here
	nor to hardcode that its digsig based at all
	since it could be url based for efficiency or prppfOfWork based.
	*/
	public static final int coretypeReserved20ForStream = 20;
	public static final int coretypeReserved21 = 21;
	public static final int coretypeReserved22 = 22;
	public static final int coretypeReserved23 = 23;
	public static final int coretypeReserved24 = 24;
	public static final int coretypeReserved25 = 25;
	public static final int coretypeReserved26 = 26;
	public static final int coretypeReserved27 = 27;
	public static final int coretypeReserved28 = 28;
	public static final int coretypeReserved29 = 29;
	public static final int coretypeReserved30 = 30;
	public static final int coretypeReserved31 = 31;
	
	public static int howManyDims(short firstHeader){
		//this code breaks if headerBitsForHowManyDims==4 changes
		return firstHeader&15;
	}
	
	/** 0 to 31, one of the coreType* consts. */
	public static int coreType(short firstHeader){
		//this code breaks if headerBitsForCoreType==5 headerBitsForHowManyDims==4 changes
		return (firstHeader>>>4)&31;
	}
	
	TODO get multiformatsVarint java code for reading long from byte[].
	

}
