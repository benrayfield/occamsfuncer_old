package occamsfuncer.util;

import occamsfuncer.Funcer;
import occamsfuncer.Op;

public class Err implements Funcer{
	
	public static final Err wal = new Err();
	
	public static final Err get = new Err();

	public boolean isErr(){
		return true;
	}

	public boolean isErrWal(){
		return this==wal;
	}

	public boolean isErrGet(){
		return this==get;
	}

	public long size(){
		return 0;
	}

	public Object lowKey() {
		throw new Error("TODO");
	}

	public Object highKey(){
		throw new Error("TODO");
	}

	public Object get(Object key){
		throw new Error("TODO");
	}

	public Funcer put(Object key, Object val){
		throw new Error("TODO");
	}

	public Funcer rem(Object key){
		throw new Error("TODO");
	}

	public Funcer cat(Funcer list){
		throw new Error("TODO");
	}

	public Funcer preI(Object key){
		throw new Error("TODO");
	}

	public Funcer preE(Object key){
		throw new Error("TODO");
	}

	public Funcer sufI(Object key){
		throw new Error("TODO");
	}

	public Funcer sufE(Object key){
		throw new Error("TODO");
	}

	public boolean equalsForest(Funcer m){
		throw new Error("TODO");
	}

	public Op leftmostOp(){
		throw new Error("TODO");
	}

	public int curries(){
		throw new Error("TODO");
	}

	public Funcer l(){
		throw new Error("TODO");
	}

	public Funcer r(){
		throw new Error("TODO");
	}

	public double d(){
		throw new Error("TODO");
	}

	public boolean isFloat(){
		throw new Error("TODO");
	}

	public Funcer f(Funcer p, double walletLimit){
		throw new Error("TODO");
	}

	public Funcer minKey(){
		throw new Error("TODO");
	}

	public Funcer maxKey(){
		throw new Error("TODO");
	}
	

}
