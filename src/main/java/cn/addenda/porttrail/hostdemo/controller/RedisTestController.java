package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("redisTest")
public class RedisTestController {

  @Autowired
  private RedisTestService redisTestService;

  @PostMapping("testSet")
  public Map<String, String> testSet(@RequestParam("key") String key,
                                     @RequestParam("value") String value) {
    redisTestService.set(key, value);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  @PostMapping("testGet")
  public Map<String, String> testSet(@RequestParam("key") String key) {
    String value = redisTestService.get(key);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  @PostMapping("testDel")
  public Map<String, String> testDel(@RequestParam("key") String key) {
    redisTestService.del(key);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    return map;
  }

  @PostMapping("testHset")
  public Map<String, String> testHset(@RequestParam("key") String key,
                                      @RequestParam("field") String field,
                                      @RequestParam("value") String value) {
    redisTestService.hset(key, field, value);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("value", value);
    return map;
  }

  @PostMapping("testHget")
  public Map<String, String> testHget(@RequestParam("key") String key,
                                      @RequestParam("field") String field) {
    String value = redisTestService.hget(key, field);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("value", value);
    return map;
  }

  @PostMapping("testHgetall")
  public Map<String, Object> hgetall(@RequestParam("key") String key) {
    Map<String, String> value = redisTestService.hgetall(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  @PostMapping("testHdel")
  public Map<String, Object> testHdel(@RequestParam("key") String key,
                                      @RequestParam("field") String field) {
    Long value = redisTestService.hdel(key, field);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("value", value);
    return map;
  }

  @PostMapping("testHexists")
  public Map<String, Object> testHexists(@RequestParam("key") String key,
                                         @RequestParam("field") String field) {
    Boolean value = redisTestService.hexists(key, field);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("value", value);
    return map;
  }

  @PostMapping("testHlen")
  public Map<String, Object> testHlen(@RequestParam("key") String key) {
    Long value = redisTestService.hlen(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

}
