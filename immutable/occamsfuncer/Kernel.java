package immutable.occamsfuncer;

/** like an opencl kernel, for example.
<br><br>
This will only use OpenclUtil and call 1 kernel at a time, no queueing multiple
kernels but maybe add opcodes or just optimizations to do that later.
<br><br>
In occamsfuncer it will be an opcode/func that takes a list (TODO linkedlist or treelist?)
of params and returns a list. In the param list its all 1d float arrays, ints, and floats,
and the ints are combined various ways to know the compute cost before calling it (as in
HaltingDictator). This wont be combined with acyclicFlow cuz thats meant for many sequential
steps and needs at most 1 loop not loop in loop etc. The returned list its all 1d float or
int arrays. Each returned 1d float array will be used as a 2d array such as maybe each
get_global_id returns an ave and stdDev so [size/2][2] or maybe instead of [2] its the
size of one of the int params such as bigintegers made of ints. I'm not sure if my
hardware supports int arrays but I am sure it supports ints and floats and does not
support doubles. I'm unsure if it supports longs. Doubles will be added in future versions
after I get a newer computer to test on and maybe even then I wouldnt use them
since floats are probably faster, except in some cases I might need more precision.
To copy between those arrays efficiently, you can use another kernel or
there will be occamsfuncer opcodes.
Ranges will be proven before running the code. Runs in opencl or javassist (TODO).
Maybe I'll write it as occamsfuncer code of a certain simple pattern
and just optimize it.
UPDATE: instead of param and return being list, it will be a map, usually with string keys.
<br><br>
OLD...
<br><br>
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

f(
	f(?? ;callWithOptimizationHints)#optim
	m(;gpu 1)
	F(
		,f(?? ;fork)#fork
		,;forkIndex
		,;forkSize
		F(
			loop
			,;mutableCounterVarIndex
			,;immutableSizeVarIndex
			F(
				,prognPairModifiesOuterState
				F(...)
				F(
					prognPairModifiesOuterState
					F(
						,loop
						,;anotherMutableCounterVarIndex
						,;anoterImmutableSizeVarIndex
						/(inBC = in[b*dSize+c])
						F(
							,put
							f(?? ;identityFunc)#this
							'FIXME this isnt getting or setting this.inBC, its setting this'
							F(,* F(+ F(,get this ,;dSize) F(,get this ,;c)))
						)
					)
				)
			)
		)
	)
)
TODO Use this with 2 var indexs: ptr and valAtPtr to read float in[].

TODO need parallel opcode to use like loop. Call it fork.

FIXME fork should return a map like the params of OpenclUtil.callOpencl?
Or should it (each in parallel seqentially) forkEdit (different kind of forking)
the map (get by identityFunc) like loop and put funcs do?

TODO I want a syntax to make things like that shorter
but still represent it the same way in memory,
something like the "varname=" and "varname?" I was considering from earlier designs.
or maybe write it as =varname ?varname, like...
F(=x F(,+ ?y ?z))
no it needs to be 3 things:
F(= ,;x F(,+ ?y ?z))
still too long. How about an =(...) syntax...
=(;x F(,+ ?y ?z))
still too long.
How about a syntax that allows you to write things like normal proglangs
but only 1 line of code at a time? like this...
y(= ?inBC[?out] in[?b * ?dSize + ?c])
How about this.inBC.out = in[...]
How about inBC.out=in[...] without =(...)? Ambiguous so no.
...
I want this to generate javassist, jdk, and opencl code
and to use it for most of my programming cuz its so well optimized.
...
Choose a good syntax and I'll want to use it that way.
...
?(this ;aMap ;aKey ;aKeyInsideTheValueOfThatKey)
=(;aMap F(...))
...
F(,* F(+ F(,get this ,;dSize) F(,get this ,;c))) <-- rewrite this
F(,* F(,+ ?(this ;dSize) ?(this ;c)))
F(,* F(,+ ?dSize ?c))
...
?xyz means F(,get this ;xyz)
?(this ;abc ;xyz) means F(,get F(,get this ;abc) ;xyz)
...
Do I really want to not write "this" in ?xyz when it means the same as ?(this ;xyz)?
...
How about parts of javascript syntax in j(...)?
The "this" object in javascript would be forkEditable map.
https://en.wikipedia.org/wiki/JavaScript
...
All javascriptlike funcs take 1 param, and that param is read using
f(?? ;identityFunc)#this aka this
and to return something other than a forkEdit of this: this = something else;
and at the end of the func whatever this is, is returned...
wait, thats not right cuz this = x means this.this = x;
unless we write this.x = y; vs this = y;
We still need to write ?y instead of just y since y could be a f(...)#y
which is a constant (and has the same id regardless of its #name).
?this.?x = ?y;
Or maybe I allow x to be ;x if theres no #x else its #x,
but that makes code depend on names so NO.
Is that just the putdeep func I got rid of cuz it was hard to optimize?
F(,putdeep this l(;abc ;xyz) someVALUE)
How about Vars start with a capital letter, and consts are almost anything else,
like + can be the #+ name of the constant f(?? "plus")#+
and Plus could be a var name meaning F(,get this ;Plus). Remember ;Plus means "Plus".
Or consts could start with UppercaseOrSymbol and vars could start with lowercase.
...
UNDECIDED: Im not going to allow this = y; cuz thats not a stateless func.
A stateless func takes a param, normall #named #this and returns an object,
and that object may be a forkEdit of this or a completely different object.
It makes sense to say this.a = b but not to say this = b,
cuz this.a = b means return a forkEdit of this where the a key has value b.
WAIT... this = b just means replace all existing vars with b's contents,
so its ok. Its f(k b).

xyz=F(...) will be a syntax that means F(,put this ;xyz F(...))
or you could write xyz=,f(...) for   F(,put this ;xyz ,f(...)) if
you want to use the f(...) as the value of this.xyz. Remember ,abc means f(k abc)
The syntax =F(...) is unnecessary (so wont be included) and means this = F(...)
cuz its the same as ,F(...).
The syntax abc.def.xyz=F(...) means a few levels of get and put, returning a forkEdited this
even if this as param doesnt have an abc key its forkEdited form will.
abc.def.ghi=abc.jj
abc.def.ghi=abc.jj is a function, so you should be able to write...
abc.def.ghi=abc.def.ghi=abc.jj meaning to set the value of this.abc.def.ghi
to the function abc.def.ghi=abc.jj.
I guess it works since we know the order of the =s,
and for more complex things you can write
abc.def.ghi=f(this abc.def.ghi=abc.jj) since this is f(?? ;identityFunc).
So #names of consts are not allowed to contain ".",
and it may complicate the syntax for nums 3.45e-20
How about abc.def.ghi is the syntax for l(;abc ;def ;ghi) aka linkedlist of 3 strings?
F(,putdeep this abc.def.ghi F(...))
F(,put this abc.def.ghi F(...)) would use the linkedlist as the key
instead of its contents.
...
Its still annoying to write F(,* F(,+ ?dSize ?c))
I'd prefer something like "` * ` ?dSize ?c", like ` is used in unlambda,
but careful not to confuse between the ` `` ``` etc ops which wrap a func of 1 param
in that many curries and it gets them by L and R funcs.
...
How about `(` ` * ?multme ` ` + ?dSize ?c). TODO verify the ?vars get the correct param in `(...).
`(`*,`?dSize,?c)
F(,* F(?dSize ?c))
I like the F(...) syntax better since things tend to take variable number of params...
but it is annoying that + and * etc only take 2 params. I could make a syntax
+(?x ?y 5 ?z) that means the F(,+ ... F....) form,
but maybe its better to generalize that to the REDUCE of mapreduce
F(,reduce ,+ L(?x ?y ,5 ?z))
+(?x ?y 5 ?z) is much shorter though only works for +.
I could do that for a few common operators: + *.
How about a REDUCE SYNTAX...
r(,+ ?x ?y ,5 ?z) in memory is: F(,reduce ,+ L(?x ?y ,5 ?z))
Or should I write it as r(,+ L(?x ?y ,5 ?z)) in case I want to name the L(?x ?y ,5 ?z)?
...
How about instead of abc.def.xyz=F(...), write:
&abc.def.xyz=F(...)
and instead of in the rvalue writing abc.def.xyz write ?abc.def.xyz
so the first char tells what kind of thing it is,
and only consts dont need such a first char since they're everything else
and cant be named starting with certain chars.
Or how about F(...)&abc.def.xyz
...
F(,fork ,;forkIndex forkSize F(...)) returns a treelist/array of
what each f(F(...) aForkIndex) returns. TODO finish the opencl optimizable kernel example
code I wrote above with the loop and fork...
Remember, a treelist and a map both are func of key to value,
in the case of a treelist the key is the list index,
so F(,fork ,;forkIndex forkSize F(...) ,5) evals to the same as f(F(...) 5).
...
 
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
