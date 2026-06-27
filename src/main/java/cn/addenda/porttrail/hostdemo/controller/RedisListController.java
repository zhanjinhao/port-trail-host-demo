package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisListService;
import io.lettuce.core.KeyValue;
import io.lettuce.core.LMoveArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("redisList")
public class RedisListController {

  @Autowired
  private RedisListService redisListService;

  // ==================== 入栈/入队 ====================

  /**
   * 从左侧头部插入元素（左进 → 可做栈/队列）
   * POST /redisList/lpush?key=&values=v1&values=v2
   */
  @PostMapping("lpush")
  public Map<String, Object> lpush(@RequestParam("key") String key,
                                   @RequestParam("values") List<String> values) {
    Long length = redisListService.lpush(key, values.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("values", values);
    map.put("length", length);
    return map;
  }

  /**
   * 从右侧尾部插入元素（右进 → 队列 / 左进 + 右进 = 双端队列）
   * POST /redisList/rpush?key=&values=v1&values=v2
   */
  @PostMapping("rpush")
  public Map<String, Object> rpush(@RequestParam("key") String key,
                                   @RequestParam("values") List<String> values) {
    Long length = redisListService.rpush(key, values.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("values", values);
    map.put("length", length);
    return map;
  }

  // ==================== 出栈/出队 ====================

  /**
   * 从左侧头部弹出并移除元素（左出 → 队列 / 栈出）
   * POST /redisList/lpop?key=
   */
  @PostMapping("lpop")
  public Map<String, String> lpop(@RequestParam("key") String key) {
    String value = redisListService.lpop(key);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  /**
   * 从右侧尾部弹出并移除元素（右出 → 栈）
   * POST /redisList/rpop?key=
   */
  @PostMapping("rpop")
  public Map<String, String> rpop(@RequestParam("key") String key) {
    String value = redisListService.rpop(key);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  // ==================== 查询 ====================

  /**
   * 获取 List 长度
   * POST /redisList/llen?key=
   */
  @PostMapping("llen")
  public Map<String, Object> llen(@RequestParam("key") String key) {
    Long length = redisListService.llen(key);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("length", length);
    return map;
  }

  /**
   * 获取 List 指定范围的元素（0 第一个，-1 最后一个）
   * POST /redisList/lrange?key=&start=0&stop=-1
   */
  @PostMapping("lrange")
  public Map<String, Object> lrange(@RequestParam("key") String key,
                                    @RequestParam("start") long start,
                                    @RequestParam("stop") long stop) {
    List<String> values = redisListService.lrange(key, start, stop);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("start", start);
    map.put("stop", stop);
    map.put("values", values);
    return map;
  }

  /**
   * 获取 List 中指定索引位置的元素
   * POST /redisList/lindex?key=&index=0
   */
  @PostMapping("lindex")
  public Map<String, String> lindex(@RequestParam("key") String key,
                                    @RequestParam("index") long index) {
    String value = redisListService.lindex(key, index);
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("index", String.valueOf(index));
    map.put("value", value);
    return map;
  }

  // ==================== 修改 ====================

  /**
   * 设置（更新） List 中指定索引位置的元素值
   * POST /redisList/lset?key=&index=0&value=newVal
   */
  @PostMapping("lset")
  public Map<String, Object> lset(@RequestParam("key") String key,
                                  @RequestParam("index") long index,
                                  @RequestParam("value") String value) {
    String result = redisListService.lset(key, index, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("index", index);
    map.put("value", value);
    map.put("result", result);
    return map;
  }

  /**
   * 删除 List 中指定数量的指定value元素（count>0 从左, <0 从右, =0 全部）
   * POST /redisList/lrem?key=&count=1&value=target
   */
  @PostMapping("lrem")
  public Map<String, Object> lrem(@RequestParam("key") String key,
                                  @RequestParam("count") long count,
                                  @RequestParam("value") String value) {
    Long removed = redisListService.lrem(key, count, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("count", count);
    map.put("value", value);
    map.put("removed", removed);
    return map;
  }

  /**
   * 裁剪 List 仅保留指定范围（常用于限制最大长度）
   * POST /redisList/ltrim?key=&start=0&stop=99
   */
  @PostMapping("ltrim")
  public Map<String, Object> ltrim(@RequestParam("key") String key,
                                   @RequestParam("start") long start,
                                   @RequestParam("stop") long stop) {
    String result = redisListService.ltrim(key, start, stop);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("start", start);
    map.put("stop", stop);
    map.put("result", result);
    return map;
  }

  /**
   * 在 pivot 元素前/后插入新元素（before=true 前插，false 后插）
   * POST /redisList/linsert?key=&before=true&pivot=target&value=newVal
   */
  @PostMapping("linsert")
  public Map<String, Object> linsert(@RequestParam("key") String key,
                                     @RequestParam("before") boolean before,
                                     @RequestParam("pivot") String pivot,
                                     @RequestParam("value") String value) {
    Long length = redisListService.linsert(key, before, pivot, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("before", before);
    map.put("pivot", pivot);
    map.put("value", value);
    map.put("length", length);
    return map;
  }

  // ==================== 阻塞操作 ====================

  /**
   * 阻塞式从左侧弹出元素，超时（秒）前无元素则阻塞等待（不可用于 Redis Cluster）
   * POST /redisList/blpop?keys=key1&keys=key2&timeout=5
   */
  @PostMapping("blpop")
  public Map<String, Object> blpop(@RequestParam("keys") List<String> keys,
                                   @RequestParam("timeout") long timeout) {
    KeyValue<String, String> kv = redisListService.blpop(timeout, keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("keys", keys);
    map.put("timeout", timeout);
    if (kv != null && kv.hasValue()) {
      map.put("key", kv.getKey());
      map.put("value", kv.getValue());
    }
    return map;
  }

  /**
   * 阻塞式从右侧弹出元素，超时（秒）前无元素则阻塞等待（不可用于 Redis Cluster）
   * POST /redisList/brpop?keys=key1&keys=key2&timeout=5
   */
  @PostMapping("brpop")
  public Map<String, Object> brpop(@RequestParam("keys") List<String> keys,
                                   @RequestParam("timeout") long timeout) {
    KeyValue<String, String> kv = redisListService.brpop(timeout, keys.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("keys", keys);
    map.put("timeout", timeout);
    if (kv != null && kv.hasValue()) {
      map.put("key", kv.getKey());
      map.put("value", kv.getValue());
    }
    return map;
  }

  // ==================== 转移操作 ====================

  /**
   * 从 source 右侧弹出元素并推入 destination 左侧（安全队列备份）
   * POST /redisList/rpoplpush?source=srcKey&destination=dstKey
   */
  @PostMapping("rpoplpush")
  public Map<String, String> rpoplpush(@RequestParam("source") String source,
                                       @RequestParam("destination") String destination) {
    String value = redisListService.rpoplpush(source, destination);
    Map<String, String> map = new HashMap<>();
    map.put("source", source);
    map.put("destination", destination);
    map.put("value", value);
    return map;
  }

  // ==================== 条件入队 ====================

  /**
   * 仅当 key 存在时从左侧插入元素（key 不存在则不做任何操作）
   * POST /redisList/lpushx?key=&values=v1&values=v2
   */
  @PostMapping("lpushx")
  public Map<String, Object> lpushx(@RequestParam("key") String key,
                                    @RequestParam("values") List<String> values) {
    Long length = redisListService.lpushx(key, values.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("values", values);
    map.put("length", length);
    return map;
  }

  /**
   * 仅当 key 存在时从右侧插入元素（key 不存在则不做任何操作）
   * POST /redisList/rpushx?key=&values=v1&values=v2
   */
  @PostMapping("rpushx")
  public Map<String, Object> rpushx(@RequestParam("key") String key,
                                    @RequestParam("values") List<String> values) {
    Long length = redisListService.rpushx(key, values.toArray(new String[0]));
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("values", values);
    map.put("length", length);
    return map;
  }

  // ==================== 位置查找 ====================

  /**
   * 查找元素在 List 中首次出现的索引位置
   * POST /redisList/lpos?key=&value=target
   */
  @PostMapping("lpos")
  public Map<String, Object> lpos(@RequestParam("key") String key,
                                  @RequestParam("value") String value) {
    Long index = redisListService.lpos(key, value);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    map.put("index", index);
    return map;
  }

  /**
   * 查找元素在 List 中出现的所有/指定范围位置（count>0 最多 N 个, rank>0 从头, rank<0 从尾）
   * POST /redisList/lposAdvanced?key=&value=target&count=0&rank=1
   */
  @PostMapping("lposAdvanced")
  public Map<String, Object> lposAdvanced(@RequestParam("key") String key,
                                          @RequestParam("value") String value,
                                          @RequestParam("count") long count,
                                          @RequestParam("rank") long rank) {
    List<Long> indices = redisListService.lpos(key, value, count, rank);
    Map<String, Object> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    map.put("count", count);
    map.put("rank", rank);
    map.put("indices", indices);
    return map;
  }

  // ==================== 定向转移 ====================

  /**
   * 从 source 弹出元素推入 destination，可控制方向
   * sourceFrom/destTo: LEFT(左/头) / RIGHT(右/尾)
   * POST /redisList/lmove?source=srcKey&destination=dstKey&sourceFrom=RIGHT&destTo=LEFT
   */
  @PostMapping("lmove")
  public Map<String, Object> lmove(@RequestParam("source") String source,
                                   @RequestParam("destination") String destination,
                                   @RequestParam("sourceFrom") String sourceFrom,
                                   @RequestParam("destTo") String destTo) {
    LMoveArgs args;
    boolean sLeft = "LEFT".equalsIgnoreCase(sourceFrom);
    boolean dLeft = "LEFT".equalsIgnoreCase(destTo);
    if (sLeft && dLeft) {
      args = LMoveArgs.Builder.leftLeft();
    } else if (sLeft && !dLeft) {
      args = LMoveArgs.Builder.leftRight();
    } else if (!sLeft && dLeft) {
      args = LMoveArgs.Builder.rightLeft();
    } else {
      args = LMoveArgs.Builder.rightRight();
    }
    String value = redisListService.lmove(source, destination, args);
    Map<String, Object> map = new HashMap<>();
    map.put("source", source);
    map.put("destination", destination);
    map.put("sourceFrom", sourceFrom);
    map.put("destTo", destTo);
    map.put("value", value);
    return map;
  }

}
