package immutable.occamsfuncer;

/** occamsfuncer objects should compile other occamsfuncer objects to these opcodes
in some cases when its code that doesnt have much branching and needs optimization
in opencl, javassist, etc.
Nomatter what opcodes are generated this will not (in theory) do anything dangerous,
unlike C programming where you can easily screw up,
and its amount of memory and compute cycles are limited by HaltingDictator as usual.
So its ok for your compiler to be untrusted code which generates combos of these opcodes,
since this lower level which runs the opcodes you are supposed to trust.
*/
public class AssemblylikeTensorlikeAcyclicflowlikeOpcodes{
	
	/*public static final class VM{
		
		//private WHATTYPE registerInL, registerInR, registerOut;
		//TODO are these registers VAL or PTR or can be used as either?
		//double or float or int?
		private int registerInLAddr, registerInRAddr, registerOutAddr;
		//private double[] dMem; ??? i dont like having 3 of these. Should use C++ with JNI instead?
		//private float[] fMem;
		//private int[] iMem;
		private float[] mem; //all floats, cast (int) for pointers, and optimize to int in opencl etc?
		Or I could do everything as ints, including approximation of scalar math.
		
		Most ops will EITHER be 4 bytes, 3 addresses 0..255 and 1 transform of (int,int)->int,
		OR still a 1 byte opcode and a 24 bit address to copy into a register.
		
		int opcodes may each read andOr write any set of registers and execute that,
		or may be a part of a 2->1 op by for example copying from larger mem
		into registerInL then copying from larger mem into registerOut
		(todo VAL or PTR?) then op negate reads something at (address in?) registerInL
		and writes its negation at address in registerOut
		
		Example:
		opcodeInt
		x loopUpTo 70{
			opcodeInt
			opcodeInt
			y loopUpTo 30
				opcodeInt
				z loopUpTo 50{
					opcodeInt
					opcodeInt
					opcodeInt
					opcodeInt
				}
			}			
		}
		opcodeInt
		
		Each opcodeInt or set of a few of them reads 2 things and writes 1 thing
		at 3 addresses such as multiply 2 scalars to get 1 scalar or bit shift etc.
		
		There is only 1 global const memory and allowed range of it per such kernel call,
		and only 1 output range and get_global_id(any number of dimensions derived by % / etc)
		per such call. If you need to put multiple CLMem params then CLMem copy to adjacent first.
		
		These intheory are a good model of opencl ndrange kernel and maybe convolutional/image kernel
		and hopefully also [acyclicFlow of bigger code size and not parallel].
		
		Each loop size will come in as param and be proven not to read out of allowed mem range,
		of global readonly memory similar to "opencl global" but can read and write in
		kernel local memory (such as up to 256 addresses which include loop vars and loop var sizes).
		Only pointer arithmetic (such as convolutional, matmul, etc) derived from
		such loop vars and sizes of loop vars (as binary forest deriving things that are updated
		each by 1 opcodeInt by VM)... only such proven numbers can be used in pointerArithmetic,
		which will help in opencl compiler optimizing data flows when this format is
		translated to opencl andOr javassist etc.
	}
	
	/*negate1ScalarTo1Scalar
	
	add2ScalarsTo1Scalar
	
	multiply2ScalarsTo1Scalar
	
	tensor ops
	
	copyRange
	
	etc
	*/

}
