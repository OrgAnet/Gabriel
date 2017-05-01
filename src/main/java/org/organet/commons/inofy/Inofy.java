package org.organet.commons.inofy;

import java.io.File;
import java.io.IOException;

public class Inofy {
  public static final String FIELDS_SEPARATOR = ",";
  static final String VALUE_SEPARATOR = "=";

  public static Index localIndex = null; // NOTE This was named as 'storage' before inofy/Index.java

  //  public static void main(String[] args) {
//    Options options = new Options();
//
//    options.addOption("p", "shared-path", true, "Absolute directory path to shared files and folders.");
//    options.addOption("w", "no-init-walk", false, "Do not walk the shared directory while application initialization.");
//    // TODO Add CLI arguments here
//
//    CommandLineParser parser = new GnuParser();
//    CommandLine cmd;
//    try {
//      cmd = parser.parse(options, args);
//    } catch (ParseException e) {
//      System.out.println("[ERROR ] main | Could not parse the commandline arguments. Aborting.");
//
//      return;
//    }
//
//    // Set global variables by CLI arguments
//    if (cmd.hasOption('p')) {
//      sharedDirPath = cmd.getOptionValue('p');
//    }
//
//    // Update variables which depends CLI arguments
//    sharedDir = new File(sharedDirPath);
//
//    if (!sharedDir.isDirectory()) {
//      System.out.println("[ERROR ] main | Shared directory was not set properly. Aborting.");
//
//      return;
//    }
//
//    // Initialize in-memory DB connection
//    storage = new InMemoryStorage<>(new SharedFileSerializer(), new SharedFileDeserializer());
//
//    // Walk shared directory directory for indexing files
//    if (cmd.hasOption('w')) {
//      System.out.println("[ INFO ] main | No shared directory walk.");
//    } else {
//      File[] sharedFiles = sharedDir.listFiles();
//
//      if (sharedFiles != null) {
//        for (File child : sharedFiles) {
//          localIndex.add(SharedFile.fromFile(child));
//        }
//      }
//    }
//
//    // TEST - TEST - TEST - TEST - TEST - TEST
//    SharedFile foo = localIndex.get(1);
//    if (foo != null) {
//      System.out.println(foo.getPath());
//    }
//    // TEST - TEST - TEST - TEST - TEST - TEST
//
//    // Watch the shared directory directory recursively for changes
//    // (create, modify and delete) and update DB accordingly
//    try {
//      new Watcher(sharedDir.getPath(), true).processEvents();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  public static void start(String sharedDirPath) {
    File sharedDir = new File(sharedDirPath);
    localIndex = new Index();

    // TODO Walk shared directory for initial indexing

    // Watch the shared directory directory recursively for changes
    try {
      (new Watcher(sharedDir.getPath())).run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
