package org.organet.commons.inofy;

import org.organet.commons.inofy.Model.SharedFile;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;

// TODO `implements Serializable` or `.serialize()`
public class Index implements Serializable {
  private ArrayList<SharedFile> sharedFiles;

  public Index() {
    sharedFiles = new ArrayList<>();
  }

  public int size() {
    return sharedFiles.size();
  }

  // Performance O(N)
  public boolean contains(String ndnid) {
    for (SharedFile sharedFile : sharedFiles) {
      if (sharedFile.getNDNID().equals(ndnid)) {
        return true;
      }
    }

    return false;
  }

  // TODO Other 'contains' methods may be implemented \
  // (e.g. `.contains(SharedFile ...)` or `.contains(File ...`)

  public boolean add(SharedFile sharedFile) {
    return sharedFiles.add(sharedFile);
  }

  public boolean remove(SharedFile sharedFile) {
    return sharedFiles.remove(sharedFile);
  }

  public void clear() {
    sharedFiles.clear();
  }

  public SharedFile get(int index) {
    return sharedFiles.get(index);
  }

  public SharedFile get(String ndnid) {
    for (SharedFile sharedFile : sharedFiles) {
      if (sharedFile.getNDNID().equals(ndnid)) {
        return sharedFile;
      }
    }

    return null;
  }

  public SharedFile remove(String ndnid) {
    SharedFile itemToBeRemoved = null;

    for (int i = 0, len = sharedFiles.size(); i < len; i++) {
      if (sharedFiles.get(i).getNDNID().equals(ndnid)) {
        itemToBeRemoved = sharedFiles.get(i);
        sharedFiles.remove(i);

        break;
      }
    }

    return itemToBeRemoved;
  }

  public SharedFile remove(Path filename) {
    SharedFile itemToBeRemoved = null;

    for (int i = 0, len = sharedFiles.size(); i < len; i++) {
      if (sharedFiles.get(i).getPath().equals(filename)) {
        itemToBeRemoved = sharedFiles.get(i);
        sharedFiles.remove(i);

        break;
      }
    }

    return itemToBeRemoved;
  }

  public int indexOf(String ndnid) {
    for (int i = 0, len = sharedFiles.size(); i < len; i++) {
      if (sharedFiles.get(i).getNDNID().equals(ndnid)) {
        return i;
      }
    }

    return -1;
  }

  // NOTE This method can be considered `.set(int, element)` of List interface
  public boolean update(String ndnid, SharedFile sharedFile) {
    int index = indexOf(ndnid);

    if (index > -1) {
      // exists - replace with the given one
      return (sharedFiles.set(index, sharedFile).getNDNID().equals(ndnid));
    } else {
      // doesn't exist - add the given shared file
      return add(sharedFile);
    }
  }
}
