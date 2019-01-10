/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.old.leafs;
import occamsfuncer.Cache;
import occamsfuncer.Funcall;
import occamsfuncer.old.Leaf;

public class S extends Leaf{

	public S(){
		super("S", 3);
	}
	
	public Funcall f(Funcall p, double walletLimit){
		//FIXME modify Wallet.wallet since this calculation has a small cost
		
		//check func call cache for triples of [func param return]
		//since otherwise this would often get exponentially slow.
		//Such cache will be cleared about as often as screen repaints (50x/sec?).
		//Should we check for that at every call (other than optimized calls such as kernel)?
		Funcall cachedRet = Cache.get((Funcall)(Object)this,p); //FIXME
		if(cachedRet != null) return cachedRet;
		//FIXME why is this code in S which is not extending Funcall
		//which Cache expects 2 Funcalls?
		
		//(((Sx)y)z) returns eval of (xz(yz)). Use java stack.
		Funcall pf = p.func; 
		Funcall x = pf.func.param;
		Funcall y = pf.param;
		Funcall z = p.param;
		Funcall xz = x.f(z,walletLimit);
		if(xz == null) return null; //check for nulls in case it fails
		Funcall yz = y.f(z,walletLimit);
		if(yz == null) return null;
		Funcall xzyz = xz.f(yz,walletLimit);
		Cache.put((Funcall)(Object)this, p, xzyz); //FIXME
		return xzyz; //null if it failed
	}

}
