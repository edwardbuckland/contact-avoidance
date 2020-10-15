package gui.user.tab.map;

public enum Building {
  BEAUREPAIRE_CENTRE                    (0.546142578,   0.131180337,    false,  false,  true),
  NONA_LEE_SPORTS_CENTRE                (0.649658203,   0.124892827,    false,  false,  true),
  BUILDING_125                          (0.401855469,   0.190054301,    true,   false,  false),
  UNIVERSITY_HOUSE                      (0.499511719,   0.182909403,    true,   false,  false),
  UNION_HOUSE                           (0.555175781,   0.222635039,    true,   false,  false),
  BALDWIN_SPENCER                       (0.630859375,   0.174335524,    false,  false,  false),
  GYLN_DAVIS                            (0.685302734,   0.23663904,     true,   true,   false),
  DAVID_CARO                            (0.760498047,   0.200628751,    true,   false,  false),
  PETER_HALL                            (0.759033203,   0.31266076,     true,   false,  false),
  MCCOY                                 (0.876220703,   0.205201486,    true,   false,  false),
  CHEMISTRY                             (0.649658203,   0.315804516,    false,  false,  false),
  OLD_ARTS                              (0.51953125,    0.314947128,    false,  false,  false),
  OLD_QUADRANGLE                        (0.570800781,   0.313803944,    false,  false,  false),
  ARTS_WEST                             (0.472167969,   0.310945985,    false,  false,  false),
  BAILLIEU_LIBRARY                      (0.474365239,   0.375250071,    true,   true,   false),
  MELBOURNE_CONSERVATORIUM_OF_MUSIC     (0.408691406,   0.316661903,    false,  false,  false),
  KENNETH_MYER                          (0.421875,      0.386110317,    true,   false,  false),
  MEDICAL                               (0.456054688,   0.475850243,    false,  false,  false),
  OLD_ENGINEERING                       (0.645019531,   0.463846813,    false,  false,  false),
  ENGINEERING_WORKSHOPS                 (0.438964844,   0.453558159,    false,  false,  false),
  BIOSCIENCES_1                         (0.447753906,   0.175478708,    false,  false,  false),
  BIOSCIENCES_2                         (0.467773438,   0.220920263,    false,  false,  false),
  BIOSCIENCES_4                         (0.433837891,   0.27607888,     false,  false,  false),
  BIOSCIENCES_3                         (0.514160156,   0.260645899,    false,  false,  false),
  OLD_PHYSICS                           (0.543701172,   0.265790226,    false,  false,  false),
  MELBOURNE_BUSINESS_SCHOOL             (0.642822266,   0.67819377,     true,   false,  false),
  GRADUATE_HOUSE                        (0.639404297,   0.614175479,    true,   false,  false),
  MELBOURNE_DENTAL_CLINIC               (0.781738281,   0.602172049,    true,   false,  false),
  LAW                                   (0.575195313,   0.798513861,    true,   true,   false),
  STUDENT_VILLAGE_MELBOURNE             (0.639404297,   0.887967991,    true,   false,  false),
  KWONG_LEE_DOW                         (0.646972656,   0.945412975,    true,   false,  false),
  DOHERTY_INSTITUTE                     (0.418701172,   0.584738497,    false,  false,  false),
  THE_SPOT                              (0.478271484,   0.733923978,    true,   false,  false),
  GIBLIN_EUNSON_LIBRARY                 (0.507324219,   0.687625036,    false,  true,   false),
  ALAN_GILBERT                          (0.490234375,   0.591026008,    true,   false,  false),
  OLD_METALLURGY                        (0.644042969,   0.408973993,    false,  false,  false),
  INFRASTRUCTURE_ENGINEERING            (0.632568359,   0.505858817,    false,  false,  false),
  DAVID_PENINGTON                       (0.096191406,   0.410688768,    true,   false,  false),
  WALTER_AND_ELIZA_HALL_INSTITUTE       (0.214111328,   0.393255216,    true,   false,  false),
  JOHN_CADE                             (0.198974609,   0.431266076,    false,  false,  false),
  CENTRE_FOR_MEDICAL_RESEARCH           (0.309814453,   0.462417834,    false,  false,  false);

  public double                         longtitude;
  public double                         latitude;

  public boolean                        library;
  public boolean                        food;
  public boolean                        sports;

  private Building(double longtitude, double latitude, boolean library, boolean food, boolean sports) {
    this.longtitude = longtitude;
    this.latitude = latitude;

    this.library = library;
    this.food = food;
    this.sports = sports;
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
