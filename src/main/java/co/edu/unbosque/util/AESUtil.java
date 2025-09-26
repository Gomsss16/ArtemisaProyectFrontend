package co.edu.unbosque.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Clase de utilidades para el cifrado y descifrado de cadenas de texto
 * utilizando el algoritmo {@code AES} con el modo de operación {@code CBC} y
 * padding {@code PKCS5}.
 *
 * <p>
 * Esta clase proporciona métodos estáticos para encriptar y desencriptar texto
 * con llaves personalizadas o con llaves por defecto preconfiguradas.
 * </p>
 *
 * <p>
 * La codificación del resultado se realiza en Base64.
 * </p>
 *
 * Ejemplo de uso:
 * 
 * <pre>{@code
 * String mensaje = "Hola mundo";
 * String encriptado = AESUtil.encrypt(mensaje);
 * String desencriptado = AESUtil.decrypt(encriptado);
 * System.out.println("Texto original: " + mensaje);
 * System.out.println("Texto cifrado: " + encriptado);
 * System.out.println("Texto descifrado: " + desencriptado);
 * }</pre>
 *
 * ⚠️ Nota: Se recomienda manejar las llaves e IV de manera segura (no
 * hardcodeadas en producción).
 *
 * @author
 * @version 1.0
 */
public class AESUtil {

	/** Algoritmo base utilizado para el cifrado */
	private static final String algoritmo = "AES";

	/** Tipo de cifrado que incluye modo de operación y padding */
	private static final String tipoCifrado = "AES/CBC/PKCS5Padding";

	/**
	 * Cifra un texto plano con una llave y un vector de inicialización (IV).
	 *
	 * @param llave llave secreta de 16 caracteres (128 bits)
	 * @param iv    vector de inicialización de 16 caracteres
	 * @param texto el texto plano a cifrar
	 * @return el texto cifrado en Base64
	 */
	public static String encrypt(String llave, String iv, String texto) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(tipoCifrado);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), algoritmo);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(texto.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		return new String(encodeBase64(encrypted));
	}

	/**
	 * Descifra un texto previamente encriptado con AES/CBC/PKCS5Padding.
	 *
	 * @param llave     llave secreta de 16 caracteres (128 bits)
	 * @param iv        vector de inicialización de 16 caracteres
	 * @param encrypted el texto cifrado en Base64
	 * @return el texto descifrado en formato plano
	 */
	public static String decrypt(String llave, String iv, String encrypted) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(tipoCifrado);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), algoritmo);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
		byte[] enc = decodeBase64(encrypted);
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		byte[] decrypted = null;
		try {
			decrypted = cipher.doFinal(enc);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		return new String(decrypted);
	}

	/**
	 * Descifra un texto cifrado utilizando una llave e IV por defecto.
	 * <p>
	 * Llave: {@code llavede16carater} <br>
	 * IV: {@code programacioncomp}
	 * </p>
	 *
	 * @param encrypted texto cifrado en Base64
	 * @return el texto plano descifrado
	 */
	public static String decrypt(String encrypted) {
		String iv = "programacioncomp";
		String key = "llavede16carater";
		return decrypt(key, iv, encrypted);
	}

	/**
	 * Cifra un texto plano utilizando una llave e IV por defecto.
	 * <p>
	 * Llave: {@code llavede16carater} <br>
	 * IV: {@code programacioncomp}
	 * </p>
	 *
	 * @param plainText el texto en claro a cifrar
	 * @return el texto cifrado en Base64
	 */
	public static String encrypt(String plainText) {
		String iv = "programacioncomp";
		String key = "llavede16carater";
		return encrypt(key, iv, plainText);
	}

}
