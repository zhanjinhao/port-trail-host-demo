package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisSetHelper;
import io.lettuce.core.ValueScanCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RedisSetService {

  @Autowired
  private RedisSetHelper redisSetHelper;

  public Long sadd(String key, String... members) {
    return redisSetHelper.sadd(key, members);
  }

  public Long srem(String key, String... members) {
    return redisSetHelper.srem(key, members);
  }

  public Long scard(String key) {
    return redisSetHelper.scard(key);
  }

  public Boolean sismember(String key, String member) {
    return redisSetHelper.sismember(key, member);
  }

  public List<Boolean> smismember(String key, String... members) {
    return redisSetHelper.smismember(key, members);
  }

  public Set<String> smembers(String key) {
    return redisSetHelper.smembers(key);
  }

  public String srandmember(String key) {
    return redisSetHelper.srandmember(key);
  }

  public List<String> srandmember(String key, long count) {
    return redisSetHelper.srandmember(key, count);
  }

  public String spop(String key) {
    return redisSetHelper.spop(key);
  }

  public Set<String> spop(String key, long count) {
    return redisSetHelper.spop(key, count);
  }

  public Boolean smove(String source, String destination, String member) {
    return redisSetHelper.smove(source, destination, member);
  }

  public Set<String> sinter(String... keys) {
    return redisSetHelper.sinter(keys);
  }

  public Long sinterstore(String destination, String... keys) {
    return redisSetHelper.sinterstore(destination, keys);
  }

  public Set<String> sunion(String... keys) {
    return redisSetHelper.sunion(keys);
  }

  public Long sunionstore(String destination, String... keys) {
    return redisSetHelper.sunionstore(destination, keys);
  }

  public Set<String> sdiff(String... keys) {
    return redisSetHelper.sdiff(keys);
  }

  public Long sdiffstore(String destination, String... keys) {
    return redisSetHelper.sdiffstore(destination, keys);
  }

  public ValueScanCursor<String> sscan(String key, String cursor, long count, String match) {
    return redisSetHelper.sscan(key, cursor, count, match);
  }

}
