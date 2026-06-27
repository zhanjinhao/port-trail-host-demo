package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisSetService;
import io.lettuce.core.ValueScanCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("redisSet")
public class RedisSetController {

  @Autowired
  private RedisSetService redisSetService;

  // ==================== 基础操作 ====================

  /**
   * 向 Set 中添加成员（自动去重）
   * POST /redisSet/sadd?key=&members=m1&members=m2
   */
  @PostMapping("sadd")
  public Map<String, Object> sadd(@RequestParam("key") String key,
                                  @RequestParam("members") List<String> members) {
    Long count = redisSetService.sadd(key, members.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("members", members);
    map.put("count", count);
    return map;
  }

  /**
   * 从 Set 中移除成员
   * POST /redisSet/srem?key=&members=m1&members=m2
   */
  @PostMapping("srem")
  public Map<String, Object> srem(@RequestParam("key") String key,
                                  @RequestParam("members") List<String> members) {
    Long count = redisSetService.srem(key, members.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("members", members);
    map.put("count", count);
    return map;
  }

  /**
   * 获取 Set 成员数量
   * POST /redisSet/scard?key=
   */
  @PostMapping("scard")
  public Map<String, Object> scard(@RequestParam("key") String key) {
    Long count = redisSetService.scard(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("count", count);
    return map;
  }

  /**
   * 判断 member 是否在 Set 中
   * POST /redisSet/sismember?key=&member=m1
   */
  @PostMapping("sismember")
  public Map<String, Object> sismember(@RequestParam("key") String key,
                                       @RequestParam("member") String member) {
    Boolean exists = redisSetService.sismember(key, member);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("member", member);
    map.put("exists", exists);
    return map;
  }

  /**
   * 批量判断多个 member 是否在 Set 中（Redis 6.2+）
   * POST /redisSet/smismember?key=&members=m1&members=m2
   */
  @PostMapping("smismember")
  public Map<String, Object> smismember(@RequestParam("key") String key,
                                        @RequestParam("members") List<String> members) {
    List<Boolean> result = redisSetService.smismember(key, members.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("members", members);
    map.put("result", result);
    return map;
  }

  // ==================== 查询全部 ====================

  /**
   * 获取 Set 中所有成员
   * POST /redisSet/smembers?key=
   */
  @PostMapping("smembers")
  public Map<String, Object> smembers(@RequestParam("key") String key) {
    Set<String> members = redisSetService.smembers(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("members", members);
    return map;
  }

  // ==================== 随机操作 ====================

  /**
   * 随机获取一个成员（不移除）
   * POST /redisSet/srandmember?key=
   */
  @PostMapping("srandmember")
  public Map<String, String> srandmember(@RequestParam("key") String key) {
    String member = redisSetService.srandmember(key);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("member", member);
    return map;
  }

  /**
   * 随机获取 count 个成员（不移除，count<0 允许重复）
   * POST /redisSet/srandmemberCount?key=&count=3
   */
  @PostMapping("srandmemberCount")
  public Map<String, Object> srandmemberCount(@RequestParam("key") String key,
                                              @RequestParam("count") long count) {
    List<String> members = redisSetService.srandmember(key, count);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("count", count);
    map.put("members", members);
    return map;
  }

  /**
   * 随机弹出并移除一个成员
   * POST /redisSet/spop?key=
   */
  @PostMapping("spop")
  public Map<String, String> spop(@RequestParam("key") String key) {
    String member = redisSetService.spop(key);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("member", member);
    return map;
  }

  /**
   * 随机弹出并移除 count 个成员
   * POST /redisSet/spopCount?key=&count=2
   */
  @PostMapping("spopCount")
  public Map<String, Object> spopCount(@RequestParam("key") String key,
                                       @RequestParam("count") long count) {
    Set<String> members = redisSetService.spop(key, count);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("count", count);
    map.put("members", members);
    return map;
  }

  // ==================== 转移操作 ====================

  /**
   * 将 member 从 source 集合移动到 destination 集合
   * POST /redisSet/smove?source=set:a&destination=set:b&member=m1
   */
  @PostMapping("smove")
  public Map<String, Object> smove(@RequestParam("source") String source,
                                   @RequestParam("destination") String destination,
                                   @RequestParam("member") String member) {
    Boolean result = redisSetService.smove(source, destination, member);
    Map<String, Object> map = new HashMap<>();
    map.put("source", source);
    map.put("destination", destination);
    map.put("member", member);
    map.put("result", result);
    return map;
  }

  // ==================== 集合运算 ====================

  /**
   * 获取多个 Set 的交集（共同好友、共同标签）
   * POST /redisSet/sinter?keys=set:a&keys=set:b
   */
  @PostMapping("sinter")
  public Map<String, Object> sinter(@RequestParam("keys") List<String> keys) {
    Set<String> result = redisSetService.sinter(keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("keys", keys);
    map.put("result", result);
    return map;
  }

  /**
   * 获取多个 Set 的交集并存储
   * POST /redisSet/sinterstore?destination=set:result&keys=set:a&keys=set:b
   */
  @PostMapping("sinterstore")
  public Map<String, Object> sinterstore(@RequestParam("destination") String destination,
                                         @RequestParam("keys") List<String> keys) {
    Long count = redisSetService.sinterstore(destination, keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("destination", destination);
    map.put("keys", keys);
    map.put("count", count);
    return map;
  }

  /**
   * 获取多个 Set 的并集（合并标签、合并好友）
   * POST /redisSet/sunion?keys=set:a&keys=set:b
   */
  @PostMapping("sunion")
  public Map<String, Object> sunion(@RequestParam("keys") List<String> keys) {
    Set<String> result = redisSetService.sunion(keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("keys", keys);
    map.put("result", result);
    return map;
  }

  /**
   * 获取多个 Set 的并集并存储
   * POST /redisSet/sunionstore?destination=set:result&keys=set:a&keys=set:b
   */
  @PostMapping("sunionstore")
  public Map<String, Object> sunionstore(@RequestParam("destination") String destination,
                                         @RequestParam("keys") List<String> keys) {
    Long count = redisSetService.sunionstore(destination, keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("destination", destination);
    map.put("keys", keys);
    map.put("count", count);
    return map;
  }

  /**
   * 获取多个 Set 的差集（第一个 key 有、后续 key 没有的）
   * POST /redisSet/sdiff?keys=set:a&keys=set:b
   */
  @PostMapping("sdiff")
  public Map<String, Object> sdiff(@RequestParam("keys") List<String> keys) {
    Set<String> result = redisSetService.sdiff(keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("keys", keys);
    map.put("result", result);
    return map;
  }

  /**
   * 获取多个 Set 的差集并存储
   * POST /redisSet/sdiffstore?destination=set:result&keys=set:a&keys=set:b
   */
  @PostMapping("sdiffstore")
  public Map<String, Object> sdiffstore(@RequestParam("destination") String destination,
                                        @RequestParam("keys") List<String> keys) {
    Long count = redisSetService.sdiffstore(destination, keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("destination", destination);
    map.put("keys", keys);
    map.put("count", count);
    return map;
  }

  // ==================== 迭代扫描 ====================

  /**
   * 此接口调用的伪代码：
   * <pre>
   *    cursor = "0"
   *    while true:
   *     调用 /redisSet/sscan?key=set:a&cursor={cursor}&count=10
   *     处理返回的 values[]
   *     if finished:
   *         break
   *     cursor = 返回值.cursor
   * </pre>
   *
   * 增量迭代扫描 Set（cursor 首次传 "0"，后续传上次返回值；match 支持 * 通配）
   * POST /redisSet/sscan?key=&cursor=0&count=10&match=
   */
  @PostMapping("sscan")
  public Map<String, Object> sscan(@RequestParam("key") String key,
                                   @RequestParam(value = "cursor", defaultValue = "0") String cursor,
                                   @RequestParam(value = "count", defaultValue = "10") long count,
                                   @RequestParam(value = "match", defaultValue = "") String match) {
    ValueScanCursor<String> scanResult = redisSetService.sscan(key, cursor, count, match);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("cursor", scanResult.getCursor());
    map.put("finished", scanResult.isFinished());
    map.put("values", scanResult.getValues());
    return map;
  }

}
