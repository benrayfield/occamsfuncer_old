/** Ben F Rayfield offers this software opensource MIT license */
package occamsfuncer.funcalls;
import occamsfuncer.Funcer;
import occamsfuncer.Op;
import occamsfuncer.Wallet;
import occamsfuncer.listmaps.EmptyListmap;
import occamsfuncer.util.Cache;
import occamsfuncer.util.Const;
import occamsfuncer.util.Err;

/** (incomplete) pure functions for number crunching AI research and MMG games
where millions of people can build things together in realtime gaming-low-lag
since the whole system is fork-editable in a microsecond and stateless,
planning opencl and javassist optimizations.
<br><br>
Immutable. Every object at VM level is either a funcall (of func/left and param/right)
or a leaf symbol or float64.
I'm undecided if I'll use the float64 form of the leafs or if they'll be something else.
<br><br>
------moved here from VM.java since its all going to be done in Funcer.java----
This is the next step in iotavm, binufnode, foundationOfMath research.
It will be a project on github etc, used for numbercrunching and realtime
puzzlegames and audivolv-like music tools etc.

sk solved by curry only 1 and use cons op as leaf, especially cons of 3 things
datahere leftconsornil rightconsornil and past that repeat datahere in childs.
undecided if cons of 2 or 3.
...
use recursion lambda and int param as kernel1d to optimize.
...
cache (by == and fast nondedup hashcode) sxyz funcalls in skstackframe or all
in root call clearing cache per video frame.
...
include float64 leafs but arrays only optimized as kernel1d Ike func that given
index returns float64. include + op, multiple, exp, onediv, but keep num of core
ops small.
...
include getmemcomleft op(from above, one num of money), and callwithlimit num
funcparam op. use java stack.
*/
public final class Funcall implements Funcer{
	
	
	/*TODO int to count curries observed (not needed)
	and int to cache which func is func/left func/left funcleft... how deep?
	The first few ops I want coded is Leafs.spend and Leafs.wallet and Leafs.s and Leafs.k,
	then test those very well before continuing.
	*/
	
	
	
	public final Funcer func;
	
	public final Funcer param;
	
	/* replaced by curries and leftmostOp field.
	/** is this immutable Funcer a RETURN else a LAZYEVAL. *
	public final boolean isRet;
	*/
	
	/** The only time this is anything other than null or a float64 is by the Leafs.kernel op.
	The Leafs.kernel op is capable of recursion such as checking the earlier values in such
	an array before writing next such values, as long as each is written only once,
	since the system must be immutable.
	Optimized recursion uses Leafs.recurse op with Leafs.kernel.
	Those are discounted ops in units of walletLimit/spend if
	optimized by opencl or javassist etc.
	<br><br>
	This is either a float64 or array of primitive to be viewed as float64,
	such as opencl optimization of Leaf.kernel might use float[] and other code might use double[]
	and graphics optimizations might use int[] for colorARGB,
	but by default the kernel type is double[], and the leaf type is a double or leafSymbol.
	<br><br>
	OLD: leaf andOr float64. I'm undecided if those will both be float64
	then cast to int then switch(int) optimize it, vs some other representation of leafs.
	Remember to define a very high range of float64 as char range,
	the same way it was done in much earlier humanainet code,
	something like range 2^51 to that plus 2^16-1 (FIXME what are the actual numbers?),
	and the low 32 bits are optional colorARGB of the char.
	Some of such chars could be opcodes, so at least they would be readable the normal way.
	*/
	public final Object data;
	
	/** This is one of the Leaf int types such as Leafs.s or Leafs.plus.
	TODO use this instead of the Leaf class. It will be just 1 Funcer class, no subclasses.
	Will use this plus at least 1 more thing, one that means is a funcall of 2 things intead of leafs.
	*
	public final int switchInt;
	*
	public final Op op;
	*/
	
	/** optimization of: Funcer f = this; while(!f.isLeaf()) f = f.func; return Op of that Funcer. */
	public final Op leftmostOp;
	
	/** how deep is leftmostOp. Example: {{{s x} y} z} is depth 3. Example: {s x} is depth 1. */
	public final int curries;
	
	public Funcall(Funcer func, Funcer param, Object data, Op leftmostOp, int curries){
		this.func = func;
		this.param = param;
		this.data = data;
		//this.switchInt = switchInt;
		//this.op = op;
		this.leftmostOp = leftmostOp;
		this.curries = curries;
	}
	
	/** used in f(Funcer,double) to curry 1 more param */
	public Funcall(Funcer func, Funcer param){
		this(func, param, null, func.leftmostOp(), func.curries()+1);
	}
	
	/** func/L if nonleaf. L of leaf is identityFunc, and R of leaf is itself, so {L() R()} returns the leaf. */
	public Funcer l(){
		return func!=null ? func : Op.i.fc;
		//FIXME identityFunc is {{SK}K} and should be in a static final var somewhere.
	}
	
	/** param/R if nonleaf. L of leaf is identityFunc, and R of leaf is itself, so {L() R()} returns the leaf. */
	public Funcer r(){
		return param!=null ? param : this;
	}
	
	/** If this wraps a double, returns it, else returns 0 */
	public double d(){
		if(data instanceof Double) return (Double)data; //excludes data==null
		return 0.;
	}
	
	public boolean isFloat(){
		return data instanceof Double;
	}

	/*TODO should there be an "int switchOptimize" here OR one of those objects
	I used in earlier versions of ufnode that as a javalambda and an int number of curries
	to wait before evaling it? Since I AM USING THE JAVA STACK HERE, I could do either way.
	
	TODO If this contains (in a certain position) Leaf.kernel, where is the double[] stored?
	This would then need to be usable as a func of int (in range of 0 to double[].length-1)
	and any other param would return some constant.
	
	"TODO cache which things? especially {func param return} until next rootcall, needed for Sxyz"
	
	"TODO use java stack, and make sure to implement the wallet and spend ops first"
	
	//TODO choose for Funcer to have the eval func in it vs VM, and make sure
	
	
	FIXME double maxSpend is correct parameter for user level code,
	but at the java/occamsfuncerVM level it must be the limit that wallet is not allowed to cross
	during recursion from here but can cross it in caller which may have a different wallet limit,
	which at user level the maxSpend is the difference between that and the caller's wallet limit.
	So how can I make a func that has a maxSpend param but actually do it as wallet limits nums?
	*/
	
	/*FIXME rename Leafs to Op since not all of them are leafs anymore.
	*/
	
	/** call this func on a param with a Leaf.spend second param,
	except that if it fails by not having enough Leaf.wallet, it returns null,
	compared to Leaf.spend which will have an extra param of what to return in that case.
	Spending is counted (FIXME or is it subtracted) in Wallet.wallet static var.
	
	Much of this is in https://en.wikipedia.org/wiki/Church_encoding etc.
	*/
	public Funcer f(Funcer p, double walletLimit){
	//public Funcer f(Funcer p, double maxSpend){
		
		/*FIXME should p include self?
		oneDiv uses 1./p.r().d() instead of 1./p.d().
		Easily solved for all ops this way:
		Use this.l() and this.r() and p.
		Example: In {{{s x} y} z}, this is {{s x} y} and p is z.
		This will need to be fixed in many of the switch cases below
		which assumed p is {this p}.
		*/
		Funcer L = this;
		Funcer R = p;
		
		if(!Wallet.spend(1)){ //Every action costs at least 1, proves no infinite loops
			return null;
		}
		
		//FIXME consider this.curries. Each Op has a number of curries it evals at,
		//and just return this if less than that, but what if more?
		
		if(curries < leftmostOp.waitCurries){
			//Example: s or {sk} or {{sk}k} dont have enough curries to eval so return self
			return new Funcall(this,p); //curry 1 more
		}
		
		Funcer ret = Cache.get(this,p);
		if(ret != null) return ret;
		
		if(leftmostOp.waitCurries < curries){
			//Example: {{{{sk}k}x}y}, cuz need to eval {{{sk}k}x} first 
			throw new Error("leftmostOp.waitCurries < curries, s");
		}
		
		switch(leftmostOp){
		case wallet:
			//{wallet x} ignores x and returns amount of wallet available here
			return wrap(Wallet.wallet-walletLimit); //dont cache cuz Wallet is stateful
		case spend:
			//{{{{spend returnThisIfFail} maxSpend} func} param}
			//returns what {func param} returns else returnThisIfFail
			Funcer func = L.r();
			Funcer param = R;
			ret = Cache.get(func, param);
			if(ret != null){
				return ret;
			}else{
				Funcer LL = L.l(); //{{spend returnThisIfFail} maxSpend}
				double maxSpend = LL.r().d();
				double newWalletLimit = Wallet.wallet-maxSpend;
				ret = func.f(param, Math.max(newWalletLimit,walletLimit));
				if(ret.isErrWal()){
					return LL.l().r();
					//returnThisIfFail, since at user level theres no java-null or error objects
					//dont cache, cuz it failed but could succeed again with more wallet,
					//or in rare cases could succeed again with less wallet cuz you can just
					//check the wallet and act nondeterministicly based on it.
				}
			}
			break;
		case l: //left at VM level is in funcalls, not conses
			//{l x} returns the func in {func param} which evals to (anything equal to) x
			ret = R.l(); //cache, cuz could be long chain of l and r
			break;
		case r: //left at VM level is in funcalls, not conses
			//{r x} returns the param in {func param} which evals to (anything equal to) x
			ret = R.r(); //cache, cuz could be long chain of l and r
			break;
		case t: //t/true aka k
			//{{t x} y} returns x
			return L.r(); //dont cache, cuz is so simple
		case f: //f/false
			//{{f x} y} returns y. Could be emulated as {t i}
			return R; //dont cache, cuz is so simple
		case s:
			//{{{s x}y}z} returns eval of {{x z}{y z}}
			Funcer LL = L.l();
			Funcer x = LL.r();
			Funcer y = L.r();
			Funcer z = R;
			Funcer xz = x.f(z,walletLimit);
			if(xz.isErrWal()){
				return xz;
			}else{
				Funcer yz = y.f(z,walletLimit);
				if(yz.isErrWal()){
					return yz;
				}else{
					Funcer xzyz = xz.f(yz,walletLimit); //{{xz}{yz}}, null if it failed
					if(xzyz.isErrWal()) return xzyz;
					ret = xzyz; //cache
				}
			}
			break;
		case i: //identityFunc
			//{i x} returns x
			return R; //dont cache, cuz is so simple
		case obWeakEquals:
			//{{obWeakEquals x} y} returns x==y using the java op ==
			x = L.r();
			y = R;
			ret = wrap(x==y);
			break;
		case floatEquals:
			//{{floatEquals x} y} returns x.d()==y.d(), but remember that nondoubles all eval to 0
			return wrap(L.r().d()==R.d()); //dont cache, cuz is so simple
		case lt:
			//{{lt x} y} returns x.d()<y.d(), but remember that nondoubles all eval to 0
			return wrap(L.r().d()<R.d()); //dont cache, cuz is so simple
		case plus:
			ret = wrap(L.r().d()+R.d());
			break;
		case oneDiv:
			ret = wrap(1./R.d());
			break;
		case isFloat:
			//{isFloat x} returns t or f
			ret = wrap(R.isFloat());
			break;
		case recurse:
			//Lx.Ly.x(cons x y)
			//{{recurse x} y} returns eval of {x {{cons x} y}}
			
			//TODO optimize since cons of 2 params could be proven not to have error.
			
			x = L.r();
			y = R;
			Funcer consX = Op.cons.f(x,walletLimit);
			if(consX.isErrWal()){
				return consX;
			}else{
				Funcer consXY = consX.f(y,walletLimit);
				if(consXY.isErrWal()){
					return consXY;
				}else{
					Funcer xConsXY = x.f(consXY, walletLimit);
					if(xConsXY.isErrWal()){
						return xConsXY;
					}else{
						ret = xConsXY; //cache
					}
				}
			}
			break;
		case kernel:
			/*This is where the extreme optimization happens (opencl, javassist, etc).
			 
			Op.kernel says QUOTE Curries so kernel works the same as
			the func its optimizing. These 2 should return the same:
			{kernel funcToOptimize arraySize {cons index otherData}}
			{funcToOptimize {cons index otherData}}
			so {kernel funcToOptimize arraySize}
			should be an optimization of funcToOptimize
			as long as its only called on integer float64s
			in range 0 to arraySize-1 (else return 0).
			{kernel funcToOptimize} can inTheory be optimized
			so it works on multiple sizes given later.
			UNQUOTE.
			*/
			throw new Error("TODO call OpenclUtil, Javassist, beanshell, etc, in sandboxed way");
		case cons:
			//Lx.Ly.Lz.zxy. Its optimized to not call cons when cons is param of car* or cdr*,
			//but since it was called, do it the slow way.
			//{{{cons x} y} z} returns eval of {{z x} y}, as in https://en.wikipedia.org/wiki/Church_encoding
			x = L.l().r();
			y = L.r();
			z = R;
			Funcer zx = z.f(x,walletLimit);
			if(zx.isErrWal()) return zx;
			Funcer zxy = zx.f(y,walletLimit);
			if(zxy.isErrWal()) return zxy;
			ret = zxy; //cache
			break;
		case car:
			//FIXME whats the exact lambda form of car and cdr, for if its param is not a cons?
			throw new Error("TODO");
		case cdr:
			//FIXME whats the exact lambda form of car and cdr, for if its param is not a cons?
			throw new Error("TODO");
		case carElseL:
			//FIXME whats the exact lambda form of car and cdr, for if its param is not a cons?
			throw new Error("TODO");
		case cdrElseR:
			//FIXME whats the exact lambda form of car and cdr, for if its param is not a cons?
			throw new Error("TODO");
		case emptyListmap:
			return EmptyListmap.instance;
		case exp:
			ret = wrap(Math.exp(R.d()));
			break;
		case listCat:
			//FIXME does this cost log or log squared? Already paid 1 before the switch,
			//but should it compute this cost at runtime? Or should it estimate it as some constant?
			if(!Wallet.spend(100)) return Err.wal;
			ret = L.cat(R);
			break;
		case listmap:
			throw new Error("TODO");
		case listmapEquals:
			throw new Error("TODO");
		case listmapGet:
			if(!Wallet.spend(10)) return Err.wal;
			//TODO does this return Funcer or double? L.r().get(R);
			throw new Error("TODO");
		case listmapHas:
			throw new Error("TODO");
		case listmapMax:
			return R.maxKey();
		case listmapMin:
			throw new Error("TODO");
		case listmapPreExcl:
			throw new Error("TODO");
		case listmapPreIncl:
			throw new Error("TODO");
		case listmapRem:
			throw new Error("TODO");
		case listmapSize:
			throw new Error("TODO");
		case listmapSufExcl:
			throw new Error("TODO");
		case listmapSufIncl:
			throw new Error("TODO");
		case log:
			ret = wrap(Math.log(R.d()));
			break;
		case mapPut:
			throw new Error("TODO");
		case mult:
			ret = wrap(L.r().d()*R.d());
			break;
		case neg:
			ret = wrap(-R.d());
			break;
		case one:
			return Const.one;
		}
		
		if(ret != null) Cache.put(L, R, ret);
		return ret; //returning null means wallet didnt spend enough
	}
	
	public static Funcer wrap(double d){
		return new Funcall(null, null, d, Op.i, Integer.MAX_VALUE);
	}
	
	public static Funcer wrap(boolean b){
		return b ? Op.t.fc : Op.f.fc;
	}

	public boolean isErr(){
		return false;
	}

	public boolean isErrWal(){
		return false;
	}

	public boolean isErrGet(){
		return false;
	}

	/** 0 cuz not a listmap */
	public long size(){
		return 0;
	}

	/** Err.get cuz not a listmap */
	public Object lowKey(){
		return Err.get;
	}

	/** Err.get cuz not a listmap */
	public Object highKey(){
		return Err.get;
	}

	/** Err.get cuz not a listmap */
	public Object get(Object key){
		return Err.get;
	}

	/** acts like an empty listmap */
	public Funcer put(Object key, Object val){
		return EmptyListmap.instance.put(key,val);
	}

	public Funcer rem(Object key){
		return this;
	}

	public Funcer cat(Funcer list){
		return list;
	}

	public Funcer preI(Object key){
		return EmptyListmap.instance;
	}

	public Funcer preE(Object key){
		return EmptyListmap.instance;
	}

	public Funcer sufI(Object key){
		return EmptyListmap.instance;
	}

	public Funcer sufE(Object key){
		return EmptyListmap.instance;
	}

	public boolean equalsForest(Funcer m){
		throw new Error("TODO");
	}

	public Op leftmostOp(){
		return leftmostOp;
	}

	public int curries(){
		return curries;
	}

	public Funcer minKey(){
		return Err.get;
	}

	public Funcer maxKey(){
		return Err.get;
	}

}

