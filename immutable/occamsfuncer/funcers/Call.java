/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;

import java.io.OutputStream;

import immutable.occamsfuncer.CacheFuncParamReturn;
import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.HaltingDictator;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;

/** A few standard combos of funcalls, like storing x and y but it calculates the same thing as
<cons x y> aka <<cons x> y> EXCEPT L returns x instead of <cons x>.
The standard kinds of Funcall are:
-- consPair <cons x y>. Has a special syntax when last in linkedlist is nil: (a (b c d) e f)
-- sCurryPair <s x y> aka <<s x> y> where if x is an sCurryPair then it has its own syntax
as s<x y z> aka s<s<x y> z> means <s <s x y> y> aka <<s <<s x> y>> y>.
-- FuncallPair is just a normal funcall: <x y>.
...
List is pair but it needs a long size (and maybe other fields) that I dont want to waste by storing here.
*/
public class Call extends AbstractFuncer{
	
	/*TODO how to represent ImportFunc/? such as <? "plus" 3 4> evals to 7,
	and <<? "plus" 3>#+ <+ 4 5>> returns 12, 
	and <? "plus"> needs to store data so its optimized as a hardcoded opcode
	even though its not written that way.
	ImportFunc/? will be a Leaf and will have its own part of int header.
	*
	
	Also I need datastruct for ````` var number of curries
	*/
	
	//Also where do the opencl and musicTools optimizations go?
	//They dont go in the core datastructs. They exist only at runtime in the lambdas in Opcodes.
	
	/** These mean different things depending what kind of funcall it is,
	such as consPair, sCurryPair, or normalFuncallPair. Which of those it is, and their small fields,
	are in (int)AbstractFuncer.header.
	*/
	public final Funcer x, y;
			
	public final Opcode leftmostOp;
			
	
	/** how many more curries before eval?
	Example: In <? "plus"> cur is 2.
	Example: In <? "plus" 5> cur is 1.
	Example: In <? "plus" 5 5> you cant have such an object cuz it would instantly eval to 10.
	If you want to lazyeval that, use <? "lazyEval" <? "plus" 5> 5>
	so <? "lazyEval" <? "plus" 5> 5 44> would eval to what <? "plus" 5 5 44> would eval to
	but what <10 44> evals to isnt interesting (since 10 is not a Call)... TODO write better example of lazyEval.
	You can also <? "triggerLazyEval" <? "lazyEval" <? "plus" 5> 5>> which would return 10.
	*/
	public final byte cur;
	//FIXME: LIMIT THIS TO 15 CURRIES (4 BITS) AND PUT IT IN INT HEADER.
	//SINCE THERES MAX 15 CURRIES, CREATE ALL 15 OF THOSE AS OPS LIKE <? "```">#``` AND <? "``">#``.
	public static final byte maxCur = 14;
	public static final byte noCur = maxCur+1;
	
	
	/** TODO will this class be shared among multipler coretypes such as:
	coretypeCall
	coretypeSCall
	coretypeConsPair (represents f(cons a b) but unlike that its L returns a and R returns b.
	...
	f(x y) is a normal funcall.
	...
	F(x y) is an sCurryFuncall meaning f(s x y) aka f(f(s x) y)
	until it has enough curries for the f(...) it returns (TODO design incomplete)
	then calls it, and whatever comes after the F(...) is called normally
	such as f(F(funcThatReturnsPlus funcThatReturns3) 4) returns 7,
	such as f(F(,+ ,3) 4) aka f(F(f(k +) f(k 3)) 4).
	...
	l(x y z) is a linkedlist of cons pairs.
	cons is the lambda ABCcab and a consPair of x y in lambda math is f(cons x y)
	but in occamsfuncer is stored in a Call that does the same thing except
	Call.L() returns x instead of f(cons x). l(x y z) is conspair<x,conspair<y,conspair<z,nil>>>.
	f(conspair<x,y> z) returns the eval of f(z x y) but normally you would
	use it the faster way of car and cdr which skips the calls in ABCcab
	so doesnt CacheFuncParamReturn those calls or spend compute time on them. 
	... 
	OLD...
	...
	FIXME where to compute this? public final Opcode leftmostOp;
	It has to be Map<Funcer,Opcode> for the standard Funcers such as <? "plus">,
	and datastructs Map and List etc hardcode a certain Opcode,
	and the rest of them just copy L().leftmostOp() in constructor.
	...
	FIXME sCurryPair and consPair should have which Opcodes?
	normalFuncallPair copies L().leftmostOp() unless is a <? anything>
	in which case it looks it up in the Map<Funcer,Opcode> and if not found
	then its Opcode.infiniteLoop (which always halts instantly by throwing).
	Its important that if different opensource forks of occamsfuncer
	support different sets of ops that those they have in common
	do exactly the same thing, and those that some have but others dont have
	either do the thing if have it else act as if they are doing it
	but its so badly optimized it costs infinite compute resources
	which HaltingDictator says cant pay that much so ends instantly
	aka HaltingDictator.evalInfiniteLoop() or HaltingDictator.$(a big number).
	All the nondeterminism is therefore limited to optimization
	(its infinitely slow if you dont know how to do it ever)
	and if nonstrictMode then also float roundoff and order of parallel multiplies adds etc.
	*/
	public Call(Funcer x, Funcer y){
		super((short)Data.coretypeCall); //got rid of the S coretypes so theres only 1 Call type
		//super((short)coretype); FI XME coretype varies cuz this class can be SCall
		if(x == Import.instance){
			//This rarely happens compared to the ELSE
			//cuz once you have such a func its normally reused many times.
			Opcode op = Opcode.getOrNull(y);
			if(op == null) HaltingDictator.evalInfiniteLoop(); //see comment of Opcode.getOrNull(Funcer)
			leftmostOp = op;
			cur = (byte)(op.waitCurries-1); //f(?? y) aka f(TheImportFunc y) has y as its first curry
		}else{
			leftmostOp = x.leftmostOp();
			cur = (byte)(x.cur()-1);
			//cur must be positive since Call is not created for the last curry,
			//instead is run in f(Funcer), fStrict(Funcer), and CacheFuncParamReturn.
		}
		this.x = x;
		this.y = y;
	}
	
	/*ocfnRedesignToHaveOnly1CallTypeNoSCallSLinkedListNodeEtcTypes QUOTE
		TODO implement SLinkedListNode and SCall and NormalCall here (different coretypes).
		There will not be coretypes for SMapPair and SAvlListNode cuz those are just sequence of forkedits.
		
		TODO should there be STypedObject? SWithSalt?
				
		SWithSalt might complicate things.
		
		Technically it appears (TODO verify) I could use only a single Call type
		and literally store the S'es in things, and it would be harder to display
		but I could find a way, EXCEPT that single Call type would not include the datastructs
		of MapPair, AvlListPair, etc, and map returns value when key is param.
		It might be worth it to have only 1 call type.
		This would mean getting rid of ALL the S-coretypes and instead literally using S.
		Maybe also including ConsPair? No, ConsPair will still be a coretype
		that happens to be returned by f(?? "cons" x y) returns ConsPair<x,y>
		and ConsPair<x,y> is a func that given param z evals f(z x y),
		unlike in churchEncoding f(cons x y z) evals f(z x y)
		but will for practical purposes work the same
		and only in certain abstractions will anything have to be done differently.
		
		Im decided there will be "ab[cd].e.f[gh]=f(...)" syntax but undecided if needs its own datastruct
		since its just a wrapper for ops getdeep and putdeep on funcs that take map and return map.
		Im decided K wont have a special datastruct but will have the "," syntax.
		Im decided not to include special datastruct or syntax for S-map or S-avl-treelist.
		Im decided coretypeHttpBytes will not have special datastruct. 
		Im undecided how to do the S-types for F(...s-funcalls...) and L(...s-linkedlist...).
		Im undecided if TypedObject will have an S form or special syntax.
		Im undecided if WithSalt will have an S form or special syntax.
		
		Im extremely wanting to have only 1 Call type (other than datastructs like mappair and conspair)
		BUT im skeptical it can be done efficiently.
		
		I could get rid of TypedObject and coretypeHttpBytes and not replace them,
		and get rid of WithSalt by putting salt (default is nil) in most object types,
		or I could keep just WithSalt of that and create an S syntax for it
		similar to the S syntax for linkedlist and funcall.
		
		So the big question, that decides if theres just 1 main call type, is...
		Whats the expanded S form of F(a L(b c d) e) and is it too big?
		Its the expanded form of F(a expandedLbcd e), which is (not expanding expandedLbcd yet):
		F(F(a expandedLbcd) e)
		f(s f(s a expandedLbcd) e)
		f(f(s f(f(s a) expandedLbcd)) e)
		Thats not too big. As long as the form in memory tostrings as "F(a L(b c d) e)" its ok.
		Next, whats the expanded form of L(b c d)?
		L(b c d) is a func that, f(L(b c d) p) returns l(f(b p) f(c p) f(d p)) aka a linkedlist of
		what those 3 things eval to. Linkedlist is made of conspair<cp>, so it returns:
		cp<f(b p),cp<f(c p),cp<f(d p),nil>>>.
		What returns that?
		f(cons x y) returns cp<x,y>.
		f(L(b c d) p) returns cp<f(b p),cp<f(c p),cp<f(d p),nil>>>, but how?
		Whats the S form of L(b c d) (aka expandedLbcd) which does that?
		F(,cons b F(,cons c F(,cons d ,nil))). Is that it? (Its not fully expanded until theres f but no F)
		Does f(F(,cons b F(,cons c F(,cons d ,nil))) p) return cp<f(b p),cp<f(c p),cp<f(d p),nil>>>?
		Probably so, but todo verify.
		Even if I got the exact form wrong, its not extremely big. Its doable.
		...
		Therefore normal funcall will be the ONLY kind of funcall, with no S-coretypes,
		AND there will be syntax (autodetected from object shape) that makes combos of S etc
		look like "F(a L(b c d) e)". Cuz otherwise it gets really complex for func to build func.
		Parts will still be optimized as opencl and javassist.
		...
		Displayability in these various forms will be cached separately from these objects
		instead of representing, forexample, that something is an SCall or SLinkedListNode
		as which coretype.
	UNQUOTE.
	*/
	
	public Opcode leftmostOp(){
		return leftmostOp;
	}

	public Funcer L(){
		return x;
	}

	public Funcer R(){
		return y;
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

	public Id id(){
		throw new Error("TODO");
	}

	/** See comment of constructor.
	
	OLD...

	CacheFuncParamReturn.get(this,param) cuz without that many things,
	including basic use of the s lambda (the main controlflow, along with ``` currying
	to use a func of 1 param as a func of n params), would cost exponential.
	The other controlflow will be optimizations that use opencl (matrix multiply etc),
	javassist (sequential looping etc), and acyclicFlow (music tools etc).	
		
	Do I want Opcode.func to be BinaryOperator<Funcer> vs UnaryOperator<Funcer>?
	The difference is an extra Call is created which means its possible to have a Funcer
	that has not returned.
	Iotavm I couldnt get it to act as debugger of debugger of debugger cuz I couldnt cross levels
	the way I thought I could, but maybe Ill figure that out if look more into it
	as this gives me motive to.
	I wanted to represent all ops as combos of iota and optimize it so usually
	the iotas werent called, lazyEval some things such as not evalling f(cons x y)
	until f(cons x y z).
	I CHOOSE BinaryOperator.
		
	FIXME these funcs are in Opcode as javalambdas. Use those instead.
	
	byte cur = cur();
	final Funcer ret; //being final may help switch optimization?
	if(cur == 1){ //<this param> has cur 0, eval now
		switch(leftmostOp()){
		case plus:
			//<<? op> 3> param 
			ret = wrap(L().R().d()+param.d());
		break;
		case s:
			ret = TODO;
		break;
		}
		CacheFuncParamReturn.put(this, param, ret);
		return ret;
		//throw new Error("TODO depends if this is consPair, sCurryPair, or normalFuncallPair");
	}
	if(cur == maxCur){
		throw new Error("FIXME what to do here?");
	}
	int todoWhatHeader = TODO;
	use cur-1
	return new Call(todoWhatHeader, this, param);
	*/
	public Funcer f(Funcer param){
		Funcer cachedRet = CacheFuncParamReturn.get(this, param);
		if(cachedRet == null){
			cachedRet = leftmostOp.func.apply(this, param);
			CacheFuncParamReturn.put(this, param, cachedRet);
		}
		return cachedRet;
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
	
	public boolean contentFitsInId(){
		return false;
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

	public Funcer setSalt(Funcer salt){
		throw new Error("TODO");
	}

	public byte cur(){
		throw new Error("TODO");
	}

	public Funcer unstub(){
		throw new Error("TODO");
	}

	public int compareTo(Object o){
		throw new Error("TODO");
	}

}
