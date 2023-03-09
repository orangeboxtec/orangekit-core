package com.orangebox.kit.core.utils

import org.apache.commons.codec.binary.Base64
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object SecUtils {
    fun crypt(keyBase: String, text: String): String {
        val infoCrypt: String
        val aesKey: Key = SecretKeySpec(keyBase.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, aesKey)
        val encrypted = cipher.doFinal(text.toByteArray())
        infoCrypt = Base64.encodeBase64String(encrypted)
        return infoCrypt
    }

    fun decrypt(keyBase: String, text: String?): String {
        val infoCrypt: String
        val aesKey: Key = SecretKeySpec(keyBase.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, aesKey)
        val textData = Base64.decodeBase64(text)
        val dados = cipher.doFinal(textData)
        infoCrypt = dados.toString()
        return infoCrypt
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generateHash(salt: String?, text: String): String {
        val iterations = 20000
        val chars = text.toCharArray()
        val saltBytes = Base64.decodeBase64(salt)
        val spec = PBEKeySpec(chars, saltBytes, iterations, 64 * 8)
        val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val hash = skf.generateSecret(spec).encoded
        return Base64.encodeBase64String(hash)
    }

    @get:Throws(NoSuchAlgorithmException::class)
    private val saltBytes: ByteArray
        get() {
            val sr = SecureRandom.getInstance("SHA1PRNG")
            val salt = ByteArray(16)
            sr.nextBytes(salt)
            return salt
        }

    @get:Throws(NoSuchAlgorithmException::class)
    val salt: String
        get() {
            val salt = saltBytes
            return Base64.encodeBase64String(salt)
        }
}