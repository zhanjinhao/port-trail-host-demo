package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.ValueScanCursor;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RedisSetHelper {

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

  public Long sadd(String key, String... members) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.sadd(key, members);
      log.info("SADD key={}, members={}, count={}", key, String.join(",", members), count);
      return count;
    } catch (Exception e) {
      log.error("SADD command failed, key={}", key, e);
      throw new RuntimeException("Redis SADD operation failed", e);
    }
  }

  public Long srem(String key, String... members) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.srem(key, members);
      log.info("SREM key={}, members={}, count={}", key, String.join(",", members), count);
      return count;
    } catch (Exception e) {
      log.error("SREM command failed, key={}", key, e);
      throw new RuntimeException("Redis SREM operation failed", e);
    }
  }

  public Long scard(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.scard(key);
      log.info("SCARD key={}, count={}", key, count);
      return count;
    } catch (Exception e) {
      log.error("SCARD command failed, key={}", key, e);
      throw new RuntimeException("Redis SCARD operation failed", e);
    }
  }

  public Boolean sismember(String key, String member) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Boolean exists = sync.sismember(key, member);
      log.info("SISMEMBER key={}, member={}, exists={}", key, member, exists);
      return exists;
    } catch (Exception e) {
      log.error("SISMEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis SISMEMBER operation failed", e);
    }
  }

  public List<Boolean> smismember(String key, String... members) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<Boolean> result = sync.smismember(key, members);
      log.info("SMISMEMBER key={}, members={}, result={}", key, String.join(",", members), result);
      return result;
    } catch (Exception e) {
      log.error("SMISMEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis SMISMEMBER operation failed", e);
    }
  }

  public Set<String> smembers(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Set<String> members = sync.smembers(key);
      log.info("SMEMBERS key={}, size={}", key, members != null ? members.size() : 0);
      return members;
    } catch (Exception e) {
      log.error("SMEMBERS command failed, key={}", key, e);
      throw new RuntimeException("Redis SMEMBERS operation failed", e);
    }
  }

  public String srandmember(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String member = sync.srandmember(key);
      log.info("SRANDMEMBER key={}, member={}", key, member);
      return member;
    } catch (Exception e) {
      log.error("SRANDMEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis SRANDMEMBER operation failed", e);
    }
  }

  public List<String> srandmember(String key, long count) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<String> members = sync.srandmember(key, count);
      log.info("SRANDMEMBER key={}, count={}, size={}", key, count, members != null ? members.size() : 0);
      return members;
    } catch (Exception e) {
      log.error("SRANDMEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis SRANDMEMBER operation failed", e);
    }
  }

  public String spop(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String member = sync.spop(key);
      log.info("SPOP key={}, member={}", key, member);
      return member;
    } catch (Exception e) {
      log.error("SPOP command failed, key={}", key, e);
      throw new RuntimeException("Redis SPOP operation failed", e);
    }
  }

  public Set<String> spop(String key, long count) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Set<String> members = sync.spop(key, count);
      log.info("SPOP key={}, count={}, size={}", key, count, members != null ? members.size() : 0);
      return members;
    } catch (Exception e) {
      log.error("SPOP command failed, key={}", key, e);
      throw new RuntimeException("Redis SPOP operation failed", e);
    }
  }

  public Boolean smove(String source, String destination, String member) {
    source = tag(source);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Boolean result = sync.smove(source, destination, member);
      log.info("SMOVE source={}, destination={}, member={}, result={}", source, destination, member, result);
      return result;
    } catch (Exception e) {
      log.error("SMOVE command failed, source={}, destination={}", source, destination, e);
      throw new RuntimeException("Redis SMOVE operation failed", e);
    }
  }

  public Set<String> sinter(String... keys) {
    String[] tagged = tag(keys);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Set<String> result = sync.sinter(tagged);
      log.info("SINTER keys={}, size={}", String.join(",", tagged), result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("SINTER command failed, keys={}", String.join(",", tagged), e);
      throw new RuntimeException("Redis SINTER operation failed", e);
    }
  }

  public Long sinterstore(String destination, String... keys) {
    String[] tagged = tag(keys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.sinterstore(destination, tagged);
      log.info("SINTERSTORE destination={}, keys={}, count={}", destination, String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("SINTERSTORE command failed, destination={}", destination, e);
      throw new RuntimeException("Redis SINTERSTORE operation failed", e);
    }
  }

  public Set<String> sunion(String... keys) {
    String[] tagged = tag(keys);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Set<String> result = sync.sunion(tagged);
      log.info("SUNION keys={}, size={}", String.join(",", tagged), result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("SUNION command failed, keys={}", String.join(",", tagged), e);
      throw new RuntimeException("Redis SUNION operation failed", e);
    }
  }

  public Long sunionstore(String destination, String... keys) {
    String[] tagged = tag(keys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.sunionstore(destination, tagged);
      log.info("SUNIONSTORE destination={}, keys={}, count={}", destination, String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("SUNIONSTORE command failed, destination={}", destination, e);
      throw new RuntimeException("Redis SUNIONSTORE operation failed", e);
    }
  }

  public Set<String> sdiff(String... keys) {
    String[] tagged = tag(keys);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Set<String> result = sync.sdiff(tagged);
      log.info("SDIFF keys={}, size={}", String.join(",", tagged), result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("SDIFF command failed, keys={}", String.join(",", tagged), e);
      throw new RuntimeException("Redis SDIFF operation failed", e);
    }
  }

  public Long sdiffstore(String destination, String... keys) {
    String[] tagged = tag(keys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.sdiffstore(destination, tagged);
      log.info("SDIFFSTORE destination={}, keys={}, count={}", destination, String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("SDIFFSTORE command failed, destination={}", destination, e);
      throw new RuntimeException("Redis SDIFFSTORE operation failed", e);
    }
  }

  public ValueScanCursor<String> sscan(String key, String cursor, long count, String match) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      ScanArgs scanArgs = ScanArgs.Builder.limit(count);
      if (match != null && !match.isEmpty()) {
        scanArgs = scanArgs.match(match);
      }
      ScanCursor scanCursor = ScanCursor.of(cursor);
      ValueScanCursor<String> result = sync.sscan(key, scanCursor, scanArgs);
      log.info("SSCAN key={}, cursor={}, count={}, match={}, resultCursor={}, size={}",
              key, cursor, count, match, result.getCursor(), result.getValues().size());
      return result;
    } catch (Exception e) {
      log.error("SSCAN command failed, key={}", key, e);
      throw new RuntimeException("Redis SSCAN operation failed", e);
    }
  }

}
