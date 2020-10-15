package gui.user.tab.map;

public enum Building {
  EXAMPLE           (0.1234567, 0.1234567),
  EXAMPLE_2         (0.6543217, 0.6543217);

  double            longtitude;
  double            latitude;

  private Building(double longtitude, double latitude) {
    this.longtitude = longtitude;
    this.latitude = latitude;
  }

  @Override
  public String toString() {
    char[] chars = super.toString().toLowerCase().replace("_", " ").toCharArray();

    for (int i = 0; i < chars.length; i++) {
      if (i == 0 || chars[i - 1] == ' ')
        chars[i] = Character.toUpperCase(chars[i]);
    }

    return String.valueOf(chars);
  }

  public static Building getValue(String string) {
    return valueOf(string.replace(" ", "_").toUpperCase());
  }
}
