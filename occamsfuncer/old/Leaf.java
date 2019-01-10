/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.old;

import occamsfuncer.Funcall;

/** a core op, like + or s or k */
public abstract class Leaf{
	
	/** how many curries to wait for before executing. Example: s waits on 3 */
	public final int waitCurries;
	
	/** Example: + or s or k.
	FIXME what type should symbol be? int? char? String? */
	public final Object symbol;
	
	/** see Funcall.f(Funcall,double). Use Wallet.wallet with walletLimit. */
	public abstract Funcall f(Funcall p, double walletLimit);
	
	public Leaf(Object symbol, int waitCurries){
		this.symbol = symbol;
		this.waitCurries = waitCurries;
	}
	
	//"TODO where to store the actual number of curries, and what if its more than waitCurries?"

}
