package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisSortedSetService;
import io.lettuce.core.KeyValue;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.ScoredValueScanCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("redisSortedSet")
public class RedisSortedSetController {

  @Autowired
  private RedisSortedSetService redisSortedSetService;

  private Map<String, Object> toMap(ScoredValue<String> sv) {
    Map<String, Object> m = new HashMap<>();
    if (sv != null) {
      m.put("member", sv.getValue());
      m.put("score", sv.getScore());
    }
    return m;
  }

  private List<Map<String, Object>> toList(List<ScoredValue<String>> list) {
    List<Map<String, Object>> result = new ArrayList<>();
    if (list != null) {
      for (ScoredValue<String> sv : list) result.add(toMap(sv));
    }
    return result;
  }

  private Map<String, Object> ok(Object... kvs) {
    Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < kvs.length; i += 2) map.put(String.valueOf(kvs[i]), kvs[i + 1]);
    return map;
  }

  // ==================== 增删改 ====================

  /**
   * 向有序集合添加成员及分数（member 存在则更新分数）
   * POST /redisSortedSet/zadd?key=&score=10.5&member=m1
   */
  @PostMapping("zadd")
  public Map<String, Object> zadd(@RequestParam("key") String key,
                                  @RequestParam("score") double score,
                                  @RequestParam("member") String member) {
    Long result = redisSortedSetService.zadd(key, score, member);
    return ok("key", key, "score", score, "member", member, "result", result);
  }

  /**
   * 从有序集合移除成员
   * POST /redisSortedSet/zrem?key=&members=m1&members=m2
   */
  @PostMapping("zrem")
  public Map<String, Object> zrem(@RequestParam("key") String key,
                                  @RequestParam("members") List<String> members) {
    Long count = redisSortedSetService.zrem(key, members.toArray(new String[0]));
    return ok("key", key, "members", members, "count", count);
  }

  /**
   * 对成员分数自增（amount 可为负数）
   * POST /redisSortedSet/zincrby?key=&amount=5&member=m1
   */
  @PostMapping("zincrby")
  public Map<String, Object> zincrby(@RequestParam("key") String key,
                                     @RequestParam("amount") double amount,
                                     @RequestParam("member") String member) {
    Double newScore = redisSortedSetService.zincrby(key, amount, member);
    return ok("key", key, "amount", amount, "member", member, "newScore", newScore);
  }

  // ==================== 查询统计 ====================

  /**
   * 获取有序集合成员数量
   * POST /redisSortedSet/zcard?key=
   */
  @PostMapping("zcard")
  public Map<String, Object> zcard(@RequestParam("key") String key) {
    Long count = redisSortedSetService.zcard(key);
    return ok("key", key, "count", count);
  }

  /**
   * 获取成员分数
   * POST /redisSortedSet/zscore?key=&member=m1
   */
  @PostMapping("zscore")
  public Map<String, Object> zscore(@RequestParam("key") String key,
                                    @RequestParam("member") String member) {
    Double score = redisSortedSetService.zscore(key, member);
    return ok("key", key, "member", member, "score", score);
  }

  /**
   * 批量获取成员分数（Redis 6.2+，不存在的成员返回 null）
   * POST /redisSortedSet/zmscore?key=&members=m1&members=m2
   */
  @PostMapping("zmscore")
  public Map<String, Object> zmscore(@RequestParam("key") String key,
                                     @RequestParam("members") List<String> members) {
    List<Double> scores = redisSortedSetService.zmscore(key, members.toArray(new String[0]));
    return ok("key", key, "members", members, "scores", scores);
  }

  /**
   * 获取成员排名（升序，0 开始，不存在返回 null）
   * POST /redisSortedSet/zrank?key=&member=m1
   */
  @PostMapping("zrank")
  public Map<String, Object> zrank(@RequestParam("key") String key,
                                   @RequestParam("member") String member) {
    Long rank = redisSortedSetService.zrank(key, member);
    return ok("key", key, "member", member, "rank", rank);
  }

  /**
   * 获取成员排名（降序，0 开始）
   * POST /redisSortedSet/zrevrank?key=&member=m1
   */
  @PostMapping("zrevrank")
  public Map<String, Object> zrevrank(@RequestParam("key") String key,
                                      @RequestParam("member") String member) {
    Long rank = redisSortedSetService.zrevrank(key, member);
    return ok("key", key, "member", member, "rank", rank);
  }

  /**
   * 统计分数区间内的成员数量 [min,max]，括号表示开区间
   * POST /redisSortedSet/zcount?key=&min=0&max=100
   */
  @PostMapping("zcount")
  public Map<String, Object> zcount(@RequestParam("key") String key,
                                    @RequestParam("min") String min,
                                    @RequestParam("max") String max) {
    Long count = redisSortedSetService.zcount(key, min, max);
    return ok("key", key, "min", min, "max", max, "count", count);
  }

  // ==================== 范围查询（按索引） ====================

  /**
   * 按索引升序获取成员（0 第一个，-1 最后一个）
   * POST /redisSortedSet/zrange?key=&start=0&stop=-1
   */
  @PostMapping("zrange")
  public Map<String, Object> zrange(@RequestParam("key") String key,
                                    @RequestParam("start") long start,
                                    @RequestParam("stop") long stop) {
    List<String> values = redisSortedSetService.zrange(key, start, stop);
    return ok("key", key, "start", start, "stop", stop, "values", values);
  }

  /**
   * 按索引升序获取成员及分数
   * POST /redisSortedSet/zrangeWithScores?key=&start=0&stop=-1
   */
  @PostMapping("zrangeWithScores")
  public Map<String, Object> zrangeWithScores(@RequestParam("key") String key,
                                              @RequestParam("start") long start,
                                              @RequestParam("stop") long stop) {
    List<ScoredValue<String>> values = redisSortedSetService.zrangeWithScores(key, start, stop);
    return ok("key", key, "start", start, "stop", stop, "values", toList(values));
  }

  /**
   * 按索引降序获取成员（排行榜 Top N）
   * POST /redisSortedSet/zrevrange?key=&start=0&stop=9
   */
  @PostMapping("zrevrange")
  public Map<String, Object> zrevrange(@RequestParam("key") String key,
                                       @RequestParam("start") long start,
                                       @RequestParam("stop") long stop) {
    List<String> values = redisSortedSetService.zrevrange(key, start, stop);
    return ok("key", key, "start", start, "stop", stop, "values", values);
  }

  /**
   * 按索引降序获取成员及分数
   * POST /redisSortedSet/zrevrangeWithScores?key=&start=0&stop=9
   */
  @PostMapping("zrevrangeWithScores")
  public Map<String, Object> zrevrangeWithScores(@RequestParam("key") String key,
                                                 @RequestParam("start") long start,
                                                 @RequestParam("stop") long stop) {
    List<ScoredValue<String>> values = redisSortedSetService.zrevrangeWithScores(key, start, stop);
    return ok("key", key, "start", start, "stop", stop, "values", toList(values));
  }

  // ==================== 范围查询（按分数） ====================

  /**
   * 按分数区间升序获取成员（min/max: 1.0 含, (1.0 不含, +inf, -inf）
   * POST /redisSortedSet/zrangebyscore?key=&min=0&max=100
   */
  @PostMapping("zrangebyscore")
  public Map<String, Object> zrangebyscore(@RequestParam("key") String key,
                                           @RequestParam("min") String min,
                                           @RequestParam("max") String max) {
    List<String> values = redisSortedSetService.zrangebyscore(key, min, max);
    return ok("key", key, "min", min, "max", max, "values", values);
  }

  /**
   * 按分数区间升序获取成员及分数
   * POST /redisSortedSet/zrangebyscoreWithScores?key=&min=0&max=100
   */
  @PostMapping("zrangebyscoreWithScores")
  public Map<String, Object> zrangebyscoreWithScores(@RequestParam("key") String key,
                                                     @RequestParam("min") String min,
                                                     @RequestParam("max") String max) {
    List<ScoredValue<String>> values = redisSortedSetService.zrangebyscoreWithScores(key, min, max);
    return ok("key", key, "min", min, "max", max, "values", toList(values));
  }

  /**
   * 按分数区间升序分页获取成员
   * POST /redisSortedSet/zrangebyscoreLimit?key=&min=0&max=100&offset=0&count=10
   */
  @PostMapping("zrangebyscoreLimit")
  public Map<String, Object> zrangebyscoreLimit(@RequestParam("key") String key,
                                                @RequestParam("min") String min,
                                                @RequestParam("max") String max,
                                                @RequestParam("offset") long offset,
                                                @RequestParam("count") long count) {
    List<String> values = redisSortedSetService.zrangebyscore(key, min, max, offset, count);
    return ok("key", key, "min", min, "max", max, "offset", offset, "count", count, "values", values);
  }

  /**
   * 按分数区间降序获取成员
   * POST /redisSortedSet/zrevrangebyscore?key=&max=100&min=0
   */
  @PostMapping("zrevrangebyscore")
  public Map<String, Object> zrevrangebyscore(@RequestParam("key") String key,
                                              @RequestParam("max") String max,
                                              @RequestParam("min") String min) {
    List<String> values = redisSortedSetService.zrevrangebyscore(key, max, min);
    return ok("key", key, "max", max, "min", min, "values", values);
  }

  /**
   * 按分数区间降序获取成员及分数
   * POST /redisSortedSet/zrevrangebyscoreWithScores?key=&max=100&min=0
   */
  @PostMapping("zrevrangebyscoreWithScores")
  public Map<String, Object> zrevrangebyscoreWithScores(@RequestParam("key") String key,
                                                        @RequestParam("max") String max,
                                                        @RequestParam("min") String min) {
    List<ScoredValue<String>> values = redisSortedSetService.zrevrangebyscoreWithScores(key, max, min);
    return ok("key", key, "max", max, "min", min, "values", toList(values));
  }

  // ==================== 弹出操作 ====================

  /**
   * 弹出分数最小的成员（优先队列出队）
   * POST /redisSortedSet/zpopmin?key=
   */
  @PostMapping("zpopmin")
  public Map<String, Object> zpopmin(@RequestParam("key") String key) {
    ScoredValue<String> sv = redisSortedSetService.zpopmin(key);
    return ok("key", key, "result", toMap(sv));
  }

  /**
   * 弹出 count 个分数最小的成员
   * POST /redisSortedSet/zpopminCount?key=&count=3
   */
  @PostMapping("zpopminCount")
  public Map<String, Object> zpopminCount(@RequestParam("key") String key,
                                          @RequestParam("count") long count) {
    List<ScoredValue<String>> list = redisSortedSetService.zpopmin(key, count);
    return ok("key", key, "count", count, "result", toList(list));
  }

  /**
   * 弹出分数最大的成员（排行榜 Top1 出队）
   * POST /redisSortedSet/zpopmax?key=
   */
  @PostMapping("zpopmax")
  public Map<String, Object> zpopmax(@RequestParam("key") String key) {
    ScoredValue<String> sv = redisSortedSetService.zpopmax(key);
    return ok("key", key, "result", toMap(sv));
  }

  /**
   * 弹出 count 个分数最大的成员
   * POST /redisSortedSet/zpopmaxCount?key=&count=3
   */
  @PostMapping("zpopmaxCount")
  public Map<String, Object> zpopmaxCount(@RequestParam("key") String key,
                                          @RequestParam("count") long count) {
    List<ScoredValue<String>> list = redisSortedSetService.zpopmax(key, count);
    return ok("key", key, "count", count, "result", toList(list));
  }

  // ==================== 按范围删除 ====================

  /**
   * 按索引范围删除成员
   * POST /redisSortedSet/zremrangebyrank?key=&start=0&stop=4
   */
  @PostMapping("zremrangebyrank")
  public Map<String, Object> zremrangebyrank(@RequestParam("key") String key,
                                             @RequestParam("start") long start,
                                             @RequestParam("stop") long stop) {
    Long count = redisSortedSetService.zremrangebyrank(key, start, stop);
    return ok("key", key, "start", start, "stop", stop, "count", count);
  }

  /**
   * 按分数区间删除成员
   * POST /redisSortedSet/zremrangebyscore?key=&min=0&max=50
   */
  @PostMapping("zremrangebyscore")
  public Map<String, Object> zremrangebyscore(@RequestParam("key") String key,
                                              @RequestParam("min") String min,
                                              @RequestParam("max") String max) {
    Long count = redisSortedSetService.zremrangebyscore(key, min, max);
    return ok("key", key, "min", min, "max", max, "count", count);
  }

  // ==================== 集合运算 ====================

  /**
   * 取多个有序集合的交集并存储（分数取 SUM）
   * POST /redisSortedSet/zinterstore?destination=zset:result&keys=zset:a&keys=zset:b
   */
  @PostMapping("zinterstore")
  public Map<String, Object> zinterstore(@RequestParam("destination") String destination,
                                         @RequestParam("keys") List<String> keys) {
    Long count = redisSortedSetService.zinterstore(destination, keys.toArray(new String[0]));
    return ok("destination", destination, "keys", keys, "count", count);
  }

  /**
   * 取多个有序集合的并集并存储（分数取 SUM）
   * POST /redisSortedSet/zunionstore?destination=zset:result&keys=zset:a&keys=zset:b
   */
  @PostMapping("zunionstore")
  public Map<String, Object> zunionstore(@RequestParam("destination") String destination,
                                         @RequestParam("keys") List<String> keys) {
    Long count = redisSortedSetService.zunionstore(destination, keys.toArray(new String[0]));
    return ok("destination", destination, "keys", keys, "count", count);
  }

  // ==================== 阻塞弹出 ====================

  /**
   * 阻塞式弹出分数最小的成员（多个 key 需同 hash tag，不可用于 Redis Cluster）
   * POST /redisSortedSet/bzpopmin?keys=zset:a&keys=zset:b&timeout=5
   */
  @PostMapping("bzpopmin")
  public Map<String, Object> bzpopmin(@RequestParam("keys") List<String> keys,
                                      @RequestParam("timeout") long timeout) {
    KeyValue<String, ScoredValue<String>> kv = redisSortedSetService.bzpopmin(timeout, keys.toArray(new String[0]));
    Map<String, Object> map = ok("keys", keys, "timeout", timeout);
    if (kv != null && kv.hasValue()) {
      map.put("key", kv.getKey());
      map.put("result", toMap(kv.getValue()));
    }
    return map;
  }

  /**
   * 阻塞式弹出分数最大的成员（多个 key 需同 hash tag，不可用于 Redis Cluster）
   * POST /redisSortedSet/bzpopmax?keys=zset:a&keys=zset:b&timeout=5
   */
  @PostMapping("bzpopmax")
  public Map<String, Object> bzpopmax(@RequestParam("keys") List<String> keys,
                                      @RequestParam("timeout") long timeout) {
    KeyValue<String, ScoredValue<String>> kv = redisSortedSetService.bzpopmax(timeout, keys.toArray(new String[0]));
    Map<String, Object> map = ok("keys", keys, "timeout", timeout);
    if (kv != null && kv.hasValue()) {
      map.put("key", kv.getKey());
      map.put("result", toMap(kv.getValue()));
    }
    return map;
  }

  // ==================== 随机操作 ====================

  /**
   * 从有序集合中随机获取一个成员
   * POST /redisSortedSet/zrandmember?key=
   */
  @PostMapping("zrandmember")
  public Map<String, String> zrandmember(@RequestParam("key") String key) {
    String member = redisSortedSetService.zrandmember(key);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("member", member);
    return map;
  }

  /**
   * 从有序集合中随机获取 count 个成员（count<0 允许重复）
   * POST /redisSortedSet/zrandmemberCount?key=&count=3
   */
  @PostMapping("zrandmemberCount")
  public Map<String, Object> zrandmemberCount(@RequestParam("key") String key,
                                              @RequestParam("count") long count) {
    List<String> members = redisSortedSetService.zrandmember(key, count);
    return ok("key", key, "count", count, "members", members);
  }

  /**
   * 从有序集合中随机获取 count 个成员及分数
   * POST /redisSortedSet/zrandmemberWithScores?key=&count=3
   */
  @PostMapping("zrandmemberWithScores")
  public Map<String, Object> zrandmemberWithScores(@RequestParam("key") String key,
                                                   @RequestParam("count") long count) {
    List<ScoredValue<String>> list = redisSortedSetService.zrandmemberWithScores(key, count);
    return ok("key", key, "count", count, "values", toList(list));
  }

  // ==================== 字典序操作 ====================

  /**
   * 按字典序统计区间成员数量（所有成员分数必须相同，min/max：[- (]+ (闭) [a  (开) - inf  + inf)
   * POST /redisSortedSet/zlexcount?key=&min=-&max=+
   */
  @PostMapping("zlexcount")
  public Map<String, Object> zlexcount(@RequestParam("key") String key,
                                       @RequestParam("min") String min,
                                       @RequestParam("max") String max) {
    Long count = redisSortedSetService.zlexcount(key, min, max);
    return ok("key", key, "min", min, "max", max, "count", count);
  }

  /**
   * 按字典序获取区间成员（自动补全场景）
   * POST /redisSortedSet/zrangebylex?key=&min=[a&max=[z
   */
  @PostMapping("zrangebylex")
  public Map<String, Object> zrangebylex(@RequestParam("key") String key,
                                         @RequestParam("min") String min,
                                         @RequestParam("max") String max) {
    List<String> values = redisSortedSetService.zrangebylex(key, min, max);
    return ok("key", key, "min", min, "max", max, "values", values);
  }

  /**
   * 按字典序区间删除成员（所有成员分数必须相同）
   * POST /redisSortedSet/zremrangebylex?key=&min=[a&max=[m
   */
  @PostMapping("zremrangebylex")
  public Map<String, Object> zremrangebylex(@RequestParam("key") String key,
                                            @RequestParam("min") String min,
                                            @RequestParam("max") String max) {
    Long count = redisSortedSetService.zremrangebylex(key, min, max);
    return ok("key", key, "min", min, "max", max, "count", count);
  }

  // ==================== 迭代扫描 ====================

  /**
   * 增量迭代扫描有序集合（cursor 首次传 "0"；match 支持 * 通配）
   * POST /redisSortedSet/zscan?key=&cursor=0&count=10&match=
   */
  @PostMapping("zscan")
  public Map<String, Object> zscan(@RequestParam("key") String key,
                                   @RequestParam(value = "cursor", defaultValue = "0") String cursor,
                                   @RequestParam(value = "count", defaultValue = "10") long count,
                                   @RequestParam(value = "match", defaultValue = "") String match) {
    ScoredValueScanCursor<String> scanResult = redisSortedSetService.zscan(key, cursor, count, match);
    return ok("key", key, "cursor", scanResult.getCursor(),
              "finished", scanResult.isFinished(),
              "values", toList(scanResult.getValues()));
  }

}
