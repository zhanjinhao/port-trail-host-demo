package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.util.SleepUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class RedisGeoTest extends AbstractHttpClient4Test {

  private static final String GEO_BASE_URL = "http://localhost:28893/porttrailhostdemo/redisGeo";

  private static final double LON_TIANANMEN = 116.397;
  private static final double LAT_TIANANMEN = 39.909;
  private static final double LON_GUGONG = 116.403;
  private static final double LAT_GUGONG = 39.918;
  private static final double LON_BEIJINGNAN = 116.385;
  private static final double LAT_BEIJINGNAN = 39.865;
  private static final double LON_GUOMAO = 116.467;
  private static final double LAT_GUOMAO = 39.913;
  private static final double LON_XIDAN = 116.381;
  private static final double LAT_XIDAN = 39.914;
  private static final double LON_ZHONGGUANCUN = 116.326;
  private static final double LAT_ZHONGGUANCUN = 39.983;
  private static final double LON_WANGFUJING = 116.417;
  private static final double LAT_WANGFUJING = 39.915;

  private static CloseableHttpClient createHttpClient() {
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(10000)
            .build();
    return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build();
  }

  private static void executePost(String label, String url) throws Exception {
    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpPost httpPost = new HttpPost(url);
      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        System.out.println("[" + label + "] Status: " + statusCode);
        System.out.println("[" + label + "] Body: " + responseBody);
        System.out.println("--------------------------------------------------");
      }
    }
  }

  // ==================== GEOADD 数据准备 ====================

  private static void testGeoadd() throws Exception {
    executePost("GEOADD-天安门", new URIBuilder(GEO_BASE_URL + "/geoadd")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("member", "store:tiananmen").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADD-故宫", new URIBuilder(GEO_BASE_URL + "/geoadd")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_GUGONG))
            .setParameter("latitude", String.valueOf(LAT_GUGONG))
            .setParameter("member", "store:gugong").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADD-北京南站", new URIBuilder(GEO_BASE_URL + "/geoadd")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_BEIJINGNAN))
            .setParameter("latitude", String.valueOf(LAT_BEIJINGNAN))
            .setParameter("member", "store:beijingnan").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADD-国贸", new URIBuilder(GEO_BASE_URL + "/geoadd")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_GUOMAO))
            .setParameter("latitude", String.valueOf(LAT_GUOMAO))
            .setParameter("member", "store:guomao").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADD-西单", new URIBuilder(GEO_BASE_URL + "/geoadd")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_XIDAN))
            .setParameter("latitude", String.valueOf(LAT_XIDAN))
            .setParameter("member", "store:xidan").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADD-中关村", new URIBuilder(GEO_BASE_URL + "/geoadd")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_ZHONGGUANCUN))
            .setParameter("latitude", String.valueOf(LAT_ZHONGGUANCUN))
            .setParameter("member", "store:zhongguancun").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADD-王府井", new URIBuilder(GEO_BASE_URL + "/geoadd")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_WANGFUJING))
            .setParameter("latitude", String.valueOf(LAT_WANGFUJING))
            .setParameter("member", "store:wangfujing").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADD-重复添加(应返回0)", new URIBuilder(GEO_BASE_URL + "/geoadd")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("member", "store:tiananmen").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    for (String member : new String[]{"store:tiananmen", "store:gugong", "store:wangfujing"}) {
      executePost("GEOADD-geo:center准备", new URIBuilder(GEO_BASE_URL + "/geoadd")
              .setParameter("key", "geo:test:center")
              .setParameter("longitude", String.valueOf(LON_TIANANMEN))
              .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
              .setParameter("member", member).build().toString());
      SleepUtils.sleep(Duration.ofMillis(10));
    }
  }

  private static void testGeoaddMulti() throws Exception {
    executePost("GEOADDMULTI-批量添加3个", new URIBuilder(GEO_BASE_URL + "/geoaddMulti")
            .setParameter("key", "geo:test:batch")
            .setParameter("longitudes", "116.4,116.41,116.42")
            .setParameter("latitudes", "39.91,39.92,39.93")
            .setParameter("members", "store:a,store:b,store:c").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADDMULTI-验证(GEOPOS)", new URIBuilder(GEO_BASE_URL + "/geopos")
            .setParameter("key", "geo:test:batch")
            .setParameter("members", "store:a")
            .setParameter("members", "store:b")
            .setParameter("members", "store:c").build().toString());
    SleepUtils.sleep(Duration.ofMillis(20));

    executePost("GEOADDMULTI-重复批量添加(应返回0)", new URIBuilder(GEO_BASE_URL + "/geoaddMulti")
            .setParameter("key", "geo:test:batch")
            .setParameter("longitudes", "116.4,116.41")
            .setParameter("latitudes", "39.91,39.92")
            .setParameter("members", "store:a,store:b").build().toString());
  }

  // ==================== GEOPOS ====================

  private static void testGeopos() throws Exception {
    executePost("GEOPOS-查询多个已有成员", new URIBuilder(GEO_BASE_URL + "/geopos")
            .setParameter("key", "geo:test:stores")
            .setParameter("members", "store:tiananmen")
            .setParameter("members", "store:guomao")
            .setParameter("members", "store:zhongguancun").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoposWithNotExist() throws Exception {
    executePost("GEOPOS-含不存在成员(应返回null)", new URIBuilder(GEO_BASE_URL + "/geopos")
            .setParameter("key", "geo:test:stores")
            .setParameter("members", "store:tiananmen")
            .setParameter("members", "store:notexist")
            .setParameter("members", "store:xidan").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoposAllNotExist() throws Exception {
    executePost("GEOPOS-全部不存在", new URIBuilder(GEO_BASE_URL + "/geopos")
            .setParameter("key", "geo:test:stores")
            .setParameter("members", "store:a")
            .setParameter("members", "store:b").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== GEODIST ====================

  private static void testGeodistAllUnits() throws Exception {
    executePost("GEODIST-m(米)", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:tiananmen")
            .setParameter("to", "store:guomao")
            .setParameter("unit", "m").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));

    executePost("GEODIST-km(公里)", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:tiananmen")
            .setParameter("to", "store:guomao")
            .setParameter("unit", "km").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));

    executePost("GEODIST-mi(英里)", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:tiananmen")
            .setParameter("to", "store:guomao")
            .setParameter("unit", "mi").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));

    executePost("GEODIST-ft(英尺)", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:tiananmen")
            .setParameter("to", "store:guomao")
            .setParameter("unit", "ft").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeodistTriangular() throws Exception {
    executePost("GEODIST-天安门->故宫", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:tiananmen")
            .setParameter("to", "store:gugong")
            .setParameter("unit", "km").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));

    executePost("GEODIST-故宫->王府井", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:gugong")
            .setParameter("to", "store:wangfujing")
            .setParameter("unit", "km").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));

    executePost("GEODIST-天安门->王府井", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:tiananmen")
            .setParameter("to", "store:wangfujing")
            .setParameter("unit", "km").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeodistSame() throws Exception {
    executePost("GEODIST-同一点(距离=0)", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:tiananmen")
            .setParameter("to", "store:tiananmen")
            .setParameter("unit", "m").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeodistOneNotExist() throws Exception {
    executePost("GEODIST-一个不存在(应返回null)", new URIBuilder(GEO_BASE_URL + "/geodist")
            .setParameter("key", "geo:test:stores")
            .setParameter("from", "store:tiananmen")
            .setParameter("to", "store:notexist")
            .setParameter("unit", "km").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== GEOHASH ====================

  private static void testGeohash() throws Exception {
    executePost("GEOHASH-正常查询", new URIBuilder(GEO_BASE_URL + "/geohash")
            .setParameter("key", "geo:test:stores")
            .setParameter("members", "store:tiananmen")
            .setParameter("members", "store:guomao")
            .setParameter("members", "store:zhongguancun").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeohashWithNotExist() throws Exception {
    executePost("GEOHASH-含不存在成员", new URIBuilder(GEO_BASE_URL + "/geohash")
            .setParameter("key", "geo:test:stores")
            .setParameter("members", "store:tiananmen")
            .setParameter("members", "store:notexist")
            .setParameter("members", "store:xidan").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeohashSamePoint() throws Exception {
    executePost("GEOHASH-邻近点前缀验证", new URIBuilder(GEO_BASE_URL + "/geohash")
            .setParameter("key", "geo:test:stores")
            .setParameter("members", "store:tiananmen")
            .setParameter("members", "store:wangfujing").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== GEORADIUS ====================

  private static void testGeoradiusWithDistCoord() throws Exception {
    executePost("GEORADIUS-天安门5km(WITHDIST+WITHCOORD+ASC)", new URIBuilder(GEO_BASE_URL + "/georadius")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("withCoord", "true")
            .setParameter("count", "10")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusWithHash() throws Exception {
    executePost("GEORADIUS-天安门5km(WITHHASH-返回内部geohash)", new URIBuilder(GEO_BASE_URL + "/georadius")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("withHash", "true")
            .setParameter("withDist", "true")
            .setParameter("count", "5")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusWithoutOpts() throws Exception {
    executePost("GEORADIUS-天安门10km(仅成员名)", new URIBuilder(GEO_BASE_URL + "/georadius")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "10")
            .setParameter("unit", "km").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusDesc() throws Exception {
    executePost("GEORADIUS-天安门5km(DESC由远到近)", new URIBuilder(GEO_BASE_URL + "/georadius")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("withCoord", "true")
            .setParameter("sort", "desc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusCountLimit() throws Exception {
    executePost("GEORADIUS-天安门10km(COUNT=3)", new URIBuilder(GEO_BASE_URL + "/georadius")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "10")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("count", "3")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusLargeRadius() throws Exception {
    executePost("GEORADIUS-大半径全量", new URIBuilder(GEO_BASE_URL + "/georadius")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "20000")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusEmpty() throws Exception {
    executePost("GEORADIUS-上海坐标无结果", new URIBuilder(GEO_BASE_URL + "/georadius")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", "121.47")
            .setParameter("latitude", "31.23")
            .setParameter("radius", "1")
            .setParameter("unit", "km").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusCoordOnly() throws Exception {
    executePost("GEORADIUS-仅返回坐标", new URIBuilder(GEO_BASE_URL + "/georadius")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "3")
            .setParameter("unit", "km")
            .setParameter("withCoord", "true")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== GEORADIUSBYMEMBER ====================

  private static void testGeoradiusbymemberFull() throws Exception {
    executePost("GEORADIUSBYMEMBER-天安门3km(全选项)", new URIBuilder(GEO_BASE_URL + "/georadiusbymember")
            .setParameter("key", "geo:test:stores")
            .setParameter("member", "store:tiananmen")
            .setParameter("radius", "3")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("withCoord", "true")
            .setParameter("count", "5")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusbymemberWithHash() throws Exception {
    executePost("GEORADIUSBYMEMBER-天安门3km(WITHHASH)", new URIBuilder(GEO_BASE_URL + "/georadiusbymember")
            .setParameter("key", "geo:test:stores")
            .setParameter("member", "store:tiananmen")
            .setParameter("radius", "3")
            .setParameter("unit", "km")
            .setParameter("withHash", "true")
            .setParameter("withDist", "true")
            .setParameter("count", "5")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusbymemberDescCount() throws Exception {
    executePost("GEORADIUSBYMEMBER-国贸3km(DESC+COUNT2)", new URIBuilder(GEO_BASE_URL + "/georadiusbymember")
            .setParameter("key", "geo:test:stores")
            .setParameter("member", "store:guomao")
            .setParameter("radius", "3")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("count", "2")
            .setParameter("sort", "desc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusbymemberLargeRadius() throws Exception {
    executePost("GEORADIUSBYMEMBER-中关村20km大范围", new URIBuilder(GEO_BASE_URL + "/georadiusbymember")
            .setParameter("key", "geo:test:stores")
            .setParameter("member", "store:zhongguancun")
            .setParameter("radius", "20")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== GEORADIUSSTORE ====================

  private static void testGeoradiusstore() throws Exception {
    executePost("GEORADIUSSTORE-存储(storeDist=false)", new URIBuilder(GEO_BASE_URL + "/georadiusstore")
            .setParameter("key", "geo:test:stores")
            .setParameter("destination", "geo:test:store1")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("count", "5")
            .setParameter("sort", "asc")
            .setParameter("storeDist", "false").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusstoreWithDist() throws Exception {
    executePost("GEORADIUSSTORE-距离存为score", new URIBuilder(GEO_BASE_URL + "/georadiusstore")
            .setParameter("key", "geo:test:stores")
            .setParameter("destination", "geo:test:store2")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("count", "5")
            .setParameter("sort", "asc")
            .setParameter("storeDist", "true").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== GEORADIUSBYMEMBERSTORE ====================

  private static void testGeoradiusbymemberstore() throws Exception {
    executePost("GEORADIUSBYMEMBERSTORE-存储", new URIBuilder(GEO_BASE_URL + "/georadiusbymemberstore")
            .setParameter("key", "geo:test:stores")
            .setParameter("destination", "geo:test:store3")
            .setParameter("member", "store:tiananmen")
            .setParameter("radius", "3")
            .setParameter("unit", "km")
            .setParameter("count", "5")
            .setParameter("sort", "asc")
            .setParameter("storeDist", "false").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeoradiusbymemberstoreWithDist() throws Exception {
    executePost("GEORADIUSBYMEMBERSTORE-距离存为score", new URIBuilder(GEO_BASE_URL + "/georadiusbymemberstore")
            .setParameter("key", "geo:test:stores")
            .setParameter("destination", "geo:test:store4")
            .setParameter("member", "store:tiananmen")
            .setParameter("radius", "3")
            .setParameter("unit", "km")
            .setParameter("count", "5")
            .setParameter("sort", "asc")
            .setParameter("storeDist", "true").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== GEOSEARCH ====================

  private static void testGeosearchByCoord() throws Exception {
    executePost("GEOSEARCH-西单5km", new URIBuilder(GEO_BASE_URL + "/geosearch")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_XIDAN))
            .setParameter("latitude", String.valueOf(LAT_XIDAN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("withCoord", "true")
            .setParameter("count", "10")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchWithHash() throws Exception {
    executePost("GEOSEARCH-天安门5km(WITHHASH)", new URIBuilder(GEO_BASE_URL + "/geosearch")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("withHash", "true")
            .setParameter("withDist", "true")
            .setParameter("count", "5")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchDesc() throws Exception {
    executePost("GEOSEARCH-天安门5km(DESC)", new URIBuilder(GEO_BASE_URL + "/geosearch")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("sort", "desc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchByMember() throws Exception {
    executePost("GEOSEARCH_BY_MEMBER-国贸5km", new URIBuilder(GEO_BASE_URL + "/geosearchByMember")
            .setParameter("key", "geo:test:stores")
            .setParameter("member", "store:guomao")
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("withCoord", "true")
            .setParameter("count", "5")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchByMemberWithHash() throws Exception {
    executePost("GEOSEARCH_BY_MEMBER-国贸5km(WITHHASH)", new URIBuilder(GEO_BASE_URL + "/geosearchByMember")
            .setParameter("key", "geo:test:stores")
            .setParameter("member", "store:guomao")
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("withHash", "true")
            .setParameter("withDist", "true")
            .setParameter("count", "5")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchByMemberLargeRadius() throws Exception {
    executePost("GEOSEARCH_BY_MEMBER-王府井15km", new URIBuilder(GEO_BASE_URL + "/geosearchByMember")
            .setParameter("key", "geo:test:stores")
            .setParameter("member", "store:wangfujing")
            .setParameter("radius", "15")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchByBox() throws Exception {
    executePost("GEOSEARCH_BY_BOX-矩形区域", new URIBuilder(GEO_BASE_URL + "/geosearchByBox")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(116.35))
            .setParameter("latitude", String.valueOf(39.85))
            .setParameter("width", "15")
            .setParameter("height", "10")
            .setParameter("unit", "km")
            .setParameter("withCoord", "true")
            .setParameter("count", "20")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchByBoxWithDist() throws Exception {
    executePost("GEOSEARCH_BY_BOX-含距离排序", new URIBuilder(GEO_BASE_URL + "/geosearchByBox")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(116.35))
            .setParameter("latitude", String.valueOf(39.85))
            .setParameter("width", "20")
            .setParameter("height", "20")
            .setParameter("unit", "km")
            .setParameter("withDist", "true")
            .setParameter("withCoord", "true")
            .setParameter("count", "10")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchByBoxWithHash() throws Exception {
    executePost("GEOSEARCH_BY_BOX-矩形区域(WITHHASH)", new URIBuilder(GEO_BASE_URL + "/geosearchByBox")
            .setParameter("key", "geo:test:stores")
            .setParameter("longitude", String.valueOf(116.35))
            .setParameter("latitude", String.valueOf(39.85))
            .setParameter("width", "20")
            .setParameter("height", "20")
            .setParameter("unit", "km")
            .setParameter("withHash", "true")
            .setParameter("withDist", "true")
            .setParameter("sort", "asc").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== GEOSEARCHSTORE ====================

  private static void testGeosearchstore() throws Exception {
    executePost("GEOSEARCHSTORE-存储成员名", new URIBuilder(GEO_BASE_URL + "/geosearchstore")
            .setParameter("key", "geo:test:stores")
            .setParameter("destination", "geo:test:search_store1")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("count", "10")
            .setParameter("sort", "asc")
            .setParameter("storeDist", "false").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  private static void testGeosearchstoreWithDist() throws Exception {
    executePost("GEOSEARCHSTORE-存储距离", new URIBuilder(GEO_BASE_URL + "/geosearchstore")
            .setParameter("key", "geo:test:stores")
            .setParameter("destination", "geo:test:search_store2")
            .setParameter("longitude", String.valueOf(LON_TIANANMEN))
            .setParameter("latitude", String.valueOf(LAT_TIANANMEN))
            .setParameter("radius", "5")
            .setParameter("unit", "km")
            .setParameter("count", "5")
            .setParameter("sort", "asc")
            .setParameter("storeDist", "true").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));
  }

  // ==================== 综合验证 ====================

  private static void testVerifyStoredResults() throws Exception {
    System.out.println("=== 验证 STORE 结果 ===");
    executePost("验证-geo:test:store1", new URIBuilder(GEO_BASE_URL + "/geopos")
            .setParameter("key", "geo:test:store1")
            .setParameter("members", "store:tiananmen")
            .setParameter("members", "store:gugong").build().toString());
    SleepUtils.sleep(Duration.ofMillis(10));

    executePost("验证-geo:test:search_store1", new URIBuilder(GEO_BASE_URL + "/geopos")
            .setParameter("key", "geo:test:search_store1")
            .setParameter("members", "store:tiananmen")
            .setParameter("members", "store:gugong").build().toString());
  }

  // ==================== main ====================

  public static void main(String[] args) throws Exception {
    System.out.println("========== Redis Geo API 测试开始 ==========\n");

    System.out.println("--- Phase 1: 数据准备 ---");
    testGeoadd();
    testGeoaddMulti();

    System.out.println("\n--- Phase 2: GEOPOS 位置查询 ---");
    testGeopos();
    testGeoposWithNotExist();
    testGeoposAllNotExist();

    System.out.println("\n--- Phase 3: GEODIST 距离计算 ---");
    testGeodistAllUnits();
    testGeodistTriangular();
    testGeodistSame();
    testGeodistOneNotExist();

    System.out.println("\n--- Phase 4: GEOHASH 编码 ---");
    testGeohash();
    testGeohashWithNotExist();
    testGeohashSamePoint();

    System.out.println("\n--- Phase 5: GEORADIUS 半径搜索 ---");
    testGeoradiusWithDistCoord();
    testGeoradiusWithHash();
    testGeoradiusWithoutOpts();
    testGeoradiusDesc();
    testGeoradiusCountLimit();
    testGeoradiusLargeRadius();
    testGeoradiusEmpty();
    testGeoradiusCoordOnly();

    System.out.println("\n--- Phase 6: GEORADIUSBYMEMBER 成员搜索 ---");
    testGeoradiusbymemberFull();
    testGeoradiusbymemberWithHash();
    testGeoradiusbymemberDescCount();
    testGeoradiusbymemberLargeRadius();

    System.out.println("\n--- Phase 7: GEORADIUSSTORE 存储 ---");
    testGeoradiusstore();
    testGeoradiusstoreWithDist();

    System.out.println("\n--- Phase 8: GEORADIUSBYMEMBERSTORE 成员存储 ---");
    testGeoradiusbymemberstore();
    testGeoradiusbymemberstoreWithDist();

    System.out.println("\n--- Phase 9: GEOSEARCH 新版搜索 ---");
    testGeosearchByCoord();
    testGeosearchWithHash();
    testGeosearchDesc();
    testGeosearchByMember();
    testGeosearchByMemberWithHash();
    testGeosearchByMemberLargeRadius();
    testGeosearchByBox();
    testGeosearchByBoxWithDist();
    testGeosearchByBoxWithHash();

    System.out.println("\n--- Phase 10: GEOSEARCHSTORE 新版存储 ---");
    testGeosearchstore();
    testGeosearchstoreWithDist();

    System.out.println("\n--- Phase 11: 综合验证 ---");
    testVerifyStoredResults();

    System.out.println("\n========== Redis Geo API 测试结束 ==========");
  }
}
