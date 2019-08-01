/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import static immutable.occamsfuncer.ImportStatic.evalInfiniteLoop;

import java.io.OutputStream;

import immutable.ids_fork7128543112795615.ob;
import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;

/** Interprets the 1d contents of array as list, instead of array as an item in a list size 1.
If the array is size 0, this represents emptyList.
*/
public class ListLeafArray extends AbstractFuncer{
	
	public final Funcer array;

	public ListLeafArray(Funcer array){
		//super((short)Opcode.coretypeAvlListLeafArray.ordinal()); //all mask bits 0
		this.array = array;
	}

	public Funcer f(Funcer param){
		throw new Error("TODO");
	}

	public Funcer fStrict(Funcer param){
		throw new Error("TODO");
	}
	
	public Funcer expand(){
		return this;
	}

	public void content(OutputStream out){
		throw new Error("TODO");
	}

	public int contentLen(){
		throw new Error("TODO");
	}

	public Funcer L(){
		throw new Error("TODO");
	}

	public Funcer R(){
		throw new Error("TODO");
	}

	public Funcer setSalt(Funcer salt){
		throw new Error("TODO");
	}

	public Funcer minKey(){
		throw new Error("TODO");
	}

	public Funcer maxKey(){
		throw new Error("TODO");
	}

	public Object v(){
		throw new Error("TODO");
	}

	public Opcode leftmostOp(){
		throw new Error("TODO");
	}

	public byte cur(){
		throw new Error("TODO");
	}

	public boolean contentFitsInId(){
		return false;
	}

	public boolean isWeakref(){
		throw new Error("TODO");
	}

	public Funcer unstub(){
		throw new Error("TODO");
	}

	public Funcer prex(Funcer startExcl){
		throw new Error("TODO");
	}

	public Funcer sufx(Funcer endExcl){
		throw new Error("TODO");
	}

	public Funcer prei(Funcer endIncl){
		throw new Error("TODO");
	}

	public Funcer sufi(Funcer startIncl){
		throw new Error("TODO");
	}

	public Funcer prex(long endExcl){
		throw new Error("TODO");
	}

	public Funcer sufx(long endExcl){
		throw new Error("TODO");
	}

	public Funcer cato(Funcer itemSuffix){
		throw new Error("TODO");
	}

	public Funcer catn(Funcer listSuffix){
		throw new Error("TODO");
	}

	public Funcer flat(){
		throw new Error("TODO");
	}

	public Funcer put(Funcer key, Funcer value){
		throw new Error("TODO");
	}

	public Funcer get(Funcer key){
		throw new Error("TODO");
	}

	public Funcer get(long key){
		throw new Error("TODO");
	}

	public Funcer get(int key){
		throw new Error("TODO");
	}

	public int compareTo(Object o){
		throw new Error("TODO");
	}
	
	public int childs(){
		return 1;
	}

	public Funcer child(int index){
		if(index == 0) return array;
		return evalInfiniteLoop();
	}
	
	/*public final Funcer array;
	
	public ListLeafArray(Funcer array){
		if(!array.isArray()) throw new Error("Not array: "+array);
		this.array = array;
	}
	
	public long size(){
		return array.size();
	}
	
	public Funcer cat(Funcer list, double walletLimit){
		throw new Error("TODO use ListPair but make sure to balance");
	}
	
	public Funcer pre(long endExcl, double walletLimit){
		return new ListLeafArray(array.pre(endExcl,walletLimit));
	}
	
	public Funcer suf(long start, double walletLimit){
		return new ListLeafArray(array.suf(start,walletLimit));
	}
	
	public Funcer listGet(long i, double walletLimit){
		return array.listGet(i,walletLimit);
	}
	
	public CoreType coreType(){
		return CoreType.listLeafArray;
	}

	public Funcer mapGet(Id key, double walletLimit){
		throw new Error("TODO");
	}

	public boolean isMap(){
		throw new Error("TODO");
	}

	public boolean isList(){
		throw new Error("TODO");
	}

	public boolean isArray(){
		throw new Error("TODO");
	}

	public Object unwrap(){
		throw new Error("TODO");
	}

	public byte[] content(double walletLimit){
		throw new Error("TODO");
	}*/

}
