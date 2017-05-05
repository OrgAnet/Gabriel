package org.organet.commons.inofy;

import java.util.ArrayList;

public class KeywordList<T extends String> extends ArrayList<String> {
  @Override
  public boolean contains(Object o) {
    String searchKeyword = (String) o;

    for (String keyword : this) {
      if (keyword.matches(searchKeyword)) {
        return true;
      }
    }

    return false;
  }
}
