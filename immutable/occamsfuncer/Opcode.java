/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import static immutable.occamsfuncer.HaltingDictator.*;
//import static immutable.occamsfuncer.storageAndCache.CachedWrap.cwrap;
//import static immutable.occamsfuncer.storageAndCache.NoncachedWrap.nwrap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

//import immutable.occamsfuncer.funcers.Funcall;
//import immutable.occamsfuncer.storageAndCache.DataUtil;

/** core funcs which all other funcs and datastructs are made of.
This is an optimization layered on top of CoreType which is
datastructs. Ops go mostly in Funcall of TheImportFunc and Utf8Array
such as [? "plus" 3 4] returns 7, where ? means TheImportFunc.
<br><br>
FIXME update many comments from before I decided on this syntax 2019-2
in (mindmap name chooseSyntaxForOccamsfuncer):
<br><br>
.3 or 3.45 or -3.45e-93 are standard way to write double numbers
(TODO do I want to force hex or base64 digits? or at least make base64 syntax for doubles the default and make you use a prefix for base10?).
"string literal"
A word by itself refers to a name defined by #, so [[? "plus"]#+ 3 [+ 4 5]]
returns 12.
? is importFunc, like [? "+" 3 4] returns 7.
<34.5 4 5 6 7> for arrayOfPrimitive.
{ } for maps, which may be written any order but are quickly normed to display in order of id.
[ ] for funcalls such as [a b [c d] e] means [[[a b] [c d]] e].
( ) for linkedlists.
$$0123456789ABCDEFGHIJKLMNOPQRSTU is the 192 bits of base64
$0123456789ABCDEFGHIJKLMNOPQRSTU, using the digits
$0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz.
# names cant start with $ or ( or ) or { or } or [ or ] or < or >
or . or - or 0 1 2 3 4 5 6 7 8 9 and cant contain whitespace.
*/
public enum Opcode{
	
	/** Wraps a func of 1 param in a chosen number of curries.
	This is the ` op (TODO use ` in OccamsfuncerParser).
	I'm undecided if this will be an op vs something built into the Funcer interface.
	FIXME It takes a variable number of params thats at least 3, so I'm unsure how to code it.
	Param0 is a currylist of ` to store an integer in unary that tells how many to curry.
	Param1 is a func that takes 1 param, which is easily made of s and k for controlflow,
	which defines a lambda func made of funcalls of pairs of getting things out of the 1 param.
	That 1 param is something like <<` <` <` `>>> funcOfABCD a b c d>. Thats written as <````funcOfABCD a b c d>.
	Param2 param3... are the params funcOfABCD pays attention to,
	but for efficiency funcOfABCD must take <<` <` <` `>>> funcOfABCD a b c d> a a param not just some
	structure of a b c and d. <<` <` <` `>>> funcOfABCD a b c d> is <<<<<<` <` <` `>>> funcOfABCD> a> b> c> d>.
	 */
	curry(-1, (UnaryOperator<Funcer>)(Funcer p)->{
		$(1);
		throw new Error("FIXME THE OPCODE ABOVE IS (THE ONLY) VARIABLE SIZE SO NEEDS SOMETHING SPECIAL IN Funcer.curry BYTE AND MAYBE SHOULDNT BE AN OPCODE AT ALL.");
	}),
	
	/** instantly throws Wallet.throwMe cuz to run a known infinite loop
	would take more compute resources than is available. This also happens whenever you try to run
	an op thats not supported by this specific occamsfuncerVM but may be supported by other occamsfuncerVMs,
	which is not technically a difference in behavior if viewed as they optimize it differently
	where one of them takes finite time and the other takes infinite time.
	Its important in occamsfuncer VM design that nondeterminism be limited to certain parts
	including Wallet and strict vs nonstrict modes and statistics on compute cycles and memory.
	*/
	infiniteLoop(1, (UnaryOperator<Funcer>)(Funcer p)->{
		return evalInfiniteLoop();
	}),
	
	
	
	/** RELATED TO EACHOTHER: CoreType.lazyEval and and Opcode.lazyEval Opcode.triggerLazyEval.
	[lazyEval func param] returns [lazyEval func param] (so its number of curries are 1 more than this).
	[lazyEval func param param2] (this is its number of curries) returns eval of [func param param2].
	[triggerLazyEval [lazyEval func param]] returns eval of [func param].
	Immutable lazyeval datastruct will be manually written in the ifTrue and ifElse
	parts of if/else etc, including in sklinkedlist (might need new syntax for that part?).
	When javastack gets to it in most cases, it wont eval, will leave it as lazyeval.
	When forExample java stack gets to it in (currylist parts of) certain opcodes
	(such as if/else) it will eval into only those lazyeval parts it needs, such as
	the ifTrue or the ifElse but not both. The syntax for this (other than sklinkedlist)
	will have some char prefix, similar to ``` prefixes 3 curries and .xyz means
	funcall of k on xyz and ;abc means "abc". Is comma taken? (I think it is but
	dont remember by what, maybe its ,xyz means funcall of k on xyz?). Just choose
	a char, and this will be ok.
	*/
	lazyEval(3, (UnaryOperator<Funcer>)(Funcer p)->{
		$();
		//throw new Error("TODO");
		return (Funcer)null;
	}),
	
	/** Example: f(? "plugin" "jstatic:java.lang.System.identityHashCode" l(someObject)))
	or maybe it would be better to put jstatic as param of "ImportFunc/?"?
	<br><br>
	One way (thats not this opcode) to hook in a plugin is to put the params somewhere in the data
	and have an external program look at that data, after occamsfuncer returns it,
	then external program uses the data and forkEdits it then calls occamsfuncer on it,
	and in that way the plugin can do anything outside the rules of occamsfuncer,
	such as to have mutable behaviors like many systems normally do,
	but occamsfuncer is immutable/stateless, and occamsfuncer stays immutable/stateless
	since occamsfuncer is only a subset of that calculation.
	<br><br>
	The other way is what this opcode does. Given the first param (such as a string name of the plugin),
	and given the second param which is the param of that plugin,
	returns the deterministic repeatable (except for roundoff etc) whatever that plugin returns.
	<br><br>
	This makes it harder to prove sandboxing of zapeconacyc, fulleconacyc, mem, com,
	not escaping into private files, etc, but it can be done inTheory,
	very carefully only including plugins which hook into those constraints.
	The plugin is hooked in at occamsfuncerVM level and must use Wallet.have and Wallet.keep etc.
	<br><br>
	If a plugin is not found, then call Opcode.infiniteLoop instead (which instantly throws Wallet.throwMe).
	<br><br>
	I'm planning to hook in learnloop RBM code (AI stuff) which is opencl optimized,
	at least until maybe eventually I'll port that to occamsfuncer code and optimize parts of occamsfuncer
	with opencl through Opcode.kernel, and optimize with javassist in Opcode.acyclicFlow.
	<br><br>
	Hopefully there will be very few (and eventually no) plugins since occamsfuncer will be
	flexible enough to build whatever we need inside its syntax and well optimized,
	cuz its meant for large scale compatibility across a p2p network where many variants of
	all kinds of software fit together seamlessly, and that wont happen if you need to download
	trusted plugins depending which code you're using.
	But it would be worse if different implementations had different core ops,
	so I'm including this plugin op to standardize that.
	*/
	plugin(2, (UnaryOperator<Funcer>)(Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** RELATED TO EACHOTHER: CoreType.lazyEval and and Opcode.lazyEval Opcode.triggerLazyEval */
	triggerLazyEval(1, (UnaryOperator<Funcer>)(Funcer p)->{
		$();
		//throw new Error("TODO");
		return (Funcer)null;
	}),
	
	/*eachEdlayAsIndependentOfOthersWithAttmultPerNolayAndBiasPerNode QUOTE
	For example, 300 lowNodes and 800 highNodes, and .1 bias per lowNode
	and -.2 bias per highNode and 1 attMult per lowNode and .6 attmult per highNode.
	Maybe lowNode attention should always be 1.
	Or more generally, such as in a timewindow considering older things less,
	attmult per node instead of per nolay.
	...
	in an edlay, per nolay or per node:
	multIncomingWeightedSum
	multOutgoingWeightedSum //attention
	biasIncoming
	UNQUOTE.
	*/
	
	
	
	
	/* NO THERE MUST NOT BE A nextOp OP CUZ IT WOULD MAKE IT HARDER TO MERGE
	OPENSOURCE FORKS OF occamsfuncer WHICH DISAGREE ON THE SET OF CORE OPS.
	I HOPE THEY STANDARDIZE TO A SMALL CORE SET, BUT THIS IS RESEARCH IN PROGRESS.
	THERE MUST BE NO ORDER AMONG THE OPS, ONLY USE THEM IN SWITCH/IF/ELSE/ETC.
	<br><br>
	/** This is a universal function, similar to but more complex than iota,
	because calling it on combos of itself can create any possible software.
	It returns the next Op, by Enum.ordinal(), past its param.
	If its param is itself, it returns the next op in occamsfuncer.Op.java.
	If its param is not an op, gets L() of L()... until finds an Op
	(cached as Funcall.leftmostOp).
	If its a listmap, then its op is listmap (even though its optimized in
	other java classes, it still acts like function calls if observed that way),
	If its the last op (in occamsfuncer.Op enum), then the next op is nextOp.
	*
	nextOp(1),
	*/
	
	
	
	
	//FIXME rename Leafs to Op since not all of them are leafs anymore.
	
	
	
	
	/** This is the only nonleaf symbol. Its used in the switch statements to mean anything
	other than a leaf, where a leaf is either a symbol or a float64.
	*
	funcall(-1), //FIXME waitCurries? Do I still need this symbol?
	
	/** this symbol means it has a float64 value, stored in Funcall.data *
	num(-1), //FIXME waitCurries? Do I still need this symbol?
	*/
	
	
	
	
	
	
	
	
	
	
	/** Funcer.leftmostOp() says...
	If isExec(), it will be in an Op.java wrapping an Opcode enum,
	else the op is Opcode.data which causes it to (TODO I'm undecided,
	eval to itself? eval to nil? eval to its param so acts like identityFunc?)
	*/
	data(0, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** TODO hook into OccamsfuncerParser.java if language is "occamsfuncer".
	<br><br>
	Opposite of lave (spelled backward).
	[eval {"lang":"occamsfuncer", "ns":mapAsNamespace, "code":codeString}].
	<br><br>
	Other langs (such as java and opencl, subject to sandboxing limits),
	and structures other than strings (such as list of tokens),
	might be added later, but for now
	the only one included by default is the "occamsfuncer" lang.
	<br><br>
	Evals a namespace and string (treelist of utf8byte) of occamsfuncer code,
	without sideeffects (except in some cases caching the ids and content which hashes to them),
	and returns a funcer. That funcer normally is not compiled yet
	unless it contains parts of code that have already been compiled and cached,
	but it may compile some parts to opencl and javassist etc when those parts run.
	<br><br>
	This is the default syntax for occamsfuncer, which is very basic,
	but you might derive, as user level funcs, other syntaxes.
	<br><br>
	Since occamsfuncer must be immutable/stateless (except memory and
	compute cycles stats), there cant be any registerLang func to change what this does,
	so whatever it does when VM starts thats what it continues doing for whole VM run.
	To add behaviors, you're supposed to derive user level code.
	Since making funcs with more than 1 param (with Op.s as controlflow) is inefficient
	at user level, this op takes 1 param, so you can easily make a competing op
	that does all the languages you want, and optionally call this op from that
	if the language is "occamsfuncer" or you could reimplement that too (using other ops)
	or use it as a lower level to compile to.
	*/
	eval(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** Opposite of eval (spelled backward).
	[lave {"lang":"occamsfuncer", "ns":mapAsNamespace, "ob":viewMeAsCodeString}]
	returns the codeString that can be used in eval.
	*/
	lave(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** see comment in Weakref.java. This is a function that returns T or F
	depending if its param is a certain object (by matching its id to the stored id)
	and is the recommended way to, if you choose to remove your copy of content after things
	are built on it (such as responding to "copyright takedown" demands),
	prevent errors in the merkle forest.
	*/
	weakref(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** This is forExample used to check lists (such as strings) for equality, costing about logLinear time and memory
	to norm each but constant time to check equality (if already hashed, else just amortized constant time)
	between any in a set of normed objects.
	This does not dedup different ways of writing the same function behaviors.
	Maps stay normed all the time.
	Example: there are many combos of lazycatPairs that form a string or list (avl-like)
	but only 1 of them is the normed form. If you want that, call Funcer.norm() or Opcode.norm on it.
	*/
	norm(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** Ignores param and returns T or F depending if using
	Funcer.f(...) (nonstrict) or Funcer.F(...) (strict)
	(see those comments in Funcer.java for important details
	about strict vs nonstrict),
	or the op forms of those are Op.strict:
	[strict func param] returns eval of [func param] in strict mode.
	Since this is deterministic in strict mode,
	it doesnt matter that code can ask if its in strict mode
	since in strict mode thats the constant T.
	*/
	callerIsStrict(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** [strictCall func param] returns eval of [func param] in strict mode. */
	strictCall(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/*
	FIXED, read paragraph at end of this comment block.
	
	FIXME I need a way to define that opencl should do
	float*float vs double*double but still use int for pointer arithmetic,
	vs however it likes in nonstrict mode.
	I dont want to duplicate number Ops
	to have a float form and a double form,
	but if I dont think of a better way I'll have to.
	...
	OLD[[[
	The plan is to get it working in nonstrict mode first,
	since most calculations will normally be done that way,
	then explore how strictness affects efficiency and
	what are the strict abilities of opencl on common hardware,
	and if that turns out to be too different,
	then redesign the whole system using what I learned.]]]
	...
	I had considered using longs without float or double, in opencl,
	for strictness, but its less efficient to derive floatlike ops
	from longs and less intuitive to Humans.
	...
	For now at least, and this is what I had planned,
	when Op.kernel is called, it will do pointers as int
	and nonpointers as float, so maybe I'll define that as strict mode,
	and everything outside of kernel will be doubles.
	But what about acyclicFlow music optimization which
	is meant to use doubles?
	
	Yes, thats what I'll do. Op.kernel will take 1 more param,
	T or F, that tells if its required to (in strict mode)
	do float32 math (instead of float64) on nonpointers,
	and pointers vs values are kind of a vague concept but I think
	I can make it act deterministicly by defining pointer
	as any number index in a map.
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
	wallet(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
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
	spend(4, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** Lx.Ly.Lz.xz(yz). The s and t/k lambdas are turingComplete by themselves
	and with the tricons optimization and "recusion lambda with kernel1d" do controlflow.
	*/
	s(3, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** Lx.Ly.x. The s and t/k lambdas are turingComplete by themselves
	and with the tricons optimization and "recusion lambda with kernel1d" do controlflow.
	*/
	t(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** Lx.Ly.y, f/false, counterpart of t/true. Could be emulated as {k i},
	but it happens so often, its an optimization to include it as an Op.
	*/
	f(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** identityFunc aka {{sk}k} but since its the left/func of every leaf, I made it an Op.
	The right/param of every leaf is that leaf itself, so leaf.L() called on leaf.R() returns leaf.
	*/
	i(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** left child at VM level, not at cons level. At VM level, every pair is a function call.
	This will only get the left of what its param Funcall returns, not at LAZYEVAL time.
	*
	l(1),
	
	/** right child at VM level, not at cons level. At VM level, every pair is a function call.
	This will only get the right of what its param Funcall returns, not at LAZYEVAL time.
	*
	r(1),
	*/
	
	/** Lx.Ly.<x<cons x y>> */
	recurse(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** TODO choose a name then update all comments and .occamsfuncer code files.
	This used to be named statefulFunc, but I'm going to name it something like...
	'param0 is data. param1 is func. param2 is param that func is called on, and I return what that returns.'>#ignoredataFuncParam
	(as in PlayConstSineWaveOnJsoundcardPlugin_4.occamsfuncer).
	<br><br>
	Used with Opcode.recurse, this Opcode.statefulFunc statelessly emulates a stateful process.
	<br><br>
	Should there be an Opcode.something for statefulfunc, that has func and data and executes like the func?
	Something that maybe takes 3 params... <Opcode.statefulFunc func data param>
	so its 2 param for (before curry the last) is <Opcode.statefulFunc func data>,
	so if it was used with Opcode.recurse then it could see itself and could get the data out.
	Yes, do that. So no ;func key needed in...
	SoundFunc sf = "FIXME Can this use Opcode.recurse instead of ;func being a map key, for ;sound key and parents ;func key";
	<br><br>
	I CHOOSE THIS ONE...
	DESIGN POSSIBILITY 1:
	<Opcode.recurse <statefulFunc func data> param>
	returns eval of <func <cons <statefulFunc func data> param>>.
	So func is normally designed to find data in its param <cons <statefulFunc func data> param>.
	<br><br>
	(((CANCEL THIS...
	DESIGN POSSIBILITY 2:
	I'm undecided if I want Opcode.statefulFunc to require Opcode.recurse vs if it should have a behavior builtin
	thats similar to Opcode.recurse but slightly more efficient. Write the alternative here:
	<statefulFunc func data param> returns eval of <func <cons <cons func data> param>>.
	)))
	<br><br>
	Compare the 2 things it evals:
	(1): <func <cons <statefulFunc func data> param>>
	(2): <func <cons <cons func data> param>>
	I choose (1) cuz its the same number of nodes and is simpler cuz reuses Opcode.recurse instead of
	creating a similar logic. 
	*/
	ignoredataFuncParam(3, (Funcer p)->{
		$();
		//<Opcode.statefulFunc func data param> returns eval of <func param>
		//aka <<<Opcode.statefulFunc func> data> param>
		Funcer func = p.L().L().R();
		Funcer param = p.R();
		return func.f(param);
	}),
	
	idAsLeaf(1, (Funcer p)->{
		$();
		return p.id().asFuncer();
	}),
	
	/** TODO rewrite these comments. Theres too many OLD and disorganized comments,
	but those closer to the top of the comment block are newer.
	<br><br>
	[kernel nonpointersAreFloat32 arrayOfThese funcToOptimize arraySize otherData index]
	returns the eval of [funcToOptimize [cons otherData index]]
	with possible loss of precision or required determinism
	depending on strict mode vs nonstrict mode and nonpointersAreFloat32 and arrayOfThese.
	<br><br>
	UPDATE: To handle strict mode vs nonstrict mode,
	added nonpointersAreFloat32 and arrayOfThese params:
	[kernel nonpointersAreFloat32 arrayOfThese funcToOptimize arraySize otherData index],
	where nonpointersAreFloat32 and isWrapInt32Array are each T or F.
	If nonpointersAreFloat32, then numbers used as map index,
	or anything which a map index can be derived from,
	are still double (optimized as int which doubles support all values of),
	but other number values are float.
	arrayOfThese is a prototype number that determines wrapped array type
	(the array whose size is arraySize)
	as one of double, float, int, utf8Byte, or bit (whichever smallest it fits in).
	At the end of calculation, or anything that has the same effect,
	stores in that type. Example: wraps an int[600] of colorARGB of a 30x20
	image to draw on screen (which may generate a different image per video frame
	or it may be a dumb sprite, or any turingComplete combo of those,
	though the more complex a calculation the less likely it will be optimized).
	<br><br>
	If nonpointersAreFloat32 then strict mode determines what to require to be a float
	by what can possibly affect anything thats used as a map index and is a number.
	<br><br>
	UPDATE: [kernel isFloat32 funcToOptimize arraySize otherData index] 
	[kernel funcToOptimize arraySize otherData index]
	returns the eval of [funcToOptimize [cons otherData index]]
	but caches that in [kernel funcToOptimize arraySize otherData]
	in a 1d primitive array size arraySize.
	<br><br>
	[kernel funcToOptimize] can have its own optimization
	thats independent of arraySize and otherData,
	such as matrixMultiply can be compiled to an opencl ndkernel (through lwjgl)
	and put those arrays and their sizes in otherData.
	<br><br>
	arraySize is there for the kernel op and those who use it to know
	what size to expect in output without having to understand otherData.
	<br><br>
	Its designed to use cons so funcToOptimize only has to take 1 param
	since using Op.s and Op.t for controlflow makes it inefficient
	for user level funcers to take more than 1 param,
	while its still efficient for these (VM level) ops to take multiple params.
	It takes exponentially many Op.s and Op.t to curry linearly deep (like in unlambda or lazyk).
	Users will have to learn to use cons car cdr etc instead of lambda currying.
	<br><br>
	OLD:
	<br><br>
	Curries so kernel works the same as
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
	kernel(6, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*occamsfuncerKernellikeSetOpsSuchAsPlusAveDevEtc
	These would be recursive, such as in matmul it would use 2 kernellikeops
	on row in one and col in the other, for all rows and cols in parallel.
	TODO port RBM.java to occamsfuncer using these.
	ForExample, instead of coding maxRadius norm in plainjava learnloop
	(like I was about to do, having lost it in an upgrade and was about to bring it back)
	code it in occamsfuncer.
	An opencl kernel code could be derived from a small number of these kernellikesetops
	containing eachother.
	Do that all in float32s.
	*/
	
	
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
	obWeakEquals(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** returns T or F depending if the .d() (returns double) form of its 2 params == */
	floatEquals(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** Lx.Ly.Lz.zxy */
	cons(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** TODO find the exact lambda of this, considering that cons is Lx.Ly.Lz.zxy */
	car(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** TODO find the exact lambda of this, considering that cons is Lx.Ly.Lz.zxy */
	cdr(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** returns first curry after cons, if its param is a cons, else returns L of its param *
	carElseL(1),
	
	/** returns second curry after cons, if its param is a cons, else returns R of its param *
	cdrElseR(1),
	*/
	
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
	*
	listmap(7),
	
	listmapGet(2),
	
	/** forkEdit a map. Returns the forkEdited map. *
	mapPut(3),
	
	/** gets float64 *
	listmapSize(1),
	
	/** returns T if has that key, else F *
	listmapHas(2),
	
	/** removes a key, returns forkEdited map *
	listmapRem(2),
	
	/** get min key *
	listmapMin(1),
	
	/** get max key *
	listmapMax(1),
	*/
	
	minKey(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	maxKey(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** [hasKey map key] returns T or F */
	hasKey(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** [get map key] returns value of that key else
	(TODO what constant? is nil=F? do I already have a nil?
	Caller should have checked hasKey if they wanted to know for sure
	it didnt map to that constant
	*/
	get(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** [put map key val] returns forkEdited map with that key/val pair */
	put(3, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** [rem map key] returns forkEdited map without that key,
	or if it already didnt have the key, returns the same map by ==.
	*/
	rem(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	
	/** [getById bitArray] returns the funcer whose id (such as a 192 bit Id.java) is those bits.
	See Funcer.asBitArray or Funcer.asLongArray (of 192 doubles which are each 0 or 1),
	forExample if ids in this system happen to be 192 bits.
	Else throws Wallet if it cant find it, maybe its on the Internet or harddrive somewhere
	but dont know its there and was not allocated enough wallet to go find it
	or wallet includes gamingLowLag statistics so wont look for long.
	*/
	getByIdBits(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** Returns bitArray of the hash kind of id of its param. */
	globalIdBits(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** returns bitArray of the localId kind of id of its param,
	which is ok to send across untrusted borders (which they should ignore) but not ok
	to accept incoming from outside untrusted borders.
	*/
	localIdBits(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	
	
	
	
	
	
	
	
	/*TODO I want an op here for acyclicFlow music tool optimization.
	It will take a forest of unary and binary number ops (such as +, *, eExponent, oneDivide)
	and n inputs per time cycle, so theres an array size n+s where s is size of state,
	and it will loop (how many times is a param of this op), each cycle
	replacing the contents of that array. It will also have space in that or another array
	for temp vars, or maybe should be done as mutable var in Funcall object (so singlethreaded)?
	Then it returns an array size s. This will happen about 100 times per second
	or however fast jsoundcard can access speakers andOr microphone buffers
	to process a small fraction of a second of sound and other related data.
	As abstract math the forest could be defined using Op.s and Op.t (aka K)
	but would still be compiled (at runtime to this interpreter in maybe .0001 seconds
	or if compiled to javassist maybe takes .2 seconds and can be reused.).
	The ops already have int magic and Enum.ordinal or could simply use array of those enum
	alongside array of double and array of int indexs to read.
	Mathevo already does this maybe efficiently enough.
	*/
	
	/** This is an optimization for realtime interactive music tools
	but may be more generally useful.
	Its similar to Op.kernel except it normally runs in cpu instead of gpu
	and does the same calculation many times sequentially.
	The main idea is a double[] of state is read and written in a loop,
	and each iteration of the loop a piece of another double[] is copied
	into the first n indexs of the state.
	For example, a state of size 30, and index 0 is microphone input,
	and indexs 1 and 2 are mouse Y and X positions,
	and indexes 3 and 4 are speaker amplitude outputs,
	and this might run 44100 times per second (called "CD quality").
	A fraction of 44100 times, 3 doubles are copied from the second array to
	the first 3 indexs of the first array, then it does the loop body
	which is any transform that doesnt require allocating memory,
	such as a forest of plus, multiply, sine, exp, min, mod, etc.
	The microphone and mouse inputs are recorded for a small fraction
	of a second, such as 441 time cycles (100 times per second),
	and that block is calculated all at once, returning a copy
	of the state (27 doubles, excluding the 3 inputs),
	which can go into the next call of this op after the interactive part.
	The interactive sounds (evolvable musical instruments etc)
	continue in small blocks like that.
	<br><br>
	(TODO fix audivolv0.1.8 which has some problems with newer systems
	and throws when trying to play evolved sounds, before continuing this)
	<br><br>
	This will normally be used with jsoundcard but is a standalone stateless op
	that doesnt have to stream to or from anywhere.
	<br><br>
	Remember that interaction with external stateful systems
	(such as jsoundcard and mouse and keyboard) can only be done
	by them forkEditing a funcer (which are all stateless/immutable)
	to have the input somewhere (use a map key),
	then run it, then look for output in what it returns,
	and continue like that, so even though the caller can be stateful,
	occamsfuncer's part is stateless/immutable.
	<br><br>
	This op has not been completely designed and is left for future expansion.
	It will be something like this:
	[acyclicFlow inputSize stateSize algorithm theInputs cycles].
	I'm undecided how to describe the algorithm object.
	It could be a forest using Op.s on binary number ops
	and an op to get leaf values (such as I'm planning for nsForkEdit
	being emulated as a normal curried param)
	OR the algorithm object could be an array of int like opcodes
	that each have 12 bits and 12 bits as 2 pointers lower in the array
	and 8 bits to choose which func of 2 numbers to 1 number.
	I tend to prefer the forest form instead of low level opcodes,
	even if those opcodes are how its implemented in occamsfuncer VM,
	or it might be implemented as javaclass with mutable double field
	and 2 final pointers to such javaobjects to get its 2 params
	and a final DoubleBinaryOperator object in each,
	and run those in an array.
	*/
	acyclicFlow(5, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//TODO BitstringkeyMap ops, separate from the avl listmap ops.
	
	
	
	
	
	
	
	
	/** /** https://en.wikipedia.org/wiki/Lambda_calculus says nil is λx.TRUE.
	In occamsfuncer syntax thats [T T]. TODO verify that works with cons car cdr T F etc.
	*/
	nil(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	
	
	
	
	/** concat lists (TODO should this also apply to maps and arrays?) */
	cat(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** prefix of list at endExcl index */
	pre(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** suffix of list at start index */
	suf(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	
	
	/** A comparator is a funcer that takes param {cons x y} and returns -1 or 0 or 1.
	<br><br>
	Comparator is only used for list index keys (integers in a certain range),
	which are always before non-list-index keys aka map keys.
	*
	getComparator(1),
	
	/** A comparator is a funcer that takes param {cons x y} and returns -1 or 0 or 1.
	ForkEdit comparator of first param , set it to second param as the comparator.
	If first param is not a listmap, then returns an empty listmap with that comparator,
	similar to what happens if you listmapPut on something thats not a listmap.
	<br><br>
	Comparator is only used for list index keys (integers in a certain range),
	which are always before non-list-index keys aka map keys.
	*
	setComparator(2),
	
	/** prefix of listmap (inclusive). treemap or treelist have subranges of keys. *
	listmapPreIncl(2),
	
	/** prefix of listmap (exclusive). treemap or treelist have subranges of keys. *
	listmapPreExcl(2),
	
	/** suffix of listmap (inclusive). treemap or treelist have subranges of keys. *
	listmapSufIncl(2),
	
	/** suffix of listmap (exclusive). treemap or treelist have subranges of keys. *
	listmapSufExcl(2),
	
	/** concat 2 lists (see uflist and uflistmap for unusual abilities
	that cost log time, including concat, sublist, equals,
	but with strange security requirements and optimization limits of them).
	*
	listCat(2),
	
	/** by local private salt. see uflistmapCollisionResistance WARNING *
	listmapEquals(2),
	*/
	
	/*TODO do I also want uflist for its log cost of concat and substring
	and equals of substrings? That equals requires local secret salt
	since its not secure against intentional attempts at collisions
	but is secure against accidental collisions,
	so theres no global name of a unique string,
	so a map cant consistently be sorted by it,
	but it is useful for forkEditing large texts such as
	in mindmap or code files.
	
	I want String available for map keys and concat,
	but I want them to work like listmap of double,
	where a certain range of doubles are viewed as chars (maybe 2^52+char?
	TODO choose the range, or look it up in old ufnode code called charOffset).
	I also want in general double[] to be usable as listmap,
	except it will copy the whole thing when written
	or fork it into smaller ranges.
	Since map uses comparator which is any func that takes {cons b c}
	to compare b and c and return -1 or 0 or 1,
	the comparator can allow arbitrary structures to represent a string
	even if they dont have efficient dedup,
	such as a treelist of double used as char,
	compare it first by listmapSize then by double values.
	*/
	
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
	
	/** ignores its param and returns an empty listmap.
	From this and the listmap ops, can derive any forest
	of maps, lists, and listmaps (with object and integer keys). 
	*
	getEmptyListmap(1),
	*/
	
	/** <? ;plusEq>#+=
	<+= aMap ;x 1> returns aMap forkEdited to have var ;x (aka "x") mapped to x+1,
	starting x at 0 if not exist or is not a number.
	Same as you could do with get, put, and plus.
	*/
	plusEq(5, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/**
	<? ;updateVar aMap ;x <? ;plus 1>> aka x++ aka (after things are #named)...
	<= aMap ;x <+ 1>>, since <+ 1> is a func that returns its param plus 1.
	...
	TODO
	How to derive a ++ func: <````` s<..TODO call that same func except with ;x being param..>> 
	*/
	updateVar(5, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** ignores its param and returns the constant (float64)1.
	From this and the other float64 ops, you can derive any float64 value.
	*/
	getOne(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** float64 plus. everything other than float64 is viewed as 0. */
	plus(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	negate(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** float64 multiply */
	multiply(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** float64 1/param */
	oneDivide(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** float64 e^param */
	eExponent(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** float64 logE(param). There is no log(String) func cuz that would be stateful. */
	logE(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** x exponent y */
	pow(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** float64 lessThan, which curries 2 params and returns T or F */
	lessThan(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	lessThanOrEq(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	greaterThan(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	greaterThanOrEq(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	sine(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	arcsine(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	min(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	max(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	floor(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	ceil(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	round(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	abs(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** sqrt(x^2 + y^2) like in java.lang.Math.hypot(double,double)
	which says it does that more precisely than the individual ops.
	If Funcer.isStrict then would lose that precision.
	*/
	hypot(2, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/*sine
	arcsine
	
	Math.cbrt?
	Math.hypot?
	floatMin
	floatMax
	floor
	absoluteValue
	Should there be a pow op instead of deriving it (TODO verify can be done) from eExponent oneDivide etc?
	what other 2 doubles to 1 double ops are common?
	*/
	
	/** isFloat and is integer and is in range 0 to maxListSize-1 * 
	isListIndex(1),
	*/
	
	/** Every object isFloat, isFuncall, or isListmap.
	isErr doesnt count since user level code cant get those.
	UPDATE: using throw/catch for Op.spend, and get/minKey/etc can return null for not found.
	Returns T if param is a float64, else F if param is a function */
	isFloat(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	}),
	
	/** Every object isFloat, isFuncall, or isListmap.
	isErr doesnt count since user level code cant get those.
	UPDATE: using throw/catch for Op.spend, and get/minKey/etc can return null for not found.
	True if is a funcall waiting on curries.
	Would technically be true if its a funcall in progress or lazyeval,
	but this wouldnt eval until its param returns.
	*/
	isFuncall(1, (Funcer p)->{
		$();
		//return cwrap(p.coreType()==CoreType.funcall);
		throw new Error("TODO");
	}),
	
	/** Every object isFloat, isFuncall, or isListmap.
	isErr doesnt count since user level code cant get those.
	UPDATE: using throw/catch for Op.spend, and get/minKey/etc can return null for not found.
	*
	isListmap(1);
	*/
	
	/** Returns T if param is a MapPair, MapSingle, or MapEmpty, else returns F */
	isMap(1, (Funcer p)->{
		$();
		throw new Error("TODO");
	});
	
	//TODO bit shift ops on viewing float64 as int32 since it can represent all int32 values,
	//similar to how javascript has int bitshift ops???
	
	//TODO binufnodeSandboxingTypes
	//TODO chooseFirstFewOptimizationsForAiMusician
	
	public final UnaryOperator<Funcer> func;

	public final int waitCurries;
	
	//FIXME how does [? "plus"] get optimized to use Op.plus?
	//public final Op funcer;
	
	//public final int magic32;
	
	private Opcode(int waitCurries, UnaryOperator<Funcer> func){
		this.func = func;
		this.waitCurries = waitCurries;
		//this.fc = new Funcall(null, null, null, this, 0);
		//this.funcer = new Op(this);
		//magic32 = DataUtil.magic32(toString()+"C"+waitCurries);
	}
	
	/** convenience func to write Op.abc.f(...) instead of Op.abc.fc.f(...) * 
	public Funcer f(Funcer p, double walletLimit){
		return funcer.f(p,walletLimit);
	}*/
	
	/*private final static Map<String,UnaryOperator<Funcer>> ops;
	private final static UnaryOperator<Funcer> infiniteLoop;
	static{
		ops = new HashMap();
		ops.put(Opcode.infiniteLoop.toString(), infiniteLoop=(Funcer p)->{
			throw Wallet.throwMe; //same as Wallet.$(1./0);
		});
		FIXME needs to know its number of curries, so how will that interact with UnaryOperator?
		ops.put(Opcode.abs.toString(), (Funcer p)->{
			//[abs x]
			$();
			return wrap(Math.abs(p.R().d()));
		});
		ops.put(Opcode.lazyEval.toString(), (Funcer p)->{
			//[lazyEval func param] returns [lazyEval func param] (so its number of curries are 1 more than this).
			//[lazyEval func param param2] (this is its number of curries) returns eval of [func param param2].
			//[triggerLazyEval [lazyEval func param]] returns eval of [func param].
			$();
			Funcer pL = p.L();
			Funcer func = pL.L().R();
			Funcer param = pL.R();
			Funcer param2 = p.R();
			return func.apply(param).apply(param2);
		});
	}*/
	
	/** Example: [? "plus" 3 4] returns 7,
	so "plus" maps to a UnaryOperator whose param in that case would be [? "plus" 3 4] and would return 7.
	The only state is in Wallet (and maybe statistics of memory and compute cycles somewhere else?)
	and strict vs nonstrict mode. Anything not found is Opcode.infiniteLoop, cuz all possible combos
	of calling any funcer on any funcer is allowed.
	<br><br>
	This is normally called very few times and cached in Funcall objects
	(or some wrapper I might call ImportCall which means TheImportFunc called on a param?).
	*/
	public static Opcode get(String importName){
		try{
			return Enum.valueOf(Opcode.class, importName);
		}catch(IllegalArgumentException e){
			return Opcode.infiniteLoop;
		}
	}

}
