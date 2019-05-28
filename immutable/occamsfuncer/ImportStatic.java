/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import java.util.Collections;

import immutable.occamsfuncer.funcers.Leaf;
import immutable.occamsfuncer.funcers.ListEmpty;
import immutable.occamsfuncer.funcers.MapPair;
import immutable.occamsfuncer.funcers.MapEmpty;
import immutable.occamsfuncer.funcers.Num;
import immutable.occamsfuncer.funcers.Import;

/** This is to be static imported in many code files in occamsfuncer */
public class ImportStatic{
	private ImportStatic(){}
	
	/** wrap */
	public static Funcer wrap(double d){
		return new Num(d);
	}
	
	/** wrap */
	public static Funcer wr(Object o){
		if(o instanceof Funcer) return (Funcer)o;
		if(o instanceof Number) return wrap(((Number)o).doubleValue());
		throw new Error("TODO");
	}
	
	public static final Funcer theImportFunc = Import.instance;
	
	public static final Funcer identityFunc = theImportFunc.f(""+Opcode.identityFunc);
	
	public static final Funcer one = wr(1.);
	
	public static final Funcer zero = wr(0.);
	
	//FIXME should nil be a class?
	public static final Funcer nil = theImportFunc.f(""+Opcode.nil);
	
	public static final Funcer emptyMap = MapEmpty.instance;
	
	public static final Funcer emptyList = ListEmpty.instance;
}