package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisBitmapHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisBitmapService {

  @Autowired
  private RedisBitmapHelper redisBitmapHelper;

  public Long setbit(String key, long offset, int value) {
    return redisBitmapHelper.setbit(key, offset, value);
  }

  public Long getbit(String key, long offset) {
    return redisBitmapHelper.getbit(key, offset);
  }

  public Long bitcount(String key) {
    return redisBitmapHelper.bitcount(key);
  }

  public Long bitcount(String key, long start, long end) {
    return redisBitmapHelper.bitcount(key, start, end);
  }

  public Long bitpos(String key, boolean bit) {
    return redisBitmapHelper.bitpos(key, bit);
  }

  public Long bitpos(String key, boolean bit, long start) {
    return redisBitmapHelper.bitpos(key, bit, start);
  }

  public Long bitpos(String key, boolean bit, long start, long end) {
    return redisBitmapHelper.bitpos(key, bit, start, end);
  }

  public Long bitopAnd(String destination, String... keys) {
    return redisBitmapHelper.bitopAnd(destination, keys);
  }

  public Long bitopOr(String destination, String... keys) {
    return redisBitmapHelper.bitopOr(destination, keys);
  }

  public Long bitopXor(String destination, String... keys) {
    return redisBitmapHelper.bitopXor(destination, keys);
  }

  public Long bitopNot(String destination, String source) {
    return redisBitmapHelper.bitopNot(destination, source);
  }

  public List<Long> bitfieldGet(String key, String type, String offset) {
    return redisBitmapHelper.bitfieldGet(key, type, offset);
  }

  public List<Long> bitfieldSet(String key, String type, String offset, long value) {
    return redisBitmapHelper.bitfieldSet(key, type, offset, value);
  }

  public List<Long> bitfieldIncrBy(String key, String type, String offset, long increment) {
    return redisBitmapHelper.bitfieldIncrBy(key, type, offset, increment);
  }

}
