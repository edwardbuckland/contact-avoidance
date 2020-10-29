package example;

import static example.ActivityGenerator.*;

import graph.bipartite.*;

public class ExampleGenerator {
  public static void small() {
    generateActivities();
  }

  public static void twoWaySplit() {
    Activity big_popular = new Activity("Big Popular Activity", 10, 2);
    Activity small_a     = new Activity("Small Activity A",     15, 1);
    Activity small_b     = new Activity("Small Activity B",     15, 1);

    for (int i = 0; i < 16; i++) {
      new Person("Person " + i).addActivities(big_popular, i%2 == 0? small_a: small_b);
    }
  }

  public static void threeWaySplit() {
    Activity big_popular = new Activity("Big Popular Activity", 10, 2);
    Activity medium      = new Activity("Medium Activity",      15, 2);
    Activity small_a     = new Activity("Small Activity A",     15, 1);
    Activity small_b     = new Activity("Small Activity B",     15, 1);

    for (int i = 0; i < 8; i++) {
      new Person("Person " + i).addActivities(big_popular, medium);
    }

    for (int i = 8; i < 16; i++) {
      new Person("Person " + i).addActivities(big_popular, i%2 == 0? small_a: small_b);
    }
  }

  public static void large() {
    generateActivities();
    PersonGenerator.generatePeople(1000, 5);
  }
}
