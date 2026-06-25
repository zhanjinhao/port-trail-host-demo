package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.KeyValue;
import io.lettuce.core.MapScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RedisHashHelper {

  @Resource
  private StatefulRedisClusterConnection<String, String> clusterConnection;

  // ==================== 基础 Hash 操作 ====================

  /**
   * 设置指定 key 的 Hash 中 field 对应的值
   * Redis HSET 命令
   */
  public Boolean hset(String key, String field, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Boolean result = sync.hset(key, field, value);
      log.info("HSET key={}, field={}, value={}, result={}", key, field, value, result);
      return result;
    } catch (Exception e) {
      log.error("HSET command failed, key={}, field={}", key, field, e);
      throw new RuntimeException("Redis HSET operation failed", e);
    }
  }

  /**
   * 获取指定 key 的 Hash 中 field 对应的值
   * Redis HGET 命令
   */
  public String hget(String key, String field) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String value = sync.hget(key, field);
      log.info("HGET key={}, field={}, value={}", key, field, value);
      return value;
    } catch (Exception e) {
      log.error("HGET command failed, key={}, field={}", key, field, e);
      throw new RuntimeException("Redis HGET operation failed", e);
    }
  }

  /**
   * 获取指定 key 的 Hash 的所有 field-value 对
   * Redis HGETALL 命令，注意不要在大 Hash 上使用，可能导致阻塞
   */
  public Map<String, String> hgetall(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Map<String, String> map = sync.hgetall(key);
      log.info("HGETALL key={}, size={}", key, map != null ? map.size() : 0);
      return map;
    } catch (Exception e) {
      log.error("HGETALL command failed, key={}", key, e);
      throw new RuntimeException("Redis HGETALL operation failed", e);
    }
  }

  /**
   * 删除指定 key 的 Hash 中一个或多个 field
   * Redis HDEL 命令
   */
  public Long hdel(String key, String... fields) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long deletedCount = sync.hdel(key, fields);
      log.info("HDEL key={}, fields={}, deletedCount={}", key, String.join(",", fields), deletedCount);
      return deletedCount;
    } catch (Exception e) {
      log.error("HDEL command failed, key={}, fields={}", key, String.join(",", fields), e);
      throw new RuntimeException("Redis HDEL operation failed", e);
    }
  }

  /**
   * 判断指定 key 的 Hash 中 field 是否存在
   * Redis HEXISTS 命令
   */
  public Boolean hexists(String key, String field) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Boolean exists = sync.hexists(key, field);
      log.info("HEXISTS key={}, field={}, exists={}", key, field, exists);
      return exists;
    } catch (Exception e) {
      log.error("HEXISTS command failed, key={}, field={}", key, field, e);
      throw new RuntimeException("Redis HEXISTS operation failed", e);
    }
  }

  /**
   * 获取指定 key 的 Hash 中 field 的数量
   * Redis HLEN 命令
   */
  public Long hlen(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.hlen(key);
      log.info("HLEN key={}, length={}", key, length);
      return length;
    } catch (Exception e) {
      log.error("HLEN command failed, key={}", key, e);
      throw new RuntimeException("Redis HLEN operation failed", e);
    }
  }

  // ==================== 条件 Hash 操作 ====================

  /**
   * 仅当 field 不存在时，才设置该 field 的值
   * Redis HSETNX 命令，常用于分布式锁或唯一性控制
   */
  public Boolean hsetnx(String key, String field, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Boolean result = sync.hsetnx(key, field, value);
      log.info("HSETNX key={}, field={}, value={}, result={}", key, field, value, result);
      return result;
    } catch (Exception e) {
      log.error("HSETNX command failed, key={}, field={}", key, field, e);
      throw new RuntimeException("Redis HSETNX operation failed", e);
    }
  }

  // ==================== Hash 遍历 ====================

  /**
   * 获取指定 key 的 Hash 中所有 field 名称
   * Redis HKEYS 命令，注意不要在大 Hash 上使用
   */
  public List<String> hkeys(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> keys = sync.hkeys(key);
      log.info("HKEYS key={}, size={}", key, keys != null ? keys.size() : 0);
      return keys;
    } catch (Exception e) {
      log.error("HKEYS command failed, key={}", key, e);
      throw new RuntimeException("Redis HKEYS operation failed", e);
    }
  }

  /**
   * 获取指定 key 的 Hash 中所有 value 值
   * Redis HVALS 命令，注意不要在大 Hash 上使用
   */
  public List<String> hvals(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> vals = sync.hvals(key);
      log.info("HVALS key={}, size={}", key, vals != null ? vals.size() : 0);
      return vals;
    } catch (Exception e) {
      log.error("HVALS command failed, key={}", key, e);
      throw new RuntimeException("Redis HVALS operation failed", e);
    }
  }

  // ==================== Hash 计数器 ====================

  /**
   * 对指定 key 的 Hash 中 field 的值进行整数自增（可为负数即自减）
   * Redis HINCRBY 命令
   */
  public Long hincrby(String key, String field, long amount) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.hincrby(key, field, amount);
      log.info("HINCRBY key={}, field={}, amount={}, result={}", key, field, amount, result);
      return result;
    } catch (Exception e) {
      log.error("HINCRBY command failed, key={}, field={}", key, field, e);
      throw new RuntimeException("Redis HINCRBY operation failed", e);
    }
  }

  /**
   * 对指定 key 的 Hash 中 field 的值进行浮点数自增（可为负数即自减）
   * Redis HINCRBYFLOAT 命令，适用于金额、分数等场景
   */
  public Double hincrbyfloat(String key, String field, double amount) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Double result = sync.hincrbyfloat(key, field, amount);
      log.info("HINCRBYFLOAT key={}, field={}, amount={}, result={}", key, field, amount, result);
      return result;
    } catch (Exception e) {
      log.error("HINCRBYFLOAT command failed, key={}, field={}", key, field, e);
      throw new RuntimeException("Redis HINCRBYFLOAT operation failed", e);
    }
  }

  // ==================== Hash 字符串操作 ====================

  /**
   * 获取指定 key 的 Hash 中 field 对应值的字符串长度
   * Redis HSTRLEN 命令
   */
  public Long hstrlen(String key, String field) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long length = sync.hstrlen(key, field);
      log.info("HSTRLEN key={}, field={}, length={}", key, field, length);
      return length;
    } catch (Exception e) {
      log.error("HSTRLEN command failed, key={}, field={}", key, field, e);
      throw new RuntimeException("Redis HSTRLEN operation failed", e);
    }
  }

  // ==================== 批量操作 ====================

  /**
   * 批量设置指定 key 的 Hash 中多个 field-value 对
   * Redis HMSET 命令（Redis 4.0+ 可使用 HSET 替代）
   */
  public String hmset(String key, Map<String, String> map) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.hmset(key, map);
      log.info("HMSET key={}, map={}, result={}", key, map, result);
      return result;
    } catch (Exception e) {
      log.error("HMSET command failed, key={}", key, e);
      throw new RuntimeException("Redis HMSET operation failed", e);
    }
  }

  /**
   * 批量获取指定 key 的 Hash 中多个 field 的值
   * Redis HMGET 命令
   */
  public List<KeyValue<String, String>> hmget(String key, String... fields) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<KeyValue<String, String>> result = sync.hmget(key, fields);
      log.info("HMGET key={}, fields={}, resultSize={}", key, String.join(",", fields), result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("HMGET command failed, key={}, fields={}", key, String.join(",", fields), e);
      throw new RuntimeException("Redis HMGET operation failed", e);
    }
  }

  // ==================== 随机字段 ====================

  /**
   * 从指定 key 的 Hash 中随机获取一个 field 名称
   * Redis HRANDFIELD 命令，适用于抽奖、随机推荐等场景
   */
  public String hrandfield(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String field = sync.hrandfield(key);
      log.info("HRANDFIELD key={}, field={}", key, field);
      return field;
    } catch (Exception e) {
      log.error("HRANDFIELD command failed, key={}", key, e);
      throw new RuntimeException("Redis HRANDFIELD operation failed", e);
    }
  }

  /**
   * 从指定 key 的 Hash 中随机获取 count 个 field 名称
   * count 为正时不可重复，为负时允许重复
   * Redis HRANDFIELD 命令
   */
  public List<String> hrandfield(String key, long count) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> fields = sync.hrandfield(key, count);
      log.info("HRANDFIELD key={}, count={}, size={}", key, count, fields != null ? fields.size() : 0);
      return fields;
    } catch (Exception e) {
      log.error("HRANDFIELD command failed, key={}, count={}", key, count, e);
      throw new RuntimeException("Redis HRANDFIELD operation failed", e);
    }
  }

  /**
   * 从指定 key 的 Hash 中随机获取 count 个 field-value 对
   * count 为正时不可重复，为负时允许重复
   * Redis HRANDFIELD 命令（WITHVALUES 选项）
   */
  public List<KeyValue<String, String>> hrandfieldWithValues(String key, long count) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<KeyValue<String, String>> result = sync.hrandfieldWithvalues(key, count);
      log.info("HRANDFIELD(key={}, count={}, withValues, size={})", key, count, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("HRANDFIELD command failed, key={}, count={}", key, count, e);
      throw new RuntimeException("Redis HRANDFIELD operation failed", e);
    }
  }

  // ==================== 迭代扫描 ====================

  /**
   * 增量迭代扫描 Hash，从游标 0 开始
   * Redis HSCAN 命令，适用于大 Hash 的遍历，不会阻塞
   */
  public MapScanCursor<String, String> hscan(String key) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      io.lettuce.core.ScanCursor cursor = new io.lettuce.core.ScanCursor("0", false);
      MapScanCursor<String, String> result = sync.hscan(key, cursor);
      log.info("HSCAN key={}, cursor={}, size={}", key, result.getCursor(), result.getMap().size());
      return result;
    } catch (Exception e) {
      log.error("HSCAN command failed, key={}", key, e);
      throw new RuntimeException("Redis HSCAN operation failed", e);
    }
  }

  /**
   * 增量迭代扫描 Hash（自定义 ScanArgs），从游标 0 开始
   * Redis HSCAN 命令，适用于大 Hash 的遍历
   */
  public MapScanCursor<String, String> hscan(String key, ScanArgs scanArgs) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      io.lettuce.core.ScanCursor cursor = new io.lettuce.core.ScanCursor("0", false);
      MapScanCursor<String, String> result = sync.hscan(key, cursor, scanArgs);
      log.info("HSCAN key={}, cursor={}, size={}", key, result.getCursor(), result.getMap().size());
      return result;
    } catch (Exception e) {
      log.error("HSCAN command failed, key={}", key, e);
      throw new RuntimeException("Redis HSCAN operation failed", e);
    }
  }

  /**
   * 从指定游标开始增量迭代扫描 Hash，支持分页大小和 pattern 匹配
   * Redis HSCAN 命令
   *
   * @param key    Hash key
   * @param cursor 游标（首次传 "0"，后续传上次返回的 cursor）
   * @param count  建议每次返回的条目数
   * @param match  pattern 模式匹配（如 "f*"），不需要匹配时传空字符串
   */
  public MapScanCursor<String, String> hscan(String key, String cursor, long count, String match) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      ScanArgs scanArgs = ScanArgs.Builder.limit(count);
      if (match != null && !match.isEmpty()) {
        scanArgs = scanArgs.match(match);
      }
      io.lettuce.core.ScanCursor scanCursor = new io.lettuce.core.ScanCursor(cursor, false);
      MapScanCursor<String, String> result = sync.hscan(key, scanCursor, scanArgs);
      log.info("HSCAN key={}, cursor={}, count={}, match={}, resultCursor={}, size={}",
          key, cursor, count, match, result.getCursor(), result.getMap().size());
      return result;
    } catch (Exception e) {
      log.error("HSCAN command failed, key={}", key, e);
      throw new RuntimeException("Redis HSCAN operation failed", e);
    }
  }

}
