package mk.webfactory.template.data.storage;

public class StorageException extends RuntimeException {

  public StorageException() {
  }

  public StorageException(String detailMessage) {
    super(detailMessage);
  }

  public StorageException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public StorageException(Throwable throwable) {
    super(throwable);
  }
}
