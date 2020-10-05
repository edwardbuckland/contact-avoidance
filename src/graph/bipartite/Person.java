package graph.bipartite;

import java.util.*;

public class Person {
  public static List<Person> people = new ArrayList<>();
  private static int uniqueIndex;

  public int index = uniqueIndex++;
  public String name;

  public Person(String name) {
    this.name = name;

    people.add(this);
  }
}
