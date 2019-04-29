package immutable.occamsfuncer.funcers;

import immutable.occamsfuncer.CacheFuncParamReturn;
import immutable.occamsfuncer.Funcer;
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
	
	TODO how to represent ImportFunc/? such as <? "plus" 3 4> evals to 7,
	and <<? "plus" 3>#+ <+ 4 5>> returns 12, 
	and <? "plus"> needs to store data so its optimized as a hardcoded opcode
	even though its not written that way.
	ImportFunc/? will be a Leaf and will have its own part of int header.
	
	Also I need datastruct for ````` var number of curries
	
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
	
	
	public Call(int header, Funcer x, Funcer y){
		super(header);
		this.x = x;
		this.y = y;
		
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
	}
	
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

	public Funcer f(Funcer param){
		Funcer cachedRet = CacheFuncParamReturn.get(this, param);
		if(cachedRet != null) return cachedRet;
		//CacheFuncParamReturn.get(this,param) cuz without that many things,
		//including basic use of the s lambda (the main controlflow, along with ``` currying
		//to use a func of 1 param as a func of n params), would cost exponential.
		//The other controlflow will be optimizations that use opencl (matrix multiply etc),
		//javassist (sequential looping etc), and acyclicFlow (music tools etc).
		
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

}
