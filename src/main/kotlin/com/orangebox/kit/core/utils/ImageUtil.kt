package com.orangebox.kit.core.utils

import com.orangebox.kit.core.photo.TypeAdjustImageEnum
import org.apache.commons.io.FileUtils
import org.apache.sanselan.Sanselan
import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.stream.ImageOutputStream

class ImageUtil {
    var tempoMaximoUploadImagem = CINCO_MINUTOS
    var tipoArquivo = JPG
    @Throws(Exception::class)
    fun mudarTamanhoImagemProporcinal(
        nomeOriginal: String,
        stream: InputStream?,
        destino: File,
        larguraPadrao: Int,
        tamanhoMinimo: Long,
        processarDelete: Boolean
    ): String {
        if (!destino.isDirectory) {
            destino.mkdirs()
        }
        val imagem: BufferedImage = ImageIO.read(stream)
        val larguraAtual: Int = imagem.getWidth()
        val alturaAtual: Int = imagem.getHeight()

        //ALTERA O TIPO DO ARQUIVO
        val nomeArquivo = nomeOriginal.replace("\\..*".toRegex(), ".$tipoArquivo")
        val pathImagem = destino.absolutePath + File.separator + nomeArquivo
        val fileImagem = File(pathImagem)
        if (imagem.getWidth() > larguraPadrao) {

            //calcula a altura proporcional
            val alturaProporcional = alturaAtual * larguraPadrao / larguraAtual
            val imagemAjustada: BufferedImage = mudarTamanhoImagem(imagem, larguraPadrao, alturaProporcional)
            imagem.flush()
            ImageIO.write(imagemAjustada, tipoArquivo, fileImagem)
            imagemAjustada.flush()
        } else {
            ImageIO.write(imagem, tipoArquivo, fileImagem)
            imagem.flush()
        }
        val agora = Date().time
        val arquivos = destino.listFiles()
        if (processarDelete && arquivos != null) {
            for (i in arquivos.indices) {
                if (agora - arquivos[i].lastModified() >= tempoMaximoUploadImagem) {
                    FileUtils.forceDelete(arquivos[i])
                }
            }
        }


        //se o tamanho (em disco) for maior que o estipulado, diminui a qualidade via 2d
        if (tamanhoMinimo != -1L && fileImagem.length() > tamanhoMinimo) {
            val fileJpg = File(destino.absoluteFile.toString() + File.separator + nomeArquivo)
            val `is`: InputStream = FileInputStream(fileImagem)
            val os: OutputStream = FileOutputStream(fileJpg)
            val quality = 0.7f // Change this as needed
            val image: BufferedImage = ImageIO.read(`is`)

            // get all image writers for JPG format
            val writers: Iterator<ImageWriter> = ImageIO.getImageWritersByFormatName("jpg")
            val writer: ImageWriter = writers.next() as ImageWriter
            val ios: ImageOutputStream = ImageIO.createImageOutputStream(os)
            writer.setOutput(ios)

            // set compression quality
            val param: ImageWriteParam = writer.getDefaultWriteParam()
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT)
            param.setCompressionQuality(quality)
            writer.write(null, IIOImage(image, null, null), param)
            os.flush()
            os.close()
            `is`.close()
        }
        return pathImagem
    }

    @Throws(Exception::class)
    fun recortarImagemComAjuste(
        origem: File,
        destino: File,
        largura: Int,
        altura: Int,
        processarDelete: Boolean,
        ajusteImagemEnum: TypeAdjustImageEnum,
        tamanhoMinimo: Long
    ): String {
        val nomeOriginalArray = origem.name.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val nomeOriginal = nomeOriginalArray[nomeOriginalArray.size - 1]
        val dados = FileUtil.readFile(origem.absolutePath)
        return recortarImagemComAjuste(
            nomeOriginal,
            dados,
            destino,
            largura,
            altura,
            processarDelete,
            ajusteImagemEnum,
            tamanhoMinimo
        )
    }

    @Throws(Exception::class)
    fun recortarImagemComAjuste(
        nomeOriginal: String,
        dados: ByteArray?,
        destino: File,
        larguraFinal: Int,
        alturaFinal: Int,
        processarDelete: Boolean,
        ajusteImagemEnum: TypeAdjustImageEnum,
        tamanhoMinimo: Long
    ): String {
        var ajusteImagemEnum: TypeAdjustImageEnum = ajusteImagemEnum
        if (!destino.isDirectory) {
            destino.mkdirs()
        }

        //e la vamos nos...
        var imagemFinal: BufferedImage? = null
        val bais = ByteArrayInputStream(dados)
        var imagemInicial: BufferedImage = ImageIO.read(bais)
        val metadata: JpegImageMetadata = Sanselan.getMetadata(dados) as JpegImageMetadata
        if (metadata != null) {
            val fieldWidth: TiffField = metadata.findEXIFValue(TiffConstants.EXIF_TAG_ORIENTATION)
            if (fieldWidth != null && fieldWidth.getIntValue() === 6) {
                imagemInicial = rotate90ToRight(imagemInicial)
            }
        }
        val larguraInicial: Int = imagemInicial.getWidth()
        val alturaInicial: Int = imagemInicial.getHeight()


        //ALTERA O TIPO DO ARQUIVO
        val nomeArquivo = nomeOriginal.replace("\\..*".toRegex(), ".$tipoArquivo")
        val pathImagem = destino.absolutePath + File.separator + nomeArquivo
        val fileImagem = File(pathImagem)
        if (ajusteImagemEnum.equals(TypeAdjustImageEnum.BASE_WIDTH)) {
            if (larguraInicial > larguraFinal) {

                //ajusta o tamanho, para que tenha a mesma largura
                val alturaProporcional = alturaInicial * larguraFinal / larguraInicial

                //se a altura proporcional for menor que a altura final, a altura
                if (alturaProporcional < alturaFinal) {
                    ajusteImagemEnum = TypeAdjustImageEnum.BASE_HEIGHT
                } else {
                    val imagemAjustada: BufferedImage =
                        mudarTamanhoImagem(imagemInicial, larguraFinal, alturaProporcional)
                    imagemInicial.flush()

                    //recorta o excedente de altura, se houver
                    if (alturaProporcional > alturaFinal) {
                        val yCrop = (alturaProporcional - alturaFinal) / 2
                        imagemFinal = imagemAjustada.getSubimage(0, yCrop, larguraFinal, alturaFinal)
                        imagemAjustada.flush()
                    } else {
                        imagemFinal = imagemAjustada
                    }
                }
            } else {
                if (alturaInicial > alturaFinal) {
                    val yCrop = (alturaInicial - alturaFinal) / 2
                    imagemFinal = imagemInicial.getSubimage(0, yCrop, larguraInicial, alturaFinal)
                    imagemInicial.flush()
                } else {
                    imagemFinal = imagemInicial
                }
            }
        }
        if (ajusteImagemEnum.equals(TypeAdjustImageEnum.BASE_HEIGHT)) {
            if (alturaInicial > alturaFinal) {

                //ajusta o tamanho, para que tenha a mesma altura
                val larguraProporcional = larguraInicial * alturaFinal / alturaInicial
                val imagemAjustada: BufferedImage = mudarTamanhoImagem(imagemInicial, larguraProporcional, alturaFinal)
                imagemInicial.flush()

                //recorta o excedente de largura, se houver
                if (larguraProporcional > larguraFinal) {
                    val xCrop = (larguraProporcional - larguraFinal) / 2
                    imagemFinal = imagemAjustada.getSubimage(xCrop, 0, larguraFinal, alturaFinal)
                    imagemAjustada.flush()
                } else {
                    imagemFinal = imagemAjustada
                }
            } else {
                if (larguraInicial > larguraFinal) {
                    val xCrop = (larguraInicial - larguraFinal) / 2
                    imagemFinal = imagemInicial.getSubimage(xCrop, 0, larguraFinal, alturaInicial)
                    imagemInicial.flush()
                } else {
                    imagemFinal = imagemInicial
                }
            }
        }
        //		else{
//			throw new Exception("Selecione um tipo de ajuste de imagem");
//		}
        ImageIO.write(imagemFinal, tipoArquivo, fileImagem)
        imagemFinal.flush()
        val agora = Date().time
        val arquivos = destino.listFiles()
        if (processarDelete && arquivos != null) {
            for (i in arquivos.indices) {
                if (agora - arquivos[i].lastModified() >= tempoMaximoUploadImagem) {
                    FileUtils.forceDelete(arquivos[i])
                }
            }
        }


        //se o tamanho (em disco) for maior que o estipulado, diminui a qualidade via 2d
        if (tamanhoMinimo != -1L && fileImagem.length() > tamanhoMinimo) {
            val fileJpg = File(destino.name + File.separator + nomeArquivo)
            val `is`: InputStream = FileInputStream(fileImagem)
            val os: OutputStream = FileOutputStream(fileJpg)
            val quality = 0.7f // Change this as needed
            val image: BufferedImage = ImageIO.read(`is`)

            // get all image writers for JPG format
            val writers: Iterator<ImageWriter> = ImageIO.getImageWritersByFormatName("jpg")
            val writer: ImageWriter = writers.next() as ImageWriter
            val ios: ImageOutputStream = ImageIO.createImageOutputStream(os)
            writer.setOutput(ios)

            // set compression quality
            val param: ImageWriteParam = writer.getDefaultWriteParam()
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT)
            param.setCompressionQuality(quality)
            writer.write(null, IIOImage(image, null, null), param)
            os.flush()
            os.close()
            `is`.close()
        }
        return pathImagem
    }

    fun rotate90ToRight(inputImage: BufferedImage): BufferedImage {
        val width: Int = inputImage.getWidth()
        val height: Int = inputImage.getHeight()
        val returnImage = BufferedImage(height, width, inputImage.getType())
        for (x in 0 until width) {
            for (y in 0 until height) {
                returnImage.setRGB(height - y - 1, x, inputImage.getRGB(x, y))
                //Again check the Picture for better understanding
            }
        }
        return returnImage
    }

    fun mudarTamanhoImagem(image: BufferedImage, width: Int, height: Int): BufferedImage {
        val type: Int = if (image.getType() == 0) BufferedImage.TYPE_INT_ARGB else image.getType()
        val resizedImage = BufferedImage(width, height, type)
        val g: Graphics2D = resizedImage.createGraphics()
        g.setComposite(AlphaComposite.Src)
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.drawImage(image, 0, 0, width, height, null)
        image.flush()
        g.dispose()
        return resizedImage
    }

    @Throws(Exception::class)
    fun moverImagens(
        pathOrigem: String,
        pathDestino: String,
        nomeImagemOrigem: Array<String>,
        nomeImagemDestino: Array<String>
    ) {
        val dirTemporiario = File(pathOrigem)
        for (i in nomeImagemDestino.indices) {
            val imagemOrigem = nomeImagemOrigem[i]
            val imagemDestino = nomeImagemDestino[i]
            val dirOrigem = File("$pathOrigem/$imagemOrigem")
            val dirDestino = File("$pathDestino/$imagemDestino")
            if (!dirDestino.exists()) {
                FileUtils.copyFile(dirOrigem, dirDestino)
            }
        }
        FileUtils.deleteDirectory(dirTemporiario)
    }

    @Throws(Exception::class)
    fun remover(pathDestino: String?) {
        val file = File(pathDestino)
        if (file.isFile) {
            FileUtils.forceDelete(file)
        }
    }

    @Throws(Exception::class)
    fun verificarImagem(pathDestino: String?): Boolean {
        return File(pathDestino).exists()
    }

    @Throws(Exception::class)
    fun obterLarguraImagem(conteudoArquivo: ByteArray?): Int {
        val bais = ByteArrayInputStream(conteudoArquivo)
        val imagem: BufferedImage = ImageIO.read(bais)
        bais.close()
        val largura: Int = imagem.getWidth()
        imagem.flush()
        return largura
    }

    @Throws(Exception::class)
    fun obterDimensoesImagem(conteudoArquivo: ByteArray?): IntArray {
        val bais = ByteArrayInputStream(conteudoArquivo)
        val imagem: BufferedImage = ImageIO.read(bais)
        bais.close()
        val largura: Int = imagem.getWidth()
        val altura: Int = imagem.getHeight()
        imagem.flush()
        return intArrayOf(largura, altura)
    }

    @Throws(Exception::class)
    fun processarImagemParaThumb(
        nomeOriginal: String,
        conteudoArquivo: ByteArray?,
        path: String,
        tamanhoPadrao: Int
    ): String {
        val filePath = File(path)
        if (!filePath.isDirectory) {
            filePath.mkdirs()
        }
        val bais = ByteArrayInputStream(conteudoArquivo)
        val imagem: BufferedImage = ImageIO.read(bais)
        bais.close() //ADICIONADO
        val larguraAtual: Int = imagem.getWidth()
        val alturaAtual: Int = imagem.getHeight()

        //ALTERA O TIPO DO ARQUIVO
        val nomeArquivo = nomeOriginal.replace("\\..*".toRegex(), ".$tipoArquivo")
        val pathImagem = path + File.separator + nomeArquivo


        //imagem horizontal
        if (larguraAtual >= alturaAtual && alturaAtual > tamanhoPadrao) {

            //calcula a largura proporcional
            val larguraProporcional = larguraAtual * tamanhoPadrao / alturaAtual
            val imagemAjustada: BufferedImage = mudarTamanhoImagem(imagem, larguraProporcional, tamanhoPadrao)
            val xCrop: Int = (imagemAjustada.getWidth() - tamanhoPadrao) / 2
            val dest: BufferedImage = imagemAjustada.getSubimage(xCrop, 0, tamanhoPadrao, tamanhoPadrao)
            ImageIO.write(dest, tipoArquivo, File(pathImagem))
            imagemAjustada.flush()
            dest.flush()
        } else if (alturaAtual >= larguraAtual && larguraAtual > tamanhoPadrao) {

            //calcula a altura proporcional
            val alturaProporcional = alturaAtual * tamanhoPadrao / larguraAtual
            val imagemAjustada: BufferedImage = mudarTamanhoImagem(imagem, tamanhoPadrao, alturaProporcional)
            val yCrop: Int = (imagemAjustada.getHeight() - tamanhoPadrao) / 2
            val dest: BufferedImage = imagemAjustada.getSubimage(0, yCrop, tamanhoPadrao, tamanhoPadrao)
            ImageIO.write(dest, tipoArquivo, File(pathImagem))
            imagemAjustada.flush()
            dest.flush()
        } else if (alturaAtual >= larguraAtual && tamanhoPadrao > larguraAtual) {
            //calcula a altura proporcional
            val alturaProporcional = alturaAtual * tamanhoPadrao / larguraAtual
            val imagemAjustada: BufferedImage = mudarTamanhoImagem(imagem, tamanhoPadrao, alturaProporcional)
            val yCrop: Int = (imagemAjustada.getHeight() - tamanhoPadrao) / 2
            val dest: BufferedImage = imagemAjustada.getSubimage(0, yCrop, tamanhoPadrao, tamanhoPadrao)
            ImageIO.write(dest, tipoArquivo, File(pathImagem))
            imagemAjustada.flush()
            dest.flush()
        } else {
            ImageIO.write(imagem, tipoArquivo, File(pathImagem))
        }
        imagem.flush()
        return pathImagem
    }

    @Throws(Exception::class)
    fun coresMaisUtilizadas(path: String?): List<String?> {
        var cores: MutableList<String?>? = null
        val file = File(path)
        val `is`: InputStream = FileInputStream(file)
        val image: BufferedImage = ImageIO.read(`is`)
        val coresMap: MutableMap<Int, Int> = HashMap()
        val w: Int = image.getWidth()
        val h: Int = image.getHeight()
        for (i in 0 until h) {
            for (j in 0 until w) {
                val clr: Int = image.getRGB(j, i)
                if (!coresMap.containsKey(clr)) {
                    coresMap[clr] = 0
                }
                coresMap[clr] = coresMap[clr]!! + 1
            }
        }
        val byMapValues: Comparator<Map.Entry<Int, Int>> = Comparator<Map.Entry<Int?, Int>> { (_, value), (_, value1) ->
            value1.compareTo(
                value
            )
        }
        val candyBars: MutableList<Map.Entry<Int, Int>> = ArrayList()
        candyBars.addAll(coresMap.entries)
        Collections.sort(candyBars, byMapValues)
        val listaFinal: MutableList<Int> = ArrayList()
        listaFinal.add(candyBars[0].key)
        for ((clr) in candyBars) {
            if (Math.abs(clr - listaFinal[listaFinal.size - 1]) > 10000000) {
                listaFinal.add(clr)
                if (listaFinal.size == MAX_CORES) {
                    break
                }
            }
        }
        cores = ArrayList()
        var i = 0
        while (i < listaFinal.size && i < MAX_CORES) {
            val clr = listaFinal[i]
            val red = clr and 0x00ff0000 shr 16
            val green = clr and 0x0000ff00 shr 8
            val blue = clr and 0x000000ff
            val hex = String.format("#%02x%02x%02x", red, green, blue)
            cores.add(hex)
            i++
        }
        return cores
    }

    companion object {
        private const val CINCO_MINUTOS = 750000
        private const val JPG = "jpg"
        private const val MAX_CORES = 4
    }
}