package org.organet.commons.inofy;

import org.organet.commons.inofy.Model.SharedFile;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// TODO `implements Serializable` or `.serialize()`
public class Index implements Serializable {
  private ArrayList<SharedFile> sharedFiles;

  public Index() {
    sharedFiles = new ArrayList<>();
  }

  public ArrayList<SharedFile> getSharedFiles() {
    return sharedFiles;
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
    // TODO Invoke the necessary method to propagate the updated index
    // TODO TR Future work'e yaz: Sadece değişiklikler propagate edilebilir
  }

  public boolean remove(SharedFile sharedFile) {
    return sharedFiles.remove(sharedFile);
    // TODO Invoke the necessary method to propagate the updated index
    // TODO TR Future work'e yaz: Sadece değişiklikler propagate edilebilir
  }

  public void clear() {
    sharedFiles.clear();
    // TODO Invoke the necessary method to propagate the updated index
    // TODO TR Future work'e yaz: Sadece değişiklikler propagate edilebilir
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
    // TODO Invoke the necessary method to propagate the updated index
    // TODO TR Future work'e yaz: Sadece değişiklikler propagate edilebilir
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
    // TODO Invoke the necessary method to propagate the updated index
    // TODO TR Future work'e yaz: Sadece değişiklikler propagate edilebilir
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
    // TODO Invoke the necessary method to propagate the updated index
    // TODO TR Future work'e yaz: Sadece değişiklikler propagate edilebilir
  }

  public List<SharedFile> search(String keyword) {
    List<SharedFile> foundSharedFiles = new ArrayList<>();

    for (SharedFile sharedFile : sharedFiles) {
      if (sharedFile.hasKeyword(keyword)) {
        foundSharedFiles.add(sharedFile);
      }
    }

    return foundSharedFiles;
  }

  @Override
  public String toString() {
    return "Index{" +
            "sharedFiles=" + sharedFiles +
            '}';
  }

  public void addAllSharedFiles(Index newIndex){
    for (int i=0;i<newIndex.size();i++){
      this.add(newIndex.get(i));
    }
  }

  public SharedFile findIndex(String selectedFileScreenName) {
    for (SharedFile sh :
            this.getSharedFiles()) {
      if (sh.getScreenName().equals(selectedFileScreenName))
        return sh;
    }

    System.out.println("SharedFile couldn't find on networkIndex");
    return null;
  }

}
