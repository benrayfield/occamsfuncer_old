/** Ben F Rayfield offers this software opensource MIT license */
package immutable.occamsfuncer;
import static immutable.occamsfuncer.ImportStatic.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import immutable.occamsfuncer.funcers.Leaf;
import immutable.util.Base64Util;
import immutable.util.BitsUtil;
import mutable.util.Rand;

//import immutable.occamsfuncer.storageAndCache.CacheIdToFuncer;
//import immutable.occamsfuncer.storageAndCache.DataUtil;
//import immutable.util.MathUtil;
//import immutable.wavetree.bit.BitsUtil;

/** 192 bit global-in-Earth id by merkle hash. Can use any custom MessageDigest
but must use standard byte[] from content of each Funcer to hash,
and maybe custom pre and post processing.
<br><br>
Only ids can be map keys, such as MapPair and MapSingle (see CoreType for the exact set of types).
<br><br>
Stored as binary. ToString returns base64 view, 24 bytes fits evenly in 32 digits.
*/
public final class Id{
	
	public final long idA, idB, idC;
	
	public static final int sizeInBytes = 24;
	
	public Id(long idA, long idB, long idC){
		this.idA = idA;
		this.idB = idB;
		this.idC = idC;
	}
	
	private static final MessageDigest sha256;
	private static final byte[] temp32Bytes = new byte[32];
	private static final OutputStream outToSha256;
	static{
		try{
			sha256 = MessageDigest.getInstance("SHA-256");
		}catch(NoSuchAlgorithmException e){
			throw new Error(e);
		}
		outToSha256 = new OutputStream(){
			public void write(int b) throws IOException{
				sha256.update((byte)b);
			}
		};
	}
	
	static Id wrapInIdPadWithBit0s(Funcer f){
		//TODO optimize to not need byte[], or copy of byte[] in ByteArrayOutputStream, or that stream. Go directly to longs.
		ByteArrayOutputStream s = new ByteArrayOutputStream(24);
		f.content(s);
		byte[] b = s.toByteArray();
		return new Id(
			BitsUtil.readLongFromByteArray(b,0),
			BitsUtil.readLongFromByteArray(b,8),
			BitsUtil.readLongFromByteArray(b,16)
		);
	}
	
	/** some header bits then last n bits of double-SHA256 */
	public static Id id(Funcer f){
		/*FIXME theres 3 kinds of id, and all have 22 bytes of data after a byte and colon:
		double-SHA256 (safe to cross untrusted borders, incoming or outgoing),
		weakref to a (last 22 bytes of) double-SHA256 (safe to cross untrusted borders, incoming or outgoing),
		and localId (not to cross untrusted borders).
		*/
		 
		if(f.contentFitsInId()) return wrapInIdPadWithBit0s(f);
		synchronized(outToSha256){
			sha256.reset();
			f.content(outToSha256);
			sha256.digest(temp32Bytes); //SHA-256
			sha256.reset();
			sha256.update(temp32Bytes);
			f.content(outToSha256);
			sha256.digest(temp32Bytes); //double-SHA256
			long longB = BitsUtil.readLongFromByteArray(temp32Bytes,8);
			/*if(!f.contentFitsInId()){
				//hash else is literal data that fits in id
				longB |= (((long)Data.maskIsHash)<<32);
			}
			if(f.isWeakref()){
				//weakref to same id except with weakref bit 0 else is the normal id such a weakref could point at
				longB |= (((long)Data.maskIsWeakref)<<32);
			}
			int header = f.firstBits();
			if(!f.contentFitsInId()){
				
			}*/
			longB = (((long)(f.firstBits()>>>28))<<60) | (longB&0x0fffffffffffffffL); //keep first 4 bits of firstBits()
			long longC = BitsUtil.readLongFromByteArray(temp32Bytes,16);
			long longD = BitsUtil.readLongFromByteArray(temp32Bytes,24);
			return new Id(longB, longC, longD);
		}
	}
	
	/*public static Id id(Funcer f){
		TODO double-SHA256 (hash the 32 bytes of sha256 concat the input, instead of whats below,
		and use the first 4 bits in chooseTheFirstFewBitVarsOfHeaderFor(application/x-occamsfuncer-fork7128543112795615).
		
		synchronized(sha256){
			sha256.reset();
			//content(sha256Out,false); //forward
			//content(sha256Out,true); //reverse
			f.content(new OutputStream(){ //TODO optimize by not creating OutputStream object every hash, share it like the sha256 is shared
				public void write(int b) throws IOException{
					sha256.update((byte)b);
				}
			});
			//add (byte wrapped)i in 1 copy of the input but not the other, to make it harder
			//to hack internal sha256 xor/plus preprocessing cycles number 16-63,
			//and to prevent prefix hacks. The same byte being there twice
			//and with another byte (i&0xff) offset value depending on index
			//means if you want it to be a certain value you can choose it one place
			//or the other but not both (except in 1/256 of the bytes where adds 0).
			//This is why I feel safe with 24 bytes (minus header) hash instead of 32.
			int[] index = new int[1]; //TODO optimize by not using array, instead the custom shared OutputStream would have an int field.
			f.content(new OutputStream(){ //TODO optimize by not creating OutputStream object every hash, share it like the sha256 is shared
				public void write(int b) throws IOException{
					sha256.update((byte)(b+(index[0]++)));
				}
			});
			sha256.digest(temp32Bytes);
			//byte header = TODO see mindmap name chooseIdDatastructForOccamsfuncer for what goes in header byte of id;
			
			long longB = BitsUtil.readLongFromByteArray(temp32Bytes,8);
			//First 2 bits are header. Next 190 bits are last bits of sha256 output.
			int headerSize = 2; //maskIsHash and masIsWeakref are the high 2 bits
			longB = ((longB>>>headerSize)<<headerSize);
			//FIXME in Leaf and Num, contentFitsInId, so dont hash but should content be modified somehow?
			//Or same content? Using multiformats_varint it should be small.
			if(!f.contentFitsInId()){
				//hash else is literal data that fits in id
				longB |= (((long)Data.maskIsHash)<<48);
			}
			if(f.isWeakref()){
				//weakref to same id except with weakref bit 0 else is the normal id such a weakref could point at
				longB |= (((long)Data.maskIsWeakref)<<48);
			}
			////longB &= 0x0000ffffffffffffL;
			//longB overwrite high bits with header. Havent decided which kind of header yet,
			//since dont know how many of the bits in (int)Funcer.header() it will use
			//and will it exactly duplicate them or is there a more standard form
			//while allowing Funcer implementation to differ from that in memory as an optimization
			//and cuz different VMs might optimize it different ways?
			//longB |= ((header&0xffffL)<<48); //replace first 16 bits with magic16 header.
			long longC = BitsUtil.readLongFromByteArray(temp32Bytes,16);
			long longD = BitsUtil.readLongFromByteArray(temp32Bytes,24);
			return new Id(longB, longC, longD);
		}
	}*/
	
	/** 24 bytes are 32 base64 chars. */
	public Id(String base64){
		throw new Error("TODO "+Base64Util.class);
	}
	
	public Id(byte[] b){
		this(b,0);
	}
	
	public Id(byte[] b, int offset){
		this(
			BitsUtil.readLongFromByteArray(b, offset),
			BitsUtil.readLongFromByteArray(b, offset+8),
			BitsUtil.readLongFromByteArray(b, offset+16)
		);
	}
	
	/** bit index 0 is 1 for hash,
	0 for literal that fits in id (such as a small string or double or float[5] or float[2][2]).
	*/
	public boolean isHash(){
		return coretypeByte()==Opcode.h_coretypeHashId.coretypeByteOrZeroIfNotCoretype;
		//return ((idA>>>48)&Data.maskIsHash)!=0;
		//return (idA&(1L<<63))!=0;
	}
	
	public boolean isWeakrefThatFitsInId(){
		return coretypeByte()==Opcode.w_coretypeOptimizedWeakrefId.coretypeByteOrZeroIfNotCoretype;
	}
	
	public byte coretypeByte(){
		return (byte)(idA>>>56);
	}
	
	/** bit index 1 is 1 for weakref (of the id with that bit flipped), 0 for normal *
	public boolean isWeakref(){
		return ((idA>>>48)&Data.maskIsWeakref)!=0;
	}*/
	
	public boolean equalsIgnoringWeakrefBit(Id i){ //TODO rename this since using type:content instead of mask
		long mask = 0x00ffffffffffffffL;
		return (idA&mask)==(idB&mask) && idB==i.idB && idC==i.idC; //FIXME should weakref equal localId this way? It does.
		//long mask = ~(((long)Data.maskIsWeakref)<<48);
		//return (idA&mask)==(i.idA&mask) && idB==i.idB && idC==i.idC;
	}
	
	public Id setCoretypeByte(byte b){
		return new Id(((b&0xffL)<<56)|(idA&0x00ffffffffffffffL), idB, idC);
	}
	
	public Id setWeakref(boolean becomeWeakref){
		if(isHash()){
			if(becomeWeakref) return setCoretypeByte(Opcode.h_coretypeHashId.coretypeByteOrZeroIfNotCoretype);
			return this;
		}
		if(isWeakrefThatFitsInId()){
		//if(isWeakrefThatFitsInId()){
			if(!becomeWeakref) return setCoretypeByte(Opcode.h_coretypeHashId.coretypeByteOrZeroIfNotCoretype);
			return this;
		}
		throw new Error("Is this a *localId? optimizedWeakref (not the generalWeakref coretype) only works between *hashId and *optimizedWeakrefId and they share the last 22 bytes of id so dont have to store them as data to hash. If you need a more general weakref to any id other than that, use *generalWeakrefId coretype.");
		
		//if(isWeakref() == becomeWeakref) return this;
		//long wr = ((long)Data.maskIsWeakref)<<48;
		//return new Id(becomeWeakref ? (idA|wr) : (idA&~wr), idB, idC);
	}
	
	/** As Comparable, Id is 192 bit unsigned integer (despite java longs being signed). */
	public int compareTo(Id o){
		final long minv = 1L<<63;
		if(idA != o.idA) return idA+minv<o.idA+minv ? -1 : 1; //like Long.compareUnsigned but faster cuz cant return 0
		if(idB != o.idB) return idB+minv<o.idB+minv ? -1 : 1;
		if(idC != o.idC) return idC+minv<o.idC+minv ? -1 : 1;
		return 0;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof Id)) return false;
		Id x = (Id) o;
		return idA==x.idA && idB==x.idB && idC==x.idC;
	}
	
	private static final long hashSaltA = Rand.strongRand.nextLong(),
		hashSaltB = Rand.strongRand.nextLong();
	
	public int hashCode(){
		long g = (idA+hashSaltA)^(idB+hashSaltB)^idC;
		return ((int)(g>>>32))+(int)g;
	}
	
	public String toString(){
		return Base64Util.toString(idA, idB, idC);
	}
	
	/** returns a leaf of int[6] since Funcers normally use doubles though can technically
	hold any primitive array type such as long[] or float[][][][][], and int fits in double.
	<br><br>
	This is used By Opcode.idAsLeaf for user level code to see ids,
	such as if you wanted to make a weakref that points at another weakref
	you cant do that using the built in weakref since it can only point at a non-weakref
	cuz theres only 1 weakref bit in an id and the other bits are what it points at
	as if the weakref bit was flipped.
	<br><br>
	TODO either make an Id be a Funcer (check Comparable constraints, if thats a consistent design?)
	or make a Funcer type that wraps an Id to save memory of not copying the 24 bytes.
	It would be most efficient if Id instanceof Funcer, but I'm unsure if that breaks constraints.
	*/
	public Funcer<int[]> asFuncer(){
		return Leaf.wrap(new int[]{
			(int)(idA>>>32),
			(int)idA,
			(int)(idB>>>32),
			(int)idB,
			(int)(idC>>>32),
			(int)idC
		});
	}
	
	public void content(OutputStream out){
		//idA starts with coretype().coretypeByteOrZeroIfNotCoretype then colon
		//Those can be doubble-SHA256, localName (not to be used across untrusted borders), or weakref (to a double-SHA256),
		//where only the last 22 bytes of double-SHA256 are used.
		BitsUtil.writeLong(idA, out);
		BitsUtil.writeLong(idB, out);
		BitsUtil.writeLong(idC, out);	
	}
	
	public int contentLen(){ return sizeInBytes; }
	
	/*replaced by a func that returns byte[]
	public void content(OutputStream out, boolean reverse) throws IOException{
		if(reverse){
			MathUtil.writeLong(idC,out,reverse);
			MathUtil.writeLong(idB,out,reverse);
			MathUtil.writeLong(idA,out,reverse);
		}else{
			MathUtil.writeLong(idA,out,reverse);
			MathUtil.writeLong(idB,out,reverse);
			MathUtil.writeLong(idC,out,reverse);
		}
	}*/
	
	/**	FIXME magic16 is obsolete, see comments in Merk.java.
	FIXME this appears to be an obsolete design, cuz ids have magic16 header, and opcodes have magic32 header,
	but maybe will merge all those into the same size of magic???
	OLD: same as Funcer.idHeader().
	*
	public byte idHeader(){
		return (byte)(idA>>>56);
	}*/
	
	/*public Id dedup(){
		CacheIdToFuncer
	}*/
	
	
	
	
	
	
	//FIXME remove the magic16 numbers from Id since id will be as described in Merk.java.
	
	/** "sha256 reverse plus" hash algorithm,
	the one (as of 2019-2) done in AbsstractFuncer.id(...)).
	Its normal sha256 of [the bytes forward (no plus)
	concat reverse order of all those bytes plus the byte index wrapped around byte].
	TODO what is "double sha256", maybe some improvement to this could be
	found in part of that? Or maybe this is good enough as it is,
	and if so, it would have a different name.
	<br><br>
	You can add another hash algorithm without replacing this magic16.
	Create its own magic16 the same way, by DataUtil.magic16(itsName)
	*
	public static short magic16_sha256rpId192 = DataUtil.magic16("sha256rpId192");
	
	/** A localId is any arbitrary bits, such as randomly or sequentially chosen,
	and it must not cross untrusted borders since anyone can make it up.
	Its ok to send across untrusted borders but not ok to receive,
	since anyone can make them up. The only security of a localId is knowing
	where it came from, like you wouldnt let users create ids in your database.
	But we do let users create secureHash ids since those cant be faked,
	and the same data gives the same id nomatter who creates it or when,
	unless the hash algorithm is cracked.
	*
	public static final short magic16_localId192 = DataUtil.magic16("localId192");
	
	/** not the same as double[1].
	Double is the same as float, int,
	utf8Byte (specific 256 possible double values in some high range, TODO choose which),
	or bit (double values 0 or 1) whichever smallest it fits in,
	so theres no need to have a magic16_floatInId192 etc.
	Its designed this way so primitives are consistently reflected in double
	without having to also store their type when viewing just 1 at a time.
	In an array, its normed form (which must be used when hashing or putting them in id)
	is the smallest type of 1d array they all fit in together.
	Bits go in a long[] as content to be hashed so have blocks of 64
	but in an id to save space theres no such padding, no block of 64. 
	*
	public static final short magic16_doubleInId192 = DataUtil.magic16("doubleInId192");
	
	public static final short magic16_doubleArrayInId192 = DataUtil.magic16("doubleArrayInId192");
	
	/** 1 byte size header, then pad 0s, then array that fits in an id which ends at end of id *
	public static final short magic16_intArrayInId192 = DataUtil.magic16("intArrayInId192");
	
	/** 1 byte size header, then pad 0s, then array that fits in an id which ends at end of id *
	public static final short magic16_floatArrayInId192 = DataUtil.magic16("floatArrayInId192");
	
	/** 1 byte size header, then pad 0s, then array that fits in an id which ends at end of id *
	public static final short magic16_utf8ArrayInId192 = DataUtil.magic16("utf8ArrayInId192");
	
	/** 1 byte size header, then pad 0s, then array that fits in an id which ends at end of id *
	public static final short magic16_bitArrayInId192 = DataUtil.magic16("bitArrayInId192");
	*/

}
