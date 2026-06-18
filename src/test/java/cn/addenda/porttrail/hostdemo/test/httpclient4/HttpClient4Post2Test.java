package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.jackson.util.JacksonUtils;
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
import java.util.HashMap;
import java.util.Map;

public class HttpClient4Post2Test extends AbstractHttpClient4Test {

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
    String url = BASE_URL + "/testRequestParamAndResponseBody?nodeType=1";

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpPost httpPost = new HttpPost(url);

      Map<String, String> map = new HashMap<>();
      map.put("a", "1");
      map.put("b", "2");

      String jsonBody = JacksonUtils.toStr(map);

      byte[] gzipCompressed = gzipCompress(jsonBody.getBytes(StandardCharsets.UTF_8));

      org.apache.http.entity.ByteArrayEntity entity = new org.apache.http.entity.ByteArrayEntity(
              gzipCompressed,
              ContentType.APPLICATION_JSON
      );
      entity.setContentEncoding("gzip");

      httpPost.setEntity(entity);

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);
      }
    }

    SleepUtils.sleep(Duration.ofMillis(10));

  }

}
