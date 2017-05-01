package org.organet.commons.inofy.Model;

import org.organet.commons.inofy.Hasher;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.net.URI;

// FIXME Do NOT extend File
public class SharedFile extends File implements Serializable {
  private String ndnid = null; // Named Data Network Identifier
  private transient String localPath = null; // Absolute path of the file, MUST be set by Watcher, TODO when not extending the File, rename it to 'path
  private transient long lastModified = -1; // TODO Is this really necessary?
  private String mimeType = null;
  private String hash = null; // TODO Is this transient or not?
  private long size = -1;

  private void initialize() {
    probeMimeType();

    // `probeMimeType` method sets the `mimeType` field
    hash = getHash();
  }

  public SharedFile(String pathname) {
    super(pathname);
    localPath = pathname;

    initialize();
  }

  public SharedFile(String parent, String child) {
    super(parent, child);

    initialize();
  }

  public SharedFile(File parent, String child) {
    super(parent, child);

    initialize();
  }

  public SharedFile(URI uri) {
    super(uri);

    initialize();
  }

  public static SharedFile fromFile(File file) {
    return new SharedFile(file.getPath());
  }

  public String getNDNID() {
    return ndnid;
  }

  private void probeMimeType() {
    // TODO Implement `probeMimeType` method

    mimeType = "text/plain";
  }

  String getMimeType() {
    return mimeType;
  }

  String getLocalPath() {
    return localPath;
  }

  String getHash(boolean force) {
    if ((hash != null && hash.length() > 0) && !force) {
      return hash;
    }

    try {
      hash = Hasher.calculateFileHash(localPath);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return hash;
  }

  String getHash() {
    return getHash(false);
  }

  long getSize() {
    return length();
  }

  long getLastModified() {
    return lastModified();
  }

  boolean isRemoteFile() {
    return (getPath() == null && lastModified == -1);
  }

  // TODO `compareTo()`
}
