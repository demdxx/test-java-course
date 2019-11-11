package space.harbour.hw2;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Abbreviation {
  private static final Map<String, String> abbreviations = Map.of(
    "PLZ", "please",
    "FYI", "for your information",
    "GTFO", "please, leave me alone",
    "ASAP", "as soon as possible"
  );

  private static final Map<String, String> smiles = Map.of(
    ":)", "[smiling]",
    ":(", "[sad]",
    "¯\\_(ツ)_/¯", "[such is life]"
  );

  public static String fixAbbreviations(String s) {
    return StringUtils.replaceEach(s,
      abbreviations.keySet().toArray(new String[abbreviations.size()]),
      abbreviations.values().toArray(new String[abbreviations.size()]));
  }

  public static String fixSmiles(String s) {
    return StringUtils.replaceEach(s,
      smiles.keySet().toArray(new String[smiles.size()]),
      smiles.values().toArray(new String[smiles.size()]));
  }
}