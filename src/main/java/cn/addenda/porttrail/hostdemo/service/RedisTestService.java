package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisTestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class RedisTestService {

  @Autowired
  private RedisTestHelper redisTestHelper;

  public void hset(String key, String field, String value) {
    redisTestHelper.hset(key, field, value);
  }

  public String hget(String key, String field) {
    return redisTestHelper.hget(key, field);
  }

  public Map<String, String> hgetall(String key) {
    return redisTestHelper.hgetall(key);
  }

  public Long hdel(String key, String field) {
    return redisTestHelper.hdel(key, field);
  }

  public Boolean hexists(String key, String field) {
    return redisTestHelper.hexists(key, field);
  }

  public Long hlen(String key) {
    return redisTestHelper.hlen(key);
  }

}
