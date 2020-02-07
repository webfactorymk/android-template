package mk.webfactory.storage

import io.reactivex.annotations.NonNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.nio.channels.FileLock


private val logger: Logger = LoggerFactory.getLogger("Util")

private const val DEF_BYTE_BUFFER_SIZE = 96000

fun readFullyUtf8(file: File): String? {
    if (!file.exists()) {
        logger.debug("File not found " + file.path)
        return null
    }
    var fis: FileInputStream? = null
    var br: BufferedReader? = null
    return try {
        fis = FileInputStream(file)
        br = BufferedReader(InputStreamReader(fis, "UTF-8"))
        val content = StringBuilder()
        var line: String?
        while (br.readLine().also { line = it } != null) {
            content.append(line)
            content.append('\n')
        }
        content.toString()
    } catch (ignored: IOException) {
        null
    } finally {
        try {
            fis?.close()
            br?.close()
        } catch (ignored: IOException) {
        }
    }
}

/** Convenience for calling [write] with a ByteArrayInputStream.  */
fun writeUtf8(content: String, outFile: File): Boolean {
    return write(content.toByteArray(), outFile)
}

/** Convenience for calling [write] with a ByteArrayInputStream.  */
fun write(bytes: ByteArray?, outFile: File): Boolean {
    return write(ByteArrayInputStream(bytes), outFile)
}

/** @return true if [content] was written in [outFile].
 */
fun write(content: InputStream, outFile: File): Boolean {
    if (outFile.parentFile.mkdirs() && !outFile.parentFile.exists()) {
        logger.debug("Cannot create file ${outFile.path}")
        return false
    }
    var outStream: BufferedOutputStream? = null
    var inStream: BufferedInputStream? = null
    var lock: FileLock? = null
    return try {
        val fos = FileOutputStream(outFile)
        lock = fos.channel.lock()
        outStream = BufferedOutputStream(fos)
        inStream = BufferedInputStream(content)
        val buffer: ByteArray = if (inStream.available() <= 0) {
            ByteArray(DEF_BYTE_BUFFER_SIZE)
        } else {
            ByteArray(Math.min(inStream.available(), DEF_BYTE_BUFFER_SIZE))
        }
        var bytesRead: Int
        while (inStream.read(buffer).also { bytesRead = it } != -1) {
            outStream.write(buffer, 0, bytesRead)
        }
        true
    } catch (e: IOException) {
        releaseFileLock(lock)
        false
    } finally {
        try {
            releaseFileLock(lock)
            if (outStream != null) {
                outStream.flush()
                outStream.close()
            }
            inStream?.close()
        } catch (ignored: IOException) {
        }
    }
}

/**
 * @return true if the file was deleted or does not exist,
 * false if an error occurred or [file] points to a directory.
 */
fun deleteFile(@NonNull file: File): Boolean {
    return !file.exists() || file.isFile && file.delete()
}

private fun releaseFileLock(lock: FileLock?) {
    if (lock != null && lock.isValid) {
        try {
            lock.release()
        } catch (ignored: IOException) {
        }
    }
}