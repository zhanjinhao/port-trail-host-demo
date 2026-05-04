package cn.addenda.porttrail.hostdemo.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;

class DurationTest {

  @Test
  void test() {
    LocalTime depTime = LocalTime.of(23, 30);
    LocalTime arrTime = LocalTime.of(1, 0);

    Duration between = Duration.between(depTime, arrTime);
    Assertions.assertEquals(90, between.toMinutes() + 24 * 60);
  }

}
