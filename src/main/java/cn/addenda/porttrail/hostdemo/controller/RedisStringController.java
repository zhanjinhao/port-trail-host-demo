package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisStringService;
import io.lettuce.core.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("redisString")
public class RedisStringController {

  @Autowired
  private RedisStringService redisStringService;

  // ==================== 基础 K-V 操作 ====================

  @PostMapping("set")
  public Map<String, String> set(@RequestParam("key") String key,
                                 @RequestParam("value") String value) {
    String result = redisStringService.set(key, value);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    map.put("result", result);
    return map;
  }

  @PostMapping("get")
  public Map<String, String> get(@RequestParam("key") String key) {
    String value = redisStringService.get(key);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  @PostMapping("del")
  public Map<String, Object> del(@RequestParam("keys") List<String> keys) {
    Long count = redisStringService.del(keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("keys", keys);
    map.put("deletedCount", count);
    return map;
  }

  // ==================== 带过期时间的 SET ====================

  @PostMapping("setex")
  public Map<String, Object> setex(@RequestParam("key") String key,
                                   @RequestParam("seconds") long seconds,
                                   @RequestParam("value") String value) {
    String result = redisStringService.setex(key, seconds, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("seconds", seconds);
    map.put("value", value);
    map.put("result", result);
    return map;
  }

  @PostMapping("psetex")
  public Map<String, Object> psetex(@RequestParam("key") String key,
                                    @RequestParam("milliseconds") long milliseconds,
                                    @RequestParam("value") String value) {
    String result = redisStringService.psetex(key, milliseconds, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("milliseconds", milliseconds);
    map.put("value", value);
    map.put("result", result);
    return map;
  }

  // ==================== 条件 SET ====================

  @PostMapping("setnx")
  public Map<String, Object> setnx(@RequestParam("key") String key,
                                   @RequestParam("value") String value) {
    Boolean result = redisStringService.setnx(key, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    map.put("result", result);
    return map;
  }

  // ==================== Key 生命周期 ====================

  @PostMapping("exists")
  public Map<String, Object> exists(@RequestParam("keys") List<String> keys) {
    Long count = redisStringService.exists(keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("keys", keys);
    map.put("count", count);
    return map;
  }

  @PostMapping("expire")
  public Map<String, Object> expire(@RequestParam("key") String key,
                                    @RequestParam("seconds") long seconds) {
    Boolean result = redisStringService.expire(key, seconds);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("seconds", seconds);
    map.put("result", result);
    return map;
  }

  @PostMapping("ttl")
  public Map<String, Object> ttl(@RequestParam("key") String key) {
    Long ttl = redisStringService.ttl(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("ttl", ttl);
    return map;
  }

  // ==================== 计数器 ====================

  @PostMapping("incr")
  public Map<String, Object> incr(@RequestParam("key") String key) {
    Long value = redisStringService.incr(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  @PostMapping("incrby")
  public Map<String, Object> incrby(@RequestParam("key") String key,
                                    @RequestParam("amount") long amount) {
    Long value = redisStringService.incrby(key, amount);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("amount", amount);
    map.put("value", value);
    return map;
  }

  @PostMapping("incrbyfloat")
  public Map<String, Object> incrbyfloat(@RequestParam("key") String key,
                                         @RequestParam("amount") double amount) {
    Double value = redisStringService.incrbyfloat(key, amount);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("amount", amount);
    map.put("value", value);
    return map;
  }

  @PostMapping("decr")
  public Map<String, Object> decr(@RequestParam("key") String key) {
    Long value = redisStringService.decr(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  @PostMapping("decrby")
  public Map<String, Object> decrby(@RequestParam("key") String key,
                                    @RequestParam("amount") long amount) {
    Long value = redisStringService.decrby(key, amount);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("amount", amount);
    map.put("value", value);
    return map;
  }

  // ==================== 批量操作 ====================

  @PostMapping("mget")
  public Map<String, Object> mget(@RequestParam("keys") List<String> keys) {
    List<KeyValue<String, String>> kvList = redisStringService.mget(keys.toArray(new String[0]));
    Map<String, String> result = new HashMap<>();
    if (kvList != null) {
      for (KeyValue<String, String> kv : kvList) {
        result.put(kv.getKey(), kv.hasValue() ? kv.getValue() : null);
      }
    }
    Map<String, Object> map = new HashMap<>();
    map.put("keys", keys);
    map.put("result", result);
    return map;
  }

  @PostMapping("mset")
  public Map<String, Object> mset(@RequestParam Map<String, String> params) {
    String result = redisStringService.mset(params);
    Map<String, Object> map = new HashMap<>();
    map.put("params", params);
    map.put("result", result);
    return map;
  }

  // ==================== GETSET ====================

  @PostMapping("getset")
  public Map<String, String> getset(@RequestParam("key") String key,
                                    @RequestParam("value") String value) {
    String oldValue = redisStringService.getset(key, value);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("newValue", value);
    map.put("oldValue", oldValue);
    return map;
  }

  // ==================== 字符串操作 ====================

  @PostMapping("append")
  public Map<String, Object> append(@RequestParam("key") String key,
                                    @RequestParam("value") String value) {
    Long newLength = redisStringService.append(key, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    map.put("newLength", newLength);
    return map;
  }

  @PostMapping("strlen")
  public Map<String, Object> strlen(@RequestParam("key") String key) {
    Long length = redisStringService.strlen(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("length", length);
    return map;
  }

  @PostMapping("getrange")
  public Map<String, Object> getrange(@RequestParam("key") String key,
                                      @RequestParam("start") long start,
                                      @RequestParam("end") long end) {
    String value = redisStringService.getrange(key, start, end);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("start", start);
    map.put("end", end);
    map.put("value", value);
    return map;
  }

  @PostMapping("setrange")
  public Map<String, Object> setrange(@RequestParam("key") String key,
                                      @RequestParam("offset") long offset,
                                      @RequestParam("value") String value) {
    Long newLength = redisStringService.setrange(key, offset, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("offset", offset);
    map.put("value", value);
    map.put("newLength", newLength);
    return map;
  }

}
