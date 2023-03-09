package com.orangebox.kit.core.utils

import java.awt.image.BufferedImage
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO

object FileUtil {
    /**
     *
     * Ler arquivo e converter para array de bytes
     *
     * @param caminhoArquivo
     * @return
     * @throws ServletException
     * @throws IOException
     */
	@JvmStatic
	fun readFile(filePath: String): ByteArray? {
        var byteArray: ByteArray? = null
        try {
            val file = File(filePath)
            var bytesread = 0
            byteArray = ByteArray(file.length().toInt())
            if (file.exists()) {
                val fis = FileInputStream(file)
                val bis = BufferedInputStream(fis)
                var b = 0
                while (bis.read().also { b = it } != -1) {
                    byteArray[bytesread++] = b.toByte()
                }
                bis.close()
            } else {
                println("File not found: $filePath")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArray
    }

    /**
     *
     * Ler arquivo e converter para array de bytes
     *
     * @param caminhoArquivo
     * @return
     * @throws ServletException
     * @throws IOException
     */
    fun readImageInfo(filePath: String?): ImageInfo? {
        var imagemInfo: ImageInfo? = null
        try {
            val file = File(filePath)
            val imagem: BufferedImage = ImageIO.read(file)
            imagemInfo = ImageInfo()
            imagemInfo.height = imagem.getHeight()
            imagemInfo.width = imagem.getWidth()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imagemInfo
    }
}