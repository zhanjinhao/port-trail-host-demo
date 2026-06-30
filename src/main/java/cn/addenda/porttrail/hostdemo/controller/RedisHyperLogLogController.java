package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisHyperLogLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("redisHyperLogLog")
public class RedisHyperLogLogController {

  @Autowired
  private RedisHyperLogLogService redisHyperLogLogService;

  private Map<String, Object> ok(Object... kvs) {
    Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < kvs.length; i += 2) map.put(String.valueOf(kvs[i]), kvs[i + 1]);
    return map;
  }

  // ==================== 添加操作 ====================

  /**
   * 向 HyperLogLog 中添加元素（自动去重，1 表示基数发生变化，0 表示未变）
   * POST /redisHyperLogLog/pfadd?key=hll:uv:2026&elements=user:1&elements=user:2
   */
  @PostMapping("pfadd")
  public Map<String, Object> pfadd(@RequestParam("key") String key,
                                   @RequestParam("elements") List<String> elements) {
    Long result = redisHyperLogLogService.pfadd(key, elements.toArray(new String[0]));
    return ok("key", key, "elements", elements, "changed", result);
  }

  // ==================== 基数估算 ====================

  /**
   * 估算 HyperLogLog 的基数（近似去重计数，标准误差 0.81%）
   * POST /redisHyperLogLog/pfcount?key=hll:uv:2026
   */
  @PostMapping("pfcount")
  public Map<String, Object> pfcountSingle(@RequestParam("key") String key) {
    Long count = redisHyperLogLogService.pfcount(key);
    return ok("key", key, "count", count);
  }

  /**
   * 估算多个 HyperLogLog 合并后的近似基数
   * POST /redisHyperLogLog/pfcountMulti?keys=hll:uv:d1&keys=hll:uv:d2
   */
  @PostMapping("pfcountMulti")
  public Map<String, Object> pfcountMulti(@RequestParam("keys") List<String> keys) {
    Long count = redisHyperLogLogService.pfcountMulti(keys.toArray(new String[0]));
    return ok("keys", keys, "count", count);
  }

  // ==================== 合并操作 ====================

  /**
   * 将多个 HyperLogLog 合并为一个，结果存入 destination（用于跨天 UV 聚合）
   * POST /redisHyperLogLog/pfmerge?destination=hll:uv:total&sourceKeys=hll:uv:d1&sourceKeys=hll:uv:d2
   */
  @PostMapping("pfmerge")
  public Map<String, Object> pfmerge(@RequestParam("destination") String destination,
                                     @RequestParam("sourceKeys") List<String> sourceKeys) {
    String result = redisHyperLogLogService.pfmerge(destination, sourceKeys.toArray(new String[0]));
    return ok("destination", destination, "sourceKeys", sourceKeys, "result", result);
  }

}
