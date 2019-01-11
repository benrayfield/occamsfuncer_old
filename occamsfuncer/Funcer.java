/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer;

/** The core object type. Immutable, except wallet is the only stateful part.
Similar to Continuations (like in Scheme) except the namespace is just another
object in the system, a treemap/treelist, and the only control-flow is
by the S (Lx.Ly.Lz.xz(yz)) and K (Lx.Ly.x) lambdas and optimized combos of them
and certain treemap/treelist ops and 1d-kernel optimizations (like in opencl).
Its a sandbox where no part can hurt any other part since its all immutable,
except by garbageCollection (garbcol) if theres no pointer to something.
This will allow millions of people to build and play together at gaming-low-lag
safe from unwanted interference and only opt-in per-object interference
in immutable/stateless ways (except wallet is the only stateful part).
<br><br>
I'm going to build some simple games to demo this, which are openended
in ways hard to imagine that take full advantage of the power of computers,
and inside those games you can play as usual
(such as with a computer game controller, mouse, or keyboard)
or drag-and-drop function onto function to create function (as lambdas normally do)
to forkEdit the game, to create new openended varieties of games etc while playing them,
openended without limit. For example, you and friends might start by playing with
some very simple game and end up making the next social networking platform
or number crunching AI software or paint program or whatever you imagine together,
all without leaving the game, and you can send such functions to eachother
like icons on a desktop emailed or chatted to eachother,
and they're all safe cuz the sandbox is global and still
blocked from the private parts of your computers
and limited in how much compute cycles and memory they can consume.
<br><br>
As a function, it takes 1 function param and returns 1 function.
A function is made of funcalls (a call of func on another func as its param that returns a func),
and some of those are listmaps which dont do much interesting except when
used with listmap ops such as concat and subrange and get and put ops.
The listmaps can do normal maps, sparse or dense lists, and combos of those.
They are avl-like treemap/treelist. Avl-like cuz it doesnt store height (which avl does)
and instead does tree rotation using log(size) as estimation of height
or (TODO) some similar view of size. Max possible list index < min possible map index.
List index is stored as long, but since occamsfuncer only has object and double types
(except will probably allow float[] for opencl optimizations, later copied to doubles),
casts that to double at user level, so you only get up to 2^53 list index,
and maybe a little less if you need to add list indexs.
<br><br>
Its generic K and V are list/map keys and values.
K and V are not param and return type. Those are always Fn in general.
The K is sometimes used as Long for lists, but other than that,
the only purpose of K and V is if you want to use these forkEditable lists/maps
outside occamsfuncer for other types than can be made
of Fn and double and double[] float[] etc. There are no arrays of more than 1 dimension,
and for that you should use a datastruct made of conses or listmaps to tell
what dimensions to use a 1d array as.
This is a very general software so should be able to represent anything you need,
and even though is designed for GPU number crunching and MMG games
is not the fastest at that
but it is the lowest lag and most flexible and openended in turingComplete ways,
and it guarantees max memory andOr compute cycles recursively at every call
even if millions of calls in many combos have different such limits
its still BigO(1) per tiny piece of computing. Everything halts within microseconds
unless you tell it its allowed to use more compute resources (wallet).
Wallet is the only stateful part.
<br><br>
When any func call ends in error (TODO remove all the throws), the returned Funcer's
isErr func returns true, and its isErrWal or isErrGet func tells what kind of error.
(TODO if there become more than those 2 kinds of errors, use an enum instead).
Alternatively you can check if it equals Err.get or Err.wal. 
*/
public interface Funcer<MapK,MapV>{
	
	/** the main function, called on a function, that returns a function, as usual in lambdas,
	except limited by wallet that can be further limited in deeper calls very efficiently.
	*/
	public Funcer f(Funcer p, double walletLimit);
	
	public default Funcer f(Funcer p, double walletLimit, Funcer ifFail){
	//public Funcer f(Funcer p, double maxSpend, Funcer ifFail){
		Funcer ret = f(p, walletLimit);
		return !ret.isErrWal() ? ret : ifFail;
	}
	
	/** True if any of the other isErr* funcs are true */
	public boolean isErr();
	
	/** If true, this (normally shared) object was returned because tried to spend more wallet than have */
	public boolean isErrWal();
	
	/** If true, this (normally shared) object was returned because a value in a listmap was not found */
	public boolean isErrGet();
	
	public long size();
	
	public MapK lowKey();
	
	public MapK highKey();
	
	/** If not found, returns Err.get, or you can check by theReturn.isErrGet() */
	public MapV get(MapK key);
	
	public Funcer<MapK,MapV> put(MapK key, MapV val);
	
	/** remove a key and its value, or if not contain it then return this */
	public Funcer<MapK,MapV> rem(MapK key);
	
	/** concat. This is allowed for map or list.
	For list, it slides the param up by this.maxKey()+1.
	For map, it is caller's responsibility to ensure that this.maxKey() < param.minKey()
	by whatever Comparator<K> this is using. Such Comparator must of course
	"max possible list index < min possible map index".
	If this has both list and map keys then list must have only list keys.
	Else "TODO return what error object (like one for NotFound, one for NotEnoughWallet?".
	*/
	public Funcer<MapK,MapV> cat(Funcer<MapK,MapV> list);
	
	/** prefix inclusive, a subrange */
	public Funcer<MapK,MapV> preI(MapK key);
	
	/** prefix exclusive, a subrange */
	public Funcer<MapK,MapV> preE(MapK key);
	
	/** suffix inclusive, a subrange */
	public Funcer<MapK,MapV> sufI(MapK key);
	
	/** suffix exclusive, a subrange */
	public Funcer<MapK,MapV> sufE(MapK key);
	
	/** compares forest shape, not ==.
	by local private salt.1 see uflistmapCollisionResistance WARNING.
	*/
	public boolean equalsForest(Funcer m);
	
	/** see Funcall.java. For listmaps, its Op.listmap. */
	public Op leftmostOp();
	
	/** see Funcall.java */
	public int curries();
	
	/** func in {func param} which if evaled would return this (or something equal to this).
	If leaf, then l() is identityFunc and r() is this, so thats still true.
	*/
	public Funcer l();
	
	/** param in {func param} which if evaled would return this (or something equal to this).
	If leaf, then l() is identityFunc and r() is this, so thats still true.
	*/
	public Funcer r();
	
	/** the double value wrapped, if isFloat */
	public double d();
	
	/** is d() the main data? */
	public boolean isFloat();
	
	public Funcer minKey();
	
	public Funcer maxKey();

}
