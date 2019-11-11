package space.harbour.hw2;

import org.junit.Assert;
import org.junit.Test;

public class AbbreviationTests {
  @Test
  public void convertTests() {
    String[] texts = {
      "FYI my dog is very :(. ¯\\_(ツ)_/¯",
      "Hey, Johnny, GTFO! I’m too tired :)",
    };
    String[] ansvers1 = {
      "for your information my dog is very :(. ¯\\_(ツ)_/¯",
      "Hey, Johnny, please, leave me alone! I’m too tired :)",
    };
    String[] ansvers2 = {
      "for your information my dog is very [sad]. [such is life]",
      "Hey, Johnny, please, leave me alone! I’m too tired [smiling]",
    };
    for (int i = 0; i < texts.length; i++) {
      String s = Abbreviation.fixAbbreviations(texts[i]);
      Assert.assertEquals("Abbreviation conversion", ansvers1[i], s);
      s = Abbreviation.fixSmiles(s);
      Assert.assertEquals("Smiles conversion", ansvers2[i], s);
    }
  }
}
