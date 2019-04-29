package immutable.occamsfuncer;

/** Funcer.header (or maybe I'll call it Funcer.mask) stuff.
These generally apply in combos, like if its a 5d array of double,
thats a 6d array whose first dim is size 64 as the bits in each double,
so isScalar mask is true even though its a 5d/6d array of scalars.
isScalar would still be true if the first dim size was 32 (float32 vs float64
vs float256 etc, though not all types are supported everywhere).
*/
public class Mask{
	private Mask(){}
	
	public static final long hashMask = 1L<<63;
	
	public static final long weakrefMask = 1L<<62;
	
	/** hashname or literal, either way the Id fits in 192 bits *
	public static int id = 1<<31;
	
	/** If true, this is a func that returns 1/true if its param is the id/object except with its weakref bit as 0.
	If I'm a weakref, then the thing I'm a weakref to is (TODO???) stored in my L and same pointer in my R
	but... How would I update such pointers if they're not loaded yet?
	TODO
	FIXME
	TODO
	FIXME
	TODO
	FIXME
	It seems Funcer needs to be an interface instead of hardcoded with final vars,
	even though it will still act as immutable merkle node other than certain vars that dont affect merkle hash
	such as Funcer.isFrozen and Funcer.myId.
	*
	public static int weakref = 1<<30;

	public static int unsignedInteger = 1<<29;
	
	public static int signedInteger = 1<<28;
	
	public static int generalText = 1<<27;
	
	/*generalText //not to be further interpreted at this level
	semanticText //to be interpreted, such as a URN (maybe excluding the "urn:" prefix?)
	or contentType, maybe "urn:ct:application/json" etc but without the "urn:" prefix? No I dont
	want to walkOnEggshells trying to expand the urn namespace. Just mount the urn namespace at
	anything that starts with "urn:", and mount contentType as anything that stats with "image/"
	or "application/" etc, and "http:" and "https:" and "udp:" etc are URLs, and allow other prefixes,
	many of them shorter, to happen among opensource without such formality until it converges.
	*
	public static int semanticText = 1<<26;
	
	/*TODO some parts of types (such as avl treelist needs to know if its a treelist of all floats, all doubles,
	all semanticText(unicodeSymbols of some size)), etc...
	So may need 3 groups of mask for that, for L, for R, and for self,
	such as avlpair needs to know if L is interpreted as listItem, listInline, or listPair.
	Most of that can be stored in the int header in L and the one in R.
	Theres just a few small things that exist only in self, such as [listItem, listInline, or listPair].
	*/
	
	//TODO https://b.com/multiformats/unsigned-varint will be part of merkle datastruct.

}
