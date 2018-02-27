/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gabriel.models;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import gabriel.Controller.Hasher;

/**
 *
 * @author TheDoctor
 */
public class SharedFileHeader implements Serializable{

	private static final long serialVersionUID = 2716422227965416854L;
	private String name = "";
    private String path = null;
    private String mimeType = null;
    private String hash = null;
    private Long size = null;
    private Long lastModified = null;

    public String getHashValue(){
    	try {
    		hash = Hasher.calculateFileHash(this.getPath());
    		return hash;
    	} catch (IOException ex) {
    		Logger.getLogger(SharedFileHeader.class.getName()).log(Level.SEVERE, null, ex);
    	} catch (NoSuchAlgorithmException ex) {
    		Logger.getLogger(SharedFileHeader.class.getName()).log(Level.SEVERE, null, ex);
    	}
    	return hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SharedFileHeader other = (SharedFileHeader) obj;
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        if (!Objects.equals(this.mimeType, other.mimeType)) {
            return false;
        }
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        if (!Objects.equals(this.size, other.size)) {
            return false;
        }
        if (!Objects.equals(this.lastModified, other.lastModified)) {
            return false;
        }
        return true;
    }
    
    
    
}
