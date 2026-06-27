package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.KeyValue;
import io.lettuce.core.LMoveArgs;
import io.lettuce.core.LPosArgs;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class RedisListHelper {

  @Resource
  private StatefulRedisClusterConnection<String, String> clusterConnection;

  /**
   * 为 key 自动添加 hash tag，确保同一前缀的 key 落在同一 slot
   * - 已有 {...} 的 key 保持不变
   * - "prefix:suffix" 格式 → "{prefix}:suffix"
   * - 无冒号的 key → "{key}"
   */
  private String tag(String key) {
    if (key == null || key.isEmpty()) {
      return key;
    }
    if (key.contains("{")) {
      return key;
    }
    int idx = key.indexOf(':');
    if (idx > 0) {
      return "{" + key.substring(0, idx) + "}" + key.substring(idx);
    }
    return "{" + key + "}";
  }

  private String[] tag(String... keys) {
    String[] result = new String[keys.length];
    for (int i = 0; i < keys.length; i++) {
      result[i] = tag(keys[i]);
    }
    return result;
  }

  // ==================== 入栈/入队 ====================

  public Long lpush(String key, String... values) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.lpush(key, values);
      log.info("LPUSH key={}, values={}, length={}", key, String.join(",", values), length);
      return length;
    } catch (Exception e) {
      log.error("LPUSH command failed, key={}", key, e);
      throw new RuntimeException("Redis LPUSH operation failed", e);
    }
  }

  public Long rpush(String key, String... values) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.rpush(key, values);
      log.info("RPUSH key={}, values={}, length={}", key, String.join(",", values), length);
      return length;
    } catch (Exception e) {
      log.error("RPUSH command failed, key={}", key, e);
      throw new RuntimeException("Redis RPUSH operation failed", e);
    }
  }

  // ==================== 出栈/出队 ====================

  public String lpop(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String value = sync.lpop(key);
      log.info("LPOP key={}, value={}", key, value);
      return value;
    } catch (Exception e) {
      log.error("LPOP command failed, key={}", key, e);
      throw new RuntimeException("Redis LPOP operation failed", e);
    }
  }

  public String rpop(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String value = sync.rpop(key);
      log.info("RPOP key={}, value={}", key, value);
      return value;
    } catch (Exception e) {
      log.error("RPOP command failed, key={}", key, e);
      throw new RuntimeException("Redis RPOP operation failed", e);
    }
  }

  // ==================== 查询操作 ====================

  public Long llen(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.llen(key);
      log.info("LLEN key={}, length={}", key, length);
      return length;
    } catch (Exception e) {
      log.error("LLEN command failed, key={}", key, e);
      throw new RuntimeException("Redis LLEN operation failed", e);
    }
  }

  public List<String> lrange(String key, long start, long stop) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> values = sync.lrange(key, start, stop);
      log.info("LRANGE key={}, start={}, stop={}, size={}", key, start, stop, values != null ? values.size() : 0);
      return values;
    } catch (Exception e) {
      log.error("LRANGE command failed, key={}", key, e);
      throw new RuntimeException("Redis LRANGE operation failed", e);
    }
  }

  public String lindex(String key, long index) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String value = sync.lindex(key, index);
      log.info("LINDEX key={}, index={}, value={}", key, index, value);
      return value;
    } catch (Exception e) {
      log.error("LINDEX command failed, key={}", key, e);
      throw new RuntimeException("Redis LINDEX operation failed", e);
    }
  }

  // ==================== 修改操作 ====================

  public String lset(String key, long index, String value) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.lset(key, index, value);
      log.info("LSET key={}, index={}, value={}, result={}", key, index, value, result);
      return result;
    } catch (Exception e) {
      log.error("LSET command failed, key={}", key, e);
      throw new RuntimeException("Redis LSET operation failed", e);
    }
  }

  public Long lrem(String key, long count, String value) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long removed = sync.lrem(key, count, value);
      log.info("LREM key={}, count={}, value={}, removed={}", key, count, value, removed);
      return removed;
    } catch (Exception e) {
      log.error("LREM command failed, key={}", key, e);
      throw new RuntimeException("Redis LREM operation failed", e);
    }
  }

  public String ltrim(String key, long start, long stop) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.ltrim(key, start, stop);
      log.info("LTRIM key={}, start={}, stop={}, result={}", key, start, stop, result);
      return result;
    } catch (Exception e) {
      log.error("LTRIM command failed, key={}", key, e);
      throw new RuntimeException("Redis LTRIM operation failed", e);
    }
  }

  public Long linsert(String key, boolean before, String pivot, String value) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.linsert(key, before, pivot, value);
      log.info("LINSERT key={}, before={}, pivot={}, value={}, length={}", key, before, pivot, value, length);
      return length;
    } catch (Exception e) {
      log.error("LINSERT command failed, key={}", key, e);
      throw new RuntimeException("Redis LINSERT operation failed", e);
    }
  }

  // ==================== 阻塞操作 ====================

  public KeyValue<String, String> blpop(long timeout, String... keys) {
    String[] tagged = tag(keys);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      KeyValue<String, String> result = sync.blpop(timeout, tagged);
      log.info("BLPOP keys={}, timeout={}, result={}", String.join(",", tagged), timeout,
          result != null ? result.getKey() + ":" + (result.hasValue() ? result.getValue() : "null") : "null");
      return result;
    } catch (Exception e) {
      log.error("BLPOP command failed, keys={}", String.join(",", tagged), e);
      throw new RuntimeException("Redis BLPOP operation failed", e);
    }
  }

  public KeyValue<String, String> brpop(long timeout, String... keys) {
    String[] tagged = tag(keys);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      KeyValue<String, String> result = sync.brpop(timeout, tagged);
      log.info("BRPOP keys={}, timeout={}, result={}", String.join(",", tagged), timeout,
          result != null ? result.getKey() + ":" + (result.hasValue() ? result.getValue() : "null") : "null");
      return result;
    } catch (Exception e) {
      log.error("BRPOP command failed, keys={}", String.join(",", tagged), e);
      throw new RuntimeException("Redis BRPOP operation failed", e);
    }
  }

  // ==================== 转移操作 ====================

  public String rpoplpush(String source, String destination) {
    source = tag(source);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String value = sync.rpoplpush(source, destination);
      log.info("RPOPLPUSH source={}, destination={}, value={}", source, destination, value);
      return value;
    } catch (Exception e) {
      log.error("RPOPLPUSH command failed, source={}, destination={}", source, destination, e);
      throw new RuntimeException("Redis RPOPLPUSH operation failed", e);
    }
  }

  // ==================== 条件入队 ====================

  public Long lpushx(String key, String... values) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.lpushx(key, values);
      log.info("LPUSHX key={}, values={}, length={}", key, String.join(",", values), length);
      return length;
    } catch (Exception e) {
      log.error("LPUSHX command failed, key={}", key, e);
      throw new RuntimeException("Redis LPUSHX operation failed", e);
    }
  }

  public Long rpushx(String key, String... values) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.rpushx(key, values);
      log.info("RPUSHX key={}, values={}, length={}", key, String.join(",", values), length);
      return length;
    } catch (Exception e) {
      log.error("RPUSHX command failed, key={}", key, e);
      throw new RuntimeException("Redis RPUSHX operation failed", e);
    }
  }

  // ==================== 位置查找 ====================

  public Long lpos(String key, String value) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long index = sync.lpos(key, value);
      log.info("LPOS key={}, value={}, index={}", key, value, index);
      return index;
    } catch (Exception e) {
      log.error("LPOS command failed, key={}, value={}", key, value, e);
      throw new RuntimeException("Redis LPOS operation failed", e);
    }
  }

  public List<Long> lpos(String key, String value, long count, long rank) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      LPosArgs lposArgs = new LPosArgs();
      lposArgs.rank(rank);
      List<Long> indices = sync.lpos(key, value, (int) count, lposArgs);
      log.info("LPOS key={}, value={}, count={}, rank={}, indices={}", key, value, count, rank, indices);
      return indices;
    } catch (Exception e) {
      log.error("LPOS command failed, key={}, value={}", key, value, e);
      throw new RuntimeException("Redis LPOS operation failed", e);
    }
  }

  // ==================== 定向转移 ====================

  public String lmove(String source, String destination, LMoveArgs args) {
    source = tag(source);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String value = sync.lmove(source, destination, args);
      log.info("LMOVE source={}, destination={}, value={}", source, destination, value);
      return value;
    } catch (Exception e) {
      log.error("LMOVE command failed, source={}, destination={}", source, destination, e);
      throw new RuntimeException("Redis LMOVE operation failed", e);
    }
  }

}
