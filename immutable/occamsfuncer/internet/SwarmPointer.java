package immutable.occamsfuncer.internet;

import immutable.occamsfuncer.Id;

/** See comments at HaltingDictator.topExpireTime
and HaltingDictator.timeNow and Funcer.liveLonger(long).
A SwarmPointer (like a bittorrent swarm except for many tiny objects (around 100 bytes)
and fewer big objects instead of everything being a big object)
is a claim (from the peer themself or from peers claiming about other peers)
that a certain peer (such as your local memory or your local harddrive
are 2 peers) will continue to make available to other peers a certain funcer
AND everything reachable through it, either by using their own bandwidth
or helping others quickly find other peers which will send it to you
and if you ask also any parts sparsely reachable from it
and you can find other peers for those, statistically redundantly
and at gaming-low-lag such as the timing of mouse movements or interactive musical instruments
played among people across the internet or creating new kinds of games like minecraft or
garysmod or mariomaker are game editors and game players except this is turingcomplete and
openended and sandboxed/safe. Sandboxed/safe means unsigned untrusted remote-code-injection
including from many malicious users and malicious AIs and good users and good AIs
is like a browser can run malicious javascript but still cant access your private files,
and unlike a browser, here it cant slow you down cuz timing and memory are limited
by HaltingDictator per peer.
In theory at least. We do need to verify these security proofs and implementation of them.
<br><br>
Use of this system does not require you to share your funcers across the network,
or to share all your funcers, so adjust local settings for such custom behaviors.
TODO there should be a checkbox visible on screen that only shares whats in that window
and everything reachable from it (you can have multiple such views, TODO) if checked
and makes sure user understands what they are doing when they click
and what it means about future behaviors of the system, in some few words.
Im still working out these kind of privacy and broadcast and gametheory things.
In general its a system designed for large scale pure functional programming
at gaming low lag, so local private computing is not the main use case
but you probably do want a few things private such as if you have passwords in it.
*/
public class SwarmPointer{
	
	//FIXME choose one at boot (use an ed25519 pubkey).
	//FIXME should this be final?
	//TODO each computer needs a different such Id
	public static final Id whichPeerIsLocalMemory = null;
	
	//FIXME choose one at boot (use an ed25519 pubkey).
	//FIXME should this be final?
	//TODO each computer needs a different such Id
	public static Id whichPeerIsLocalHarddrive = null;
	
	/** Which peer said peerSwarming is swarming funcer until minExpireTime?
	This is for gametheory to tit-for-tat adjust perception of value of a peer connection,
	for if they broadcast things that tend to be true and useful vs for example
	if they say certain peers will help you get the Funcer you asked about but then they dont
	(including recursively in the swarm peers helping you find peers... to find what you asked for).
	<br><br>
	Whatever this local computer believes as sets of [peerSwarming funcer minExpireTime]
	as a result of sometimes multiple [peerClaiming peerSwarming funcer minExpireTime]
	with different peers claiming it or claiming funcers its reachable from...
	that would be [whichPeerIsLocalMemory peerSwarming funcer minExpireTime]
	*/
	public final Id peerClaiming;
	
	/** The peer which supposedly has or can quickly help you get a copy of the SwarmPointer.funcer
	(get as Funcer, not just the Id of it), until at least minExpireTime.
	*/
	public final Id peerSwarming;
	
	public final Id funcer;
	
	/** seconds since year 1970 UTC. See HaltingDictator.topExpireTime and HaltingDictator.timeNow
	and Funcer.minExpireTime().
	*/
	public final long minExpireTime;
	
	public SwarmPointer(Id peerClaiming, Id peerSwarming, Id funcer, long minExpireTime){
		this.peerClaiming = peerClaiming;
		this.peerSwarming = peerSwarming;
		this.funcer = funcer;
		this.minExpireTime = minExpireTime;
	}
	
	/*
	https://www.reddit.com/r/AskProgramming/comments/bct7ql/what_kind_of_programming_deals_with_gametheory/
	What kind of programming deals with gametheory such as an object that says X believes (or would say
	if asked) Y, so another object could say [peerA believes [peerB believes statementC]], so
	peerA could sign a set of these without peerB having to sign each or route them individually?
	...
	https://en.wikipedia.org/wiki/Dempster%E2%80%93Shafer_theory was recommended in that thread.
	...
	turingquery andOr goalFuncNamespace could be useful here
	as a way for peers to explain goals to eachother,
	such as the goal of reacting a certain way to ed25519-signed mouse movements and key presses
	given the previous state of that peer, where state is a Funcer (immutable, stateless, forest node).
	Could use Opcode.ignoredataFuncParam for turingquery as its the normal way to store state with a func
	that can return another pair of state and func.
	*/

}
