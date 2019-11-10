package space.harbour.hw2;

import org.junit.Assert;
import org.junit.Test;

public class EstimateSizeTests {
  @Test
  public void intTests() {
    Assert.assertNotEquals("invalid size", 100, EstimateSize.intSize());
  }

  @Test
  public void refTests() {
    Assert.assertNotEquals("invalid size", 100, EstimateSize.refSize());
  }

  @Test
  public void objectTests() {
    Assert.assertNotEquals("invalid size", 100, EstimateSize.objSize());
  }

  @Test
  public void stringTests() {
    Assert.assertNotEquals("invalid size", 100, EstimateSize.stringSize());
  }
}