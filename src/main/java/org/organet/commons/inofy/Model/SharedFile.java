package org.organet.commons.inofy.Model;

import org.organet.commons.inofy.FileTypes;
import org.organet.commons.inofy.Hasher;
import org.organet.commons.inofy.KeywordList;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

// FIXME Do NOT extend File
public class SharedFile extends File implements Serializable {
  private String ndnid = null; // Named Data Network Identifier
  private String ndntype = null; // Named Data Network File Type
  private List<String> keywords = new KeywordList<>(); // TODO
//  private List<String> keywords = new ArrayList<>(); // TODO
  private transient String localPath = null; // Absolute path of the file, MUST be set by Watcher, TODO when not extending the File, rename it to 'path
  private transient long lastModified = -1; // TODO Is this really necessary?
  private String hash = null; // TODO Is this transient or not?
  private long size = -1;

  private void initialize() {
    ndntype = FileTypes.getFileType(getExtension());
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

  String getExtension() {
    String name = getName();

    try {
      return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
    } catch (Exception e) {
      return "";
    }
  }

  String getNDNType() {
    return ndntype;
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

  // Keyword can be Java regex string
  public boolean hasKeyword(String keyword) {
    return keywords.contains(keyword);
  }

  public String getScreenName(){
    return getName();
  }

  // TODO `compareTo()`


  @Override
  public String toString() {
    return "SharedFile{" +
            "ndnid='" + ndnid + '\'' +
            ", ndntype='" + ndntype + '\'' +
            ", keywords=" + keywords +
            ", localPath='" + localPath + '\'' +
            ", lastModified=" + lastModified +
            ", hash='" + hash + '\'' +
            ", size=" + size +
            '}';
  }
}
