/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;

/** As an optimization, code may modify a leaf before any Funcer sees it.
To prevent a Funcer from seeing it, the pointer in this Mut is set to null
when a Funcer is created to wrap it, and (TODO) code will not be allowed
(TODO sandbox prevents it by constraints on what code strings can be run)
if that code has any var pointing at Mut.v (the object that may be wrapped in a Funcer later).
<br><br>
Example:
Mut<float[][][]> floats = ...;
for(...{
	floats.v[x][y][z] = floats.v[x+3*y][y][z]+7.89f;
	...
}
Funcer immutable = floats.freeze();
floats.v[-1][0][0] //throws ArrayIndexOutOfBoundsException, allowed
floats.v[0] //throws NullPointerException, but allowed.
//Also is high Wallet cost when anything is thrown cuz causes many cache misses
float[][][] floatsDotV = floats.v; //sandbox would prevent whole code from running if contains this line
System.out.println("this calls a func outside occamsfuncer"); //sandbox would prevent this, cuz no external funcs allowed except
//those whitelisted such as maybe java.lang.Math.sin(double) etc.
}catch(... //NOT allowed by sandbox unless proven to not interfere with Opcode.spend as in Wallet.throwMe.
Object x = new float[20][20]; //NOT allowed by sandbox cuz all nonstack memory allocation must go through Wallet.
Instead, Mut<float[][]> x = ...; or Funcer<float[][]> if want it to start as immutable instead of option to freeze later.
while(true); //Sandbox will NOT allow this cuz it doesnt pay to Wallet before branching (to loop again).
abc:
while(true){
	$();
	while(true){
		$();
		if(...) break abc; //SANDBOX wont allow this cuz you're not allowed to break to a label
		//(TODO make sandbox understand proofs about breaking to label so can prove it safe in some cases?)
		//and maybe breaking and continuing and switch statements in general wont be allowed?
		//Will gradually add more abilities that can be proven safe
		//(formal verification, kind of, though at a higher level than usual, instead of the hardware opcode level).
	}
}
...
int money = $prepayInt(x*y*z*z);
money--; //cuz if you do anything you have to pay at least 1, to prevent infinite loops
try{
	while(money > 0){
		...
		money--;
	}
}finally{
	$depositInt(money);
	money = 0;
}
//TODO things like that will be allowed as optimization, as long as they are proven not to counterfeit.
//Its not cryptocurrency, just the counting and distribution of compute resources
//from one fraction of the second to the next in local computer.
*/
public class Mut<LeafType>{
	
	/** the object allowed to be modified until (freeze()) any Funcer sees it,
	other than internal VM calculation of running code which Funcers command it to do atomicly.
	*/
	public LeafType v;
	
	public Funcer freeze(){
		LeafType wrapMe = v;
		v = null;
		throw new Error("TODO return Funcer that wraps wrapMe");
	}

}
