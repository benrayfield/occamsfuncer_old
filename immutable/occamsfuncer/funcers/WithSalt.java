package immutable.occamsfuncer.funcers;

import java.io.OutputStream;

import immutable.occamsfuncer.Data;
import immutable.occamsfuncer.Funcer;
import immutable.occamsfuncer.Opcode;

/** ANSWER: to the following, YES...
TODO do I want a WithSalt object instead of putting salt in almost everything?
It would cost 1 more recursive depth and affect the forest shape.
Mostly I want to salt Call, but I might want to sometimes salt maps nums strings etc.
I could put the salt var ONLY in call,
or I could create a coreType for salt.
But about 1/3 of the coreTypes are wrapped in Call since theres s call types that return map,
linkedlist, etc.
Maybe I want ignoredataFuncParam to be a coretype instead of f(?? "ignoredataFuncParam") (igfp)
and to put the salt in the ignoredata part and let the L() and R() see the data
but in some cases I want the @# syntax and in some cases I want the call syntax???
The coretype instance could be returned by (igfp data func)
similar to f(cons x y) returns conspair<x,y> (an instance of conspair coretype).
<br><br>
I could define the datastruct as being 1 byte or 4 bytes, or something like that,
to represent a null (Funcer)salt, but that wouldnt help in database where
pointers are always stored (and may be int64 with pair of int64 and int192
mapped in another table).
Less than 10% of funcers will have salt,
so why should all of them have a salt pointer?
Also some objects, including Num, already dont allow salt.
On the other hand, a WithSalt object costs memory for an extra object
since theres its own pointer, pointer to the thing being salted, and pointer to the salt,
compared to just the pointer for the salt (is that 1 extra or 2 extra?).
Its just 1 new pointer since the thing being salted and the salt already existed
(though the thing being salted would have a different hash if designed without salt),
and anything that would have pointed at "thing being salted" would point at WithSalt,
so its 1 extra pointer and 1  extra object and extra calculations.
Does it make it harder to use the syntax abc@#aName where ;aName
is both the salt and prehopfieldName of abc else would use abc@differentSalt#aName?
No, that syntax could be used even if theres a WithSalt wrapper.
<br><br>
I'm starting to prefer WithSalt cuz less than 10% of funcers will have salt
and so anything can be salted (including Num etc).
<br><br>
WithSalt will have only 2 fields, thing being salted and the salt.
<br><br>
WithSalt will do the same thing as IgnoredataFuncParam (where salt is the ignoredata)
except will have a builtin syntax
and will normally only be used with small string as data (less general than IgnoredataFuncParam).
<br><br>
DESIGN: Removed Salt from Funcer interface and from datastructs
but keep the abc@#aName and abc@differentSalt#aName syntax
and the same behavior as IgnoredataFuncParam where salt is the data to ignore,
and make it clear in various comments that less than 10% of funcers have salt
so the compute time difference isnt much, especially considering that
computing funcers directly is already slow and is recommended to
use automatic compiling to opencl and javassist for the bottlenecks.
<br><br>
MOVED FROM Funcer interface BEFORE MOVED SALT FUNCS TO WITHSALT CLASS,
QUOTE:
	** This is to make a funcer uniquely namable even though it does the exact same thing as another funcer,
	such as this way of naming (something that does the same as) R allows you to name it multiple things
	instead of just lastParam, and to name F(L R) things other than secondLastParam.
	This affects id, while the /(...) and '' comments and #names go in separate maps
	so you can change them separately without changing the ids of the functions they are comments and names of.
	
	DO THIS: occamsfuncerNameSaltingJust1LevelDeep
	AND IF SOMEDAY WANT UPGRADE TO RECURSIVE SALTING DO THIS: occamsfuncerNameSaltingRecursivelyFastByCopyingJustTheTopNodeOfTheFuncAtTheBottomAndDefiningItsSaltFieldAsTheNextLowerObjectAndItsSaltIsSoOnRecursively
	BUT NEVER DO IT THE SLOW WAY: occamsfuncerNameSaltingRecursivelyTheSlowWayThatCallsRecursivelyIntoSaltWrapperIntoSaltWrapperAndSoOn
	
	FIXED IT (occamsfuncerNameSaltingJust1LevelDeep):
	;mindmapSearch
	/(
		Func of 2 params: mindmapContentMap string query to treelist (v or a) of keys in that map, best first.
		f(`` thisFunc paramA paramB) aka f(f(f(`` thisFunc) paramA) paramB)
	)
	f(
		``
		F(
			'FIXED the need for recursive saltNaming, just dont do it, just write it 1 level deep like this:'
			F(L R)@#mmContentMap
			R@#mmQuery

			"TODO"
		)
	)
	
	DONT DO THIS[
		/(
			;paramGetters
			l(
				R@#lastParam
				
				'Example: in a func of 2 params gets it from f(f(f(`` thisFunc) paramA) paramB). You can get secondLastParam by reusing this in other funcs.'
				F(L R)@#secondLastParam
				
				F(L L R)@#thirdLastParam
				
				F(L L L R)@#fourthLastParam
			)
		)
	]
	
	
	;mindmapSearch
	/(
		Func of 2 params: mindmapContentMap string query to treelist (v or a) of keys in that map, best first.
		f(`` thisFunc paramA paramB) aka f(f(f(`` thisFunc) paramA) paramB)
	)
	f(
		``
		F(
			FIXME I want a place in every object to put an arbitrary string (or general object?),
			considered a comment thats part of the data, so I dont have to do this:
		
		
			'I put in igfp instead of using F(L R) directly so I could name this specific thing secondLastParam without naming every F(L R) that. FIXME Im unsure if this will optimize well enough.'
			(igfp "secondLastParam" F(L R))#secondLastParam
			(igfp "lastParam" R)#lastParam
			"TODO"
		)
	)
	So instead it would be written something like...
	F(L R)@secondLastParam#secondLastParam
	and in cases when the @ and # are equal write it this way:
	F(L R)@#secondLastParam
	You might give it nil @ in which case you dont write @ at all like F(L R)#secondLastParam,
	or you might give it an arbitrary @ in which case you write it like:
	F(L R)@553453453#secondLastParam
	*
	public Funcer salt();
	
	** true if x.setSalt(y).salt()==y, false if salt is always nil.
	Examples of nonsaltable classes: Weakref, TheImportFunc. TODO Leaf shouldnt be saltable?
	*
	public boolean canSalt();
	
	** Returns this forkEdited to have the given salt. TODO create Num.java thats just a double
	and doesnt store header or salt and its salt is always NIL, and if setSalt on that get a Leaf.java
	of that same double with the chosen salt.
	*
	public Funcer<LeafType> setSalt(Funcer salt);
UNQUOTE.
*/
public class WithSalt extends AbstractFuncer{
	
	public final Funcer salt;
	
	public final Funcer val;
	
	/** salt is first in param list so its like IgnoredataFuncParam */
	public WithSalt(Funcer salt, Funcer val){
		super((short)Data.coretypeWithSalt); //all mask bits 0
		if(val instanceof WithSalt) throw new Error("Cant salt a salt");
		this.salt = salt;
		this.val = val;
	}
	
	public Funcer f(Funcer param){
		return val.f(param);
	}
	
	public Funcer fStrict(Funcer param){
		return val.fStrict(param);
	}
	
	public Funcer expand(){
		throw new Error("TODO return this? Or should it be something else? I havent used salt enough to have chosen that design yet. Salt is an unusual thing compared to the other funcers.");
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

}