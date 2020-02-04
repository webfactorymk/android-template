package mk.webfactory.template.data.storage;

import android.support.annotation.NonNull;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileLock;
import timber.log.Timber;

final class Util {

  private static final int DEF_BYTE_BUFFER_SIZE = 96000;

  public static String readFullyUtf8(File file) {
    if (!file.exists()) {
      Timber.d("File not found " + file.getPath());
      return null;
    }
    FileInputStream fis = null;
    BufferedReader br = null;
    try {
      fis = new FileInputStream(file);
      br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
      StringBuilder content = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line);
        content.append('\n');
      }
      return content.toString();
    } catch (IOException ignored) {
      return null;
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
        if (br != null) {
          br.close();
        }
      } catch (IOException ignored) {
      }
    }
  }

  /** Convenience for calling {@link #write(InputStream, File)} with a ByteArrayInputStream. */
  public static boolean writeUtf8(String content, File outFile) {
    return write(content.getBytes(), outFile);
  }

  /** Convenience for calling {@link #write(InputStream, File)} with a ByteArrayInputStream. */
  public static boolean write(byte[] bytes, File outFile) {
    return write(new ByteArrayInputStream(bytes), outFile);
  }

  /** @return {@code true} if {@code content} was written in {@code outFile}. */
  public static boolean write(InputStream content, File outFile) {
    if (outFile.getParentFile().mkdirs() && !outFile.getParentFile().exists()) {
      Timber.d("Cannot create file " + outFile.getPath());
      return false;
    }
    BufferedOutputStream outStream = null;
    BufferedInputStream inStream = null;
    FileLock lock = null;
    try {
      FileOutputStream fos = new FileOutputStream(outFile);
      lock = fos.getChannel().lock();
      outStream = new BufferedOutputStream(fos);
      inStream = new BufferedInputStream(content);
      final byte[] buffer;
      if (inStream.available() <= 0) {
        buffer = new byte[DEF_BYTE_BUFFER_SIZE];
      } else {
        buffer = new byte[Math.min(inStream.available(), DEF_BYTE_BUFFER_SIZE)];
      }
      int bytesRead;
      while ((bytesRead = inStream.read(buffer)) != -1) {
        outStream.write(buffer, 0, bytesRead);
      }
      return true;
    } catch (IOException e) {
      releaseFileLock(lock);
      return false;
    } finally {
      try {
        releaseFileLock(lock);
        if (outStream != null) {
          outStream.flush();
          outStream.close();
        }
        if (inStream != null) {
          inStream.close();
        }
      } catch (IOException ignored) {
      }
    }
  }

  /**
   * @return {@code true} if the file was deleted or does not exist,
   * {@code false} if an error occurred or {@code file} points to a directory.
   */
  public static boolean deleteFile(@NonNull File file) {
    return !file.exists() || file.isFile() && file.delete();
  }

  private static void releaseFileLock(FileLock lock) {
    if (lock != null && lock.isValid()) {
      try {
        lock.release();
      } catch (IOException ignored) {
      }
    }
  }

  private Util() {
  }
}
