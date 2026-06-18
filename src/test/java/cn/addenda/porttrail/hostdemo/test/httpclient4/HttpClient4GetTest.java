package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.util.SleepUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class HttpClient4GetTest extends AbstractHttpClient4Test {

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
    String url = BASE_URL + "/testGet?id=100&name=hello";

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpGet httpGet = new HttpGet(url);

      try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);
      }
    }

    SleepUtils.sleep(Duration.ofMillis(10));

  }

}
