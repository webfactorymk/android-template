/*
 * MIT License
 *
 * Copyright (c) 2020 Web Factory LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mk.webfactory.template.data.storage

import androidx.annotation.NonNull
import timber.log.Timber
import java.io.*
import java.nio.channels.FileLock


private const val DEF_BYTE_BUFFER_SIZE = 96000

fun readFullyUtf8(file: File): String? {
    if (!file.exists()) {
        Timber.d("File not found " + file.path)
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
        Timber.d("Cannot create file ${outFile.path}")
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