package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisListHelper;
import io.lettuce.core.KeyValue;
import io.lettuce.core.LMoveArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisListService {

  @Autowired
  private RedisListHelper redisListHelper;

  public Long lpush(String key, String... values) {
    return redisListHelper.lpush(key, values);
  }

  public Long rpush(String key, String... values) {
    return redisListHelper.rpush(key, values);
  }

  public String lpop(String key) {
    return redisListHelper.lpop(key);
  }

  public String rpop(String key) {
    return redisListHelper.rpop(key);
  }

  public Long llen(String key) {
    return redisListHelper.llen(key);
  }

  public List<String> lrange(String key, long start, long stop) {
    return redisListHelper.lrange(key, start, stop);
  }

  public String lindex(String key, long index) {
    return redisListHelper.lindex(key, index);
  }

  public String lset(String key, long index, String value) {
    return redisListHelper.lset(key, index, value);
  }

  public Long lrem(String key, long count, String value) {
    return redisListHelper.lrem(key, count, value);
  }

  public String ltrim(String key, long start, long stop) {
    return redisListHelper.ltrim(key, start, stop);
  }

  public Long linsert(String key, boolean before, String pivot, String value) {
    return redisListHelper.linsert(key, before, pivot, value);
  }

  public KeyValue<String, String> blpop(long timeout, String... keys) {
    return redisListHelper.blpop(timeout, keys);
  }

  public KeyValue<String, String> brpop(long timeout, String... keys) {
    return redisListHelper.brpop(timeout, keys);
  }

  public String rpoplpush(String source, String destination) {
    return redisListHelper.rpoplpush(source, destination);
  }

  public Long lpushx(String key, String... values) {
    return redisListHelper.lpushx(key, values);
  }

  public Long rpushx(String key, String... values) {
    return redisListHelper.rpushx(key, values);
  }

  public Long lpos(String key, String value) {
    return redisListHelper.lpos(key, value);
  }

  public List<Long> lpos(String key, String value, long count, long rank) {
    return redisListHelper.lpos(key, value, count, rank);
  }

  public String lmove(String source, String destination, LMoveArgs args) {
    return redisListHelper.lmove(source, destination, args);
  }

}
