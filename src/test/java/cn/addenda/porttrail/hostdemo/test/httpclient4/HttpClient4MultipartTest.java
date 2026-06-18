package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.util.SleepUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class HttpClient4MultipartTest extends AbstractHttpClient4Test {

  private static CloseableHttpClient createHttpClient() {
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(10000)
            .build();

    return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build();
  }

  private static File getImageFile() {
    String fileName = "static/雪地里的北极熊妈妈和孩子_彼岸图网.jpg";
    URL resource = HttpClient4MultipartTest.class.getClassLoader().getResource(fileName);
    if (resource == null) {
      throw new RuntimeException("文件不存在: " + fileName);
    }
    try {
      return new File(resource.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws IOException {
    String url = BASE_URL + "/testMultipartFormData";

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpPost httpPost = new HttpPost(url);

      // 构建 multipart/form-data 实体
      File imageFile = getImageFile();
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      // 这里决定文件名的charset
      builder.setCharset(StandardCharsets.UTF_8);
      builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      builder.addBinaryBody("file", imageFile, ContentType.IMAGE_JPEG, imageFile.getName());
      builder.addTextBody("nodeType", "啊", ContentType.create("text/plain", "utf-8"));

      httpPost.setEntity(builder.build());

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
