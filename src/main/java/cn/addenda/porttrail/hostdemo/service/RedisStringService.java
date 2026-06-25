package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisStringHelper;
import io.lettuce.core.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RedisStringService {

  @Autowired
  private RedisStringHelper redisStringHelper;

  public String set(String key, String value) {
    return redisStringHelper.set(key, value);
  }

  public String get(String key) {
    return redisStringHelper.get(key);
  }

  public Long del(String... keys) {
    return redisStringHelper.del(keys);
  }

  public String setex(String key, long seconds, String value) {
    return redisStringHelper.setex(key, seconds, value);
  }

  public String psetex(String key, long milliseconds, String value) {
    return redisStringHelper.psetex(key, milliseconds, value);
  }

  public Boolean setnx(String key, String value) {
    return redisStringHelper.setnx(key, value);
  }

  public Long exists(String... keys) {
    return redisStringHelper.exists(keys);
  }

  public Boolean expire(String key, long seconds) {
    return redisStringHelper.expire(key, seconds);
  }

  public Long ttl(String key) {
    return redisStringHelper.ttl(key);
  }

  public Long incr(String key) {
    return redisStringHelper.incr(key);
  }

  public Long incrby(String key, long amount) {
    return redisStringHelper.incrby(key, amount);
  }

  public Double incrbyfloat(String key, double amount) {
    return redisStringHelper.incrbyfloat(key, amount);
  }

  public Long decr(String key) {
    return redisStringHelper.decr(key);
  }

  public Long decrby(String key, long amount) {
    return redisStringHelper.decrby(key, amount);
  }

  public List<KeyValue<String, String>> mget(String... keys) {
    return redisStringHelper.mget(keys);
  }

  public String mset(Map<String, String> map) {
    return redisStringHelper.mset(map);
  }

  public String getset(String key, String value) {
    return redisStringHelper.getset(key, value);
  }

  public Long append(String key, String value) {
    return redisStringHelper.append(key, value);
  }

  public Long strlen(String key) {
    return redisStringHelper.strlen(key);
  }

  public String getrange(String key, long start, long end) {
    return redisStringHelper.getrange(key, start, end);
  }

  public Long setrange(String key, long offset, String value) {
    return redisStringHelper.setrange(key, offset, value);
  }

}
