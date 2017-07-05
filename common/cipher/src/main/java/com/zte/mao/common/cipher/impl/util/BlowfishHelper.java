package com.zte.mao.common.cipher.impl.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

import com.zte.mao.common.cipher.api.CipherException;
import com.zte.mao.common.cipher.api.CipherService;
import com.zte.mao.common.cipher.impl.engines.BlowfishEngine;
import com.zte.mao.common.cipher.impl.exception.DataLengthException;
import com.zte.mao.common.cipher.impl.exception.InvalidCipherTextException;
import com.zte.mao.common.cipher.impl.padding.PaddedBufferedBlockCipher;
import com.zte.mao.common.cipher.impl.params.KeyParameter;
import com.zte.mao.common.cipher.impl.util.encoder.Hex;


/**
 * Blowfish加解密算法
 * 对字符串采用UTF-8
 * @author hu.jianghui
 *
 */
public class BlowfishHelper  implements CipherHelperInterface{

	public String encrypt(String algName, String keyName, String clearText)throws CipherException{
		if (keyName.equalsIgnoreCase(CipherService.SecretKeys.Blowfish_256_20120220)){
			return BlowfishHelper.encrypt(clearText, key256);
		}
		throw new CipherException(algName + " not support key " + keyName);
	}
	
	public String decrypt(String algName, String keyName, String cipherText)throws CipherException{
		if (keyName.equalsIgnoreCase(CipherService.SecretKeys.Blowfish_256_20120220)){
			return BlowfishHelper.decrypt(cipherText, key256);
		}
		throw new CipherException(algName + " not support key " + keyName);
	}
	
	public static String encrypt(String clearText, byte[] key) throws CipherException{
		// check param
		if (clearText == null){
			throw new InvalidParameterException("DES encrypt invalid parameter, clearText is null!");
		}
		if (key == null || Arrays.isZeroized(key)){
			throw new InvalidParameterException("DES encrypt invalid parameter, key is null or zero!");
		}
		// perform op
		byte[] input = null;
		try {
			input = clearText.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new CipherException("encyrpt error: ", e);
		}
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new BlowfishEngine());
		cipher.reset();
		cipher.init(true, new KeyParameter(key));
		byte[] outblock = new byte[cipher.getOutputSize(input.length)];
		int len = cipher.processBytes(input, 0, input.length, outblock, 0);
		try {
			len += cipher.doFinal(outblock, len);
		} catch (DataLengthException e) {
			throw new CipherException("encyrpt error: ", e);
		} catch (IllegalStateException e) {
			throw new CipherException("encyrpt error: ", e);
		} catch (InvalidCipherTextException e) {
			throw new CipherException("encyrpt error: ", e);
		}
		cipher.reset();
		String cipherText = Hex.encodeHex(outblock);
		return cipherText.toUpperCase();		
	}
	
	
	public static String decrypt(String cipherText, byte[] key) throws CipherException{
		// check param
		if (cipherText == null){
			throw new InvalidParameterException("DES decrypt invalid parameter, cipherText is null!");
		}
		if (key == null || Arrays.isZeroized(key)){
			throw new InvalidParameterException("DES decrypt invalid parameter, key is null or zero!");
		}
		// perform op
		byte[] input = null;
		input = Hex.decode(cipherText);
		
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new BlowfishEngine());
		cipher.reset();
		cipher.init(false, new KeyParameter(key));
		byte[] outblock = new byte[cipher.getOutputSize(input.length)];
		int len = cipher.processBytes(input, 0, input.length, outblock, 0);
		try {
			len += cipher.doFinal(outblock, len);
		} catch (DataLengthException e) {
			throw new CipherException("decyrpt error: ", e);
		} catch (IllegalStateException e) {
			throw new CipherException("decyrpt error: ", e);
		} catch (InvalidCipherTextException e) {
			throw new CipherException("decyrpt error: ", e);
		}
		cipher.reset();
		String clearText;
		try {
			clearText = new String(outblock, 0, len, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new CipherException("decyrpt error: ", e);
		}
		return clearText;		
	}
	
	
	public static void main(String[] args) throws Exception {
		byte[] key = {-38, 21, 100, -99, 108, 80, -95, 94, 66, -116, -119, 100, 92, 12, 60, 52, 63, -8, 117, 57, 71, -94, -96, -36, 84, 28, 39, 89, -28, -62, 43, 71};
		String clearTexts[] = {"U_tywg_2008_ftp", ""};
		for (String clearText : clearTexts){
			String ct = CipherService.getInstance().encrypt(CipherService.Algorithms.Blowfish, 
					CipherService.SecretKeys.Blowfish_256_20120220, 
					clearText);  //BlowfishHelper.encrypt(clearText, key);
			System.out.println(ct);
		}
		
		String cipherTexts[] = {"B65D34C3DD3EB6AC344D624D2D154600",	"DBA6A121EC1A7426"};
		for (String cipherText : cipherTexts){
			String ct = CipherService.getInstance().decrypt(CipherService.Algorithms.Blowfish, 
					CipherService.SecretKeys.Blowfish_256_20120220, 
					cipherText);
			System.out.println(ct);		
		}
	}
	
	private final static byte[] key256 = {-38, 21, 100, -99, 108, 80, -95, 94, 66, -116, -119, 100, 92, 12, 60, 52, 63, -8, 117, 57, 71, -94, -96, -36, 84, 28, 39, 89, -28, -62, 43, 71};

}
