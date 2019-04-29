package immutable.util;

public class Base64Util{
	private Base64Util(){}
	
	//ascending UTF8 and ASCII order
	public static final String digits =
		"$0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
	
	public static String toString(long a, long b, long c){
		//TODO optimize by not copying to byte array
		byte[] arr = new byte[24];
		BitsUtil.writeLongToByteArray(a, arr, 0);
		BitsUtil.writeLongToByteArray(b, arr, 8);
		BitsUtil.writeLongToByteArray(c, arr, 16);
		return toString(arr);
	}
	
	/** interprets the last byte as padded with low 0s if not a multiple of 6 bits */
	public static String toString(byte[] b){
		int data = 0;
		int bitsInInt = 0;
		int arrayIndex = 0;
		char[] c = new char[((b.length*8)+5)/6];
		for(int i=0; i<c.length; i++){
			if(bitsInInt < 6){
				data = (data<<8)|(b[arrayIndex++]&0xff);
				bitsInInt += 8;
			}
			c[i] = digits.charAt(data&63);
			data >>>= 6;
			bitsInInt -= 6;
		}
		return new String(c);
	}

}