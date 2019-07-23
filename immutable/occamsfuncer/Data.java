/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import static mutable.util.Lg.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import immutable.util.Text;
import mutable.util.Rand;

/** The main merkle datastruct all kinds of Funcer are made of
(which includes the Id datastruct processed in class Id):
<br><br>
If the first bit is 1, then its bit isHash, bit isWeakref, bit[190] hashContent, else it is:
<br><br>
bit[7] masks;
	isHash isWeakref isScalar isInteger isSigned isText isSemantic //isSigned means plus/minus, not digsig.
	//semantic: URL, URN, contentType, etc, normally used with isText but could also be isInteger, such as "image/jpeg".
bit[4] numOfDims; //dim means dimension, 0-15 dimensions
bit[5] coreType;
	Examples: Import Nil Call Leaf MapEmpty MapSingle MapPair ListEmpty ListSingle ListPair
		TypedObject Tlapp P2pStreamChunkLikeForMmgmouseai (some are reserved for future expansion)
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
	
	static{
		todo("TODO get sCall working asap to make sure the logic is consistent, even though it probably is.");
		todo("TODO option (without paying much extra memory when its off, nor needing to recompile to change it)\r\n" 
			+"	for multiple groups of fulleconacyc, multiple groups of zapeconacyc, and single group of memlock,\r\n" 
			+"	and single group of expireTime as in HaltingDictator,\r\n"
			+"	and single group of topWallet as in HaltingDictator.");
		todo("TODO get java code for multiformatsVarint"
			+" Constructor will get array whose dims can be seen by Arrays.getLength and other reflect funcs,"
			+" but will need Data.*varint* funcs forconverting byte[] or InputStream to Funcer"
			+" such as the bytes viewing a database table of all the MapPairs,"
			+" and another table of all the MapSingles, etc.");
		todo("TODO optimize: find that thing I wrote in mindmap about how to do avl concat, prefix, and suffix in average log time but worst case might be logSquared, compared to how its done in wavetree which I'm unsure of the bigO.");
		todo("TODO in MemStat, memlock");
		todo("TODO in MemStat, stats on harddrive lag, capacity allocated, and same for peers on internet.");
		todo("TODO in MemStat, memory and harddrive and other parts of internet"
				+" should each have a peerId, and along with time, can use pair<peerId,time>"
				+" as name of econacycCost");
		}
	
	public static final int headerBitsForMask = 7;
	public static final int headerBitsForHowManyDims = 4;
	public static final int headerBitsForCoreType = 5;
	
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
	public static final short maskIsSemantic = (short)(1<<9);
	
	FIXME rename all *avl* coretype to *lazycat* types
	and optional-per-op enforce avl but keep enough info in each lazycatPair and lazycatSingle
	to do avl if you want to do avl. That info at least includes the size
	since we can (if find some ratio, maybe goldenRatio? what is it?)
	do avl rotation by size (number of keyvals) instead of height.
	
	/** the func that statelessly imports funcs, written as ??,
	like f(?? "plus" 3 4) returns 7, and f(f(?? "plus")#+ 3 f(+ 4 1)) returns 8.
	*/
	public static final int coretypeImport = 0;
	/** TODO find churchEncoding of nil as a lambda, which this is an optimization of */
	public static final int coretypeNil = 1;
	public static final int coretypeWithSalt = 2;
	/** Example: float[50][99937][20], or Double, or Long, or Integer, etc, with the type info
	in the short/16 firstHeader such as to say it isSemantic or not like semantic "image/jpeg".
	*/
	public static final int coretypeLeaf = 3;
	public static final int coretypeCall = 4;
	public static final int coretypeSCall = 5;
	public static final int coretypeConsPair = 6;
	public static final int coretypeMapEmpty = 7;
	public static final int coretypeMapSingle = 8;
	public static final int coretypeSMapSingle = 9;
	public static final int coretypeMapPair = 10;
	//public static final int coretypeSMapPair = 11;
	public static final int coretypeAvlListEmpty = 12;
	public static final int coretypeAvlListLeafSingle = 13;
	public static final int coretypeAvlListLeafArray = 14;
	//FIXME do I need coretypeSAvlListLeafSingle and coreTypeSAvlListLeafArray? Probably not, but verify.
	//public static final int coretypeSListSingle = 15;
	public static final int coretypeSLinkedListPair = 16; //the S form of coretypeConsPair
	public static final int coretypeAvlListPair = 17;
	//public static final int coretypeSAvlListPair = 18;
	/** 2 pointers (TODO also a salt?): type and value,
	though technically the whole thing is a Funcer like anythning else so is normally viewed
	as a value, it may be useful forExample to have a typedObject
	where the type is an isSemantic of "image/jpeg" and value to be an array of unsigned int1 or signed bytes etc.
	*/
	public static final int coretypeTypedObject = 19;
	public static final int coretypeSTypedObject = 20;
	/** Datastruct described in https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol
	which has single-line string key/value pairs then an empty line (delimiter is \r\n\r\n)
	then binary data. In the simplest case it needs the "HTTP/1.1 200 OK" first line
	and "Content-Type: application/json" then \r\n\r\n then the json,
	or "Content-Type: image/jpeg" then \r\n\r\n then the image bytes,
	The main reason I want the http datastruct is it has Content-Type and binary data.
	I'm not much interested in the other fields but I do want to leave it open
	for future expansion. A benefit of this is any response got from a http web call
	can be represented directly in occamsfuncer,
	though you might want to remove some of the headers
	such as "Server: Apache/1.3.3.7 (Unix) (Red-Hat/Linux)".
	<br><br>
	For help parsing this datastruct, you could use Occamserver or Apache HttpCore or Tomcat.
	<br><br>
	This is similar to coretypeTypedObject and coretypeSTypedObject
	except those are always of other funcers recursively
	and this is always a leaf of strings and binary data.
	<br><br>
	Even if you use some other network protocol, or if things are generated without networking,
	you can still use the http datastruct for a variety of things,
	such as a view of files on your harddrive or of a generated java.awt.Image or ogg sound file etc.
	<br><br>
	If you want just a bitstring or bytestring, use coretypeLeaf.
	Its recommended to make subranges of this coretypeHttpBytes's bits viewable as coretypeLeaf
	andOr the avl list of bits coretypes, or at least the content section (after the first \r\n\r\n)
	viewable as a separate binary object. I'm undecided on that optimization and datastruct design. 
	*/
	public static final int coretypeHttpBytes = 21;
	/** tlapp is Torrent Like Acyc Part Packet (in mindmap, basically a kind of compression (incomplete design)
	for merkle forest where can refer to other tlapps or leafs by noncompressed id at some crossSection).
	*/
	public static final int coretypeReserved19ForTlapp = 22;
	/** like mmgMouseai p2p streams, a chunk of data from it or from multiple of them,
	and part of that being a pointer at the name of the stream which may be any Funcer
	especially a publickey such as ed25519 but I dont want to hardcode the kind of digsig algorithm here
	nor to hardcode that its digsig based at all
	since it could be url based for efficiency or prppfOfWork based.
	*/
	public static final int coretypeReserved20ForStream = 23;
	public static final int coretypeReserved24 = 24;
	public static final int coretypeReserved25 = 25;
	public static final int coretypeReserved26 = 26;
	public static final int coretypeReserved27 = 27;
	public static final int coretypeReserved28 = 28;
	public static final int coretypeReserved29 = 29;
	public static final int coretypeReserved30 = 30;
	public static final int coretypeReserved31 = 31;
	
	
	/*Do I want a coretype for the p(...) syntax that does the same as the progn op?
			
	Do I want coretypes for occamsfuncerSyntax ab[cd].e.f[gh]=f(...)
	which is just a wrapper around getdeep and putdeep?
			
	Is it consistent design for there to be an S map?
	Is it consistent design for there to be an S avllist?
	In both of those, the internal structure returned differs from the order
	its added, but its ok for it to forkEdit from an emptyList to do that
	so would represent a sequence of mapputs and sequence of listputs
	but not a tree of those.
	So it could be done as 2 ops (1 for map and 1 for list)
	that takes a linkedlist of key val key val or for list just val val val.
	I could still have a syntax for that M(...) and A(...) (or whatever the syntax is),
	but it seems less needed now.
	...
	How about instead of that syntax for Smap and Savllist,
	a general syntax V(getfunc getparama getparamb getparamc...)
	that means F(getfunc L(getparama getparamb getparamc...))?
	*/
	
	public static int howManyDims(short firstHeader){
		//compiler will optimize to a >>> and an &
		return (firstHeader>>>headerBitsForCoreType)&((1<<headerBitsForHowManyDims)-1);
	}
	
	/** 0 to 31, one of the coreType* consts. */
	public static int coreType(short firstHeader){
		//compiler will optimize to a &
		return firstHeader&((1<<headerBitsForCoreType)-1);
	}
	
	/** https://github.com/multiformats/unsigned-varint
	The encoding is:
	unsigned integers are serialized 7 bits at a time, starting with the least significant bits
	the most significant bit (msb) in each output byte indicates if there is a continuation byte (msb = 1)
	there are no signed integers
	Examples:
	1     => 00000001
	127   => 01111111
	128   => 10000000 00000001
	255   => 11111111 00000001
	300   => 10101100 00000010
	16384 => 10000000 10000000 00000001
	*/
	
	/** https://github.com/multiformats/unsigned-varint
	FIXME test this.
	*/
	public static int varintCountBytes(byte[] b, int offset, int throwIfMoreThan){
		int i = 0;
		while(b[offset+i] < 0){
			i++;
			if(i >= throwIfMoreThan) throw new Error("more than "+throwIfMoreThan+" varint bytes");
		}
		return i+1;
	}
	
	/** throws if varint is more than 4 bytes of which only 28 bits are used so 256mbit.
	TODO varint31 instead of 28, allow fifth byte but not all possible values of it.
	FIXME test this.
	*
	public static int varint28(byte[] b, int offset){
		int ret = 0;
		int i = 0;
		do{
			byte by = b[offset+i];
			ret = (ret<<7)|(by&127);
			if(by >= 0) return ret;
			i++;
			if(i >= 4) throw new Error("more than 4 varint bytes");
		}while(true);
	}*/
	
	public static int varint31(byte[] b, int offset){
		//TODO optimize by using int instead of long and limit to 5 bytes and last byte cant be completely used
		long g = varint63(b,offset);
		if(g <= Integer.MAX_VALUE) return (int)g;
		throw new Error("bigger than fits in int31");
	}
	
	/** throws if varint is more than 9 bytes which is 63 bits at 7 bits each.
	FIXME test this.
	*/
	public static long varint63(byte[] b, int offset){
		long ret = 0;
		int i = 0;
		do{
			byte by = b[offset+i];
			ret |= ((by&127)<<(i*7));
			if(by >= 0) return ret;
			i++;
			if(i >= 9) throw new Error("more than 9 varint bytes");
		}while(true);
	}
	
	public static void writeVarint(long varint, OutputStream o){
		if(varint < 0) throw new Error("neg");
		boolean isLastByte;
		long nextVarint;
		do{
			nextVarint = varint>>>7;
			isLastByte = nextVarint == 0;
			try{
				o.write(isLastByte ? (byte)(varint&127) : (byte)(128|(varint&127)));
			}catch(IOException e){ throw new Error(e); }
			varint = nextVarint;
		}while(nextVarint != 0);
	}
	
	public static void main(String... args){
		int i = Rand.strongRand.nextInt()&Integer.MAX_VALUE;
		i >>>= Rand.strongRand.nextInt(31);
		
		//i = 300; //300   => 10101100 00000010 says https://github.com/multiformats/unsigned-varint
	
		//int i = Rand.strongRand.nextInt(128);
		//int i = 16384; //0b100000001000000000000001;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeVarint(i, out);
		byte[] b = out.toByteArray(); //copy
		int i2 = varint31(b,0);
		String debug = "i="+Text.intToPaddedBinString(i)+" i2="+Text.intToPaddedBinString(i2)
			+" array="+Text.bytesToBinString(b);
			//+" arrayBitsReversed="+Text.reverse(Text.bytesToBinString(b));
		if(i!=i2){
			//throw new Error("varint31 failed. i="+Integer.toHexString(i)+" i2="+Integer.toHexString(i2)
			//	+" array="+Text.bytesToHex(b));
			throw new Error("varint31 failed "+debug);
		}else{
			lg("pass "+debug);
		}
		lg("varint31 test pass");
	}
	
	/** writes 1-4 bytes *
	public static void writeVarint28(int varint, byte[] b, int offset){
		if(varint < 0) throw new Error("neg");
		int i=0;
		do{
			b[offset+i] = TODO;
			i++;
			TODO
		}while(true);
	}*/
	
	/** writes 1-5 bytes *
	public static void writeVarint31(int varint, byte[] b, int offset){
		if(varint < 0) throw new Error("neg");
		TODO
	}*/
	
	/** writes 1-9 bytes *
	public static void writeVarint31(long varint, byte[] b, int offset){
		if(varint < 0) throw new Error("neg");
		TODO
	}*/
	

}
