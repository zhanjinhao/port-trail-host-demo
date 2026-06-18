package cn.addenda.porttrail.hostdemo.test.httpclient4;

import cn.addenda.component.base.util.SleepUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

public class HttpClient4DownloadTest extends AbstractHttpClient4Test {

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
    String url = BASE_URL + "/testDownload?nodeType=1";

    Path targetTestClassesDir = Paths.get("target", "test-classes");
    if (!Files.exists(targetTestClassesDir)) {
      Files.createDirectories(targetTestClassesDir);
    }

    try (CloseableHttpClient httpClient = createHttpClient()) {
      HttpGet httpGet = new HttpGet(url);

      try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Status Code: " + statusCode);

        HttpEntity entity = response.getEntity();
        if (entity == null) {
          System.out.println("Download failed: no entity in response.");
          return;
        }

        byte[] fileBytes = EntityUtils.toByteArray(entity);

        String contentDisposition = null;
        Header cdHeader = response.getFirstHeader("Content-disposition");
        if (cdHeader != null) {
          contentDisposition = cdHeader.getValue();
        }
        String fileName = extractFileName(contentDisposition);
        if (fileName == null) {
          fileName = "downloaded_file_" + UUID.randomUUID().toString().substring(0, 8);
        }

        Path destPath = targetTestClassesDir.resolve(fileName);
        try (FileOutputStream fos = new FileOutputStream(destPath.toFile())) {
          fos.write(fileBytes);
          fos.flush();
        }

        System.out.println("Download complete. File saved to: " + destPath.toAbsolutePath());
        System.out.println("File size: " + fileBytes.length + " bytes");
      }
    }

    SleepUtils.sleep(Duration.ofMillis(10));

  }

  private static String extractFileName(String contentDisposition) {
    if (contentDisposition == null) {
      return null;
    }
    for (String part : contentDisposition.split(";")) {
      part = part.trim();
      if (part.toLowerCase().startsWith("filename=")) {
        String fileName = part.substring("filename=".length());
        if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
          fileName = fileName.substring(1, fileName.length() - 1);
        }
        try {
          fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
          // 解码失败则使用原始值
        }
        return fileName;
      }
    }
    return null;
  }

}
