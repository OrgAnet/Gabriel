package org.organet.commons.gabriel.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Index implements Serializable {
  ArrayList<SharedFileHeader> fileHeaders;

  public Index(ArrayList<SharedFileHeader> fileHeaders) {
    this.fileHeaders = fileHeaders;
  }

  public Index() {
    this.fileHeaders = new ArrayList<>();

    //falsely filled
    SharedFileHeader fakeSharedFile = new SharedFileHeader();
    fakeSharedFile.setName("ilk file header");
    this.fileHeaders.add(fakeSharedFile);
    //false

  }

  public ArrayList<SharedFileHeader> getFileHeaders() {
    return fileHeaders;
  }

  public void setFileHeaders(ArrayList<SharedFileHeader> fileHeaders) {
    this.fileHeaders = fileHeaders;
  }

}
