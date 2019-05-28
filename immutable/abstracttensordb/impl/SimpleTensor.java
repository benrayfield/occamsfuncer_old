package immutable.abstracttensordb.impl;
import immutable.abstracttensordb.Dim;

public class SimpleTensor{
	
	public final Dim[] dims;
	
	/** n dimensional in 1d, described by Dim[]. Size of this array is multiply of Dim.size where !Dim.isAbstract. */
	public final double[] data;
	
	public SimpleTensor(Dim[] dims, double[] data){
		this.dims = dims;
		this.data =  data;
	}

}
