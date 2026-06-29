package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.*;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class RedisSortedSetHelper {

  @Resource
  private StatefulRedisClusterConnection<String, String> clusterConnection;

  private String tag(String key) {
    if (key == null || key.isEmpty()) return key;
    if (key.contains("{")) return key;
    int idx = key.indexOf(':');
    if (idx > 0) return "{" + key.substring(0, idx) + "}" + key.substring(idx);
    return "{" + key + "}";
  }

  private String[] tag(String... keys) {
    String[] result = new String[keys.length];
    for (int i = 0; i < keys.length; i++) result[i] = tag(keys[i]);
    return result;
  }

  /**
   * 解析 score range 字符串，支持: "1.0", "(1.0", "+inf", "-inf"
   */
  private Range<String> parseRange(String min, String max) {
    Range.Boundary<String> lower = parseBound(min);
    Range.Boundary<String> upper = parseBound(max);
    if (lower != null && upper != null) return Range.from(lower, upper);
    if (lower != null) return Range.from(lower, Range.Boundary.unbounded());
    if (upper != null) return Range.from(Range.Boundary.unbounded(), upper);
    return Range.unbounded();
  }

  private Range<Double> parseScoreRange(String minStr, String maxStr) {
    Range.Boundary<Double> lower = null;
    Range.Boundary<Double> upper = null;
    if (minStr != null && !minStr.isEmpty() && !"-inf".equalsIgnoreCase(minStr)) {
      boolean exclusive = minStr.startsWith("(");
      double val = Double.parseDouble(exclusive ? minStr.substring(1) : minStr);
      lower = exclusive ? Range.Boundary.excluding(val) : Range.Boundary.including(val);
    }
    if (maxStr != null && !maxStr.isEmpty() && !"+inf".equalsIgnoreCase(maxStr)) {
      boolean exclusive = maxStr.startsWith("(");
      double val = Double.parseDouble(exclusive ? maxStr.substring(1) : maxStr);
      upper = exclusive ? Range.Boundary.excluding(val) : Range.Boundary.including(val);
    }
    if (lower != null && upper != null) return Range.from(lower, upper);
    if (lower != null) return Range.from(lower, Range.Boundary.unbounded());
    if (upper != null) return Range.from(Range.Boundary.unbounded(), upper);
    return Range.unbounded();
  }

  private Range.Boundary<String> parseBound(String value) {
    if (value == null || value.isEmpty()) return null;
    if ("+inf".equalsIgnoreCase(value)) return null;
    if ("-inf".equalsIgnoreCase(value)) return null;
    if (value.startsWith("("))
      return Range.Boundary.excluding(value.substring(1));
    return Range.Boundary.including(value);
  }

  // ==================== 增删改 ====================

  public Long zadd(String key, double score, String member) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.zadd(key, score, member);
      log.info("ZADD key={}, score={}, member={}, result={}", key, score, member, result);
      return result;
    } catch (Exception e) {
      log.error("ZADD command failed, key={}", key, e);
      throw new RuntimeException("Redis ZADD operation failed", e);
    }
  }

  public Long zrem(String key, String... members) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zrem(key, members);
      log.info("ZREM key={}, members={}, count={}", key, String.join(",", members), count);
      return count;
    } catch (Exception e) {
      log.error("ZREM command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREM operation failed", e);
    }
  }

  public Double zincrby(String key, double amount, String member) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Double newScore = sync.zincrby(key, amount, member);
      log.info("ZINCRBY key={}, amount={}, member={}, newScore={}", key, amount, member, newScore);
      return newScore;
    } catch (Exception e) {
      log.error("ZINCRBY command failed, key={}", key, e);
      throw new RuntimeException("Redis ZINCRBY operation failed", e);
    }
  }

  // ==================== 查询统计 ====================

  public Long zcard(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zcard(key);
      log.info("ZCARD key={}, count={}", key, count);
      return count;
    } catch (Exception e) {
      log.error("ZCARD command failed, key={}", key, e);
      throw new RuntimeException("Redis ZCARD operation failed", e);
    }
  }

  public Double zscore(String key, String member) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Double score = sync.zscore(key, member);
      log.info("ZSCORE key={}, member={}, score={}", key, member, score);
      return score;
    } catch (Exception e) {
      log.error("ZSCORE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZSCORE operation failed", e);
    }
  }

  public List<Double> zmscore(String key, String... members) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<Double> scores = sync.zmscore(key, members);
      log.info("ZMSCORE key={}, members={}, scores={}", key, String.join(",", members), scores);
      return scores;
    } catch (Exception e) {
      log.error("ZMSCORE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZMSCORE operation failed", e);
    }
  }

  public Long zrank(String key, String member) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long rank = sync.zrank(key, member);
      log.info("ZRANK key={}, member={}, rank={}", key, member, rank);
      return rank;
    } catch (Exception e) {
      log.error("ZRANK command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANK operation failed", e);
    }
  }

  public Long zrevrank(String key, String member) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long rank = sync.zrevrank(key, member);
      log.info("ZREVRANK key={}, member={}, rank={}", key, member, rank);
      return rank;
    } catch (Exception e) {
      log.error("ZREVRANK command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREVRANK operation failed", e);
    }
  }

  public Long zcount(String key, String min, String max) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zcount(key, parseScoreRange(min, max));
      log.info("ZCOUNT key={}, min={}, max={}, count={}", key, min, max, count);
      return count;
    } catch (Exception e) {
      log.error("ZCOUNT command failed, key={}", key, e);
      throw new RuntimeException("Redis ZCOUNT operation failed", e);
    }
  }

  // ==================== 范围查询（按索引） ====================

  public List<String> zrange(String key, long start, long stop) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> values = sync.zrange(key, start, stop);
      log.info("ZRANGE key={}, start={}, stop={}, size={}", key, start, stop, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZRANGE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANGE operation failed", e);
    }
  }

  public List<ScoredValue<String>> zrangeWithScores(String key, long start, long stop) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<ScoredValue<String>> values = sync.zrangeWithScores(key, start, stop);
      log.info("ZRANGE key={}, start={}, stop={}, size={}", key, start, stop, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZRANGE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANGE operation failed", e);
    }
  }

  public List<String> zrevrange(String key, long start, long stop) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> values = sync.zrevrange(key, start, stop);
      log.info("ZREVRANGE key={}, start={}, stop={}, size={}", key, start, stop, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZREVRANGE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREVRANGE operation failed", e);
    }
  }

  public List<ScoredValue<String>> zrevrangeWithScores(String key, long start, long stop) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<ScoredValue<String>> values = sync.zrevrangeWithScores(key, start, stop);
      log.info("ZREVRANGE key={}, start={}, stop={}, size={}", key, start, stop, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZREVRANGE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREVRANGE operation failed", e);
    }
  }

  // ==================== 范围查询（按分数） ====================

  public List<String> zrangebyscore(String key, String min, String max) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> values = sync.zrangebyscore(key, parseScoreRange(min, max));
      log.info("ZRANGEBYSCORE key={}, min={}, max={}, size={}", key, min, max, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZRANGEBYSCORE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANGEBYSCORE operation failed", e);
    }
  }

  public List<ScoredValue<String>> zrangebyscoreWithScores(String key, String min, String max) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<ScoredValue<String>> values = sync.zrangebyscoreWithScores(key, parseScoreRange(min, max));
      log.info("ZRANGEBYSCORE key={}, min={}, max={}, size={}", key, min, max, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZRANGEBYSCORE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANGEBYSCORE operation failed", e);
    }
  }

  public List<String> zrangebyscore(String key, String min, String max, long offset, long count) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Limit limit = Limit.create(offset, count);
      List<String> values = sync.zrangebyscore(key, parseScoreRange(min, max), limit);
      log.info("ZRANGEBYSCORE key={}, min={}, max={}, offset={}, count={}, size={}",
              key, min, max, offset, count, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZRANGEBYSCORE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANGEBYSCORE operation failed", e);
    }
  }

  public List<String> zrevrangebyscore(String key, String max, String min) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> values = sync.zrevrangebyscore(key, parseScoreRange(min, max));
      log.info("ZREVRANGEBYSCORE key={}, max={}, min={}, size={}", key, max, min, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZREVRANGEBYSCORE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREVRANGEBYSCORE operation failed", e);
    }
  }

  public List<ScoredValue<String>> zrevrangebyscoreWithScores(String key, String max, String min) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<ScoredValue<String>> values = sync.zrevrangebyscoreWithScores(key, parseScoreRange(min, max));
      log.info("ZREVRANGEBYSCORE key={}, max={}, min={}, size={}", key, max, min, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZREVRANGEBYSCORE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREVRANGEBYSCORE operation failed", e);
    }
  }

  // ==================== 弹出操作 ====================

  public ScoredValue<String> zpopmin(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      ScoredValue<String> result = sync.zpopmin(key);
      log.info("ZPOPMIN key={}, result={}", key, result);
      return result;
    } catch (Exception e) {
      log.error("ZPOPMIN command failed, key={}", key, e);
      throw new RuntimeException("Redis ZPOPMIN operation failed", e);
    }
  }

  public List<ScoredValue<String>> zpopmin(String key, long count) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<ScoredValue<String>> result = sync.zpopmin(key, count);
      log.info("ZPOPMIN key={}, count={}, size={}", key, count, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("ZPOPMIN command failed, key={}", key, e);
      throw new RuntimeException("Redis ZPOPMIN operation failed", e);
    }
  }

  public ScoredValue<String> zpopmax(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      ScoredValue<String> result = sync.zpopmax(key);
      log.info("ZPOPMAX key={}, result={}", key, result);
      return result;
    } catch (Exception e) {
      log.error("ZPOPMAX command failed, key={}", key, e);
      throw new RuntimeException("Redis ZPOPMAX operation failed", e);
    }
  }

  public List<ScoredValue<String>> zpopmax(String key, long count) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<ScoredValue<String>> result = sync.zpopmax(key, count);
      log.info("ZPOPMAX key={}, count={}, size={}", key, count, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("ZPOPMAX command failed, key={}", key, e);
      throw new RuntimeException("Redis ZPOPMAX operation failed", e);
    }
  }

  // ==================== 按范围删除 ====================

  public Long zremrangebyrank(String key, long start, long stop) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zremrangebyrank(key, start, stop);
      log.info("ZREMRANGEBYRANK key={}, start={}, stop={}, count={}", key, start, stop, count);
      return count;
    } catch (Exception e) {
      log.error("ZREMRANGEBYRANK command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREMRANGEBYRANK operation failed", e);
    }
  }

  public Long zremrangebyscore(String key, String min, String max) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zremrangebyscore(key, parseScoreRange(min, max));
      log.info("ZREMRANGEBYSCORE key={}, min={}, max={}, count={}", key, min, max, count);
      return count;
    } catch (Exception e) {
      log.error("ZREMRANGEBYSCORE command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREMRANGEBYSCORE operation failed", e);
    }
  }

  // ==================== 集合运算 ====================

  public Long zinterstore(String destination, String... keys) {
    String[] tagged = tag(keys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zinterstore(destination, tagged);
      log.info("ZINTERSTORE destination={}, keys={}, count={}", destination, String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("ZINTERSTORE command failed, destination={}", destination, e);
      throw new RuntimeException("Redis ZINTERSTORE operation failed", e);
    }
  }

  public Long zunionstore(String destination, String... keys) {
    String[] tagged = tag(keys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zunionstore(destination, tagged);
      log.info("ZUNIONSTORE destination={}, keys={}, count={}", destination, String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("ZUNIONSTORE command failed, destination={}", destination, e);
      throw new RuntimeException("Redis ZUNIONSTORE operation failed", e);
    }
  }

  // ==================== 阻塞弹出 ====================

  /**
   * 阻塞式弹出分数最小的成员（多个 key 需同 hash tag）
   * Redis BZPOPMIN 命令
   */
  public KeyValue<String, ScoredValue<String>> bzpopmin(long timeout, String... keys) {
    String[] tagged = tag(keys);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      KeyValue<String, ScoredValue<String>> result = sync.bzpopmin(timeout, tagged);
      log.info("BZPOPMIN keys={}, timeout={}, result={}", String.join(",", tagged), timeout, result);
      return result;
    } catch (Exception e) {
      log.error("BZPOPMIN command failed, keys={}", String.join(",", tagged), e);
      throw new RuntimeException("Redis BZPOPMIN operation failed", e);
    }
  }

  /**
   * 阻塞式弹出分数最大的成员（多个 key 需同 hash tag）
   * Redis BZPOPMAX 命令
   */
  public KeyValue<String, ScoredValue<String>> bzpopmax(long timeout, String... keys) {
    String[] tagged = tag(keys);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      KeyValue<String, ScoredValue<String>> result = sync.bzpopmax(timeout, tagged);
      log.info("BZPOPMAX keys={}, timeout={}, result={}", String.join(",", tagged), timeout, result);
      return result;
    } catch (Exception e) {
      log.error("BZPOPMAX command failed, keys={}", String.join(",", tagged), e);
      throw new RuntimeException("Redis BZPOPMAX operation failed", e);
    }
  }

  // ==================== 随机操作 ====================

  /**
   * 从有序集合中随机获取一个成员
   * Redis ZRANDMEMBER 命令（Redis 6.2+）
   */
  public String zrandmember(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String member = sync.zrandmember(key);
      log.info("ZRANDMEMBER key={}, member={}", key, member);
      return member;
    } catch (Exception e) {
      log.error("ZRANDMEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANDMEMBER operation failed", e);
    }
  }

  /**
   * 从有序集合中随机获取 count 个成员（count 为负时允许重复）
   * Redis ZRANDMEMBER 命令（Redis 6.2+）
   */
  public List<String> zrandmember(String key, long count) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> members = sync.zrandmember(key, count);
      log.info("ZRANDMEMBER key={}, count={}, size={}", key, count, members != null ? members.size() : 0);
      return members;
    } catch (Exception e) {
      log.error("ZRANDMEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANDMEMBER operation failed", e);
    }
  }

  /**
   * 从有序集合中随机获取 count 个成员及分数
   * Redis ZRANDMEMBER 命令（Redis 6.2+）
   */
  public List<ScoredValue<String>> zrandmemberWithScores(String key, long count) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<ScoredValue<String>> result = sync.zrandmemberWithScores(key, count);
      log.info("ZRANDMEMBER key={}, count={}, withScores, size={}", key, count, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("ZRANDMEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANDMEMBER operation failed", e);
    }
  }

  // ==================== 字典序操作 ====================

  /**
   * 按字典序统计区间成员数量（所有成员分数必须相同）
   * Redis ZLEXCOUNT 命令，用于自动补全等场景
   */
  public Long zlexcount(String key, String min, String max) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zlexcount(key, parseRange(min, max));
      log.info("ZLEXCOUNT key={}, min={}, max={}, count={}", key, min, max, count);
      return count;
    } catch (Exception e) {
      log.error("ZLEXCOUNT command failed, key={}", key, e);
      throw new RuntimeException("Redis ZLEXCOUNT operation failed", e);
    }
  }

  /**
   * 按字典序范围获取成员（所有成员分数必须相同）
   * Redis ZRANGEBYLEX 命令，用于自动补全等场景
   * min/max: [a (开) [b (闭) - (无穷小) + (无穷大)
   */
  public List<String> zrangebylex(String key, String min, String max) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> values = sync.zrangebylex(key, parseRange(min, max));
      log.info("ZRANGEBYLEX key={}, min={}, max={}, size={}", key, min, max, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("ZRANGEBYLEX command failed, key={}", key, e);
      throw new RuntimeException("Redis ZRANGEBYLEX operation failed", e);
    }
  }

  /**
   * 按字典序范围删除成员（所有成员分数必须相同）
   * Redis ZREMRANGEBYLEX 命令
   */
  public Long zremrangebylex(String key, String min, String max) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.zremrangebylex(key, parseRange(min, max));
      log.info("ZREMRANGEBYLEX key={}, min={}, max={}, count={}", key, min, max, count);
      return count;
    } catch (Exception e) {
      log.error("ZREMRANGEBYLEX command failed, key={}", key, e);
      throw new RuntimeException("Redis ZREMRANGEBYLEX operation failed", e);
    }
  }

  // ==================== 迭代扫描 ====================

  public ScoredValueScanCursor<String> zscan(String key, String cursor, long count, String match) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      ScanArgs scanArgs = ScanArgs.Builder.limit(count);
      if (match != null && !match.isEmpty()) scanArgs = scanArgs.match(match);
      ScoredValueScanCursor<String> result = sync.zscan(key, ScanCursor.of(cursor), scanArgs);
      log.info("ZSCAN key={}, cursor={}, count={}, match={}, resultCursor={}, size={}",
              key, cursor, count, match, result.getCursor(), result.getValues().size());
      return result;
    } catch (Exception e) {
      log.error("ZSCAN command failed, key={}", key, e);
      throw new RuntimeException("Redis ZSCAN operation failed", e);
    }
  }

}
