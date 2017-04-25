/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author TheDoctor
 */
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
