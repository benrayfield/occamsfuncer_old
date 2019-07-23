/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import static immutable.occamsfuncer.ImportStatic.*;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import immutable.occamsfuncer.funcers.Import;
import mutable.occamsfuncer.memstat.MemStat;

/*IMPORTANT!!!!!!!!!!!!
I am going to start using merkleforest soon, today is 2019-4-10 (if can get few parts built).
What was holding me back is I felt I needed to decide on all the idlits
(inline data in ids, such as float[5] or float[0] or double[2] or double)
To get started, I just need these kinds of ids [hashname, idlitDouble, idlitContentType,
idlitContentTypeOfBiggerContentTypeStoredInContent] and to leave space for expansion
within that 191 bit idlit space. The first bit will be 1 if its a hashname. If 0 its an idlit.
and to choose the contentTypes of all the n dimensional primitive arrays and of treemapnode.
Also I'll soon want lazycatnode so I can do avl treelists but I'm undecided if I'll allow
unbalanced shapes of them and exclude the avl balance var(s) or make it avl specific.
I can add treelists later. What I need most now (among the research)
is treemaps and primitive n dimensional arrays.
I want to store float[][]s in maps which have utf8[] keys, representing the vars in
recurrentjava's lstm neuralnet and various research processes I'll build around it.
...
Do I want both byte and utf8Byte types?
Do I want both long[] as bit array and long[] as long array?
A consistent design is to use long[] as bit array and use 0-63 bits of last long padding a 1 bit where it ends,
and use int float double as they are and for byte always to mean utf8 byte.
Do I also want a type for (vaguely you could see it this way)executable text such as urns, contentTypes,
strings that start with "java:" or "javascript:" or "opencl:" etc (would still sandbox, not run just any code requested), etc?
That would likely be better done as an execBit (in some mask of bits).
A hashname is executable since it refers to the object hashed instead of itself.
...
Trying to design header bits[
	1 isHash //Theres only 1 kind of hash algorithm per set of compatible data, though different p2p nets will evolve with different hash algorithms.
	1 isExec //true if isHash or if [isText and the text is to be interpreted as contentType or urn etc, but not occamsfuncerFuncall which is higher level cuz its hash isExec but the content is id func id param (and few other things) which is executable at a higher level]
	1 isText //true if the first dim is interpreted as some size of unicode (utf8 utf16 utf20 utf32(utf20 with 12 high 0s) etc).
	1 isScalar //There are types already defined by https://en.wikipedia.org/wiki/IEEE_754 for float32 up to float256 at least.
	1 isSignedInteger
	1 isUnsignedInteger
	4 howManyDims //4 bits for 0..15 dims. float[][][][][] is 6 dims (first is 32 bits of the float itself), and float is 1 dim.
	
	
	FIXME what size is howManyDims? Must specify dim sizes where? At least the first of them (such as 64 bits for double)
	If I made ids be 256 bits then all first array sizes would be a powOf2, but I probably dont want to pay that.
	
	How about https://b.com/multiformats/unsigned-varint for size headers?
	https://golang.org/srgithuc/encoding/binary/varint.go [
		// PutUvarint encodes a uint64 into buf and returns the number of bytes written.
		// If the buffer is too small, PutUvarint will panic.
		func PutUvarint(buf []byte, x uint64) int {
			i := 0
			for x >= 0x80 {
				buf[i] = byte(x) | 0x80
				x >>= 7
				i++
			}
			buf[i] = byte(x)
			return i + 1
		}
	]
	
	I want those header bits squashed into 4 bits, then use the other 4 bits for num of dims, then that many unsigned-varints, then the data.
	For example, a lone double [64] is 10 bytes, and an array of 5 floats [32][5] is 23 bytes and a float[1000][1000] is 4000006 bytes.
]
...
Trying again to create header[
	4 first4Bits are any of these 16 things[
		hashnameStrongref
		hashnameWeakref //Function of object to bit, returns 1/true if param is that object, else returns 0/false. See Weakref.java
		scalar
		unsignedInteger //bit is an example of this
		signedInteger
		generalText //not to be further interpreted at this level
		semanticText //to be interpreted, such as a URN (maybe excluding the "urn:" prefix?) or contentType, maybe "urn:ct:application/json" etc but without the "urn:" prefix? No I dont want to walkOnEggshells trying to expand the urn namespace. Just mount the urn namespace at anything that starts with "urn:", and mount contentType as anything that stats with "image/" or "application/" etc, and "http:" and "https:" and "udp:" etc are URLs, and allow other prefixes, many of them shorter, to happen among opensource without such formality until it converges.
		lparen //lparan and rparan are for if I want to define content to be hashed as a list (or in some cases lists inside lists)) of these general object types. But maybe Id like bsonlike pointers for efficiency, to know where to jump to?
		rparen
		pairWithSizes //After this (TODO dont interfere with howManyDims and it listing the dim sizes) somewhere there will be 2 https://github.com/multiformats/unsigned-varint numbers that are the sizes of my 2 childs. This is the "bsonlike pointers for efficiency" lacking in lparan and rparen.
		(the rest of the 16 things are reserved for future expansion)
	]
	4 howManyDims  //4 bits for 0..15 dims. float[][][][][] is 6 dims (first is 32 bits of the float itself), and float is 1 dim.
	//If its a hashname* then still need the next byte to say its first dimension is 174 more bits (22 bytes after 2 byte header).
]
FIXME hashname* fills the rest of 24 bytes so size header wont fit in 1 varint byte. Would cost 2. I dont know if I'm willing to pay that.
160 bits isnt considered a secureHash, though I'm sure I could make a secureHash thats 160 bits it would cost too much compute cycles.
Maybe I should go up to 256 bit ids? No cuz 192 bits is well aligned to having up to 64 bit header and a powOf2 size 128 remains.
I wouldnt want to often waste 128 bit header.
SOLUTION: First 2 bits are isHashname and isWeakref. If isHashname, then the next 190 bits are the last 190 bits of the hash.
...
I dont (UPDATE: I do) trust sha256 to be secure in 190 bits, even with the concat(input,reverseInputPerBytePlusByteIndex).
Consider doing some of the more secure kinds of hashing that I know how to design but select from them something fast.
Wait, sha256 is probably ok (with that preprocessing of input) cuz of the expanding from 16 to 64 cycles cuz that wont let the 256 bit state converge to less than 2^256 possible outputs. Sha256 can output I'd guess 2^230 possible values.
Change my hash preprocessing to concat(input,inputPlusByteIndexPerByte) since reversing the second copy of input only slightly disadvantages palindromes.
...
I want the first byte to be "`" (or which symbol?) if its a pair, similar to unlambda syntax except it doesnt mean funcall.
...
avlnodes (lazycatnode*) will know the type of their listitems (including inside nodes wrapping arrays vs listsingle which would wrap an array (or anything else) as 1 object), and if they're not all the same object type then generalize by saying its a list of objects.
...
public class Leaf<T>{
	
	/** tells if its generalText, semanticText, scalar, signedInteger, unsignedInteger, etc. *
	public final byte header;
	
	/** Examples: double, float[][][][][], int[][] *
	public final T val;
}
...
consPair
sFuncallPair (aka sCurryPair)
funcallPair
avlPair //4 kinds or maybe 2 bits data. data only in leafs.
mapPair //4 kinds or maybe 2 bits data. data only in leafs.
...
mapSingle
mapEmpty
listInline //Example: wrap a Leaf<float[]> to use all those floats as list items
listSingle
listEmpty
...
And I may need some datastruct for the ` op which does var size currying such as <<`<` `> funcOf3Params a b c> waits for the c as <<`<` `> funcOf3Params a> before returning eval of <funcOf3Params <<`<` `> funcOf3Params a b c>>. <`<` `> is written as ```.
Also remember theres func caching of [func param return] by java== so the S calls wont expand exponentially.
...
Are avl pre, suf, and cat bigO of log? Or log squared? Or what? It was slower than I expected in wavetree, so why?
Insert, delete, and search cost log.
Cat (of any 2 forests even of very different (by at most log) heights)
costs log if define cat recursively and only cat things that are already balanced.
Pre and suf each call at most 1 pre or suf then call at most 1 cat, so at worst its log squared.
Pre and suf cost log squared in worst case.
Can I make the worst case faster than log squared?
INSERT: log
DELETE: log
REPLACE: log //for example, in the list of LearningVec (the thin vertical lines showing how RbmEditor/Slide is learning), replace a LearningVec with a forkEdit of itself, where itself is a map.
SEARCH: log
CAT: log
PRE: (UPDATE: average log, worse case log^2) log^2 FIXME CAN PRE AND SUF BE DONE IN LOG WORSTCASE? How about put the at most log number of disconnected parts in an array and cat them? Wait thats still log^2. I think it can be done faster...
SUF: (UPDATE: average log, worse case log^2) log^2
...
faster PRE:
Example: Start at root. param is in my right child, so put left child in thingsToCat. Recurse into right child. Its param is in its right child so put its left in thingsToCat (now size 2). Recurse into its right. Its param is in its left child so dont put anything more in thingsToCat now. Recurse into left child. Its param is in its right child so put left in thingsToCat (now size 3) and recurse into right. Right is a leaf and is less than param (where param is adjusted in each recursion since each child is a selfcontained avl treelist) so put right in thingsToCat. Now we have about log/2 number of things in thingsToCat in (approx or exact?) nonincreasing heights. How to cat those efficiently? It will tend to cost less if cat the 2 on the right of thingsToCat first, then cat the third from the right with that, and so on. But whats its bigO? Appears to be AVERAGE of log and WORSTCASE of log^2. Ok I can live with that. If do it this way, and maybe wavetree did it differently (didnt think this deep into it when coding wavetree but might have done this by accident anyways?).
...
Also I want flatten func of treelist, such as append a bunch of small float[]s and floats then flatten to a single float[], using System.arraycopy, so can opencl parallel andOr javassist loop through it efficiently. Such as how to generate int[width*height] pixels or int[][] pixels.
...
avlPair must have 9 kinds instead of 4. 3x3 kinds. The 3 kinds (of leftChild and separately rightChild)
are: listItem, listInline, listPair(aka avlPair).
Compare that to just the 2 kinds in mapPair (per leftChild and rightChild separately): mapItem, mapPair.
listSingle has 2 kinds: listItem and listInline.
A leaf thats an array, such as float[], by itself is not a list, since we wouldnt know if its a list size 1 (of itself) or if its a list of its contents. Its an array, not a list.
So there are a few core types: map, list, leaf(including single primitives (double is array of 64 bits) and n dimensional arrays),
and there are the *pair types:
consPair
sFuncallPair (aka sCurryPair)
funcallPair
avlPair //9 kinds
mapPair //4 kinds
mapSingle //1 kind
listSingle //2 kinds
mapEmpty
listEmpty
...
Fields (not shared by all such types but I'm trying to find patterns in the design I want):
enumOrMaybeGeneralizeToId whichStandardDatastruct OR MAYBE THIS SHOULD SPECIFY avlPair mapPair mapSingle listSingle etc...
id L //left child
id R //right child
id minKey
id maxKey
enum LContext //maplistItem, listInline, maplistPair... todo are there more contexts to include in this enum?
enum RContext
long maplistSize
byte cur //as in Funcall.java, number of more params before execute
enum3(avlLeftIs1Taller,avlLeftAndRightAreEqualHeight,avlRightIs1Taller) avlBalance
bitstring leafHeaderAndContent //only if leaf. THIS INCLUDES IS IT MEANT AS unsignedInteger, generalText, semanticText, scalar, (ndimen.. of that etc)
//Also need optimizations, potentially mutable vars, for Funcall.leftmostOp, double zapeconacyc, double fulleconacyc, bit memlock, etc
It appears that 5 pointers and an int mask would handle all of that, optimizable by a switch statement(s) on part of the mask,
but I wouldnt want to use 4 null pointers for a leaf whose other pointer is to a double float[][][][][] etc???
On the other hand, cpu caching loads memory in blocks such as 128 bytes on some kinds of hardware, so loading 5 final pointers and an int
(and whatever object overhead java adds such as pointer counting, excluding nulls) probably isnt a big deal,
as long as dont hash 192 bits for each of those not used. Storing a long size in a leaf is still wasteful,
but remember that would only be in memory not the data format of the merkle forest which goes across network and is stored.
Wont store the https://github.com/multiformats/unsigned-varint in java memory but it will be in the merkle datastruct
cuz in java memory you know how many dims a float[][][] has by its class, and you'll also be able to see how many dims
it has from the int mask/header which is in java memory, and you can ask for example theFloat[0][0].length or theFloat.length.
...
*/
public interface Funcer<LeafType> extends Comparable<Funcer>{
	
	/** This is the ONLY mutable part of Funcer.
	MUTABLE memory statistics, especially fullEconacyc, zapeconacyc,
	and maybe timing and storage capacity of harddrive, internet storage of Funcers,
	and maybe memlock (keep in memory not harddrive), etc,
	and maybe multiple (small number of) groups of each of those.
	This is the only mutable part of a Funcer.
	To save mem, it doesnt know which Funcer its the stat of and instead
	takes Funcer as param to update the stats, and Funcer knows its MemStat,
	even though Funcer is immutable in every other way except pointing at a MemStat.
	Since kinds of stats vary in efficiency (like zapeconacyc requires a ChanceTree
	of reverse pointers per Funcer), MemStat will have various subclasses).
	Stats on compute cycles are done in HaltingDictator (TODO move it into mutable package).
	*/
	public MemStat mem();
	
	public default Funcer f(Funcer param){
		return HaltingDictator.topIsStrict ? fStrict(param) : fNonstrict(param);
	}
	
	/** this as lambda function. Examples: the s and k Opcodes, and a Map takes key as param
	and returns value else returns nil if not found, and a leaf ignores its param and returns itself,
	and an avl-list takes list index as key and returns value at that index.
	<br><br>
	Lambda, except limited recursively by allowed compute and memory as defined
	in HaltingDictator.topWallet etc.
	*/
	public Funcer fNonstrict(Funcer param);
	
	/** the strict mode form of f(Funcer). It must use strictfp java keyword (if implemented in java,
	else the same strictness of IEEE754 scalar math however you implement it.
	The strictness doesnt end at roundoff. Theres also order of ops such as different
	orders of adding float32s give slightly different results
	and could be normed either by always summing them sequentially or always in groups of powOf2 size
	recursively log depth. You cant call java.lang.StrictMath.sin(double) unless there are testcases
	in occamsfuncer that run at boot time that specify exactly which bits of double that func will do.
	I'm not confident in opencl's strict option which I've read varies on some systems.
	*/
	public Funcer fStrict(Funcer param);
	
	public default Funcer f(Object param){
		return f(wr(param));
	}
	
	public default Funcer fStrict(Object param){
		return fStrict(wr(param));
	}
	
	/** 0-15 dimensions. Everything is 0 dimensional except Leaf (and maybe ListLeafArray?).
	Example of 4d: wrapper of float[55][99937][20], which has a [32] last dim since float is 32 bits.
	*/
	public default int dims(){
		return Data.howManyDims(firstHeader());
	}
	
	/** 0 <= dimIndex() < dims().
	After firstHeader which is 16 bits, theres multiformatsVarint per dim, then the content which
	is as many bits as the multiply of all those dim sizes
	(TODO rounded up to the next byte if not a multiple of 8?), aka a tensor.
	*/
	public int dimSize(int dimIndex);

	/** for merkle hashing into my id, write the content to be hashed,
	just my few local vars, not recursively into other Funcers I can reach but include their ids.
	If this is an array (such as subclasses of AbstractArray), after header is the whole array
	but first must find if it fits in int[] (regardless if its double[] float[] or int[])
	and put it as int[] if so, else if it fits in float[] then put it as float[], else as double[].
	Thats so at a math abstraction level only has to use id, double, array of double, and map,
	while optimizing things to actually use int[] and float[] such as float[] is best in opencl.
	<br><br>
	This is not just the content to hash. Its the content to store as that hash'es value.
	*/
	public void content(OutputStream out);
	
	/** how many bytes will content(OutputStream) write? Bigger maps and lists than fit in int
	are made of multiple tiny objects, sometimes bigger leafs but never bigger than fits in int.
	*/
	public int contentLen();
	
	
	//OLD: often doesnt use the whole int. includes LContext RContext avlBalance etc.
	/** this is the first 16 bits of header, before the multiformatsVarints and content,
	as described in Data class comment.
	*/
	public short firstHeader();
	
	public long maplistSize();
	
	/** Same as bh(2). This generates on observe (lazyEval) unless LDeepIndex()==2. See comment in bh(int).
	For efficiency, LDeep() and RDeep() are used far more often than L() and R().
	*/
	public Funcer L();
	
	/** Same as bh(3). This generates on observe (lazyEval) unless RDeepIndex()==3. See comment in bh(int).
	For efficiency, LDeep() and RDeep() are used far more often than L() and R().
	*/
	public Funcer R();
	
	/** optimization of bh(LDeepIndex()). See comment in bh(int).
	For efficiency, LDeep() and RDeep() are used far more often than L() and R().
	*/
	public Funcer LDeep();
	
	/** optimization of bh(RDeepIndex()). See comment in bh(int).
	For efficiency, LDeep() and RDeep() are used far more often than L() and R().
	*/
	public Funcer RDeep();
	
	/**  See comment in bh(int). */
	public default int LDeepIndex(){ return 2; }
	
	/**  See comment in bh(int). */
	public default int RDeepIndex(){ return 3; }
	
	/** expanded view that bh(int) sees. For example, bh(0b10) returns expand().L(), and bh(0b11) returns ewxpand().R(), and deeper.
	expand(0b1) returns this. If LDeepIndex()==2 && RDeepIndex()==3 then always returns this.
	*/  
	public default Funcer expand(){ return this; }
	
	/** Each node stores 2 main childs, but they're not always the direct L and R.
	For example, a conspair represents f(cons itsLDeep itsRDeep) aka f(f(cons itsLDeep) itsRDeep)
	so R is itsRDeep, but L is f(cons itsLDeep) so would generate that when observed.
	LDeep() returns itsLDeep(). without creating f(cons itsLDeep)
	since itsLDeep and itsRDeep are stored directly but L and R are normally not stored directly.
	There are more complex structures such as MapPair stores minKey, maxKey, minChild, maxChild, and long size.
	Its minChild and maxChild are its LDeep and RDeep. All are reachable by bh(int)
	since (TODO) every node type will be represented as a forest of Call but only when observed
	as it will actually be storing mainly LDeep and RDeep and in some node types a few extra things.
	<br><br>
	get L R R L L... by BinHeapIndex (bh),
	like THIS is 1, L is 2, R is 3, LL is 4, LR is 5, RL is 6, and so on,
	similar to the binheap indexing in Urbit but limited to int.
	The default implementation is slow compared to that subclasses should probably override this.
	<br><br>
	occamsfuncerRedesignToMakeSLinkedListEtcConsistentAndUrbitlike.
	QUOTE Replace L and R with int binheap indexing, and have 2 const ints telling
	which are actually stored per sCallType vs are generated when observed.
	Make absolutely everything Call. Like mappair is a call on 2 child's.
	But store same way. Is minKey func consistent with that? Binheap indexing
	like in urbit but only int not caring (FIXME spellcorrectedwrong).
	Also implement urbit completely as a coretype(s) andor ops (???), bit level
	urbit compatibility since urbit is similar and pure deterministic and has sk
	or at least something very similar and is all pairs and integers. I therefore
	gain existing urbit software and keep my software which if nonstrict allows
	roundoff and better optimizations. I'm undecided if I want urbit ops but I do
	at least want the binheap indexing instead of L and R and for each node to say
	which 2 ints (2 is L and 3 is R and 1 is THIS, etc) are the 2 child pointers it
	actually stores vs would have to generate others.
	UNQUOTE
	*/
	public default Funcer bh(int binheapIndex){
		if(binheapIndex == 1) return this;
		if(binheapIndex == LDeepIndex()) return LDeep();
		if(binheapIndex == RDeepIndex()) return RDeep();
		if(binheapIndex < 1) throw new Error("binheapIndex="+binheapIndex);
		TODO use expand() and recurse into bh since once expanded it wont expand the same part again
		and will return a this.
		
		
		/*if(binheapIndex == 1) return this;
		if(binheapIndex == LDeepIndex()) return LDeep();
		if(binheapIndex == RDeepIndex()) return RDeep();
		if(binheapIndex < 1) throw new Error("binheapIndex="+binheapIndex);
		if((binheapIndex&2)==0){
			return L().bh(binheapIndex>>1);
		}else{
			return R().bh(binheapIndex>>1);
		}
		*/
		
		FIXME theres an extra 1 as high bit which this shouldnt be recursing but is correctly part of binheapIndex.
		FIXME test this with a few recursions
	}
	
	
	public default Funcer<LeafType> setSalt(Object salt){
		return setSalt(wr(salt));
	}
	
	/** in a map, the min key is minKey().id(). Everywhere else this does HaltingDictator.evalInfiniteLoop() */
	public Funcer minKey();
	
	/** Since each MapPair, MapSingle, and MapEmpty (and AvlList*) knows its maplistSize(),
	its nth key binarySearchable like a treelist's literal (value of key, not nth key) keys.
	*/
	public default Funcer nthKey(long n){
		long siz = maplistSize();
		if(n < 0 || siz <= n) return nil;
		Funcer L = L(), R = R();
		long lSiz = L.maplistSize();
		return n<lSiz ? L.nthKey(n) : R.nthKey(n-lSiz);
	}
	
	/** in a map, the max key is maxKey().id(). Everywhere else this does HaltingDictator.evalInfiniteLoop() */
	public Funcer maxKey();
	
	/** leaf value. Examples: float[][][][][], Double, int[][],
	byte[] (could be generalText or semanticText, depending on short firstHeader()).
	*/
	public LeafType v();
	
	/** same as ((Number)v()).doubleValue() except if its not a Number (such as Double or Integer) then returns 0.
	This should usually be overridden for efficiency.
	*/
	public default double d(){
		LeafType leaf = v();
		return (leaf instanceof Number) ? ((Number)leaf).doubleValue() : 0.;
		//TODO optimize using a Num class which has a double field so dont use java.lang.Double and checking its type?
		//and other kinds of leafs (such as float[][][][][] or Long) could go in Leaf.java.
	}
	
	public default int i(){
		return (int)d();
	}
	
	public default float f(){
		return (float)d();
	}
	
	/** the op right after theImportFunc,
	or theres a constant op for map, for list, and a few other things.
	*/
	public Opcode leftmostOp();
	
	/** the "?" in <? "plus" 3 4> which returns 7.
	In <? "plus"> and <? "plus" 3>, leftmostOp is plus (TODO whats its exact name?)
	*
	public default boolean isTheImportFunc(){ return this==TheImportFunc.instance; }
	*/
	
	/** number of curries remaini`ng before eval (max 15). TODO max 14 and use 15 to mean will never eval? */
	public byte cur();
	
	/** may trigger lazyEval of hashing many Funcers below to get their ids, but will remember Id of each after that */
	public Id id();
	
	/** Even if this is a weakref, its content still fits in id
	since its a func that recognizes (returns 1 else 0 if) if its param is
	that id with the weakref bit 0 (recognizes the non-weakref form of itself).
	*/
	public default boolean contentFitsInId(){
		return (firstHeader()&Data.maskIsHash)!=0;
	}
	
	/** Mask.weakrefMask */
	public default boolean isWeakref(){
		return (firstHeader()&Data.maskIsWeakref)!=0;
	}
	
	/** If this Funcer wraps another Funcer and duplicates its every function call,
	such as Stub.java may download a Funcer from harddrive or Internet using a Supplier<Funcer>,
	then returns the wrapped Funcer in case you want to use it directly instead of
	less efficiently continue using it through this wrapper (if this is such a wrapper).
	If this is not such a wrapper, returns this.
	Either way, this.x().equals(this.unstub().x()) for any func x in the Funcer interface
	but not all funcs in java.lang.Object such as getClass(). 
	<br><br>
	Such downloading etc must $(...) (pay to HaltingDictator)
	like anything else so if it takes longer than allowed
	(counting compute cycles wasted while waiting on it, unless somehow redesigned to be async)
	then must end the usual HaltingDictator way, so be careful to follow that spec
	when creating Supplier<Funcer> to go in Stub constructor.
	<br><br>
	Async will be supported but not through stubs, such as auto loading the Funcers expected to be used
	in the next few seconds, not as stubs but as fully loaded, except maybe some of the bigger array leafs
	would be stubs.
	*/
	public Funcer unstub();
	
	/** PREfix eXclusive. submap or sublist(depending on param viewed as number).
	TODO only grab prefix/suffix in balanced treelist (treemap is log) in ave log time but worst case log^2.
	<br><br>	
	BIG-O:
	Are avl pre, suf, and cat bigO of log? Or log squared? Or what? It was slower than I expected in wavetree, so why?
	Insert, delete, and search cost log.
	Cat (of any 2 forests even of very different (by at most log) heights)
	costs log if define cat recursively and only cat things that are already balanced.
	Pre and suf each call at most 1 pre or suf then call at most 1 cat, so at worst its log squared.
	Pre and suf cost log squared in worst case.
	Can I make the worst case faster than log squared?
	INSERT: log
	DELETE: log
	REPLACE: log //for example, in the list of LearningVec (the thin vertical lines showing how
	RbmEditor/Slide is learning), replace a LearningVec with a forkEdit of itself, where itself is a map.
	SEARCH: log
	CAT: log
	PRE: (UPDATE: average log, worse case log^2) log^2 FIXME CAN PRE AND SUF BE DONE IN LOG
	WORSTCASE? How about put the at most log number of disconnected parts in an array and
	cat them? Wait thats still log^2. I think it can be done faster...
	SUF: (UPDATE: average log, worse case log^2) log^2
	...
	faster PRE:
	Example: Start at root. param is in my right child, so put left child in thingsToCat.
	Recurse into right child. Its param is in its right child so put its left in thingsToCat
	(now size 2). Recurse into its right. Its param is in its left child so dont put
	anything more in thingsToCat now. Recurse into left child. Its param is in its right
	child so put left in thingsToCat (now size 3) and recurse into right. Right is a leaf
	and is less than param (where param is adjusted in each recursion since each child is
	a selfcontained avl treelist) so put right in thingsToCat. Now we have about log/2 number
	of things in thingsToCat in (approx or exact?) nonincreasing heights. How to cat those
	efficiently? It will tend to cost less if cat the 2 on the right of thingsToCat first,
	then cat the third from the right with that, and so on. But whats its bigO? Appears to
	be AVERAGE of log and WORSTCASE of log^2. Ok I can live with that. If do it this way,
	and maybe wavetree did it differently (didnt think this deep into it when coding wavetree
	but might have done this by accident anyways?).
	*/
	public Funcer prex(Funcer startExcl);
	
	/** PREfix eXclusive */
	public default Funcer prex(Object startExcl){
		return prex(wr(startExcl));
	}
	
	/** SUFfix eXclusive. submap or sublist(depending on param viewed as number).
	TODO only grab prefix/suffix in balanced treelist (treemap is log) in ave log time but worst case log^2.
	*/
	public Funcer sufx(Funcer endExcl);
	
	/** SUFfix eXclusive */
	public default Funcer sufx(Object endExcl){
		return sufx(wr(endExcl));
	}
	
	/** PREfix Inclusive. TODO only grab prefix/suffix in balanced treelist (treemap is log) in ave log time but worst case log^2.
	TODO only grab prefix/suffix in balanced treelist (treemap is log) in ave log time but worst case log^2.
	*/
	public Funcer prei(Funcer endIncl);
	
	/** PREfix Inclusive. */
	public default Funcer prei(Object endIncl){
		return prei(wr(endIncl));
	}
	
	/** SUFfix Inclusive. submap or sublist(depending on param viewed as number).
	TODO only grab prefix/suffix in balanced treelist (treemap is log) in ave log time but worst case log^2.
	*/
	public Funcer sufi(Funcer startIncl);
	
	/** SUFfix Inclusive. */
	public default Funcer sufi(Object startIncl){
		return sufi(wr(startIncl));
	}
		
	
	/** PREfix eXclusive. avl list prefix, else throws HaltingDictator.throwMe if not an avl list.
	TODO only grab prefix/suffix in balanced treelist (treemap is log) in ave log time but worst case log^2.
	*/
	public Funcer prex(long endExcl);
	
	/** SUFfix eXclusive. avl list suffix, else throws HaltingDictator.throwMe if not an avl list.
	TODO only grab prefix/suffix in balanced treelist (treemap is log) in ave log time but worst case log^2.
	*/
	public Funcer sufx(long endExcl);
	
	/** conCAT Object. this avl treelist with param viewed as 1 list item, else throws HaltingDictator.throwMe if this is not an avl treelist
	TODO only cat balanced treelist to balanced treelist, for log cost.
	*/
	public Funcer cato(Funcer itemSuffix);
	
	/** conCAT Object. */
	public default Funcer cato(Object itemSuffix){
		return cato(wr(itemSuffix));
	}
	
	/** conCAT N objects. concat this avl treelist with contents of param avl treelist, else throws HaltingDictator.throwMe if either is not an avl treelist.
	TODO only cat balanced treelist to balanced treelist, for log cost.
	*/
	public Funcer catn(Funcer listSuffix);
	
	/** conCAT N objects. */
	public default Funcer catn(Object listSuffix){
		return catn(wr(listSuffix));
	}
	
	/** If this is an avl treelist whose contents are all primitives, flattens into a single array,
	such as opencl or javassist might efficiently use, but whats returned is not efficiently forkEditable.
	If this is not such a treelist, returns this.
	*/
	public Funcer flat();
	
	public Funcer put(Funcer key, Funcer value);
	
	public default Funcer put(Object key, Object value){
		return put(wr(key),wr(value));
	}
	
	public Funcer get(Funcer key);
	
	public default Funcer get(long key){
		return get(wr(key));
	}
	
	public default Funcer get(int key){
		return get((long)key);
	}
	
	/** automaticly wraps */
	public default Funcer get(Object key){
		return get(wr(key));
	}
	
	/** add at end of list, if its a list, else TODO what? */
	public default Funcer push(Funcer value){
		throw new Error("TODO");
	}
	
	public default Funcer push(Object value){
		return push(wr(value));
	}
	
	/*
	//Use long minExpireTime, SwarmPointer, etc. Local harddrive and local memory are 2 different peers
	//so locally theres 2 minExpireTimes (2 longs) per Funcer.
	OLD:
	This is not an occamsfuncer Weakref, which has nothing to do with java garbcol.
	occamsfuncer Weakref is usable at user level. Java javaWeakReferenceToMe is inside occamsfuncer VM. 
	<br><br>
	May return the same WeakReference in multiple calls, best if it does,
	but this shouldnt be allocated until first called since its (so far) only for zapeconacyc
	which is log*objects (continuously log more every few objects created)
	compared to fulleconacyc costs the number of objects squared, scanning all the objects again
	after every new object is created. zapeconacyc needs a chancetree of reverse pointers from
	each object so can select a random reverse pointer from any object,
	starting at a random bit of memory (a chancetree containing all objects,
	weighted by local memory used) and going a random path upward on reverse pointers,
	updating statistical vars along the way, so given any of a few potential root states
	you are considering using next, you can choose one that uses less memory
	andOr look recursively to find approximately if you should branch L() or R()
	to find the big memory you might want to forkEdit to not include.	
	*
	public WeakReference<Funcer<LeafType>> vmInternal_javaWeakReferenceToMe();
	*/
	
	/** If garbcol (garbage collection) is done by expire time,
	ignores else increases that time recursively if enough resources in HaltingDictator.
	The other kinds of garbcol are zapeconacyc and fullEconacyc,
	or you may use multiple kinds at once. HaltingDictator.topWallet is
	for (TODO a freemarket trading ratio of, or maybe there should be a separate
	HaltingDictator.topComputeCycles and .topMemAlloc)
	simple compute cycles and memory allocation limiting.
	<br><br>
	Recursively raises all Funcers reachable from here to expire time at least the param,
	paying to HaltingDictator.topWallet else throwing. The param is normally
	(TODO should it not be a param at all?) HaltingDictator.topExpireTime.
	This is the local memory version of SwarmPointer which allows the same thing,
	though with more gametheory, to happen across potentially billions of computers
	at gaming-low-lag.
	*/
	public default void liveLonger(long atLeastUntil){
		throw new Error("TODO");
	}

}
