package cn.addenda.porttrail.hostdemo.test.lettuce;

import cn.addenda.porttrail.hostdemo.PortTrailHostDemoApplication;
import cn.addenda.porttrail.hostdemo.helper.RedisStringHelper;
import cn.addenda.porttrail.hostdemo.helper.RedisTestHelper;
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
  private RedisTestHelper redisTestHelper;

  @Resource
  private RedisStringHelper redisStringHelper;

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
      redisStringHelper.set(TEST_KEY, TEST_VALUE);
    }, "SET命令应该成功执行");

    String actualValue = redisStringHelper.get(TEST_KEY);
    assertEquals(TEST_VALUE, actualValue, "SET的值应该能够通过GET获取");
  }

  @Test
  @Order(2)
  @DisplayName("测试GET命令")
  public void testGet() {
    String value = redisStringHelper.get(TEST_KEY);
    assertNotNull(value, "GET返回的值不应该为null");
    assertEquals(TEST_VALUE, value, "GET返回的值应该与SET的值一致");

    String nonExistentValue = redisStringHelper.get("non:existent:key");
    assertNull(nonExistentValue, "不存在的key应该返回null");
  }

  @Test
  @Order(3)
  @DisplayName("测试DEL命令")
  public void testDel() {
    String tempKey = "test:del:key";
    redisStringHelper.set(tempKey, "temp_value");

    String valueBeforeDel = redisStringHelper.get(tempKey);
    assertNotNull(valueBeforeDel, "删除前key应该存在");

    Long deletedCount = redisStringHelper.del(tempKey);
    assertEquals(1, deletedCount.longValue(), "应该删除1个key");

    String valueAfterDel = redisStringHelper.get(tempKey);
    assertNull(valueAfterDel, "删除后key应该不存在");

    Long deleteNonExistent = redisStringHelper.del("non:existent:key");
    assertEquals(0, deleteNonExistent.longValue(), "删除不存在的key应该返回0");
  }

  @Test
  @Order(4)
  @DisplayName("测试HSET和HGET命令")
  public void testHSetAndHGet() {
    assertDoesNotThrow(() -> {
      redisTestHelper.hset(TEST_HASH_KEY, TEST_FIELD_1, "value1");
      redisTestHelper.hset(TEST_HASH_KEY, TEST_FIELD_2, "value2");
    }, "HSET命令应该成功执行");

    String value1 = redisTestHelper.hget(TEST_HASH_KEY, TEST_FIELD_1);
    assertEquals("value1", value1, "HGET应该返回正确的值");

    String value2 = redisTestHelper.hget(TEST_HASH_KEY, TEST_FIELD_2);
    assertEquals("value2", value2, "HGET应该返回正确的值");

    String nonExistentField = redisTestHelper.hget(TEST_HASH_KEY, "non_existent_field");
    assertNull(nonExistentField, "不存在的field应该返回null");
  }

  @Test
  @Order(5)
  @DisplayName("测试HGETALL命令")
  public void testHGetAll() {
    Map<String, String> allFields = redisTestHelper.hgetall(TEST_HASH_KEY);
    assertNotNull(allFields, "HGETALL返回的map不应该为null");
    assertEquals(2, allFields.size(), "Hash应该包含2个field");
    assertEquals("value1", allFields.get(TEST_FIELD_1));
    assertEquals("value2", allFields.get(TEST_FIELD_2));
  }

  @Test
  @Order(6)
  @DisplayName("测试HEXISTS命令")
  public void testHExists() {
    Boolean exists1 = redisTestHelper.hexists(TEST_HASH_KEY, TEST_FIELD_1);
    assertTrue(exists1, "存在的field应该返回true");

    Boolean exists2 = redisTestHelper.hexists(TEST_HASH_KEY, "non_existent_field");
    assertFalse(exists2, "不存在的field应该返回false");
  }

  @Test
  @Order(7)
  @DisplayName("测试HLEN命令")
  public void testHLen() {
    Long length = redisTestHelper.hlen(TEST_HASH_KEY);
    assertEquals(2, length.longValue(), "Hash的长度应该是2");
  }

  @Test
  @Order(8)
  @DisplayName("测试HDEL命令")
  public void testHDel() {
    redisTestHelper.hset(TEST_HASH_KEY, TEST_FIELD_3, "value3");

    Long lengthBeforeDel = redisTestHelper.hlen(TEST_HASH_KEY);
    assertEquals(3, lengthBeforeDel.longValue(), "删除前应该有3个field");

    Long deletedCount = redisTestHelper.hdel(TEST_HASH_KEY, TEST_FIELD_3);
    assertEquals(1, deletedCount.longValue(), "应该删除1个field");

    Long lengthAfterDel = redisTestHelper.hlen(TEST_HASH_KEY);
    assertEquals(2, lengthAfterDel.longValue(), "删除后应该有2个field");

    Boolean exists = redisTestHelper.hexists(TEST_HASH_KEY, TEST_FIELD_3);
    assertFalse(exists, "删除后的field应该不存在");

    Long deleteNonExistent = redisTestHelper.hdel(TEST_HASH_KEY, "non_existent_field");
    assertEquals(0, deleteNonExistent.longValue(), "删除不存在的field应该返回0");
  }

  @Test
  @Order(9)
  @DisplayName("测试批量DEL命令")
  public void testBatchDel() {
    String key1 = "test:batch:del:1";
    String key2 = "test:batch:del:2";
    String key3 = "test:batch:del:3";

    redisStringHelper.set(key1, "value1");
    redisStringHelper.set(key2, "value2");
    redisStringHelper.set(key3, "value3");

    Long deletedCount = redisStringHelper.del(key1, key2, key3);
    assertEquals(3, deletedCount.longValue(), "应该删除3个key");

    assertNull(redisStringHelper.get(key1), "key1应该被删除");
    assertNull(redisStringHelper.get(key2), "key2应该被删除");
    assertNull(redisStringHelper.get(key3), "key3应该被删除");
  }

  @Test
  @Order(10)
  @DisplayName("测试多个Hash操作")
  public void testMultipleHashOperations() {
    String hashKey = "test:user:info";

    redisTestHelper.hset(hashKey, "name", "张三");
    redisTestHelper.hset(hashKey, "age", "25");
    redisTestHelper.hset(hashKey, "city", "北京");
    redisTestHelper.hset(hashKey, "email", "zhangsan@example.com");

    Map<String, String> userInfo = redisTestHelper.hgetall(hashKey);
    assertNotNull(userInfo, "用户信息不应该为null");
    assertEquals(4, userInfo.size(), "用户信息应该有4个字段");
    assertEquals("张三", userInfo.get("name"));
    assertEquals("25", userInfo.get("age"));
    assertEquals("北京", userInfo.get("city"));
    assertEquals("zhangsan@example.com", userInfo.get("email"));

    Long fieldCount = redisTestHelper.hlen(hashKey);
    assertEquals(4, fieldCount.longValue(), "Hash长度应该是4");

    redisTestHelper.hdel(hashKey, "email");
    assertFalse(redisTestHelper.hexists(hashKey, "email"), "email字段应该被删除");
    assertEquals(3, redisTestHelper.hlen(hashKey).longValue(), "删除后Hash长度应该是3");

    redisStringHelper.del(hashKey);
    assertNull(redisTestHelper.hgetall(hashKey), "删除hash后应该返回null或空map");
  }

  @AfterAll
  public static void cleanup() {
    System.out.println("Redis Cluster 测试完成");
  }
}
