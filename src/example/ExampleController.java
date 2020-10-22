package example;

import java.util.*;

import graph.*;
import graph.bipartite.*;
import gui.admin.tab.*;

public class ExampleController {
  public static Map<String, Class<? extends Example>>   examples    = new HashMap<>();

  static {
    examples.put("blah", null);
  }

  public static void loadExample(Example example) {
    Node.nodes.clear();
    Activity.activities.clear();

    example.load();

    ActivitiesTable.update.run();
  }

  public interface Example {
    void load();
  }
}
