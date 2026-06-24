package cn.addenda.porttrail.hostdemo.config;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RedisConfig {

  @Value("${redis.cluster.nodes}")
  private List<String> clusterNodes;

  @Value("${redis.cluster.max-redirects}")
  private int maxRedirects;

  @Value("${redis.lettuce.pool.max-active:8}")
  private int maxActive;

  @Value("${redis.lettuce.pool.max-idle:4}")
  private int maxIdle;

  @Value("${redis.lettuce.pool.min-idle:2}")
  private int minIdle;

  @Value("${redis.lettuce.pool.max-wait:3000}")
  private long maxWait;

  @Value("${redis.lettuce.shutdown-timeout:100}")
  private int shutdownTimeout;

  @Bean(destroyMethod = "shutdown")
  public RedisClusterClient redisClusterClient() {
    List<RedisURI> redisURIs = clusterNodes.stream()
            .map(node -> {
              String[] parts = node.split(":");
              return RedisURI.create(parts[0], Integer.parseInt(parts[1]));
            })
            .collect(Collectors.toList());

    RedisClusterClient clusterClient = RedisClusterClient.create(redisURIs);

    ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
            .enablePeriodicRefresh(Duration.ofMinutes(10))
            .enableAllAdaptiveRefreshTriggers()
            .build();

    clusterClient.setOptions(ClusterClientOptions.builder()
            .topologyRefreshOptions(topologyRefreshOptions)
            .maxRedirects(maxRedirects)
            .build());

    return clusterClient;
  }

  @Bean(destroyMethod = "close")
  public StatefulRedisClusterConnection<String, String> clusterConnection(RedisClusterClient clusterClient) {
    StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
    connection.setTimeout(Duration.ofMinutes(60));
    return connection;
  }

}
