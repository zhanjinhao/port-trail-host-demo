package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.jackson.util.JacksonUtils;
import cn.addenda.component.base.util.SleepUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class HttpClient4PutTest extends AbstractHttpClient4Test {

  private static final String BASE_URL = "http://localhost:28893/porttrailhostdemo/httpTest";

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
    String url = BASE_URL + "/testPut";

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpPut httpPut = new HttpPut(url);

      Map<String, String> map = new HashMap<>();
      map.put("key", "updatedValue");
      map.put("status", "active");

      httpPut.setEntity(new StringEntity(JacksonUtils.toStr(map), org.apache.http.entity.ContentType.APPLICATION_JSON));

      try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);
      }
    }

    SleepUtils.sleep(Duration.ofMillis(10));

  }

}
