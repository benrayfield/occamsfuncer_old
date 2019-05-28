/** Ben F Rayfield offers this software opensource MIT license */
package immutable.abstracttensordb.old;

/** Its abstract in how a tensor can be much bigger than is stored anywhere
and instead represents a possible calculation that may be optimized some other way,
normally lazyEvaled at the top few tensors of a forest of much bigger tensors
such as matrix multiply might cost n^3 space if stored literally during the calculation
insted of n^2. 
<br><br>
do I want parallel opcodes similar to sql where all tables have 1 scalar
column and the other columns are compositeKey of dense array ranges such as representing
float[50][30] and can multiply that by a float[30][70] by forkEdit adding a dimension
of 70 to the first (copies all its floats abstractly) and forkEdit adding a dimension
of 50 to the second (copies all its floats abstractly) and reordering dims so they are
both 50 30 70 then doing a per-element multiply tben REDUCE the dimension (the one of
size 30) by SUM. All scalar ops are either REDUCE or perElement. All other ops are for
dimensions. This would maybe make opencl optimization easier to sandbox.
*/
public enum MatOpcode{
	
	/** per-element multiply between 2 matrix of any num of dims *
	elElMul,
	
	elElAdd,
	
	/** just 1 matrix *
	elNeg,
	
	dimMax,
	
	FIXME mostly the same ops will exist perElement and in a REDUCE,
	such as REDUCE by in pairs do ADD until the whole dim is size 1.
	*/

}
