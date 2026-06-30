package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisHyperLogLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisHyperLogLogService {

  @Autowired
  private RedisHyperLogLogHelper redisHyperLogLogHelper;

  public Long pfadd(String key, String... elements) {
    return redisHyperLogLogHelper.pfadd(key, elements);
  }

  public Long pfcount(String key) {
    return redisHyperLogLogHelper.pfcount(key);
  }

  public Long pfcountMulti(String... keys) {
    return redisHyperLogLogHelper.pfcountMulti(keys);
  }

  public String pfmerge(String destination, String... sourceKeys) {
    return redisHyperLogLogHelper.pfmerge(destination, sourceKeys);
  }

}
