/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import immutable.occamsfuncer.funcers.Leaf;
import immutable.occamsfuncer.funcers.Num;

/** This is to be static imported in many code files in occamsfuncer */
public class ImportStatic{
	private ImportStatic(){}
	
	public static Funcer wrap(double d){
		return new Num(d);
	}
	
	public static final Funcer identityFunc = null; //FIXME
	
	public static final Funcer one = wrap(1.);
	
	public static final Funcer zero = wrap(0.);
	
	public static final Funcer nil = zero; //FIXME should be a separate object than zero
}