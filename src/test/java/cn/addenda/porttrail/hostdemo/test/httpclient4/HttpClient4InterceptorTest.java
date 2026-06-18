package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.jackson.util.JacksonUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HttpClient4InterceptorTest {

  private static final String BASE_URL = "http://localhost:28893/porttrailhostdemo/httpTest";

  private static final String REQUEST_ID_KEY = "request_id";

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private static CloseableHttpClient createHttpClient() {
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(10000)
            .build();

    return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            // addFirst是将interceptor放在暂存队列的头，对暂存队列循环加入到执行队列时，调用的也是addFirst
            .addInterceptorFirst(new HttpRequestInterceptor1())
            .addInterceptorFirst(new HttpRequestInterceptor2())
            .addInterceptorFirst(new HttpResponseInterceptor1())
            .addInterceptorFirst(new HttpResponseInterceptor2())
            .build();
  }

  public static void main(String[] args) throws IOException {
    String url = BASE_URL + "/testRequestParamAndResponseBody?nodeType=1";

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpPost httpPost = new HttpPost(url);

      Map<String, String> map = new HashMap<>();
      map.put("a", "1");
      map.put("b", "2");

      httpPost.setEntity(new StringEntity(JacksonUtils.toStr(map), org.apache.http.entity.ContentType.APPLICATION_JSON));

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);
      }
    }
  }

  private static class HttpRequestInterceptor1 implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      System.out.println("request 1");
    }
  }

  private static class HttpRequestInterceptor2 implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      System.out.println("request 2");
    }
  }

  private static class HttpResponseInterceptor1 implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
      System.out.println("response 1");
    }
  }

  private static class HttpResponseInterceptor2 implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
      System.out.println("response 2");
    }
  }

}
