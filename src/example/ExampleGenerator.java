package example;

import static example.ActivityGenerator.*;

import java.util.*;

import graph.bipartite.*;
import gui.user.tab.map.*;

public class ExampleGenerator {
  public static void small() {
    generateActivities();

    Activity coffee    = new Activity("Coffee", 7, 1);
    Activity maths     = new Activity("Maths", 11, 2);
    Activity computing = new Activity("Computing", 16.25, 3);
    Activity library   = new Activity("Library", 14.25, 1);
    Activity sushi     = new Activity("Sushi", 13, 1);

    new Person("Kevin Nguyen").addActivities(coffee, maths, sushi, computing);
    new Person("John Citizen").addActivities(coffee, maths, sushi);
    new Person("Jessica Kym").addActivities(maths, sushi, library);
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

  public static void medium() {
    generateActivities();
    PersonGenerator.generatePeople(250, 8);
  }

  public static void large() {
    generateActivities();
    PersonGenerator.generatePeople(5000, 8);
  }
  
  public static void benchmarking() {
    large();
    
    for (int clusters: new int[]{ 2, 5, 10 }) {
      Map<Integer, Activity> activities = new TreeMap<>(Map.of(500,  new Activity("Benchmark 500",  0, 0),
                                                               1000, new Activity("Benchmark 1000", 0, 0),
                                                               1500, new Activity("Benchmark 1500", 0, 0)));
      
      for (int i = 0; i < Person.people.size(); i++) {
        Person person = Person.people.get(i);
        
        for (Map.Entry<Integer, Activity> entry: activities.entrySet()) {
          if (i < entry.getKey()) {
            person.addActivities(entry.getValue());
          }
        }
      }
      
      for (Activity activity: activities.values()) {
        for (int i = 0; i < clusters; i++) {
          activity.locations.add(Building.UNION_HOUSE);
        }
        activity.clusteredSplit();
      }
    }
  }
}
