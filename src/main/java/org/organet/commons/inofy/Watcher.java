package org.organet.commons.inofy;

import org.organet.commons.gabriel.App;
import org.organet.commons.inofy.Model.SharedFileHeader;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class Watcher implements Runnable {
  private final WatchService watcher;
  private final Map<WatchKey, Path> keys;

  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>)event;
  }

  /**
   * Creates a WatchService and registers the given directory
   */
  Watcher(Path dir) throws IOException {
    watcher = FileSystems.getDefault().newWatchService();
    keys = new HashMap<>();

    registerAll(dir);
  }

  public Watcher(String path) throws IOException {
    this(Paths.get(path));
  }

  /**
   * Register the given directory with the WatchService
   */
  private void register(Path dir) throws IOException {
    WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

    Path prev = keys.get(key);
    if (prev == null) {
      // TODO Continue formatting printed text
      System.out.format("register: %s\n", dir);
    } else {
      if (!dir.equals(prev)) {
        System.out.format("update: %s -> %s\n", prev, dir);
      }
    }

    keys.put(key, dir);
  }

  /**
   * Register the given directory, and all its sub-directories, with the
   * WatchService.
   */
  private void registerAll(final Path start) throws IOException {
    // register directory and sub-directories
    Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        register(dir);

        return FileVisitResult.CONTINUE;
      }
    });
  }

  /**
   * Process all events for keys queued to the watcher
   */
  @Override
  public void run() {
    for (;;) {
      // wait for key to be signalled
      WatchKey key;
      try {
        // Returns a queued key. If no queued key is available, this method waits.
        key = watcher.take();
      } catch (InterruptedException x) {
        return;
      }

      Path dir = keys.get(key);
      if (dir == null) {
        System.err.println("WatchKey not recognized!!");
        continue;
      }

      // Process the pending events for the key.
      for (WatchEvent<?> event: key.pollEvents()) {
        WatchEvent.Kind kind = event.kind();

        // TBD - provide example of how OVERFLOW event is handled
        if (kind == OVERFLOW) {
          continue;
        }

        // Context for directory entry event is the file name of entry
        WatchEvent<Path> ev = cast(event);
        Path filename = ev.context();
        Path child = dir.resolve(filename);

        boolean isDirectory = Files.isDirectory(child, NOFOLLOW_LINKS);
        if (isDirectory) {
          // if directory is created, and watching recursively, then
          // register it and its sub-directories
          if (kind == ENTRY_CREATE) {
            try {
              if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                registerAll(child);
              }
            } catch (IOException x) {
              // ignore to keep sample readable
            }
          }
        } else {
          // Check if file is hidden - if so omit the file
          if (filename.toFile().isHidden()) {
            continue;
          }

          // TODO Decide file type (DOCUMENT, IMAGE, VIDEO, AUDIO etc.) \
          // File types are implementation specific

          if (kind == ENTRY_CREATE) {
            // Create a shared file and add to the local index
            SharedFileHeader sh = new SharedFileHeader(child.toString());
            App.localIndex.add(sh);
            System.out.println("new Shared File: " + sh.toString());
            // FIXME Implement this behaviour in another way (i.e. anywhere else) \
            // filename MUST stay as it is, it is not the problem here
            App.mainForm.LocalIndexListModel.addElement(filename.toString());
          } else if (kind == ENTRY_MODIFY) {
            // TODO Re-calculate the hash and update the local index
          } else if (kind == ENTRY_DELETE) {
            // Remove file from the local index
            App.localIndex.remove(child);

            // FIXME Implement this behaviour in another way (i.e. anywhere else) \
            // filename MUST stay as it is, it is not the problem here
            App.mainForm.LocalIndexListModel.removeElement(filename.toString());
          }
        }
      }

      // reset key and remove from set if directory no longer accessible
      boolean valid = key.reset();
      if (!valid) {
        keys.remove(key);

        // all directories are inaccessible
        if (keys.isEmpty()) {
          break;
        }
      }
    }
  }
}
