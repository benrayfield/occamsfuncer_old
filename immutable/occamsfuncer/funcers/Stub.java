package immutable.occamsfuncer.funcers;
import java.util.function.Supplier;

import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Id;
import immutable.occamsfuncer.Opcode;

/** wraps a Funcer claimed to exist somewhere but we dont have it here yet.
When it gets here, such as from harddrive or Internet, its the Supplier<Funcer>'s
responsibility to verify its the expected Funcer by its immutable Id (secureHash or literal)
so you dont have to trust the source, then this wrapper acts as that Funcer it got.
You can call unstub() to get the Funcer contained which would be more efficient
to use it directly, or you can less efficiently continue to use it inside the Stub.
The Supplier<Funcer> must return the same Funcer by .equals every time its get is called,
not a series of different objects.
<br><br>
Its inefficient to get just 1 Funcer at a time so normally most Funcers will
be loaded from harddrive andOr internet except the big arrays in leafs
which would be stubs in that forest.
<br><br>
Its Supplier<Funcer>'s responsibility to call HaltingDictator.$(long)
to pay for whatever compute resources (including wasting of time synchronously)
in getting the Funcer for the first time, but after that just remember the pointer.
<br><br>
Other than by comparing getClass() and maybe toString() and by efficiency of
how much compute resources they cost HaltingDictator
(including that it might take so long to download it that runs out of
HaltingDictator.topWallet) and debugger inspection,
there is no measurable difference between a Stub and what it wraps (unstub()).
They equal by .equals, .hashCode, and .compareTo.
*/
public class Stub<LeafType> implements Funcer<LeafType>{
	
	protected final Supplier<Funcer<LeafType>> unstub;
	
	public Stub(Supplier<Funcer<LeafType>> unstub){
		this.unstub = unstub;
	}
	
	public Funcer<LeafType> unstub(){
		return unstub.get();
	}

	public int header(){
		return unstub().header();
	}

	public long maplistSize(){
		return unstub().maplistSize();
	}

	public Funcer L(){
		return unstub().L();
	}

	public Funcer R(){
		return unstub().R();
	}

	public Funcer minKey(){
		return unstub().minKey();
	}

	public Funcer maxKey(){
		return unstub().maxKey();
	}

	public LeafType v(){
		return unstub().v();
	}

	public Id id(){
		return unstub().id();
	}

	public Funcer f(Funcer param){
		return unstub().f(param);
	}

	public Funcer prex(Funcer startExcl){
		return unstub().prex(startExcl);
	}

	public Funcer sufx(Funcer endExcl){
		return unstub().sufx(endExcl);
	}

	public Funcer prei(Funcer endIncl){
		return unstub().prei(endIncl);
	}

	public Funcer sufi(Funcer startIncl){
		return unstub().sufi(startIncl);
	}

	public Funcer prex(long endExcl){
		return unstub().prex(endExcl);
	}

	public Funcer sufx(long endExcl){
		return unstub().sufx(endExcl);
	}

	public Funcer cato(Funcer itemSuffix){
		return unstub().cato(itemSuffix);
	}

	public Funcer catn(Funcer listSuffix){
		return unstub().catn(listSuffix);
	}

	public Funcer flat(){
		return unstub().flat();
	}

	public Funcer put(Funcer key, Funcer value){
		return unstub().put(key,value);
	}

	public Funcer get(Funcer key){
		return unstub().get(key);
	}

	public Funcer get(long key){
		return unstub().get(key);
	}

	public Funcer get(int key){
		return unstub().get(key);
	}
	
	public boolean equals(Object o){
		return unstub().equals(o);
	}

	public int compareTo(Funcer o){
		return unstub().compareTo(o);
	}
	
	public int hashCode(){
		return unstub().hashCode();
	}

	public Opcode leftmostOp(){
		return unstub().leftmostOp();
	}

	public boolean isTheImportFunc(){
		return unstub().isTheImportFunc();
	}
	
	public boolean contentFitsInId(){
		return unstub().contentFitsInId();
	}

	public boolean isWeakref(){
		return unstub().isWeakref();
	}

	public byte cur(){
		return unstub().cur();
	}

	public Funcer<LeafType> setSalt(Funcer salt){
		return unstub().setSalt(salt);
	}
	
	public Funcer salt(){
		return unstub().salt();
	}

}
