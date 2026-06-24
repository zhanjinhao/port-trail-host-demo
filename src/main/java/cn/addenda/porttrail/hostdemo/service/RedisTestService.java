package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class RedisTestService {

  @Autowired
  private RedisHelper redisHelper;

  public void set(String key, String value) {
    redisHelper.set(key, value);
  }

  public String get(String key) {
    return redisHelper.get(key);
  }

  public void del(String key) {
    redisHelper.del(key);
  }

  public void hset(String key, String field, String value) {
    redisHelper.hset(key, field, value);
  }

  public String hget(String key, String field) {
    return redisHelper.hget(key, field);
  }

  public Map<String, String> hgetall(String key) {
    return redisHelper.hgetall(key);
  }

  public Long hdel(String key, String field) {
    return redisHelper.hdel(key, field);
  }

  public Boolean hexists(String key, String field) {
    return redisHelper.hexists(key, field);
  }

  public Long hlen(String key) {
    return redisHelper.hlen(key);
  }

}
