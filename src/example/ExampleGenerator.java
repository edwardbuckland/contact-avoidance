package example;

import static example.ActivityGenerator.*;

import graph.bipartite.*;

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
    
    Activity benchmark_500  = new Activity("Benchmark 500",  0, 0);
    Activity benchmark_1000 = new Activity("Benchmark 1000", 0, 0);
    Activity benchmark_1500 = new Activity("Benchmark 1500", 0, 0);
    
    for (int i = 0; i < Person.people.size(); i++) {
    	Person person = Person.people.get(i);
    	
    	if (i < 500) {
    		person.addActivities(benchmark_500);
    	}
    	if (i < 1000) {
    		person.addActivities(benchmark_1000);
    	}
    	if (i < 1500) {
    		person.addActivities(benchmark_1500);
    	}
    }
  }
}
