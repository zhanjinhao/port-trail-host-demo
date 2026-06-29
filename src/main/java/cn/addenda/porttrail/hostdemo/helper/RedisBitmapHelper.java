package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.BitFieldArgs;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class RedisBitmapHelper {

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

  // ==================== 位操作 ====================

  /**
   * 设置指定位偏移的值（0 或 1），返回旧值
   * Redis SETBIT 命令
   */
  public Long setbit(String key, long offset, int value) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long oldValue = sync.setbit(key, offset, value);
      log.info("SETBIT key={}, offset={}, value={}, oldValue={}", key, offset, value, oldValue);
      return oldValue;
    } catch (Exception e) {
      log.error("SETBIT command failed, key={}", key, e);
      throw new RuntimeException("Redis SETBIT operation failed", e);
    }
  }

  /**
   * 获取指定位偏移的值（0 或 1）
   * Redis GETBIT 命令
   */
  public Long getbit(String key, long offset) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long value = sync.getbit(key, offset);
      log.info("GETBIT key={}, offset={}, value={}", key, offset, value);
      return value;
    } catch (Exception e) {
      log.error("GETBIT command failed, key={}", key, e);
      throw new RuntimeException("Redis GETBIT operation failed", e);
    }
  }

  // ==================== 统计 ====================

  /**
   * 统计位图中值为 1 的 bit 数量
   * Redis BITCOUNT 命令
   */
  public Long bitcount(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.bitcount(key);
      log.info("BITCOUNT key={}, count={}", key, count);
      return count;
    } catch (Exception e) {
      log.error("BITCOUNT command failed, key={}", key, e);
      throw new RuntimeException("Redis BITCOUNT operation failed", e);
    }
  }

  /**
   * 统计位图指定字节区间内值为 1 的 bit 数量
   * Redis BITCOUNT key start end（start/end 是字节偏移，不是 bit）
   */
  public Long bitcount(String key, long start, long end) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.bitcount(key, start, end);
      log.info("BITCOUNT key={}, start={}, end={}, count={}", key, start, end, count);
      return count;
    } catch (Exception e) {
      log.error("BITCOUNT command failed, key={}", key, e);
      throw new RuntimeException("Redis BITCOUNT operation failed", e);
    }
  }

  // ==================== 查找 ====================

  /**
   * 查找第一个值为 bit（0 或 1）的位置
   * Redis BITPOS 命令
   */
  public Long bitpos(String key, boolean bit) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long pos = sync.bitpos(key, bit);
      log.info("BITPOS key={}, bit={}, pos={}", key, bit, pos);
      return pos;
    } catch (Exception e) {
      log.error("BITPOS command failed, key={}", key, e);
      throw new RuntimeException("Redis BITPOS operation failed", e);
    }
  }

  /**
   * 从指定字节偏移开始查找第一个值为 bit 的位置
   * Redis BITPOS key bit start（start 是字节偏移）
   */
  public Long bitpos(String key, boolean bit, long start) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long pos = sync.bitpos(key, bit, start);
      log.info("BITPOS key={}, bit={}, start={}, pos={}", key, bit, start, pos);
      return pos;
    } catch (Exception e) {
      log.error("BITPOS command failed, key={}", key, e);
      throw new RuntimeException("Redis BITPOS operation failed", e);
    }
  }

  /**
   * 在指定字节区间内查找第一个值为 bit 的位置
   * Redis BITPOS key bit start end（start/end 是字节偏移）
   */
  public Long bitpos(String key, boolean bit, long start, long end) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long pos = sync.bitpos(key, bit, start, end);
      log.info("BITPOS key={}, bit={}, start={}, end={}, pos={}", key, bit, start, end, pos);
      return pos;
    } catch (Exception e) {
      log.error("BITPOS command failed, key={}", key, e);
      throw new RuntimeException("Redis BITPOS operation failed", e);
    }
  }

  // ==================== 位运算 ====================

  /**
   * 对多个位图执行 AND（与）运算，结果存入 destination
   * Redis BITOP AND 命令，常用于统计连续签到
   */
  public Long bitopAnd(String destination, String... keys) {
    String[] tagged = tag(keys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.bitopAnd(destination, tagged);
      log.info("BITOP AND destination={}, keys={}, count={}", destination, String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("BITOP AND command failed, destination={}", destination, e);
      throw new RuntimeException("Redis BITOP AND operation failed", e);
    }
  }

  /**
   * 对多个位图执行 OR（或）运算，结果存入 destination
   * Redis BITOP OR 命令，常用于合并多天签到
   */
  public Long bitopOr(String destination, String... keys) {
    String[] tagged = tag(keys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.bitopOr(destination, tagged);
      log.info("BITOP OR destination={}, keys={}, count={}", destination, String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("BITOP OR command failed, destination={}", destination, e);
      throw new RuntimeException("Redis BITOP OR operation failed", e);
    }
  }

  /**
   * 对多个位图执行 XOR（异或）运算，结果存入 destination
   * Redis BITOP XOR 命令，常用于对比差异
   */
  public Long bitopXor(String destination, String... keys) {
    String[] tagged = tag(keys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.bitopXor(destination, tagged);
      log.info("BITOP XOR destination={}, keys={}, count={}", destination, String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("BITOP XOR command failed, destination={}", destination, e);
      throw new RuntimeException("Redis BITOP XOR operation failed", e);
    }
  }

  /**
   * 对单个位图执行 NOT（取反）运算，结果存入 destination
   * Redis BITOP NOT 命令
   */
  public Long bitopNot(String destination, String source) {
    source = tag(source);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.bitopNot(destination, source);
      log.info("BITOP NOT destination={}, source={}, count={}", destination, source, count);
      return count;
    } catch (Exception e) {
      log.error("BITOP NOT command failed, destination={}, source={}", destination, source, e);
      throw new RuntimeException("Redis BITOP NOT operation failed", e);
    }
  }

  // ==================== 位域操作 ====================

  /**
   * BITFIELD GET：读取指定位宽和偏移的值
   *
   * @param type   位宽类型，如 "u8" 无符号8位, "i16" 有符号16位
   * @param offset 位偏移（可带 # 表示乘位宽，如 #2 表示第2个i16字段）
   */
  public List<Long> bitfieldGet(String key, String type, String offset) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      BitFieldArgs args = parseBitFieldArgs("GET", type, offset, null);
      List<Long> result = sync.bitfield(key, args);
      log.info("BITFIELD GET key={}, type={}, offset={}, result={}", key, type, offset, result);
      return result;
    } catch (Exception e) {
      log.error("BITFIELD GET command failed, key={}", key, e);
      throw new RuntimeException("Redis BITFIELD GET operation failed", e);
    }
  }

  /**
   * BITFIELD SET：设置指定位宽和偏移的值
   */
  public List<Long> bitfieldSet(String key, String type, String offset, long value) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      BitFieldArgs args = parseBitFieldArgs("SET", type, offset, String.valueOf(value));
      List<Long> result = sync.bitfield(key, args);
      log.info("BITFIELD SET key={}, type={}, offset={}, value={}, result={}", key, type, offset, value, result);
      return result;
    } catch (Exception e) {
      log.error("BITFIELD SET command failed, key={}", key, e);
      throw new RuntimeException("Redis BITFIELD SET operation failed", e);
    }
  }

  /**
   * BITFIELD INCRBY：对指定位宽和偏移的值进行自增
   */
  public List<Long> bitfieldIncrBy(String key, String type, String offset, long increment) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      BitFieldArgs args = parseBitFieldArgs("INCRBY", type, offset, String.valueOf(increment));
      List<Long> result = sync.bitfield(key, args);
      log.info("BITFIELD INCRBY key={}, type={}, offset={}, increment={}, result={}", key, type, offset, increment, result);
      return result;
    } catch (Exception e) {
      log.error("BITFIELD INCRBY command failed, key={}", key, e);
      throw new RuntimeException("Redis BITFIELD INCRBY operation failed", e);
    }
  }

  private BitFieldArgs parseBitFieldArgs(String op, String type, String offset, String value) {
    BitFieldArgs.BitFieldType fieldType;
    if (type.startsWith("u") || type.startsWith("U")) {
      fieldType = BitFieldArgs.unsigned(Integer.parseInt(type.substring(1)));
    } else {
      fieldType = BitFieldArgs.signed(Integer.parseInt(type.substring(1)));
    }
    BitFieldArgs args = new BitFieldArgs();
    if ("GET".equals(op)) {
      if (offset.startsWith("#")) {
        args = args.get(fieldType, Integer.parseInt(offset.substring(1)));
      } else {
        args = args.get(fieldType, Integer.parseInt(offset));
      }
    } else if ("SET".equals(op)) {
      if (offset.startsWith("#")) {
        args = args.set(fieldType, Integer.parseInt(offset.substring(1)), Long.parseLong(value));
      } else {
        args = args.set(fieldType, Integer.parseInt(offset), Long.parseLong(value));
      }
    } else if ("INCRBY".equals(op)) {
      if (offset.startsWith("#")) {
        args = args.incrBy(fieldType, Integer.parseInt(offset.substring(1)), Long.parseLong(value));
      } else {
        args = args.incrBy(fieldType, Integer.parseInt(offset), Long.parseLong(value));
      }
    }
    return args;
  }

}
