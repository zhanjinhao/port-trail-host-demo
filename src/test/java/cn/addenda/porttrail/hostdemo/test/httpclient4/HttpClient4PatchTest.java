package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.jackson.util.JacksonUtils;
import cn.addenda.component.base.util.SleepUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class HttpClient4PatchTest extends AbstractHttpClient4Test {

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
    String url = BASE_URL + "/testPatch";

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpPatch httpPatch = new HttpPatch(url);

      Map<String, String> map = new HashMap<>();
      map.put("field", "partialUpdate");
      map.put("status", "active");

      httpPatch.setEntity(new StringEntity(JacksonUtils.toStr(map), org.apache.http.entity.ContentType.APPLICATION_JSON));

      try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);
      }
    }

    SleepUtils.sleep(Duration.ofMillis(10));

  }


}
