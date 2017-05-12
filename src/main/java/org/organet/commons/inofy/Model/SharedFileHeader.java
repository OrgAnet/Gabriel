package org.organet.commons.inofy.Model;

import org.organet.commons.inofy.FileTypes;
import org.organet.commons.inofy.Hasher;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

// FIXME Do NOT extend File
public class SharedFileHeader extends File implements Serializable {
  private Integer ndnid = null; // Named Data Network Identifier
  private String ndntype = null; // Named Data Network File Type
  private transient String localPath = null; // Absolute path of the file, MUST be set by Watcher, TODO when not extending the File, rename it to 'path
  private transient long lastModified = -1; // TODO Is this really necessary?
  private String hash = null; // TODO Is this transient or not?
  public String fileName;
  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getFileName() {
    return fileName;
  }

  private String ip;

  private ArrayList<String> keywords = new ArrayList<>();


  private void initialize() {
    ndntype = FileTypes.getFileType(getExtension());
    keywords.add(getName());
    fileName = getName();
    try {
      ip = InetAddress.getLocalHost().getHostAddress().toString();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    hash = getHash();
  }

  public SharedFileHeader(String pathname) {
    super(pathname);
    localPath = pathname;

    initialize();
  }

  public SharedFileHeader(String parent, String child) {
    super(parent, child);

    initialize();
  }

  public SharedFileHeader(File parent, String child) {
    super(parent, child);

    initialize();
  }

  public SharedFileHeader(URI uri) {
    super(uri);

    initialize();
  }

  public static SharedFileHeader fromFile(File file) {
    return new SharedFileHeader(file.getPath());
  }

  public Integer getNDNID() {
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
    return ndnid +" - "+getName() +" - "+ ip;
  }

  // TODO `compareTo()`


  public List<String> getKeywords() {
    return keywords;
  }

  @Override
  public String toString() {
    return "SharedFileHeader{" +
            "fileName='"+ fileName +"\'"+
            ", ip='" +ip+"\'"+
            ", ndnid='" + ndnid + '\'' +
            ", ndntype='" + ndntype + '\'' +
            ", keywords=" + keywords +
            ", localPath='" + localPath + '\'' +
            ", lastModified=" + lastModified +
            ", hash='" + getHash() + '\'' +
            ", size=" + getSize() +
            '}';
  }

  public void setNDNid(Integer NDNid) {
    this.ndnid = NDNid;

  }
}
