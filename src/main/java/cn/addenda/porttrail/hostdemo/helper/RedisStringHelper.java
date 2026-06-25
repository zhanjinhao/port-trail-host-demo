package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.KeyValue;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RedisStringHelper {

  @Resource
  private StatefulRedisClusterConnection<String, String> clusterConnection;

  // ==================== 基础 K-V 操作 ====================

  public String set(String key, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.set(key, value);
      log.info("SET key={}, value={}, result={}", key, value, result);
      return result;
    } catch (Exception e) {
      log.error("SET command failed, key={}", key, e);
      throw new RuntimeException("Redis SET operation failed", e);
    }
  }

  public String get(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String value = sync.get(key);
      log.info("GET key={}, value={}", key, value);
      return value;
    } catch (Exception e) {
      log.error("GET command failed, key={}", key, e);
      throw new RuntimeException("Redis GET operation failed", e);
    }
  }

  public Long del(String... keys) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long deletedCount = sync.del(keys);
      log.info("DEL keys={}, deletedCount={}", String.join(",", keys), deletedCount);
      return deletedCount;
    } catch (Exception e) {
      log.error("DEL command failed, keys={}", String.join(",", keys), e);
      throw new RuntimeException("Redis DEL operation failed", e);
    }
  }

  // ==================== 带过期时间的 SET ====================

  public String setex(String key, long seconds, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.setex(key, seconds, value);
      log.info("SETEX key={}, seconds={}, value={}, result={}", key, seconds, value, result);
      return result;
    } catch (Exception e) {
      log.error("SETEX command failed, key={}", key, e);
      throw new RuntimeException("Redis SETEX operation failed", e);
    }
  }

  public String psetex(String key, long milliseconds, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.psetex(key, milliseconds, value);
      log.info("PSETEX key={}, ms={}, value={}, result={}", key, milliseconds, value, result);
      return result;
    } catch (Exception e) {
      log.error("PSETEX command failed, key={}", key, e);
      throw new RuntimeException("Redis PSETEX operation failed", e);
    }
  }

  // ==================== 条件 SET ====================

  public Boolean setnx(String key, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Boolean result = sync.setnx(key, value);
      log.info("SETNX key={}, value={}, result={}", key, value, result);
      return result;
    } catch (Exception e) {
      log.error("SETNX command failed, key={}", key, e);
      throw new RuntimeException("Redis SETNX operation failed", e);
    }
  }

  // ==================== Key 生命周期 ====================

  public Long exists(String... keys) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.exists(keys);
      log.info("EXISTS keys={}, count={}", String.join(",", keys), count);
      return count;
    } catch (Exception e) {
      log.error("EXISTS command failed, keys={}", String.join(",", keys), e);
      throw new RuntimeException("Redis EXISTS operation failed", e);
    }
  }

  public Boolean expire(String key, long seconds) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Boolean result = sync.expire(key, seconds);
      log.info("EXPIRE key={}, seconds={}, result={}", key, seconds, result);
      return result;
    } catch (Exception e) {
      log.error("EXPIRE command failed, key={}", key, e);
      throw new RuntimeException("Redis EXPIRE operation failed", e);
    }
  }

  public Long ttl(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.ttl(key);
      log.info("TTL key={}, result={}", key, result);
      return result;
    } catch (Exception e) {
      log.error("TTL command failed, key={}", key, e);
      throw new RuntimeException("Redis TTL operation failed", e);
    }
  }

  // ==================== 计数器 ====================

  public Long incr(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.incr(key);
      log.info("INCR key={}, result={}", key, result);
      return result;
    } catch (Exception e) {
      log.error("INCR command failed, key={}", key, e);
      throw new RuntimeException("Redis INCR operation failed", e);
    }
  }

  public Long incrby(String key, long amount) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.incrby(key, amount);
      log.info("INCRBY key={}, amount={}, result={}", key, amount, result);
      return result;
    } catch (Exception e) {
      log.error("INCRBY command failed, key={}", key, e);
      throw new RuntimeException("Redis INCRBY operation failed", e);
    }
  }

  public Double incrbyfloat(String key, double amount) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Double result = sync.incrbyfloat(key, amount);
      log.info("INCRBYFLOAT key={}, amount={}, result={}", key, amount, result);
      return result;
    } catch (Exception e) {
      log.error("INCRBYFLOAT command failed, key={}", key, e);
      throw new RuntimeException("Redis INCRBYFLOAT operation failed", e);
    }
  }

  public Long decr(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.decr(key);
      log.info("DECR key={}, result={}", key, result);
      return result;
    } catch (Exception e) {
      log.error("DECR command failed, key={}", key, e);
      throw new RuntimeException("Redis DECR operation failed", e);
    }
  }

  public Long decrby(String key, long amount) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.decrby(key, amount);
      log.info("DECRBY key={}, amount={}, result={}", key, amount, result);
      return result;
    } catch (Exception e) {
      log.error("DECRBY command failed, key={}", key, e);
      throw new RuntimeException("Redis DECRBY operation failed", e);
    }
  }

  // ==================== 批量操作 ====================

  public List<KeyValue<String, String>> mget(String... keys) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<KeyValue<String, String>> result = sync.mget(keys);
      log.info("MGET keys={}, resultSize={}", String.join(",", keys), result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("MGET command failed, keys={}", String.join(",", keys), e);
      throw new RuntimeException("Redis MGET operation failed", e);
    }
  }

  public String mset(Map<String, String> map) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.mset(map);
      log.info("MSET map={}, result={}", map, result);
      return result;
    } catch (Exception e) {
      log.error("MSET command failed, map={}", map, e);
      throw new RuntimeException("Redis MSET operation failed", e);
    }
  }

  // ==================== GETSET ====================

  public String getset(String key, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String oldValue = sync.getset(key, value);
      log.info("GETSET key={}, newValue={}, oldValue={}", key, value, oldValue);
      return oldValue;
    } catch (Exception e) {
      log.error("GETSET command failed, key={}", key, e);
      throw new RuntimeException("Redis GETSET operation failed", e);
    }
  }

  // ==================== 字符串操作 ====================

  public Long append(String key, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long newLength = sync.append(key, value);
      log.info("APPEND key={}, value={}, newLength={}", key, value, newLength);
      return newLength;
    } catch (Exception e) {
      log.error("APPEND command failed, key={}", key, e);
      throw new RuntimeException("Redis APPEND operation failed", e);
    }
  }

  public Long strlen(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.strlen(key);
      log.info("STRLEN key={}, length={}", key, length);
      return length;
    } catch (Exception e) {
      log.error("STRLEN command failed, key={}", key, e);
      throw new RuntimeException("Redis STRLEN operation failed", e);
    }
  }

  public String getrange(String key, long start, long end) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.getrange(key, start, end);
      log.info("GETRANGE key={}, start={}, end={}, result={}", key, start, end, result);
      return result;
    } catch (Exception e) {
      log.error("GETRANGE command failed, key={}", key, e);
      throw new RuntimeException("Redis GETRANGE operation failed", e);
    }
  }

  public Long setrange(String key, long offset, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long newLength = sync.setrange(key, offset, value);
      log.info("SETRANGE key={}, offset={}, value={}, newLength={}", key, offset, value, newLength);
      return newLength;
    } catch (Exception e) {
      log.error("SETRANGE command failed, key={}", key, e);
      throw new RuntimeException("Redis SETRANGE operation failed", e);
    }
  }

}
