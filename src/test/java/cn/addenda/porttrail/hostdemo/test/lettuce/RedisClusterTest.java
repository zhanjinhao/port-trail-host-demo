package cn.addenda.porttrail.hostdemo.test.lettuce;

import cn.addenda.porttrail.hostdemo.PortTrailHostDemoApplication;
import cn.addenda.porttrail.hostdemo.helper.RedisHelper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PortTrailHostDemoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Redis Cluster 功能测试")
public class RedisClusterTest {

  @Resource
  private RedisHelper redisHelper;

  private static final String TEST_KEY = "test:string:key";
  private static final String TEST_VALUE = "test_value_123";
  private static final String TEST_HASH_KEY = "test:hash:key";
  private static final String TEST_FIELD_1 = "field1";
  private static final String TEST_FIELD_2 = "field2";
  private static final String TEST_FIELD_3 = "field3";

  @Test
  @Order(1)
  @DisplayName("测试SET命令")
  public void testSet() {
    assertDoesNotThrow(() -> {
      redisHelper.set(TEST_KEY, TEST_VALUE);
    }, "SET命令应该成功执行");

    String actualValue = redisHelper.get(TEST_KEY);
    assertEquals(TEST_VALUE, actualValue, "SET的值应该能够通过GET获取");
  }

  @Test
  @Order(2)
  @DisplayName("测试GET命令")
  public void testGet() {
    String value = redisHelper.get(TEST_KEY);
    assertNotNull(value, "GET返回的值不应该为null");
    assertEquals(TEST_VALUE, value, "GET返回的值应该与SET的值一致");

    String nonExistentValue = redisHelper.get("non:existent:key");
    assertNull(nonExistentValue, "不存在的key应该返回null");
  }

  @Test
  @Order(3)
  @DisplayName("测试DEL命令")
  public void testDel() {
    String tempKey = "test:del:key";
    redisHelper.set(tempKey, "temp_value");

    String valueBeforeDel = redisHelper.get(tempKey);
    assertNotNull(valueBeforeDel, "删除前key应该存在");

    Long deletedCount = redisHelper.del(tempKey);
    assertEquals(1, deletedCount.longValue(), "应该删除1个key");

    String valueAfterDel = redisHelper.get(tempKey);
    assertNull(valueAfterDel, "删除后key应该不存在");

    Long deleteNonExistent = redisHelper.del("non:existent:key");
    assertEquals(0, deleteNonExistent.longValue(), "删除不存在的key应该返回0");
  }

  @Test
  @Order(4)
  @DisplayName("测试HSET和HGET命令")
  public void testHSetAndHGet() {
    assertDoesNotThrow(() -> {
      redisHelper.hset(TEST_HASH_KEY, TEST_FIELD_1, "value1");
      redisHelper.hset(TEST_HASH_KEY, TEST_FIELD_2, "value2");
    }, "HSET命令应该成功执行");

    String value1 = redisHelper.hget(TEST_HASH_KEY, TEST_FIELD_1);
    assertEquals("value1", value1, "HGET应该返回正确的值");

    String value2 = redisHelper.hget(TEST_HASH_KEY, TEST_FIELD_2);
    assertEquals("value2", value2, "HGET应该返回正确的值");

    String nonExistentField = redisHelper.hget(TEST_HASH_KEY, "non_existent_field");
    assertNull(nonExistentField, "不存在的field应该返回null");
  }

  @Test
  @Order(5)
  @DisplayName("测试HGETALL命令")
  public void testHGetAll() {
    Map<String, String> allFields = redisHelper.hgetall(TEST_HASH_KEY);
    assertNotNull(allFields, "HGETALL返回的map不应该为null");
    assertEquals(2, allFields.size(), "Hash应该包含2个field");
    assertEquals("value1", allFields.get(TEST_FIELD_1));
    assertEquals("value2", allFields.get(TEST_FIELD_2));
  }

  @Test
  @Order(6)
  @DisplayName("测试HEXISTS命令")
  public void testHExists() {
    Boolean exists1 = redisHelper.hexists(TEST_HASH_KEY, TEST_FIELD_1);
    assertTrue(exists1, "存在的field应该返回true");

    Boolean exists2 = redisHelper.hexists(TEST_HASH_KEY, "non_existent_field");
    assertFalse(exists2, "不存在的field应该返回false");
  }

  @Test
  @Order(7)
  @DisplayName("测试HLEN命令")
  public void testHLen() {
    Long length = redisHelper.hlen(TEST_HASH_KEY);
    assertEquals(2, length.longValue(), "Hash的长度应该是2");
  }

  @Test
  @Order(8)
  @DisplayName("测试HDEL命令")
  public void testHDel() {
    redisHelper.hset(TEST_HASH_KEY, TEST_FIELD_3, "value3");

    Long lengthBeforeDel = redisHelper.hlen(TEST_HASH_KEY);
    assertEquals(3, lengthBeforeDel.longValue(), "删除前应该有3个field");

    Long deletedCount = redisHelper.hdel(TEST_HASH_KEY, TEST_FIELD_3);
    assertEquals(1, deletedCount.longValue(), "应该删除1个field");

    Long lengthAfterDel = redisHelper.hlen(TEST_HASH_KEY);
    assertEquals(2, lengthAfterDel.longValue(), "删除后应该有2个field");

    Boolean exists = redisHelper.hexists(TEST_HASH_KEY, TEST_FIELD_3);
    assertFalse(exists, "删除后的field应该不存在");

    Long deleteNonExistent = redisHelper.hdel(TEST_HASH_KEY, "non_existent_field");
    assertEquals(0, deleteNonExistent.longValue(), "删除不存在的field应该返回0");
  }

  @Test
  @Order(9)
  @DisplayName("测试批量DEL命令")
  public void testBatchDel() {
    String key1 = "test:batch:del:1";
    String key2 = "test:batch:del:2";
    String key3 = "test:batch:del:3";

    redisHelper.set(key1, "value1");
    redisHelper.set(key2, "value2");
    redisHelper.set(key3, "value3");

    Long deletedCount = redisHelper.del(key1, key2, key3);
    assertEquals(3, deletedCount.longValue(), "应该删除3个key");

    assertNull(redisHelper.get(key1), "key1应该被删除");
    assertNull(redisHelper.get(key2), "key2应该被删除");
    assertNull(redisHelper.get(key3), "key3应该被删除");
  }

  @Test
  @Order(10)
  @DisplayName("测试多个Hash操作")
  public void testMultipleHashOperations() {
    String hashKey = "test:user:info";

    redisHelper.hset(hashKey, "name", "张三");
    redisHelper.hset(hashKey, "age", "25");
    redisHelper.hset(hashKey, "city", "北京");
    redisHelper.hset(hashKey, "email", "zhangsan@example.com");

    Map<String, String> userInfo = redisHelper.hgetall(hashKey);
    assertNotNull(userInfo, "用户信息不应该为null");
    assertEquals(4, userInfo.size(), "用户信息应该有4个字段");
    assertEquals("张三", userInfo.get("name"));
    assertEquals("25", userInfo.get("age"));
    assertEquals("北京", userInfo.get("city"));
    assertEquals("zhangsan@example.com", userInfo.get("email"));

    Long fieldCount = redisHelper.hlen(hashKey);
    assertEquals(4, fieldCount.longValue(), "Hash长度应该是4");

    redisHelper.hdel(hashKey, "email");
    assertFalse(redisHelper.hexists(hashKey, "email"), "email字段应该被删除");
    assertEquals(3, redisHelper.hlen(hashKey).longValue(), "删除后Hash长度应该是3");

    redisHelper.del(hashKey);
    assertNull(redisHelper.hgetall(hashKey), "删除hash后应该返回null或空map");
  }

  @AfterAll
  public static void cleanup() {
    System.out.println("Redis Cluster 测试完成");
  }
}
