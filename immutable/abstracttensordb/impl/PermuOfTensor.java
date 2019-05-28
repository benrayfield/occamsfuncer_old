package immutable.abstracttensordb.impl;

import immutable.abstracttensordb.Tensor;

/** group known permutations of dims of the same tensor together to avoid recomputing that tensor,
though there are many combos of ops such as push a dim then pop it, which would not be detected
as the same tensor.
*/
public class PermuOfTensor{
	
	public final int[] permu;
	
	public final Tensor tensor;
	
	public PermuOfTensor(int[] permu, Tensor t){
		this.permu = permu;
		this.tensor = t;
	}

}
