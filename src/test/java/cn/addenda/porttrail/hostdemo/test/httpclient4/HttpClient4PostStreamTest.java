package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.util.SleepUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

public class HttpClient4PostStreamTest extends AbstractHttpClient4Test {

  private static CloseableHttpClient createHttpClient() {
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(10000)
            .build();

    return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build();
  }

  public static void main(String[] args) throws IOException {
    String url = BASE_URL + "/testPostStream";

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpPost httpPost = new HttpPost(url);

      String testData = "这是一段测试数据，用于测试流式传输。\n包含中文和换行符。\n" +
              "TestData: " + UUID.randomUUID();

      byte[] gzipCompressed = gzipCompress(testData.getBytes(StandardCharsets.UTF_8));

      org.apache.http.entity.ByteArrayEntity entity = new org.apache.http.entity.ByteArrayEntity(
              gzipCompressed,
              ContentType.APPLICATION_OCTET_STREAM
      );
      entity.setContentEncoding("gzip");

      httpPost.setEntity(entity);

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);
      }
    } catch (Exception e) {
      System.err.println("请求失败: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    SleepUtils.sleep(Duration.ofSeconds(10));
  }

}
