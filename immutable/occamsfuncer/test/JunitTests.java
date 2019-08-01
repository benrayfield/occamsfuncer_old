package immutable.occamsfuncer.test;
import static immutable.occamsfuncer.ImportStatic.*;
import immutable.occamsfuncer.*;
import org.junit.jupiter.api.Test;

import immutable.occamsfuncer.Funcer;

public class JunitTests{
	private JunitTests(){}
	
	static Funcer eval(String occamsfuncerCode){
		return null;
	}
	
	@Test
	public void testExamplePlugin(){
		throw new Error("TODO verify f(?? ;plugin ;immutable.occamsfuncer.Plugins.notanocfnplugExamplePlusOne 100) throws and f(?? ;plugin ;immutable.occamsfuncer.Plugins.ocfnplugExamplePlusOne 100) evals to 101");
	}
	
	public void testSCall(){
		throw new Error("TODO sCallList(TODO)");
	}
	
	static Funcer sCallList(Funcer... f){
		throw new Error("TODO");
	}
	
	static Funcer sCall(Funcer L, Funcer R){
		throw new Error("TODO");
	}

}
