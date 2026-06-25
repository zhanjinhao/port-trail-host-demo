package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisHashHelper;
import io.lettuce.core.KeyValue;
import io.lettuce.core.MapScanCursor;
import io.lettuce.core.ScanArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RedisHashService {

  @Autowired
  private RedisHashHelper redisHashHelper;

  public Boolean hset(String key, String field, String value) {
    return redisHashHelper.hset(key, field, value);
  }

  public String hget(String key, String field) {
    return redisHashHelper.hget(key, field);
  }

  public Map<String, String> hgetall(String key) {
    return redisHashHelper.hgetall(key);
  }

  public Long hdel(String key, String... fields) {
    return redisHashHelper.hdel(key, fields);
  }

  public Boolean hexists(String key, String field) {
    return redisHashHelper.hexists(key, field);
  }

  public Long hlen(String key) {
    return redisHashHelper.hlen(key);
  }

  public Boolean hsetnx(String key, String field, String value) {
    return redisHashHelper.hsetnx(key, field, value);
  }

  public List<String> hkeys(String key) {
    return redisHashHelper.hkeys(key);
  }

  public List<String> hvals(String key) {
    return redisHashHelper.hvals(key);
  }

  public Long hincrby(String key, String field, long amount) {
    return redisHashHelper.hincrby(key, field, amount);
  }

  public Double hincrbyfloat(String key, String field, double amount) {
    return redisHashHelper.hincrbyfloat(key, field, amount);
  }

  public Long hstrlen(String key, String field) {
    return redisHashHelper.hstrlen(key, field);
  }

  public String hmset(String key, Map<String, String> map) {
    return redisHashHelper.hmset(key, map);
  }

  public List<KeyValue<String, String>> hmget(String key, String... fields) {
    return redisHashHelper.hmget(key, fields);
  }

  public String hrandfield(String key) {
    return redisHashHelper.hrandfield(key);
  }

  public List<String> hrandfield(String key, long count) {
    return redisHashHelper.hrandfield(key, count);
  }

  public List<KeyValue<String, String>> hrandfieldWithValues(String key, long count) {
    return redisHashHelper.hrandfieldWithValues(key, count);
  }

  public MapScanCursor<String, String> hscan(String key) {
    return redisHashHelper.hscan(key);
  }

  public MapScanCursor<String, String> hscan(String key, ScanArgs scanArgs) {
    return redisHashHelper.hscan(key, scanArgs);
  }

  public MapScanCursor<String, String> hscan(String key, String cursor, long count, String match) {
    return redisHashHelper.hscan(key, cursor, count, match);
  }

}
