package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisSortedSetHelper;
import io.lettuce.core.KeyValue;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.ScoredValueScanCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisSortedSetService {

  @Autowired
  private RedisSortedSetHelper redisSortedSetHelper;

  public Long zadd(String key, double score, String member) {
    return redisSortedSetHelper.zadd(key, score, member);
  }

  public Long zrem(String key, String... members) {
    return redisSortedSetHelper.zrem(key, members);
  }

  public Double zincrby(String key, double amount, String member) {
    return redisSortedSetHelper.zincrby(key, amount, member);
  }

  public Long zcard(String key) {
    return redisSortedSetHelper.zcard(key);
  }

  public Double zscore(String key, String member) {
    return redisSortedSetHelper.zscore(key, member);
  }

  public List<Double> zmscore(String key, String... members) {
    return redisSortedSetHelper.zmscore(key, members);
  }

  public Long zrank(String key, String member) {
    return redisSortedSetHelper.zrank(key, member);
  }

  public Long zrevrank(String key, String member) {
    return redisSortedSetHelper.zrevrank(key, member);
  }

  public Long zcount(String key, String min, String max) {
    return redisSortedSetHelper.zcount(key, min, max);
  }

  public List<String> zrange(String key, long start, long stop) {
    return redisSortedSetHelper.zrange(key, start, stop);
  }

  public List<ScoredValue<String>> zrangeWithScores(String key, long start, long stop) {
    return redisSortedSetHelper.zrangeWithScores(key, start, stop);
  }

  public List<String> zrevrange(String key, long start, long stop) {
    return redisSortedSetHelper.zrevrange(key, start, stop);
  }

  public List<ScoredValue<String>> zrevrangeWithScores(String key, long start, long stop) {
    return redisSortedSetHelper.zrevrangeWithScores(key, start, stop);
  }

  public List<String> zrangebyscore(String key, String min, String max) {
    return redisSortedSetHelper.zrangebyscore(key, min, max);
  }

  public List<ScoredValue<String>> zrangebyscoreWithScores(String key, String min, String max) {
    return redisSortedSetHelper.zrangebyscoreWithScores(key, min, max);
  }

  public List<String> zrangebyscore(String key, String min, String max, long offset, long count) {
    return redisSortedSetHelper.zrangebyscore(key, min, max, offset, count);
  }

  public List<String> zrevrangebyscore(String key, String max, String min) {
    return redisSortedSetHelper.zrevrangebyscore(key, max, min);
  }

  public List<ScoredValue<String>> zrevrangebyscoreWithScores(String key, String max, String min) {
    return redisSortedSetHelper.zrevrangebyscoreWithScores(key, max, min);
  }

  public ScoredValue<String> zpopmin(String key) {
    return redisSortedSetHelper.zpopmin(key);
  }

  public List<ScoredValue<String>> zpopmin(String key, long count) {
    return redisSortedSetHelper.zpopmin(key, count);
  }

  public ScoredValue<String> zpopmax(String key) {
    return redisSortedSetHelper.zpopmax(key);
  }

  public List<ScoredValue<String>> zpopmax(String key, long count) {
    return redisSortedSetHelper.zpopmax(key, count);
  }

  public Long zremrangebyrank(String key, long start, long stop) {
    return redisSortedSetHelper.zremrangebyrank(key, start, stop);
  }

  public Long zremrangebyscore(String key, String min, String max) {
    return redisSortedSetHelper.zremrangebyscore(key, min, max);
  }

  public Long zinterstore(String destination, String... keys) {
    return redisSortedSetHelper.zinterstore(destination, keys);
  }

  public Long zunionstore(String destination, String... keys) {
    return redisSortedSetHelper.zunionstore(destination, keys);
  }

  public ScoredValueScanCursor<String> zscan(String key, String cursor, long count, String match) {
    return redisSortedSetHelper.zscan(key, cursor, count, match);
  }

  public KeyValue<String, ScoredValue<String>> bzpopmin(long timeout, String... keys) {
    return redisSortedSetHelper.bzpopmin(timeout, keys);
  }

  public KeyValue<String, ScoredValue<String>> bzpopmax(long timeout, String... keys) {
    return redisSortedSetHelper.bzpopmax(timeout, keys);
  }

  public String zrandmember(String key) {
    return redisSortedSetHelper.zrandmember(key);
  }

  public List<String> zrandmember(String key, long count) {
    return redisSortedSetHelper.zrandmember(key, count);
  }

  public List<ScoredValue<String>> zrandmemberWithScores(String key, long count) {
    return redisSortedSetHelper.zrandmemberWithScores(key, count);
  }

  public Long zlexcount(String key, String min, String max) {
    return redisSortedSetHelper.zlexcount(key, min, max);
  }

  public List<String> zrangebylex(String key, String min, String max) {
    return redisSortedSetHelper.zrangebylex(key, min, max);
  }

  public Long zremrangebylex(String key, String min, String max) {
    return redisSortedSetHelper.zremrangebylex(key, min, max);
  }

}
