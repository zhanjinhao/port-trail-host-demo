package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RedisHyperLogLogHelper {

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

  public Long pfadd(String key, String... elements) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.pfadd(key, elements);
      log.info("PFADD key={}, elements={}, result={}", key, String.join(",", elements), result);
      return result;
    } catch (Exception e) {
      log.error("PFADD command failed, key={}", key, e);
      throw new RuntimeException("Redis PFADD operation failed", e);
    }
  }

  public Long pfcount(String key) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.pfcount(key);
      log.info("PFCOUNT key={}, count={}", key, count);
      return count;
    } catch (Exception e) {
      log.error("PFCOUNT command failed, key={}", key, e);
      throw new RuntimeException("Redis PFCOUNT operation failed", e);
    }
  }

  public Long pfcountMulti(String... keys) {
    String[] tagged = tag(keys);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long count = sync.pfcount(tagged);
      log.info("PFCOUNT keys={}, count={}", String.join(",", tagged), count);
      return count;
    } catch (Exception e) {
      log.error("PFCOUNT command failed, keys={}", String.join(",", tagged), e);
      throw new RuntimeException("Redis PFCOUNT operation failed", e);
    }
  }

  public String pfmerge(String destination, String... sourceKeys) {
    String[] tagged = tag(sourceKeys);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      String result = sync.pfmerge(destination, tagged);
      log.info("PFMERGE destination={}, sourceKeys={}, result={}", destination, String.join(",", tagged), result);
      return result;
    } catch (Exception e) {
      log.error("PFMERGE command failed, destination={}", destination, e);
      throw new RuntimeException("Redis PFMERGE operation failed", e);
    }
  }

}
