package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
public class RedisTestHelper {

  @Resource
  private StatefulRedisClusterConnection<String, String> clusterConnection;

  public void hset(String key, String field, String value) {
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      sync.hset(key, field, value);
      log.info("HSET key={}, field={}, value={}", key, field, value);
    } catch (Exception e) {
      log.error("HSET command failed, key={}, field={}", key, field, e);
      throw new RuntimeException("Redis HSET operation failed", e);
    }
  }

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

}
