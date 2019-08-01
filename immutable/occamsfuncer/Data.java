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
	
	/*TODO everything is datastruct: type colon content.
	1 byte types are coretypes. All are ascii and printable.
	There will be 3 of those for: hash, weakrefOfHash, localId. All 3 of those are id192.
	The others are things like MapPair, MapSingle, Nil, Call, SCall, SLinkedList, TheImportFunc, etc.
	Only the Leaf coretype (including Num as an optimization of Leaf) has the varint array sizes etc.
	AvlPair and ListSingle have 1 byte to tell is all their contents the same type (such as float, int4, etc)
	and what is the log2 of its size, and has some (or all?) of the same masks as in Leaf.
	If I need room for expansion, I just use bigger types before the colon.
	If I want to define exactly which version/fork of occamsfuncer,
	I can prefix forExample: application/x-occamsfuncer-fork7128543112795615:h:...theHashBytes...
	TODO Im undecided if should use varint after each colon for the size remaining,
	which is still compatible with such prefixing.
	
	How about contenttypes that start with a Capital letter are nonstandard so dont have to waste data
	prefixing them with x-?
	Or can I afford to prefix with forexample x-h: which leaves only 160 bits of double-SHA256?
	How about types that dont have a slash are automatically localtypes?

	
	CANCEL[[[ TODO define at most 26 coretypes, including that 2 of them are: hash, localid.
	These are written as the 26 letters. Double that by isWeakref meaning capital/lowercase of those.
	Concat a colon for compatibility with application/x-occamsfuncer-fork7128543112795615 .
	All 52 of those 1-byte content-types are reserved for occamsfuncer internals.
	Anything bigger can be normal content-type such as "image/jpeg:...bytes of jpg file...".
	Hash will be [either "Z:" or "z:"] concat the last 176 bits of double-SHA256.
	Weakref is therefore a certain bit (is 0 if weakref), since capital+32=lowercase.
	WAIT... isLocal bit needs to branch the same as isweakref, so however many coretypes there are,
	needs to be 4 times that many.
	]]]
	
	
	
	
	
	
	
	
	
	
	TODO update code for:
		SOLUTION:
			First bit is isCoretypeOrHashElseContenttypethencolonthencontent.
			If 1 then its either a coretype or a hash of a coretype or a hash of a 
			contenttypethencolonthencontent.
			If 0 then its a contenttypethencolonthencontent.
			contenttypethencolonthencontent include forExample image/jpeg:...bytesOfJpgFile...
			and forExample merkleList:* aHashInBase64 anotherHashInBase64. In merkleList and
			the merkleChilds list in merkleJson, an id192 is always base64 (whose digits include _
					and $ and are in ascending ascii/utf8 order so can sort ids by their base64 
					and get the same sort as their binary form which is the same sort in the map
					coretypes). Contenttypethencolonthencontent literals, such as every word 
			in this sentence thats not length 32 or contains a non-base64-char (or maybe I'll
					make them start with an extra $ so anything that doesnt start with $ in
					merkleList is a literal.... YES, use the $ so every id is 33 base64 chars 
					and 192 bits. You cant have a weakref to a contenttypethencolonthencontent
					but you can have a weakref to any coretype which points at it, and you can 
					derive your own weakref object by multiple coretypes since a weakref just
					means a function that returns 1 if its param is that object else returns
					0 (for compatibility with goalfuncs)... actually I'll just define a new 
					coretype for weakref to contenttypethencolonthencontent, which has only
					1 pointer in it but is still less efficient than using the weakref bit
					to point at the same 191 other bits with the weakref bit  of 0.
		WAIT, theres also maskIsMerkle so theres 4 bits not 3: The other is maskIsMerkle,
		so hash will be last 188 bits of double-sha256.
		The 4 bit vars will be:
			isCoretypeOrHashElseContenttypethencolonthencontent
			maskIsMerkle
			maskIsHash
			maskIsWeakref
	*/	
			
	
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
	
	
	
	/*FIXME rename all *avl* coretype to *lazycat* types
	and optional-per-op enforce avl but keep enough info in each lazycatPair and lazycatSingle
	to do avl if you want to do avl. That info at least includes the size
	since we can (if find some ratio, maybe goldenRatio? what is it?)
	do avl rotation by size (number of keyvals) instead of height.
	*/
	
	
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
	
	/*public static int howManyDims(short firstHeader){
		//compiler will optimize to a >>> and an &
		return (firstHeader>>>headerBitsForCoreType)&((1<<headerBitsForHowManyDims)-1);
	}
	
	/** 0 to 31, one of the coreType* consts. *
	public static int coreType(short firstHeader){
		//compiler will optimize to a &
		return firstHeader&((1<<headerBitsForCoreType)-1);
	}*/
	
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
