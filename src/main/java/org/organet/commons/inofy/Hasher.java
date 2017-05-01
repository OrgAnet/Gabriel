package org.organet.commons.inofy;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
  public static String calculateFileHash(String path) throws NoSuchAlgorithmException, IOException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    FileInputStream fis = new FileInputStream(path);

    long startTime = System.nanoTime();

    byte[] dataBytes = new byte[1024];

    int nread = 0;
    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    }
    byte[] mdbytes = md.digest();

    //convert the byte to hex format method 1
    StringBuffer sb = new StringBuffer();
    for (byte mdbyte : mdbytes) {
      sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
    }
    String hash = sb.toString();

    long endTime = System.nanoTime();

    System.out.format("%fms - %s\n", (endTime - startTime) / 1e6, hash); // FIXME Remove console print

    return hash;
  }
}
