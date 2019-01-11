package occamsfuncer;

/** The wallet float64 is the only stateful part of occamsfuncer,
not counting cache as state since it can be rederived.
Every function call takes a walletLimit param.
The amount of wallet available to calls from there is Wallet.wallet-walletLimit.
A call may limit deeper calls to less than that, but not more,
by using a higher walletLimit (so Wallet.wallet-walletLimit is less).
Wallet.wallet only reduces during calls. It can increase only by an external
software modifying Wallet.wallet directly, between such calls,
which would normally happen between each 2 video frames of a game,
giving it just enough wallet to finish before the next video frame should happen.
Optimized code, such as Op.kernel (in a sandbox) using opencl andOr javassist,
costs much less wallet than interpreted ops done on java heap.
<br><br>
The behaviors of wallet may vary between implementations of Occamsfuncer
but hopefully will converge to some standard. The reason these will vary
is wallet should act kind of like an economy where renting of memory,
allocating of memory, freeing of memory, consuming of compute cycles,
and timing of those things, are traded in a free market between the
various parts of the program, automated by the internal workings
of occamsfuncer so each function call only needs to know
how much wallet it has and how much its willing to let each deeper
call possibly use, and the internal workings should either make that
happen or return an error object saying there was not enough wallet.
The other main kind of error object is saying a list/map key was not found.
See the isErr func for details on that. 
*/
public class Wallet{
	private Wallet(){}
	
	/** This is the money-like var modified by the consuming of compute resources
	as estimated in the walletLimit param in Funcall.f(Funcall,walletLimit) used recursively.
	Think of this money-like number like an economy that automatically trades
	between compute cycles and allocating and freeing of memory
	so you only need 1 unit of value in those funcs.
	*/
	public static double wallet;

	/** Since money is just a local calculation, not among multiple computers or people,
	its not done with blockchain security, so this doesnt waste compute cycles
	checking if you're spending a negative amount. This is singleThreaded.
	It returns true if it removed the param amount.
	Normally this is called many times after each small calculation,
	and fewer times with bigger amounts in opencl and javassist optimizations.
	*/
	public static boolean spend(double amount){
		double newWallet = wallet-amount;
		if(0 < newWallet){
			wallet = newWallet;
			return true;
		}else{
			return false;
		}
	}

}
