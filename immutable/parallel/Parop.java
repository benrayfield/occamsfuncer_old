package immutable.parallel;

import java.util.List;

/** parallel op, like an opencl kernel or acyclicFlow in a dependnet forest.
Designed for gaming-low-lag of large forests of many combos of these, like a JIT compiler.
Similar to OpenclUtil.callOpencl(String,int[],Object[]) which returns same size Object[],
there can be inputs and outputs of various (use as) immutable types such as
float[], double[], int[], int (which OpenclUtil doesnt support all of yet),
but unlike OpenclUtil, an index can only be input or output but not both.
*/
public interface Parop{
	
	/** Function definition is ins().get(0).val(),
	such as String of opencl code or some wrapper of int[] acyclicFlow ops,
	or various other models of computing I might include later.
	FIXME todo and verify: Only sandboxed function definitions will run,
	and others will return empty arrays of the expected sizes.
	<br><br>
	reading the metadata of these Lazobs doesnt trigger lazyEval of the Parops, but reading val() does.
	*/
	public List<Lazob> ins();
	
	/** reading the metadata of these Lazobs doesnt trigger lazyEval of the Parops, but reading val() does */
	public List<Lazob> outs();
	
	/** number of compute ops of the func from ins() to outs().
	This is the same number of ops regardless of gpu and cpu being faster at
	some things the other is slower at,
	such as matmul vs a million sequential steps of (float,float)->float each at 3 const indexs
	which is similar to what acyclicFlow does (int12 ptrA, int12 ptrB, int8 op, write to int12 &thisOp).
	This should be multiplied by an efficiency factor wherever its actually run,
	as a forest of Parops is meant to run on variety of software platforms together
	such as lwjgl opencl doing matmul and acyclicFlow's int[] opcodes on cpu
	and copying mem between them depending on needs of lag vs ops per sec.
	Opencl intheory supports cpu and gpu efficiently together
	but it doesnt compile nearly as fast as acyclicFlow's int[] opcodes
	which can run a new code in a few microseconds
	compared to lwjgl opencl's .2 second compile time.
	*/
	public double opsCost();
	
	//"TODO should code string be 1 of the ins() or should it be returned by func()?"
	//"Similar question for the const int params, should they go in ins() or somewhere else.""

}
