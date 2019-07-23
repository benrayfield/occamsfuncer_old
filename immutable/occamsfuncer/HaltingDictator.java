/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;

import java.util.function.Supplier;

import mutable.occamsfuncer.memstat.MemStat;

/** workaround for lack of halting oracle (which is impossible math to ever exist).
Every program halts when it runs out of compute resources, recursively limited
by Spend calls inside Spend calls which can be as deep as the java stack allows
without making it significantly slower. Is turingComplete up to that limit
similar to how npcomplete is what a nondeterminstic turing machine can do in n steps
and P (polynomial) is what a deterministic turing machine can do in n steps.
Effectively this is a halting oracle (return true, everything halts)
for every program it can possibly run,
and any program that has ever been run in the history of computers
has only so far computed a finite number of steps
so could be rewritten to run in this system.
Even when a program goes into an "infinite loop" and you ctrl+alt+del it
to end it, it technically halted since you didnt change the computing hardware
and the hardware didnt break or make an error and that program is not running anymore.
Thats what I want for programs in this system, to be able to efficiently
(many thousands of times per second) ctrl+alt+del their calls of functions inside functions,
so nomatter how bad things get they will always halt and move on to something better,
except it works even singlethreaded as limit of compute resources
like Spend and HaltingDictator are similar to try/catch for compute resources.
(TODO rewrite this paragraph simpler) 
*/
public class HaltingDictator extends RuntimeException{
	private HaltingDictator(){}
	
	/** shared throwable for efficiency since it could be thrown many thousands of times per second.
	Throw this whenever cant prepay for compute resources, where aboutToSpend >= Wallet.top (TODO > vs >=?).
	*/
	public static final HaltingDictator throwMe = new HaltingDictator();
	
	//TODO permissions map (which can only shrink in deeper calls) generalize strict, topWallet, etc?
	//Maybe but not for topWallet etc cuz thats mutable even though it can only decrease during deeper calls.
	
	/** Starts true, but in most uses it will probably be quickly set to false since thats more efficient.
	Set by some opcode that makes deeper calls be strict, similar to how Opcode.spend affects topWallet.
	*/
	public static boolean topIsStrict = true;
	
	/** This is the amount available to the current running Funcer and whatever it calls recursively
	UNTIL an Opcode.spend is called which moves some of this into a local var
	then calls a func and param with that lower limit of compute resources available
	then either by catch(any Throwable, especially Wallet.throwMe)
	or by it returning normally (to the Spend)
	without having exceeded those resources (this var is positive),
	the Spend adds that local var back into whatever is left here.
	That works recursively as you can for example have 1000 Spend calls recursively
	inside eachother (beware of java stack depth) without it getting noticably slower.
	Each function call can have a different limit of compute resources than what it calls.
	<br><br>
	Optimized code, such as opencl, costs less per stuff computed.
	<br><br>
	In the simplest case, this measures compute cycles plus memory allocated (ignoring freed),
	added in some ratio depending on how much of each is available. Memory freed would be
	added back into here after the next video frame as it would check how much
	(by internally keeping count, not by asking JVM) memory is in use (non-garbcolable)
	and choose how much to allow in the next video frame by setting this var.
	<br><br>
	In more advanced cases, this might depend on stats of sharing memory between objects
	upward along reverse pointers, such as (sparsely, log(objects)*objects bigO and
	updated continuously about log(objects) cost per new object on average, zapeconacyc
	or (very slow, objects^2 bigO) fullEconacyc, etc.
	*/
	public static long topWallet;
	
	/** TODO there should be a Supplier<MemStat> in boot call to occamsfuncer
	but that would have to happen before static Funcers are createdsuch as ImporStatic.nil.
	When thats done, this func will return from that Supplier.
	*/
	public static MemStat newMemStat(){
		throw new Error("TODO");
	}
	
	/** TODO MAKE THIS AN OPTION.
	nanoseconds since year 1970 UTC. Every funcer created pays to topWallet to
	raise the long expireTime of every Funcer under itself recursively to this expire time
	unless (and this is what usually happens) some child is already at least that expire time
	so dont recurse that path. In theory, this will on average cost BigO(1) per new object
	even in a network of billions of computers garbcoling together
	as they'll have network connections near directly to those relevant peers.
	<br><br>
	This is only for the peer/datasource of the local memory.
	Harddrive is a different peer/datasource so has a different set of long per object.
	Peers on internet will have their own set of Funcers and longs for each.
	Heres how the gametheory will work of funcer swarming together...
	<br><br>
	purefunctinalprogrammingswarm...
	[idofinternetaddress idoffuncer longnanosecondssince1970]. these are statements
	that peers say to eachother to swarmlikebittorrent per funcer. but need mutable
	long in funcer for local faster view of that before hashing. peer commits to swarm
	a specific funcer and everything reachable from it until that long. peer commits no
	specific bandwidth and refers you to those who have more bandwidth as needed.
	punishthenonpunishers gametheory to prevent lying about offering these funcers
	during such time and prevent lying about what funcers others swarm recursively.
	can raise a long but not lower it. must have statistical redundancy of child
	funcers enough halfreliable peers per child funcer to claim having one of their
	their infinity parents during a time that expires during that.
	...
	also i want mmgmouseai optimized routing of simpler bitor scalar streams.
	...
	local mem and local harddrive are 2 different peers though same os process.
	...
	local code must pay proportional to localmem mult  time added per funcer, adjusted
	by some supply/demand of total mem. like op spend, theres an op to raise the
	longexpiretime of every funcer recursively under a chosen funcer every funcer created
	in that context as HaltingDictator has mutable static vars for topWallet and topExpireTime
	and timenow.
	
	no... old...
	use longnanosecondssince1970 recursiveexpiretime in occamsfuncer where expiretime is
	immutable per object and part of its hash, except objects that fit in an id never expire
	since if ypu point at them you already have a copy. this will do garbcol in realtime in p2p.
	*/
	private static long topExpireTime;
	
	//TODO change Opcode.spend to specify a new topExpireTime or dt? Or use a different op?
	
	
	/** nanoseconds since year 1970 UTC */
	private static long timeNow;
	
	/** return this instead of throwing UnsupportedOperationException */
	public static Funcer evalInfiniteLoop(){
		//optimization of $(infinity) aka infinite loop, but since all calculations
		//must be prepaid but in this case lacks the compute resources to do that,
		//branch to the ELSE of the topmost Spend call.
		throw throwMe;
	}
	
	//TODO if Wallet.top is private, how will opSpend and opWallet use it?
	
	/** pay 1 else throw */
	public static void $(){
		topWallet--;
		if(topWallet == 0){
			topWallet++;
			throw throwMe;
		}
	}
	
	/** UNDECIDED ABOUT... Its callers responsibility for param to be positive.
	TODO That will be enforced by sandbox only approving code which does that,
	but not enforced in code outside the system (its their responsibility to know how to use the system correctly).
	This is not a cryptocurrency, just a local counting and allocating of compute resources. 
	*/
	public static void $(long pay){
		if(pay < 0) throw new Error("pay negative: "+pay); //TODO as optimization should this be callers responsibility?
		topWallet -= pay;
		if(topWallet <= 0){
			topWallet += pay;
			throw throwMe;
		}
	}

}
