package immutable.occamsfuncer;

/** like an opencl kernel, for example.
Params are const ints, 1 readonly range of global float[],
and writes 1 float per get_global_id (which parallel call in kernel).
Its a sequence of code with loops and sequences in loops and sequences
but all loops are of size of a constant int so the whole number
of calculations is constant once you know the params of the kernel call.
Theres mutable float and int vars local to the get_global_id,
which can be modified at any part of the loop.
Each such op is an int thats used as 4 bytes: 3 indexs and 1 opcode.
Local memory is therefore limited to 256 things,
which include the params of the kernel func,
the ints looping from 0 to eachParam-1,
get_global_id, and some mutable float and int indexs can use for anything.
It will be proven not to read or write memory outside allowed ranges
and obey read vs write permissions and exactly how many calculations.
<br><br>
Example, matrix multiply, and each of these var names refers to
a specific index 0..255:

params: float[] in, parallel b bSize, int cSize, parallel d dSize
//out[b*dSize+d] = sum of multiply(in[some func of b and c],in[some func of c and d])
//b and c are already set. They are like get_global_id(0) and (1) except supports maybe up to 16 dims
float out = 0;
float sum = 0;
float inBC = 0;
float inCD = 0;
float mult = 0;
int bcSize = bSize*cSize;
loop c cSize
	inBC = in[b*dSize+c]; //FIXME can only do 1 (number,number)->number op per line
	inCD = in[bcSize+c*dSize+d]; //FIXME
	mult = inBC*inCD;
	sum = sum + mult;
//out[b*dSize+c] = sum; //FIXME I dont want it doing pointer math on the out. just use an out var.
out = sum; //whatever the "out" var equals at the end, thats what the kernel returns for those parallels.

Do I want an opcode for read and an opcode for write,
where theres only 1 float[] in and only 1 float[] out,
and read takes an int index where to read, and out takes no index since its already known,
and both of them have to know where to put it? Or how about 1 of the vars is the last read float?
Also, I'd be ok with having a constant number of floats output per kernel,
such as outputting ave and stdDev which another kernel could read and output in other places,
but not giving it freedom to output at variable index.

For the read index, how about theres a certain 2 vars ptr and valAtPtr that whenever (int)ptr
is changed, (float)valAtPtr is automatically changed?
In opencl that would be written as "ptr = ...; x = x + in[ptr];" etc.

(
	loop
	mutableCounterVarIndex
	immutableSizeVarIndex
	(
		prognPairModifiesOuterState
		(...)
		(
			prognPairModifiesOuterState
			(
				loop
				anotherMutableCounterVarIndex
				anoterImmutableSizeVarIndex
				(...)
			)
		)
	)
)
Use this with 2 var indexs: ptr and valAtPtr to read float in[].
*/
public class Kernel{
	
	/*UPDATE: for acyclicflow music tools, just do func of double[],int[],int to double[]
		where the third int param is the size of double[] to return to get the last
		that many values from the int[] opcodes which are each int12Ptr,int12Ptr,int8Opcode.
		This will be fast enough for realtime music on cpu cuz will be in one of the cpu caches
		(not sure if L1 L2 etc) but probably wont be in general mem
		and even if it is in general mem it just means have to limit it to size ~70 double[]s
		instead of forexample size 300.
		Each int8Opcode is a (double,double)->double func such as multiply, plus,
		negate(1 double), java.lang.Math.sin(double), asin, exp, sqrt, sigmoid, tanh, log1p, etc.
		AudivolvGates can be derived from these, though these are more general
		and I dont plan to limit them to audivolv-like ops.
	...
	The model of kernels in the comment of this class
	(loop mutableCounterVarIndex immutableSizeVarIndex (...body...))
	seems correct though could use some alignment to opencl since opencl might be
	confused by my compiler's optimization attempts which opencl would also try to optimize
	as if it were written by a Human.
	
	I want general use of CLMem objects and to use both CPU and GPU
	(which on my laptop I see 2 CLDevice objects, 1 that appears to be cpu and 1 gpu,
	and the cpu supports float64 and the gpu float32, among other "capabilities").
	I want CLQueue'd work scheduled to flow between cpu and gpu
	during a low-lag time then return to immutable objects outside opencl.
	but be careful to not let opencl lag by moving things between cpus
	which at least in java is slower than gaming-low-lag even to move 1 byte.
	
	TODO?
	*/

}
