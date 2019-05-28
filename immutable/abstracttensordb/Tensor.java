/** Ben F Rayfield offers this software opensource MIT license */
package immutable.abstracttensordb;

import java.awt.Graphics;

import immutable.occamsfuncer.Opcode;

/** immutable, lazyEval, TODO auto optimized (opencl, javassist, beanshell, jdk, openjdk) at runtime */
public interface Tensor{
	
	/*
	TODO merge Tensor, Ndim.java, and Leaf.java (all will be Funcers).
	
	TODO change Funcer to have a bit that says isRet, which is true for things that eval to themself,
	and is false for things that have not been evaled yet such as f(s x y z) aka f(f(f(s x) y) z).
	f(f(s x) y) isRet, but f(f(f(s x) y) z) !isRet. This isRet bit will, not actually stored as a bit,
	but it will be Call.cur==0, or maybe it should be a bit since it can happen in things other than Call
	just by any Funcer thats !isRet being reachable from self that makes self !isRet
	and can find the paths to all !isRet by if !isRet then either its something local not finished
	or !L().isRet or !R().isRet. This is separate from Opcode.lazyEval and Opcode.triggerLazyEval
	but TODO maybe merge those vs keep them as thats user level lazyEval of 2 params
	interpretable as func and param vs this is or contains a Call datastruct whose work isnt done.
	I NEED THE EVAL TO HAPPEN LATER FOR SOME COMBOS OF TENSORS SUCH AS IN MATMUL
	IT NEEDS TO WAIT TO EVAL UNTIL THE WHOLE MATMUL IS DEFINED AS A FOREST OF TENSOR OPS
	INSTEAD OF CREATING THE 3D TENSOR THEN REDUCING IT TO 2D. ITS FAR MORE EFFICIENT
	TO OPTIMIZE TO OPENCL DOE WHICH DOES A LOOP FOR 1 OF THOSE 3 DIMS AND ONLY DOES IO OF 2 2D ARRAYS.
	THERE WILL BE OTHER THINGS OPTIMIZABLE, INCLUDING WHATS IN immutable.abstracttensordb.Test.
	
	Also I want occamsfuncer Opcodes made for every func in Tensor except the one that paints
	a Tensor of intARGB to a java.awt.Graphics since Graphics is stateful
	but I do want that code to be in a view of Funcer, maybe using Var<Funcer>
	which is a local publish/subscribe.
	
	I NEED A WAY TO DECIDE WHEN TO TRIGGER LAZYEVAL BEFORE ITS REQUIRED.
	LANGUAGES LIKE SCHEME WAIT TO EVAL UNTIL REQUIRED, OR SO THEIR DESIGN DOCS SAY,
	BUT I DONT THINK THATS ALWAYS TRUE. I THINK IF THEY WAITED THEN THEYD RUN OUT OF MEMORY
	SINCE THE ONLY TIME YOU ACTUALLY NEED TO EVAL IS WHEN RETURNING TO A STATEFUL PROCESS
	SUCH AS DO A BUNCH OF STUFF FOR HOURS THEN PRINTLN THE RESULT
	SHOULD TECHNICALLY IN SCHEME LAZYEVAL FOR HOURS AND RUN OUT OF MEMORY.
	
	TODO EVEN THOUGH THERES A BUILTIN AVL TREELIST TYPE,
	IT WOULD BE A GOOD TESTCASE TO DERIVE A SLOWERT AVL TREELIST WITHOUT USING THAT.
	
	I WANT ***NO RECURSIVE OPTIMIZATIONS IN OCCAMSFUNCERVM OTHER THAN STORAGE COMPRESSION*** AND INSTEAD
	I WANT OPCODES FOR HOW THE OPTIMIZATIONS WORK AND FOR OCCAMSFUNCER CODE TO
	COMMAND SUCH OPTIMIZATIONS TO HAPPEN (WHICH EACH ARE 1 OPCODE),
	ESPECIALLY THE TENSOR AND ACYCLICFLOW OPTIMIZATIONS.
	
	One of those opcodes will be func of constsizelist of tensors (of var sizes) to tensor (of derived size).
	A constsizelist of tensors can be viewed as a tensor of all their dims plus 1 more dim whose index
	is list size and whose value is the scalar in 1 of the tensors (depending on list index) in that list.
	Most kernel funcs can be viewed as a set of dim sizes, such as multiplying BCd by bCD (aka BC by CD)
	gives BD by REDUCE on c of BcD. But I dont want that abstraction. I want sequential ops.
	Viewing list<BC,CD> as a single tensor is <B,C0,C1,D,whichOf2Tensors> which is a 5d tensor,
	and in matmul its used with a size of B C D where it just happens to not read all the indexs.
	tripleloop b c d, val: mat[b][c][c][d][0]*mat[b][c][c][d][1] (aka BC[b][c]*CD[c][d]).
	But how to get it into a BD?
	*/
			
			
			
	
	
	
	/** Example: numType(true,32) chooses float32. numType(false,64) chooses long.
	But in early implementations only double will be supported (and depending
	on if opencl supports doubles reliably on enough computers I might change that to default float32.
	*/
	public Tensor numType(boolean isFloat, int bits);
	
	public int dims();
	
	public int size(int dimIndex);
	
	/** get value at position. position[i] ranges 0 to size(i)-1.
	This is inefficient compared to array(). Similar funcs f(int...) j i n.
	Use with numType(...) to tell it what precision and integer vs scalar you want.
	*/
	public double d(int... position);
	public default float f(int... position){ return (float)d(position); }
	public default long j(int... position){ return (long)d(position); }
	public default int i(int... position){ return (int)d(position); }
	public default Number n(int... position){ return d(position); }
	
	/** for each pair of positions, 1 in each tensor,
	reads 2 scalars and gives you 1 scalar in the same size of tensor.
	Op must be a func of 2 scalars to 1 scalar (such as multiply, plus, min, max).
	equalSize must have the same dims() and size of each as this.
	*/
	public Tensor bin(Object op, Tensor equalSize);
	
	/** same as bin but a unary op per scalar, such as NEGATE, SINE, ARCSINE, SIGMOID, EXP, ONEDIVIDE */
	public Tensor una(Object op);
	
	/** pushes a dim where the scalar at each position is the same at all positions in that dim */
	public Tensor pushDim(int size);
	
	/** REDUCE DIM. op may be a func of 2 scalars to 1 scalar (such as multiply, plus, min, max)
	or more complex ops may be supported in future versions such as median.
	Returns a Tensor with 1 less dims(), with dimIndex removed and others slide down.
	*
	public Tensor dimToS(int dimIndex, Object op);
	*/
	public Tensor popDim(Object reduceOp);
	
	/** flattens the top 2 dims into 1 dim whose size is the multiply of their sizes.
	Example: float[3][4][5].popFlat() is [3][20] and .popFlat() that is [60].
	*/
	public Tensor popFlat();
	
	/** Opposite of popFlat. Expands 1 dim into 2, given a size thats divisible. TODO which dim?
	float[3][4][5].popFlat().popFlat().pushDeflat(20).pushDeflat(5) rebuilds an equal float[3][4][5].
	*/
	public Tensor pushDeflat();
	
	public default Tensor swapDim(int dimIndex){
		int d = dims();
		if(dimIndex < 0 || d <= dimIndex) throw new IndexOutOfBoundsException("dims="+d+" tried="+dimIndex);
		int[] p = new int[d];
		for(int i=1; i<d; i++) p[i] = i;
		p[dimIndex] = 0;
		p[0] = dimIndex;
		return permuDims(p);
	}
	
	public Tensor permuDims(int... newDimIndexs);
	
	/** For convolutional math such as cellular automata or convolutional neuralnets.
	plus(position in siblingDim, position in newDim) has the same value as
	this's position in siblingDim. siblingDim shrinks so that sum of indexs cant go out of bounds.
	For example, float[30][50] convolve dim0 (size 30) at size 10
	returns float[21][50][10]. (0..20)+(0..9) ranges (0..29).
	returned[b][c][d]==original[b+d][c].
	*/
	public Tensor conv(int siblingDim, int size);
	
	/** concat 2 Tensors in 1 dimIndex where they can be different sizes,
	but all dims except that one must be the same size.
	x.pre(d,i).cat(x.suf(d,i)).equals(x), if d and i are in range.
	*/
	public Tensor cat(int dimIndex, Tensor sameSizeInAllDimsExcept);
	
	/** prefix in a certain dim. x.pre(d,i).cat(x.suf(d,i)).equals(x), if d and i are in range. */
	public Tensor pre(int dimIndex, int endExcl);
	
	/** suffix in a certain dim. x.pre(d,i).cat(x.suf(d,i)).equals(x), if d and i are in range. */
	public Tensor suf(int dimIndex, int startIncl);
	
	/** flips the positions in a dim, from 0 to size(dimIndex)-1 */
	public Tensor flip(int dimIndex);
	
	/** keeps only the even positions in dimIndex, so either half size or half+.5. */
	public Tensor evens(int dimIndex);
	
	/** keeps only the odd positions in dimIndex, so either half size or half-.5. */
	public Tensor odds(int dimIndex);
	
	/** Returns a Tensor of same size as this whose value is position in a certain dimIndex.
	Example: float[50][30][100] reflect 1 gives a [50][30][100] whose values are 0..29,
	and reflect 0 has values 0..49.
	This is needed cuz bin and una only see values, not indexs. This views index in a value.
	If you need to see multiple dim indexs, create a Tensor for each.
	Creating a tensor doesnt mean actually doing the op in memory.
	It means an abstract calculation thats likely to be optimized in common patterns
	to opencl (lwjgl) andOr java (javassist, beanshell, jdk, openjdk) code
	which uses local stack or openclKernel memory to do multiple Tensor ops at once
	such as a*b+c*d is written as using 4 Tensors to create 3 more Tensors,
	but only the topmost Tensor that returns that answer would go on the heap.
	A reflectIndex tensor wouldnt go on the heap since its
	just a loop var or opencl get_global_id(0) etc or int derived from it. 
	*/
	public Tensor pos(int dimIndex);
	
	/** a Tensor of same sizes but all values are the param */
	public Tensor fill(Number n);
	
	/** Pics arent all the same size. Going with array of Tensor alternateAftransPic for now,
	which has aftrans pic aftrans pic... of any even size, and allows different pic sizes.
	aftrans[3][3] of doubles (transforms <y,x,1>). pic[anyHeight][anyWidth] of int32 colorARGB.
	Returns pic[param height][param width]. 1-bit transparency in Alpha byte is supported.
	<br><br>
	Example: put the image of each char or word at many positions on screen.
	Example: in RbmEditor.java it displays 2d array of weights of each layer, and on diagonal
	it displays a tilted array of node states at different zigzagIndex and nodeIndex.
	Example: sprites in simple games, or generate a new sprite per video frame so its not really a sprite.
	Example: recursive rectangle components such as a program is a rectangle window on screen.
	<br><br>
	Im considering an optimization of this which would lazyEval the returned tensor's graphics
	only if observed and if not observed java.awt.Graphics would be used on alternateAftransPic.
	<br><br>
	OLD...
	Returns a Tensor[height][width] of int32 colorARGB.
	aftranses[picIndex][3][3] transforms <y,x,1> similar to:
	<br><br>
	https://docs.oracle.com/javase/7/docs/api/java/awt/geom/AffineTransform.html QUOTE.
		[ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
	    [ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
	    [ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
    UNQUOTE.
    <br><br>
	pics[picIndex][y][x] is int32 colorARGB, where A(lpha) is 255 for visible, 0 for invisible,
	<br><br>
	and by default only those 2 extremes (1 bit transparency) are supported
	but TODO??? future versions may support gradual transparency.
	<br><br>
	OLD: do I want 3d or more dimensions? Example: even in old games like mariokart
	there was 1 big 3d polygon of the ground. I could use aftrans[4][4] and still support that,
	though Im unsure how fast it would run on cpu or how fast it could stream new textures into opengl.
	Technically I could do bumpmapping using Tensor but Im skeptical of how fast
	opencl can do a lot of different kernels in parallel, though it does have funcs
	to schedule a dependnet of kernels.???
	Purefunctional code is not efficient for those kinds of things. Stick to 2d.
	YOU COULD OF COURSE DISPLAY 3D GRAPHICS ON THE PIXELS OF A SINGLE Tensor generated some other way.
	*
	public Tensor graphics(int height, int width, Tensor aftranses, Tensor pics);
	*/
	public Tensor graphics2d(int height, int width, Tensor... alternateAftransPic);
	
	public static void paint2d(Graphics g, Tensor aftrans, Tensor pic){
		throw new Error("TODO");
	}
	
	/** Example: double[50][30][100]. Everyone must not modify the array since it may be a backing array
	of 1 or more Tensors, and every Tensor is immutable. Or it may be a copy.
	A double is a 0 dimensional array of double.
	*/
	public Object array();
	
	/** example: double[50][30][100]. Everyone must not modify the array after giving it as param here
 	since it may become a backing array of 1 or more Tensors, and every Tensor is immutable,
 	or it may be copied. A double is a 0 dimensional array of double.
 	*/
	public static Tensor wrap(Object array){
		throw new Error("TODO");
	}


}
