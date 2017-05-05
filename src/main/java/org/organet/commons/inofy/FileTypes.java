package org.organet.commons.inofy;

import java.util.*;

public class FileTypes {
  private static Map<String, String> extensions = new HashMap<>();

  static void initialize() {
    extensions.put("aac", "Audio");
    extensions.put("flac", "Audio");
    extensions.put("m4a", "Audio");
    extensions.put("mp3", "Audio");
    extensions.put("ogg", "Audio");
    extensions.put("wav", "Audio");

    extensions.put("doc", "Document");
    extensions.put("docx", "Document");
    extensions.put("odt", "Document");
    extensions.put("pdf", "Document");
    extensions.put("rtf", "Document");
    extensions.put("tex", "Document");
    extensions.put("txt", "Document");
    extensions.put("xps", "Document");

    extensions.put("bmp", "Image");
    extensions.put("gif", "Image");
    extensions.put("ico", "Image");
    extensions.put("jpeg", "Image");
    extensions.put("jpg", "Image");
    extensions.put("png", "Image");
    extensions.put("tga", "Image");
    extensions.put("tiff", "Image");
    extensions.put("webp", "Image");

    extensions.put("3gp", "Video");
    extensions.put("avi", "Video");
    extensions.put("flv", "Video");
    extensions.put("mkv", "Video");
    extensions.put("mov", "Video");
    extensions.put("mp4", "Video");
    extensions.put("mpeg", "Video");
    extensions.put("mpg", "Video");
    extensions.put("webm", "Video");
    extensions.put("wmv", "Video");
  }

  public static String getFileType(String extension) {
    return extensions.getOrDefault(extension, "Other");
  }
}
