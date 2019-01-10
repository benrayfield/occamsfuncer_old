package occamsfuncer;


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
