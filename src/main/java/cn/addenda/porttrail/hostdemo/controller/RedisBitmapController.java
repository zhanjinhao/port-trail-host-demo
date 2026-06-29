package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisBitmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("redisBitmap")
public class RedisBitmapController {

  @Autowired
  private RedisBitmapService redisBitmapService;

  private Map<String, Object> ok(Object... kvs) {
    Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < kvs.length; i += 2) map.put(String.valueOf(kvs[i]), kvs[i + 1]);
    return map;
  }

  // ==================== 位操作 ====================

  /**
   * 设置指定位偏移的值（0 或 1），返回旧值
   * POST /redisBitmap/setbit?key=checkin:2026&offset=14&value=1
   */
  @PostMapping("setbit")
  public Map<String, Object> setbit(@RequestParam("key") String key,
                                    @RequestParam("offset") long offset,
                                    @RequestParam("value") int value) {
    Long oldValue = redisBitmapService.setbit(key, offset, value);
    return ok("key", key, "offset", offset, "value", value, "oldValue", oldValue);
  }

  /**
   * 获取指定位偏移的值（0 或 1）
   * POST /redisBitmap/getbit?key=checkin:2026&offset=14
   */
  @PostMapping("getbit")
  public Map<String, Object> getbit(@RequestParam("key") String key,
                                    @RequestParam("offset") long offset) {
    Long value = redisBitmapService.getbit(key, offset);
    return ok("key", key, "offset", offset, "value", value);
  }

  // ==================== 统计 ====================

  /**
   * 统计位图中值为 1 的 bit 数量（全部）
   * POST /redisBitmap/bitcount?key=checkin:2026
   */
  @PostMapping("bitcount")
  public Map<String, Object> bitcount(@RequestParam("key") String key) {
    Long count = redisBitmapService.bitcount(key);
    return ok("key", key, "count", count);
  }

  /**
   * 统计位图指定字节区间内值为 1 的 bit 数量（0/-1 表示全部）
   * POST /redisBitmap/bitcountRange?key=checkin:2026&start=0&end=-1
   */
  @PostMapping("bitcountRange")
  public Map<String, Object> bitcountRange(@RequestParam("key") String key,
                                           @RequestParam("start") long start,
                                           @RequestParam("end") long end) {
    Long count = redisBitmapService.bitcount(key, start, end);
    return ok("key", key, "start", start, "end", end, "count", count);
  }

  // ==================== 查找 ====================

  /**
   * 查找第一个值为 bit（0 或 1）的位置
   * POST /redisBitmap/bitpos?key=checkin:2026&bit=1
   */
  @PostMapping("bitpos")
  public Map<String, Object> bitpos(@RequestParam("key") String key,
                                    @RequestParam("bit") boolean bit) {
    Long pos = redisBitmapService.bitpos(key, bit);
    return ok("key", key, "bit", bit, "pos", pos);
  }

  /**
   * 从指定字节(是BYTE，不是bit)偏移开始查找第一个值为 bit 的位置
   * POST /redisBitmap/bitposStart?key=checkin:2026&bit=1&start=0
   */
  @PostMapping("bitposStart")
  public Map<String, Object> bitposStart(@RequestParam("key") String key,
                                         @RequestParam("bit") boolean bit,
                                         @RequestParam("start") long start) {
    Long pos = redisBitmapService.bitpos(key, bit, start);
    return ok("key", key, "bit", bit, "start", start, "pos", pos);
  }

  /**
   * 在指定字节(是BYTE，不是bit)区间内查找第一个值为 bit 的位置
   * POST /redisBitmap/bitposRange?key=checkin:2026&bit=1&start=0&end=3
   */
  @PostMapping("bitposRange")
  public Map<String, Object> bitposRange(@RequestParam("key") String key,
                                         @RequestParam("bit") boolean bit,
                                         @RequestParam("start") long start,
                                         @RequestParam("end") long end) {
    Long pos = redisBitmapService.bitpos(key, bit, start, end);
    return ok("key", key, "bit", bit, "start", start, "end", end, "pos", pos);
  }

  // ==================== 位运算 ====================

  /**
   * 对多个位图执行 AND（与）运算（统计连续签到）
   * POST /redisBitmap/bitopAnd?destination=checkin:both&keys=chk:u1&keys=chk:u2
   */
  @PostMapping("bitopAnd")
  public Map<String, Object> bitopAnd(@RequestParam("destination") String destination,
                                      @RequestParam("keys") List<String> keys) {
    Long count = redisBitmapService.bitopAnd(destination, keys.toArray(new String[0]));
    return ok("destination", destination, "keys", keys, "count", count);
  }

  /**
   * 对多个位图执行 OR（或）运算（合并多天签到）
   * POST /redisBitmap/bitopOr?destination=chk:month&keys=chk:d1&keys=chk:d2
   */
  @PostMapping("bitopOr")
  public Map<String, Object> bitopOr(@RequestParam("destination") String destination,
                                     @RequestParam("keys") List<String> keys) {
    Long count = redisBitmapService.bitopOr(destination, keys.toArray(new String[0]));
    return ok("destination", destination, "keys", keys, "count", count);
  }

  /**
   * 对多个位图执行 XOR（异或）运算（对比差异）
   * POST /redisBitmap/bitopXor?destination=chk:diff&keys=chk:d1&keys=chk:d2
   */
  @PostMapping("bitopXor")
  public Map<String, Object> bitopXor(@RequestParam("destination") String destination,
                                      @RequestParam("keys") List<String> keys) {
    Long count = redisBitmapService.bitopXor(destination, keys.toArray(new String[0]));
    return ok("destination", destination, "keys", keys, "count", count);
  }

  /**
   * 对单个位图执行 NOT（取反）运算
   * POST /redisBitmap/bitopNot?destination=chk:invert&source=chk:d1
   */
  @PostMapping("bitopNot")
  public Map<String, Object> bitopNot(@RequestParam("destination") String destination,
                                      @RequestParam("source") String source) {
    Long count = redisBitmapService.bitopNot(destination, source);
    return ok("destination", destination, "source", source, "count", count);
  }

  // ==================== 位域操作 ====================

  /**
   * BITFIELD GET：读取指定位宽和偏移的值（type 如 u8/i16，offset 如 0 或 #2）
   * POST /redisBitmap/bitfieldGet?key=player:1&type=u8&offset=0
   */
  @PostMapping("bitfieldGet")
  public Map<String, Object> bitfieldGet(@RequestParam("key") String key,
                                         @RequestParam("type") String type,
                                         @RequestParam("offset") String offset) {
    List<Long> result = redisBitmapService.bitfieldGet(key, type, offset);
    return ok("key", key, "type", type, "offset", offset, "result", result);
  }

  /**
   * BITFIELD SET：设置指定位宽和偏移的值
   * POST /redisBitmap/bitfieldSet?key=player:1&type=u8&offset=0&value=100
   *
   * @return 返回的是此域之前的值
   */
  @PostMapping("bitfieldSet")
  public Map<String, Object> bitfieldSet(@RequestParam("key") String key,
                                         @RequestParam("type") String type,
                                         @RequestParam("offset") String offset,
                                         @RequestParam("value") long value) {
    List<Long> result = redisBitmapService.bitfieldSet(key, type, offset, value);
    return ok("key", key, "type", type, "offset", offset, "value", value, "result", result);
  }

  /**
   * BITFIELD INCRBY：对指定位宽和偏移的值进行自增
   * POST /redisBitmap/bitfieldIncrBy?key=player:1&type=i32&offset=0&increment=1
   *
   * @return 返回此位域自增之后的值
   */
  @PostMapping("bitfieldIncrBy")
  public Map<String, Object> bitfieldIncrBy(@RequestParam("key") String key,
                                            @RequestParam("type") String type,
                                            @RequestParam("offset") String offset,
                                            @RequestParam("increment") long increment) {
    List<Long> result = redisBitmapService.bitfieldIncrBy(key, type, offset, increment);
    return ok("key", key, "type", type, "offset", offset, "increment", increment, "result", result);
  }

}
