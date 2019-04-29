/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.old;

import immutable.occamsfuncer.Id;

public final class Funcer_TODOMakeThisAnInterface<LeafType>{
	
	//FIXME where does weakref go in here?
	
	public final int header; //often doesnt use the whole int. includes LContext RContext avlBalance etc.
	public final long maplistSize;
	public final Funcer_TODOMakeThisAnInterface L, R, minKey, maxKey;
	public final LeafType v; //leaf value. Examples: float[][][][][], Double, int[][], byte[] (could be generalText or semanticText, depending on int header)
	//Example: Funcer<float[][]> x = ...; x.v[5][7] is a float.

	public Funcer_TODOMakeThisAnInterface(
		int header, long maplistSize,
		Funcer_TODOMakeThisAnInterface L,
		Funcer_TODOMakeThisAnInterface R,
		Funcer_TODOMakeThisAnInterface minKey,
		Funcer_TODOMakeThisAnInterface maxKey,
		LeafType v
	){
	//public Funcer_TODOMakeThisAnInterface(boolean startFrozen, int header, long maplistSize, Funcer_TODOMakeThisAnInterface L, Funcer_TODOMakeThisAnInterface R, Funcer_TODOMakeThisAnInterface minKey, Funcer_TODOMakeThisAnInterface maxKey, LeafType v){
		//this.isFrozen = startFrozen; //mutable 1-way
		this.header = header;
		this.maplistSize = maplistSize;
		this.L = L;
		this.R = R;
		this.minKey = minKey;
		this.maxKey = maxKey;
		this.v = v;
		//Is this check for frozen too expensive?
		//if((L!=null&&!L.isFrozen)||(R!=null&&!R.isFrozen)||(minKey!=null&&!minKey.isFrozen)||(maxKey!=null&&!maxKey.isFrozen))
		//	throw new Error("Childs arent all frozen");
		
		//Am I going to allow "leaf", which is the only place arrays exist, to contain Funcer array?
		//NO. No Funcer arrays, cuz they would make garbcol and econacyc stats and database etc inefficient.
		//Use treelist and treemap of single Funcers for that.
	}
	
	//Or maybe these should all go in a separate mutable datastruct? For example, if you want zapeconacyc you use a different such datastruct with more fields for it?
	private Id myId; //lazyEval. Or should this be in a separate Map<Funcer,Id> (WeakHashMap?)?
	
	/*private boolean isFrozen;
	public boolean isFrozen(){ return isFrozen; }
	public void freeze(){ isFrozen = true; }
	*/
	
	//TODO implement Lambda<Funcer> interface like in Funcer.java...
	//
	//Lambda, except limited recursively by allowed compute and memory as defined
	//in Wallet.have and Wallet.keep (see Opcode.spend and Opcode.wallet)
	public Funcer_TODOMakeThisAnInterface f(Funcer_TODOMakeThisAnInterface param){
		throw new Error("TODO");
	}
	
	/** submap or sublist(depending on param viewed as number) */
	public Funcer_TODOMakeThisAnInterface prex(Funcer_TODOMakeThisAnInterface startExcl){
		throw new Error("TODO");
	}
	
	/** submap or sublist(depending on param viewed as number) */
	public Funcer_TODOMakeThisAnInterface sufx(Funcer_TODOMakeThisAnInterface endExcl){
		throw new Error("TODO");
	}
	
	public Funcer_TODOMakeThisAnInterface prei(Funcer_TODOMakeThisAnInterface endIncl){
		throw new Error("TODO");
	}
	
	/** submap or sublist(depending on param viewed as number) */
	public Funcer_TODOMakeThisAnInterface sufi(Funcer_TODOMakeThisAnInterface startIncl){
		throw new Error("TODO");
	}
		
	
	/** avl list prefix, else throws Wallet.throwMe if not an avl list */
	public Funcer_TODOMakeThisAnInterface prex(long endExcl){
		throw new Error("TODO");
	}
	
	/** avl list suffix, else throws Wallet.throwMe if not an avl list */
	public Funcer_TODOMakeThisAnInterface sufx(long endExcl){
		throw new Error("TODO");
	}
	
	/** concat this avl treelist with param viewed as 1 list item, else throws Wallet.throwMe if this is not an avl treelist */
	public Funcer_TODOMakeThisAnInterface cato(Funcer_TODOMakeThisAnInterface itemSuffix){
		throw new Error("TODO");
	}
	
	/** concat this avl treelist with contents of param avl treelist, else throws Wallet.throwMe if either is not an avl treelist */
	public Funcer_TODOMakeThisAnInterface catn(Funcer_TODOMakeThisAnInterface listSuffix){
		throw new Error("TODO");
	}
	
	/** If this is an avl treelist whose contents are all primitives, flattens into a single array,
	such as opencl or javassist might efficiently use, but whats returned is not efficiently forkEditable.
	If this is not such a treelist, returns this.
	*/
	public Funcer_TODOMakeThisAnInterface flat(){
		throw new Error("TODO");
	}
	
	public Funcer_TODOMakeThisAnInterface put(Funcer_TODOMakeThisAnInterface key, Funcer_TODOMakeThisAnInterface value){
		throw new Error("TODO");
	}
	
	public Funcer_TODOMakeThisAnInterface get(Funcer_TODOMakeThisAnInterface key){
		throw new Error("TODO");
	}
	
	public Funcer_TODOMakeThisAnInterface get(long key){
		throw new Error("TODO");
	}
	
	public Funcer_TODOMakeThisAnInterface get(int key){
		throw new Error("TODO");
	}
}
