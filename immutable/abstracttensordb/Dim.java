package immutable.abstracttensordb;

/** double[sum for each !isAbstract dim: mult*position] where startIncl<=position<endExcl.
mult*=2 when evens() or odds(), and startIncl andOr endExcl shrink to about half size
*/
public class Dim{
	
	public final int startIncl, endExcl;
	
	/** SimpleTensor.data[sum for all dims: Dim[dimIndex].mult*positionInThatDimIndex].
	This allows dims to be permutated without changing double[] SimpleTensor.data.
	*/
	public final int mult;
	
	/** an abstract dim is added by pushDim and is not stored.
	Example: matrix XY multiply YZ returns XZ,
	but first they must have the same dim sizes so XY push a dim the size of Z,
	and XZ push a dim the size of X, and permutate dims to align (so both are XYZ not XZY),
	then multiply 2 3d tensors per-element, then popDim(dim index of y,"+") to get XZ
	by summing the y dim.
	*/
	public final boolean isAbstract;
	
	public Dim(int startIncl, int endExcl, int mult, boolean isAbstract){
		this.startIncl = startIncl;
		this.endExcl = endExcl;
		this.mult = mult;
		this.isAbstract = isAbstract;
	}
	
	public int size(){ return endExcl-startIncl; }
	
	public Dim evens(){
		throw new Error("TODO mult*=2 when evens() or odds(), and startIncl andOr endExcl shrink to about half size");
	}
	
	public Dim odds(){
		throw new Error("TODO mult*=2 when evens() or odds(), and startIncl andOr endExcl shrink to about half size");
	}
	
	/** prefix in this dim. param endExcl is in range 0 to size()-1,
	compared to this.endExcl*this.mult which is relative to double[] this is a subset of.
	*/
	public Dim pre(int endExcl){
		if(endExcl<0 || size()<endExcl) throw new IndexOutOfBoundsException("param endExcl="+endExcl);
		return new Dim(this.startIncl, this.startIncl+endExcl, mult, isAbstract);
	}
	
	/** suffix in this dim. param startIncl is in range 0 to size()-1,
	compared to this.startIncl*this.mult which is relative to double[] this is a subset of.
	*/
	public Dim suf(int startIncl){
		if(startIncl<0 || size()<startIncl) throw new IndexOutOfBoundsException("param endExcl="+endExcl);
		return new Dim(this.startIncl+startIncl, this.endExcl, mult, isAbstract);
	}

}
