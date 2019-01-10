/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer;

/** core funcs which all other funcs and datastructs are made of */
public enum Op{
	
	
	
	
	
	
	
	//FIXME rename Leafs to Op since not all of them are leafs anymore.
	
	
	
	
	/** This is the only nonleaf symbol. Its used in the switch statements to mean anything
	other than a leaf, where a leaf is either a symbol or a float64.
	*
	funcall(-1), //FIXME waitCurries? Do I still need this symbol?
	
	/** this symbol means it has a float64 value, stored in Funcall.data *
	num(-1), //FIXME waitCurries? Do I still need this symbol?
	*/
	
	
	
	
	//All enum values below this line are lone symbols without any extra data in Funcall.data etc.
	
	
	
	
	
	/** reads how much compute resources (all estimated in a single unit,
	which is imprecise but think of it like flowing economics trading
	between compute cycles and memory depending on supply and demand of
	it within the VM) are available to this part of execution and its recursive calls.
	This is related to econacyc and its variations (mindmap binufnodeSandboxingTypes).
	Remember, memory in forest is shared by all incoming pointers recursively,
	not owned by whoever allocates it. Similarly compute cycles can be cached
	in a hashtable of triples of func param and return,
	which must be cached at least for forests of S down to SKK (identityFunc
	gets param given at top of the forest of S) and Kx (for any x, gets x),
	and maybe will be cached for every call not just those,
	and maybe all such cache will be emptied ~50 times per second (per rootCall)
	as often as Human does things in UI such as a small mouse movement.
	*/
	wallet(1),
	
	/* {spend returnThisIfFail maxSpend func param}
	returns what {func param} returns else returnThisIfFail.
	aka {{{{spend returnThisIfFail} maxSpend} func} param}
	OLD: 
	FIXME TODO "except that if it fails by not having enough Leaf.wallet, it returns null,
	compared to Leaf.spend which will have an extra param of what to return in that case."
	/** {spend maxmoneyAsFloat64 func param} calls {func param} with that limit
	or the limit of wallet, whichever is less.
	This is done by keeping a static var that changes as compute and memory are used
	and each recursion in java functions having a param thats the max (or is it min)
	that number can become before giving up, so its bigO(1) per use of it
	nomatter how deep of recursion it gets used again and again inside recursions
	with potentially different limits (can only limit to less, cant grab more).
	This is related to econacyc and its variations (mindmap binufnodeSandboxingTypes).
	*/
	spend(4),
	
	/** Lx.Ly.Lz.xz(yz). The s and t/k lambdas are turingComplete by themselves
	and with the tricons optimization and "recusion lambda with kernel1d" do controlflow.
	*/
	s(3),
	
	/** Lx.Ly.x. The s and t/k lambdas are turingComplete by themselves
	and with the tricons optimization and "recusion lambda with kernel1d" do controlflow.
	*/
	t(2),
	
	/** Lx.Ly.y, f/false, counterpart of t/true. Could be emulated as {k i},
	but it happens so often, its an optimization to include it as an Op.
	*/
	f(2),
	
	/** identityFunc aka {{sk}k} but since its the left/func of every leaf, I made it an Op.
	The right/param of every leaf is that leaf itself, so leaf.L() called on leaf.R() returns leaf.
	*/
	i(1),
	
	/** left child at VM level, not at cons level. At VM level, every pair is a function call.
	This will only get the left of what its param Funcall returns, not at LAZYEVAL time.
	*/
	l(1),
	
	/** right child at VM level, not at cons level. At VM level, every pair is a function call.
	This will only get the right of what its param Funcall returns, not at LAZYEVAL time.
	*/
	r(1),
	
	/** Lx.Ly.x(cons x y) */
	recurse(2),
	
	
	/** Curries so kernel works the same as
	the func its optimizing. These 2 should return the same:
	{kernel funcToOptimize arraySize {cons index otherData}}
	{funcToOptimize {cons index otherData}}
	so {kernel funcToOptimize arraySize}
	should be an optimization of funcToOptimize
	as long as its only called on integer float64s
	in range 0 to arraySize-1 (else return 0).
	{kernel funcToOptimize} can inTheory be optimized
	so it works on multiple sizes given later.
	<br><br>
	funcToOptimize will often be a {recurse funcToOptimize}
	which means {recurse funcToOptimize p}
	calls {funcToOptimize funcToOptimize p},
	and p will be {cons index otherData}.
	<br><br>
	As long as the recurse gives the same float64 for the same index,
	regardless of the combos its recursed to generate those,
	it can inTheory be optimized in a double[] and boolean[]
	where the boolean stops the same index in double[]
	from being written more than once,
	or there could be more efficient ways to do it. 
	<br><br>
	<br><br>
	OLD...
	<br><br>
	/** FIXME ASAP, modify comment of kernel op so funcToOptimize is a recursive func,
	thats called a consed param containing itself,
	maybe {funcToOptimize {cons funcToOptimize {cons index otherData}}},
	OR at least make it clear in the comment that the kernel optimization will look for
	such a recursion lambda in a nonrecursive lambda that starts the recursion,
	and maybe such recursion wrapper should be a core op: Lf.Lx.f((cons f) x).
	Its important to use cons instead of currying more than necessary cuz
	it makes the s and k forest exponentially big and hard to optimize to curry without cons.
	Also, do I want to use linkedlists ending with nil instead of adding complexity
	by ending the list 1 early to avoid the nil?
	Also what lambda is nil (see church encoding page on wikipedia)?
	The recursion will inTheory allow acyclicFlow music optimization
	and opencl kernels, all the same way.
	/** {kernel arraySize funcToOptimize otherData x},
	where x is a float64 thats an integer
	thats in range 0 to arraySize-1, caches all those calls
	of {funcToOptimize {cons index otherData}}.
	TODO decide on pair/cons/tricons datastructs.
	The otherData is like the arrays passed into opencl (or javassist etc).
	Technically you could define the kernel to have that data hardCoded
	but it would make optimizing harder.
	I'm undecided if this is the exact right API, but its close.
	<br><br>
	OLD:
	//TODO should there be an op that requests this optimization:
	//"use recursion lambda and int param as kernel1d to optimize."
	//vs should that optimization happen on its own?
	The kernel1d lambda will create double[] (arrays) caching the results of
	a function called on its indexs (each a float64) in some range.
	*/
	kernel(3),
	
	//public static final int cons = 2;
	
	/*2019-1-6 occamsfuncerChooseToKeepTriconsOrNot QUOTE
		TODO choose to keep both cons and tricons (and their relevant ops) OR
		emulate {tricons x y} as {cons T {cons x y}} or {cons F endsHere}.
		I'm willing to pay the extra memory and compute cost to simplify things
		since it can still be optimized to nearly as fast using the recurse and kernel ops,
		but I might want to modify the recurse op, or to have a tricons-like variant of it,
		that does Lx.Ly.x(cons T (cons (cons F x) (cons F y))) or something like that
		which is the tricons form (or is it something similar?)
		compared to the original recurse op which is: Lx.Ly.x(cons x y).
		Or is tricons so much more efficient and simpler to write than that,
		that I would want to keep it (and normal cons)?
				
		Should car and cdr do L and R if their param is not a cons (regardless of the church encoding doing it a little differently)?
	UNQUOTE.
	*/
	
	/** returns T/K (Lx.Ly.x) or F (Lx.Ly.y) depending if its 2 curried params == */
	obWeakEquals(2),
	
	floatEquals(2),
	
	/** Lx.Ly.Lz.zxy */
	cons(2),
	
	/** TODO find the exact lambda of this, considering that cons is Lx.Ly.Lz.zxy */
	car(1),
	
	/** TODO find the exact lambda of this, considering that cons is Lx.Ly.Lz.zxy */
	cdr(1),
	
	/** returns first curry after cons, if its param is a cons, else returns L of its param */
	carElseL(1),
	
	/** returns second curry after cons, if its param is a cons, else returns R of its param */
	cdrElseR(1),
	
	/** avl-like treemap node whose params are like a struct (TODO choose order):
	comparator, value, leftMap, rightMap, mapSize, minKey, maxKey.
	Its avl-like instead of exactly avl, cuz it doesnt consider height
	but does consider mapSize and estimates height by log(mapSize) or
	something very similar (TODO choose exact logic to prove log bigO).
	Log cost for forkEdit the map 1 key at a time, to add search or delete,
	but I'm unsure of the cost to merge 2 maps even if their
	key ranges dont overlap (maybe it costs log or log squared???).
	If minKey equals maxKey, then this is a map of minKey to value,
	else leftMap and rightMap are nonnull.
	You get either child maps or local value but not both,
	and you can also tell that from mapSize==1.
	TODO use mapSize==0 to represent emptyMap?
	Comparator takes {cons a b} param and returns 1, 0, or -1,
	normally just wraps string comparing,
	first by length (for efficiency) then by chars.
	{map someCertainCompator} can be optimized so the VM doesnt
	have to consider the comparator every time,
	which is why its the first param.
	{map comparator value} can also be reused for nonleaf maps
	so only need to actally store leftMap, rightMap, mapSize, minKey, maxKey.
	<br><br>
	As a func, {map comparator value leftMap rightMap mapSize minKey maxKey x}
	returns eval of {x comparator value leftMap rightMap mapSize minKey maxKey},
	similar to {cons x y z} returns eval of {z y x}.
	<br><br>
	TODO i want to optimize this in a java class that takes those params
	instead of {{{{{{comparator value} leftMap} rightMap} mapSize} minKey} maxKey}
	(or whatever order I decide on for design of maps),
	so the 2 main classes would be Funcall and Map
	(and maybe another for optimizing Kernel).
	I already partially wrote such Map code somewhere.
	*/
	map(7),
	
	/** forkEdit a map. */
	mapGet(2),
	
	/** forkEdit a map. Returns the forkEdited map. */
	mapPut(3),
	
	/** gets float64 */
	maplistSize(1),
	
	/** returns T if has that key, else F */
	maplistHas(2),
	
	/** removes a key, returns forkEdited map */
	maplistRem(2),
	
	/** get min key */
	maplistMin(1),
	
	/** get max key */
	maplistMax(1),
	
	/** prefix of maplist (inclusive). treemap or treelist have subranges of keys. */ 
	maplistPreIncl(2),
	
	/** prefix of maplist (exclusive). treemap or treelist have subranges of keys. */ 
	maplistPreExcl(2),
	
	/** suffix of maplist (inclusive). treemap or treelist have subranges of keys. */ 
	maplistSufIncl(2),
	
	/** suffix of maplist (exclusive). treemap or treelist have subranges of keys. */ 
	maplistSufExcl(2),
	
	/** concat 2 lists (see uflist and ufmaplist for unusual abilities
	that cost log time, including concat, sublist, equals,
	but with strange security requirements and optimization limits of them).
	*/
	listCat(2),
	
	/** by local private salt. see ufmaplistCollisionResistance WARNING */
	maplistEquals(2),
	
	TODO do I also want uflist for its log cost of concat and substring
	and equals of substrings? That equals requires local secret salt
	since its not secure against intentional attempts at collisions
	but is secure against accidental collisions,
	so theres no global name of a unique string,
	so a map cant consistently be sorted by it,
	but it is useful for forkEditing large texts such as
	in mindmap or code files.
	
	I want String available for map keys and concat,
	but I want them to work like maplist of double,
	where a certain range of doubles are viewed as chars (maybe 2^52+char?
	TODO choose the range, or look it up in old ufnode code called charOffset).
	I also want in general double[] to be usable as maplist,
	except it will copy the whole thing when written
	or fork it into smaller ranges.
	Since map uses comparator which is any func that takes {cons b c}
	to compare b and c and return -1 or 0 or 1,
	the comparator can allow arbitrary structures to represent a string
	even if they dont have efficient dedup,
	such as a treelist of double used as char,
	compare it first by maplistSize then by double values.
	
	/** like cons except theres also data at every node,
	and if you tricar or tricdr past leafs in that forest of tricons
	you keep seeing the data at that leaf.
	I want the data at every node to make it easier for combos of S to
	call tricar and tricdr without having to worry (by whoever designs them)
	if the param will have conses on that path vs will it infiniteLoop.
	If you dont want to use the data at each node, just at the leafs,
	then check for leafs using triend and put nil
	(TODO which labmda is nil? T? F? other? check church encoding)
	as the tridata in the other triconses
	so they act more like normal conses.
	<br><br>
	A normal cons is Lx.Ly.Lz.zxy.
	tricons may be Lw.Lx.Ly.Lz.zwxy EXCEPT if y and z are not triconses with 3 curries each
	then replaces them with some symbol that makes triend say T about this,
	and maybe it needs to also include the triend in it as a fifth curry
	but I'd like to avoid that and do it at the VM level instead for efficiency.
	I may avoid the whole Lw.Lx.Ly.Lz.zwxy thing and create symbols that
	readtricar writetricar readtricdr writetricdr etc.
	I'm undecided on some of these core datastructs and behaviors.
	*
	tricons,
	
	tricar,
	
	tricdr,
	
	/** checks if a node is a tricons without child triconses,
	but tricar and tricdr will still work they will just say self tricons is those childs.
	Returns T/K (Lx.Ly.x) or F (Lx.Ly.y).
	*
	triend,
	*/
	
	/** float64 plus. everything other than float64 is viewed as 0. */
	plus(2),
	
	neg(1),
	
	/** float64 multiply */
	mult(2),
	
	/** float64 1/param */
	oneDiv(1),
	
	/** float64 e^param */
	exp(1),
	
	/** float64 logE(param). There is no log(String) func cuz that would be stateful. */
	log(1),
	
	/** float64 lessThan, which curries 2 params */
	lt(2),
	
	/** Returns T if param is a float64, else F if param is a function */
	isFloat(1);
	
	//TODO bit shift ops on viewing float64 as int32 since it can represent all int32 values,
	//similar to how javascript has int bitshift ops???
	
	//TODO binufnodeSandboxingTypes
	//TODO chooseFirstFewOptimizationsForAiMusician

	public final int waitCurries;
	
	public final Funcall fc;
	
	private Op(int waitCurries){
		this.waitCurries = waitCurries;
		this.fc = new Funcall(null, null, null, this, 0);
	}
	
	/** convenience func to write Op.abc.f(...) instead of Op.abc.fc.f(...) */ 
	public Funcall f(Funcall p, double walletLimit){
		return fc.f(p,walletLimit);
	}

}
