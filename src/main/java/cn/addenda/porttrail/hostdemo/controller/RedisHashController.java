package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisHashService;
import io.lettuce.core.KeyValue;
import io.lettuce.core.MapScanCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("redisHash")
public class RedisHashController {

  @Autowired
  private RedisHashService redisHashService;

  /**
   * 设置 Hash 中 field 的值（field 存在则覆盖）
   * POST /redisHash/hset?key=&field=&value=
   */
  @PostMapping("hset")
  public Map<String, Object> hset(@RequestParam("key") String key,
                                  @RequestParam("field") String field,
                                  @RequestParam("value") String value) {
    Boolean result = redisHashService.hset(key, field, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("value", value);
    map.put("result", result);
    return map;
  }

  /**
   * 获取 Hash 中 field 的值
   * POST /redisHash/hget?key=&field=
   */
  @PostMapping("hget")
  public Map<String, String> hget(@RequestParam("key") String key,
                                  @RequestParam("field") String field) {
    String value = redisHashService.hget(key, field);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("value", value);
    return map;
  }

  /**
   * 获取 Hash 中全部 field-value 对
   * POST /redisHash/hgetall?key=
   */
  @PostMapping("hgetall")
  public Map<String, Object> hgetall(@RequestParam("key") String key) {
    Map<String, String> value = redisHashService.hgetall(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  /**
   * 删除 Hash 中一个或多个 field
   * POST /redisHash/hdel?key=&fields=field1&fields=field2
   */
  @PostMapping("hdel")
  public Map<String, Object> hdel(@RequestParam("key") String key,
                                  @RequestParam("fields") List<String> fields) {
    Long count = redisHashService.hdel(key, fields.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("fields", fields);
    map.put("deletedCount", count);
    return map;
  }

  /**
   * 判断 Hash 中 field 是否存在
   * POST /redisHash/hexists?key=&field=
   */
  @PostMapping("hexists")
  public Map<String, Object> hexists(@RequestParam("key") String key,
                                     @RequestParam("field") String field) {
    Boolean value = redisHashService.hexists(key, field);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("value", value);
    return map;
  }

  /**
   * 获取 Hash 中 field 的数量
   * POST /redisHash/hlen?key=
   */
  @PostMapping("hlen")
  public Map<String, Object> hlen(@RequestParam("key") String key) {
    Long value = redisHashService.hlen(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  /**
   * 仅当 field 不存在时才设置值（分布式锁场景）
   * POST /redisHash/hsetnx?key=&field=&value=
   */
  @PostMapping("hsetnx")
  public Map<String, Object> hsetnx(@RequestParam("key") String key,
                                    @RequestParam("field") String field,
                                    @RequestParam("value") String value) {
    Boolean result = redisHashService.hsetnx(key, field, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("value", value);
    map.put("result", result);
    return map;
  }

  /**
   * 获取 Hash 中所有 field 名称
   * POST /redisHash/hkeys?key=
   */
  @PostMapping("hkeys")
  public Map<String, Object> hkeys(@RequestParam("key") String key) {
    List<String> keys = redisHashService.hkeys(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("keys", keys);
    return map;
  }

  /**
   * 获取 Hash 中所有 value 值
   * POST /redisHash/hvals?key=
   */
  @PostMapping("hvals")
  public Map<String, Object> hvals(@RequestParam("key") String key) {
    List<String> vals = redisHashService.hvals(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("vals", vals);
    return map;
  }

  /**
   * 对 Hash 中 field 的值进行整数自增（amount 可为负数即自减）
   * POST /redisHash/hincrby?key=&field=&amount=
   */
  @PostMapping("hincrby")
  public Map<String, Object> hincrby(@RequestParam("key") String key,
                                     @RequestParam("field") String field,
                                     @RequestParam("amount") long amount) {
    Long value = redisHashService.hincrby(key, field, amount);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("amount", amount);
    map.put("value", value);
    return map;
  }

  /**
   * 对 Hash 中 field 的值进行浮点数自增（适用于金额、分数等）
   * POST /redisHash/hincrbyfloat?key=&field=&amount=
   */
  @PostMapping("hincrbyfloat")
  public Map<String, Object> hincrbyfloat(@RequestParam("key") String key,
                                          @RequestParam("field") String field,
                                          @RequestParam("amount") double amount) {
    Double value = redisHashService.hincrbyfloat(key, field, amount);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("amount", amount);
    map.put("value", value);
    return map;
  }

  /**
   * 获取 Hash 中 field 值的字符串长度
   * POST /redisHash/hstrlen?key=&field=
   */
  @PostMapping("hstrlen")
  public Map<String, Object> hstrlen(@RequestParam("key") String key,
                                     @RequestParam("field") String field) {
    Long length = redisHashService.hstrlen(key, field);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    map.put("length", length);
    return map;
  }

  /**
   * 批量设置 Hash 中多个 field-value 对
   * POST /redisHash/hmset?key=&k1=v1&k2=v2
   */
  @PostMapping("hmset")
  public Map<String, Object> hmset(@RequestParam("key") String key,
                                   @RequestParam Map<String, String> params) {
    String result = redisHashService.hmset(key, params);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("params", params);
    map.put("result", result);
    return map;
  }

  /**
   * 批量获取 Hash 中多个 field 的值
   * POST /redisHash/hmget?key=&fields=f1&fields=f2
   */
  @PostMapping("hmget")
  public Map<String, Object> hmget(@RequestParam("key") String key,
                                   @RequestParam("fields") List<String> fields) {
    List<KeyValue<String, String>> kvList = redisHashService.hmget(key, fields.toArray(new String[0]));
    Map<String, String> result = new HashMap<>();
    if (kvList != null) {
      for (KeyValue<String, String> kv : kvList) {
        result.put(kv.getKey(), kv.hasValue() ? kv.getValue() : null);
      }
    }
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("fields", fields);
    map.put("result", result);
    return map;
  }

  /**
   * 从 Hash 中随机获取一个 field 名称
   * POST /redisHash/hrandfield?key=
   */
  @PostMapping("hrandfield")
  public Map<String, Object> hrandfield(@RequestParam("key") String key) {
    String field = redisHashService.hrandfield(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("field", field);
    return map;
  }

  /**
   * 从 Hash 中随机获取 count 个 field 名称（正数不重复，负数允许重复）
   * POST /redisHash/hrandfieldCount?key=&count=
   */
  @PostMapping("hrandfieldCount")
  public Map<String, Object> hrandfieldCount(@RequestParam("key") String key,
                                             @RequestParam("count") long count) {
    List<String> fields = redisHashService.hrandfield(key, count);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("count", count);
    map.put("fields", fields);
    return map;
  }

  /**
   * 从 Hash 中随机获取 count 个 field-value 对（正数不重复，负数允许重复）
   * POST /redisHash/hrandfieldWithValues?key=&count=
   */
  @PostMapping("hrandfieldWithValues")
  public Map<String, Object> hrandfieldWithValues(@RequestParam("key") String key,
                                                  @RequestParam("count") long count) {
    List<KeyValue<String, String>> kvList = redisHashService.hrandfieldWithValues(key, count);
    Map<String, String> result = new HashMap<>();
    if (kvList != null) {
      for (KeyValue<String, String> kv : kvList) {
        result.put(kv.getKey(), kv.hasValue() ? kv.getValue() : null);
      }
    }
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("count", count);
    map.put("result", result);
    return map;
  }

  /**
   * 增量迭代扫描 Hash（cursor 首次传 "0"，后续传上次返回值；count 默认 10；match 支持 * 通配）
   * POST /redisHash/hscan?key=&cursor=0&count=10&match=
   */
  @PostMapping("hscan")
  public Map<String, Object> hscan(@RequestParam("key") String key,
                                   @RequestParam(value = "cursor", defaultValue = "0") String cursor,
                                   @RequestParam(value = "count", defaultValue = "10") long count,
                                   @RequestParam(value = "match", defaultValue = "") String match) {
    MapScanCursor<String, String> scanResult = redisHashService.hscan(key, cursor, count, match);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("cursor", scanResult.getCursor());
    map.put("finished", scanResult.isFinished());
    map.put("entries", scanResult.getMap());
    return map;
  }

}
