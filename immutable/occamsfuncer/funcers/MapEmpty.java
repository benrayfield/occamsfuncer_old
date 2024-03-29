/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer.funcers;
import static immutable.occamsfuncer.ImportStatic.evalInfiniteLoop;

import java.io.OutputStream;

import immutable.ids_fork7128543112795615.ob;
import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.ImportStatic;
import immutable.occamsfuncer.Opcode;

/** My only data (in byte[] content) is magic32.
This is needed cuz MapPair and MapSingle cant represent an empty map.
*/
public class MapEmpty extends AbstractFuncer{
	
	public static final MapEmpty instance = new MapEmpty();

	/*public MapEmpty(){
		//super((short)Data.coretypeMapEmpty); //all mask bits 0
	}*/

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
		return true;
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
		return 0;
	}

	public Funcer child(int index){
		return evalInfiniteLoop();
	}
	
	
	
	/*public MapEmpty(byte[] content){}

	public long size(){ return 0; }
	
	public CoreType coreType(){
		return CoreType.mapEmpty;
	}

	public Funcer cat(Funcer list, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer pre(long endExcl, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer suf(long start, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer mapGet(Id key, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer listGet(long listIndex, double walletLimit){
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
	}
	*/

}
