/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import java.util.Collections;

import immutable.occamsfuncer.funcers.Leaf;
import immutable.occamsfuncer.funcers.ListEmpty;
import immutable.occamsfuncer.funcers.MapPair;
import immutable.occamsfuncer.funcers.MapEmpty;
import immutable.occamsfuncer.funcers.Num;
import mutable.util.Time;
import mutable.util.Var;
import immutable.occamsfuncer.funcers.Import;

/** This is to be static imported in many code files in occamsfuncer */
public class ImportStatic{
	private ImportStatic(){}
	
	/** This is a stateful func that allocates more compute resources etc in HaltingDictator
	and empties cache of function calls in CacheFuncParamReturn.
	This will normally be called once per video frame (like 40 times per second).
	Im undecided if some function calls should be cached longer, but TODO get the basics working first.
	After calling this the next funcer call should probably call opcode.wallet and opcode.spend to limit the compute resources
	since this allocates Long.MAX_VALUE amount of resources, or TODO maybe it should allocate an amount of resources
	thats statistically detected to be available in the time of 1 video frame.
	And if you want nonstrict mode, TODO theres something similar to opcode.spend for that.
	*/
	public static synchronized void resetHaltingDictatorAndCache(){
		HaltingDictator.topIsStrict = true;
		HaltingDictator.topWallet = Long.MAX_VALUE;
		HaltingDictator.topExpireTime = Long.MAX_VALUE; //never
		HaltingDictator.timeNow = Time.nowNano();
		CacheFuncParamReturn.clear();
	}
	
	/** return this instead of throwing UnsupportedOperationException */
	public static Funcer evalInfiniteLoop(){
		//optimization of $(infinity) aka infinite loop, but since all calculations
		//must be prepaid but in this case lacks the compute resources to do that,
		//branch to the ELSE of the topmost Spend call.
		throw HaltingDictator.throwMe;
	}
	
	public static Funcer eval(String occamsfuncerCode){
		return eval(occamsfuncerCode, emptyMap);
	}
	
	/** namespace is map of string to Funcer */
	public static Funcer eval(String occamsfuncerCode, Funcer namespace){
		throw new Error("TODO");
	}
	
	//TODO if Wallet.top is private, how will opSpend and opWallet use it?
	
	/** pay 1 else throw */
	public static void $(){
		HaltingDictator.topWallet--;
		if(HaltingDictator.topWallet == 0){
			HaltingDictator.topWallet++;
			throw HaltingDictator.throwMe;
		}
	}
	
	/** UNDECIDED ABOUT... Its callers responsibility for param to be positive.
	TODO That will be enforced by sandbox only approving code which does that,
	but not enforced in code outside the system (its their responsibility to know how to use the system correctly).
	This is not a cryptocurrency, just a local counting and allocating of compute resources. 
	*/
	public static void $(long pay){
		if(pay < 0) throw new Error("pay negative: "+pay); //TODO as optimization should this be callers responsibility?
		HaltingDictator.topWallet -= pay;
		if(HaltingDictator.topWallet <= 0){
			HaltingDictator.topWallet += pay;
			throw HaltingDictator.throwMe;
		}
	}
	
	/** wrap */
	public static Funcer wr(double d){
		return new Num(d);
	}
	
	/** wrap */
	public static Funcer wr(Object o){
		if(o instanceof Funcer) return (Funcer)o;
		if(o instanceof Number) return wr(((Number)o).doubleValue());
		throw new Error("TODO");
	}
	
	public static final Funcer theImportFunc = Import.instance;
	
	public static final Funcer identityFunc = theImportFunc.f(""+Opcode.identityFunc);
	
	public static final Funcer one = wr(1.);
	
	public static final Funcer zero = wr(0.);
	
	//FIXME should nil be a class?
	public static final Funcer nil = theImportFunc.f(""+Opcode.nil);
	
	public static final Funcer emptyMap = MapEmpty.instance;
	
	public static final Funcer emptyList = ListEmpty.instance;
	
	/** Each JVM running occamsfuncer has at each moment 1 immutable state,
	and during the transition between states also has a prev immutable state.
	Its normally a f(?? ;igmoreparamFuncReturn map func) where the map is a namespace of various things it stores
	and func selfModifies the whole f(?? ;igmoreparamFuncReturn map func) when given a param,
	sometimes after external systems forkEdit the map. The param is normally nil but could be other things,
	since its better to forkEdit the map, but the root state is a func that can be called on any param.
	When called on nil it must return its next state but when called on other params it could return other things
	such as f(aRootState ;talk "hello whats your name?") might answer ;occamsfuncer.
	*/
	public static final Var<Funcer> rootState;
	static{
		rootState = new Var(nil);
	}
}