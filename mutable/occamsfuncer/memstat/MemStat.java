package mutable.occamsfuncer.memstat;

import immutable.occamsfuncer.Funcer;

/** This is the ONLY mutable part of Funcer.
MUTABLE memory statistics, especially fullEconacyc, zapeconacyc,
and maybe timing and storage capacity of harddrive, internet storage of Funcers,
and maybe memlock (keep in memory not harddrive), etc,
and maybe multiple (small number of) groups of each of those.
This is the only mutable part of a Funcer.
To save mem, it doesnt know which Funcer its the stat of and instead
takes Funcer as param to update the stats, and Funcer knows its MemStat,
even though Funcer is immutable in every other way except pointing at a MemStat.
Since kinds of stats vary in efficiency (like zapeconacyc requires a ChanceTree
of reverse pointers per Funcer), MemStat will have various subclasses).
Stats on compute cycles are done in HaltingDictator (TODO move it into mutable package).
*/
public interface MemStat{
	
	/** stat approx of zapeconacyc thats decays over time toward
	selecting a random existing Funcer, chance weighted by that Funcer's memory,
	and repeat go up on a randomly selected incoming pointer (in ChanceTree at each MemStat)
	and all along that path update the zapeconacyc stats.
	If there are n top level Funcers which have no incoming pointers
	and do that n*log(number of Funcers) times then will get an approximation
	of which paths from all n of those lead to the big memory,
	so can consider which L() vs R() childs to keep and form into new Funcers.
	If theres only 1 top Funcer (state of the system as viewed from this computer)
	then it still works. 
	*/
	public double zapCost();
	
	/** excluding the econacycCost of child Funcers (such as map.minKey),
	estimation of how many bits of memory this Funcer costs,
	such as float[].length*32 + few other small fields and memory cost per java object.
	*/
	public double localCost();
	
	/** fullEconacyc cost, which is deterministic other than double roundoff.
	At any cross-section of Funcers, the sum of fullCost() is the sum of localCost()
	of them and all Funcers they can reach, each counted once.
	*/
	public double fullCost(Funcer rootButDontTriggerLazyHash);
	/*FIXME fullCost must be updated all at once and must pay HaltingDictator.topWallet
	and may run out of compute resources along the way,
	so maybe it should be an inner object that MemStat uses
	and have multiple fullCost groups keyed by an arbitrary Id,
	which technically should be the Id of the top Funcer its fullCost down from
	but I dont want to trigger lazyHash.
	*/
	
	/** number of incoming pointers from Funcer to Funcer. 0 or more. */
	public double ins();
	
	/** number of outgoing pointers from Funcer to child Funcers. 0 if leaf. */
	public double outs();
	


}
