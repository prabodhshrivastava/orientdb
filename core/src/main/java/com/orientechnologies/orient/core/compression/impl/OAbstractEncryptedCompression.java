package com.orientechnologies.orient.core.compression.impl;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import com.orientechnologies.orient.core.exception.OSecurityException;

/***
 * @see https://github.com/orientechnologies/orientdb/issues/89
 * 
 * @see https://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html
 * 
 * @author giastfader
 *
 */
public abstract class OAbstractEncryptedCompression extends OAbstractCompression {

	//these variables are initialized by the init() method
	
	
	//@see https://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
	protected  String  transformation; 					// es: "AES/CBC/PKCS5Padding"
	protected  String  algorithmName;						// es: "AES" 
	protected  byte[] key;
	private String secretKeyFactoryAlgorithmName;
	
    private  boolean isInitialized=false;

    
    protected boolean isInitialized() {
		return isInitialized;
	}

	protected void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}
	
	protected OAbstractEncryptedCompression(){}
	
	/***
	 * Subclasses have to invoke this method next the constructor
	 * @param spec This object contains the password, the salt and the keysize. It is computed by subclasses
	 * @param transformation Example: AES/CBC/PKCS5Padding @see https://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html#ciphertrans
	 * @param alghorithmName Example: AES @see https://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
	 * @param secretKeyFactoryAlgorithmName Example: PBKDF2WithHmacSHA1 @see https://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
	 */
	protected void init(
			String transformation,
			String algorithmName,
			String secretKeyFactoryAlgorithmName,
			byte[] key)
	{
		
		this.transformation=transformation;
		this.algorithmName=algorithmName;
		this.key=key;
		this.secretKeyFactoryAlgorithmName=secretKeyFactoryAlgorithmName;
		
		System.out.println("** transformation: " + transformation);
		System.out.println("** algorithmName: " + algorithmName);
		System.out.println("** secretKeyFactoryAlgorithmName: " + secretKeyFactoryAlgorithmName);
		System.out.println("** key: " + Arrays.toString(key));
		
		/*this.transformation="DES/ECB/PKCS5Padding";
		this.algorithmName="DES";
		this.secretKeyFactoryAlgorithmName="DES";	*/
	}
	
	protected Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException{
		return Cipher.getInstance(transformation);
	}
	
	protected SecretKeySpec getSecretKeySpec(){
		return new SecretKeySpec(key,algorithmName);
	}
	
	
	@Override
	public  byte[] compress(byte[] content, int offset, int length){
        try {
	        
	        System.out.println("** ENCRYPTION **");
	        System.out.println("** content: " + Arrays.toString(content));
	        System.out.println("** offset: " + offset);
	        System.out.println("** length: " + length);
	        
	        byte[] encriptedContent = encryptOrDecrypt(Cipher.ENCRYPT_MODE,content, offset,  length);
	        System.out.println("** encriptedContent: " + Arrays.toString(encriptedContent));
	        
	        return encriptedContent;
        } catch (Throwable e) {
			throw new OSecurityException(e.getMessage(),e);
		} 
	};

	@Override
	public  byte[] uncompress(byte[] content, int offset, int length){
        try {
	        System.out.println("** DECRYPTION **");
	        System.out.println("** content: " + Arrays.toString(content));
	        System.out.println("** offset: " + offset);
	        System.out.println("** length: " + length);
	        
	        byte[] decriptedContent = encryptOrDecrypt(Cipher.DECRYPT_MODE,content, offset,  length);
	        System.out.println("** decriptedContent: " + Arrays.toString(decriptedContent));

	        return decriptedContent;
        } catch (Throwable e) {
			throw new OSecurityException(e.getMessage(),e);
		} 
	};

	public   byte[] encryptOrDecrypt(int mode, byte[] input, int offset, int length) throws Throwable {
		SecretKeyFactory skf = SecretKeyFactory.getInstance(secretKeyFactoryAlgorithmName); //PBKDF2WithHmacSHA1 
		SecretKeySpec ks = new SecretKeySpec(key, algorithmName); //AES
		//SecretKey desKey = skf.generateSecret(ks.getEncoded()); 
		Cipher cipher = Cipher.getInstance(transformation); // AES/CBC/PKCS5Padding for SunJCE
		cipher.init(mode, ks);
		
		byte[] content;
        if (offset==0 && length==input.length){
        	content=input;
        }else{
        	content = new byte[length];
	        System.arraycopy(input,offset,content,0,length);
	        System.out.println("** content: " + Arrays.toString(content));
        }
		byte[] output=cipher.doFinal(content);
		return output;
	}
	
	@Override
	public abstract String name();

	
}
