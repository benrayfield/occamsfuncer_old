package immutable.parallel;

/** lazyevaled object, a 1d array or primitive
thats input to [0 or more] and output of 1 at most 1 Parop,
and if its an output then its a certain index of output of that Parop.
*/
public interface Lazob<T>{
	
	/*TODO should ONLY 1d primitive arrays be allowed?
	For example instead of Integer use an int[1]. In opencl it would be const int so no speed loss there.
	A code string could be byte[] utf8 or char[] utf16.
	*/

	
	/** may trigger lazyEval recursively. Normally this is only called on a few Lazobs at top of forest */
	public T val();
	
	/** array length if valType() is array, such as ((float[])val()).length.
	Unlike val(), this doesnt trigger lazyEval. This is needed for proving pointers stay in range.
	TODO what should this return if its a primitive instead of 1d array of primitive? 1? 0? -1?
	*/
	public int valLen();
	
	/** bits of mem in val such as valSize()*32. if val() instanceof float[],
	not including slight overhead of internal JVM pointer counting etc. Doesnt trigger lazyEval.
	*/
	public double valMem();
	
	/** Examples: float[].class, Integer.class */
	public Class<T> valType();
	
	/** the Parop whose output index fromInd() is this */
	public Parop from();
	
	/** this.val() is the fromInd()'th output of from() */
	public int fromInd();

}
