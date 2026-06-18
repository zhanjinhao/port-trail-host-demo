package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.util.SleepUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.time.Duration;

public class HttpClient4HeadTest extends AbstractHttpClient4Test {

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
    String url = BASE_URL + "/testHead";

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpHead httpHead = new HttpHead(url);

      try (CloseableHttpResponse response = httpClient.execute(httpHead)) {
        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Headers:");
        for (Header header : response.getAllHeaders()) {
          System.out.println("  " + header.getName() + ": " + header.getValue());
        }
      }
    }

    SleepUtils.sleep(Duration.ofMillis(10));

  }

}
