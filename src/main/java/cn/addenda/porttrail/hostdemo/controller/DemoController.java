package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.DemoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

  @Resource
  private DemoService demoService;

//  @GetMapping("query")
//  public List<Workflow> query(@RequestParam("nodeType") String nodeType) {
//    executeUpdate();
//    return helloService.query(nodeType);
//  }

  /**
   * request: application/json
   * response: application/json
   */
  @PostMapping("testOnlyRequestParam")
  public Map<String, String> testOnlyRequestParam(@RequestParam("nodeType") String nodeType) {
    executeUpdate();
    demoService.update(nodeType);
    Map<String, String> map = new HashMap<>();
    map.put("testOnlyRequestParam", "testOnlyRequestParam");
    return map;
  }

  /**
   * request: application/json
   * response: application/json
   */
  @PostMapping("testRequestParamAndResponseBody")
  public Map<String, String> testRequestParamAndResponseBody(@RequestParam("nodeType") String nodeType, @RequestBody Map<String, String> postParam) {
    executeUpdate();
    System.out.println(postParam);
    demoService.update(nodeType);
    Map<String, String> map = new HashMap<>();
    map.put("testRequestParamAndResponseBody", "testRequestParamAndResponseBody");
    return map;
  }

  /**
   * request:application/x-www-form-urlencoded
   * response: application/json
   */
  @PostMapping(value = "testFormUrlencoded", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public Map<String, String> testFormUrlencoded(@RequestParam("nodeType") String nodeType, @RequestParam("f1") String f1) {
    executeUpdate();
    System.out.println("f1=" + f1);
    demoService.update(nodeType);
    Map<String, String> map = new HashMap<>();
    map.put("testFormUrlencoded", "testFormUrlencoded");
    return map;
  }

  /**
   * request:multipart/form-data
   * response: application/json
   */
  @PostMapping(value = "testMultipartFormData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Map<String, String> testMultipartFormData(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("nodeType") String nodeType) {
    System.out.println("file=" + file.getName() + ",filename=" + file.getOriginalFilename() + ",size=" + file.getSize());
    // 将文件写入 resources/static 目录
    try {
      File dest = new File(DemoController.class.getClassLoader().getResource("").toURI().getPath(), file.getOriginalFilename());
      file.transferTo(dest);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // 处理文件和文本参数
    demoService.update(nodeType);
    Map<String, String> map = new HashMap<>();
    map.put("testMultipartFormDatas", "testMultipartFormData");
    return map;
  }

  /**
   * request: application/json
   * response: application/octet-stream
   */
  @GetMapping(value = "/testDownload")
  public void testDownload(@RequestParam("nodeType") String nodeType, HttpServletResponse response) {
    String fileName = "雪地里的北极熊妈妈和孩子_彼岸图网.jpg";
    String fileLocation = "static/" + fileName;

    try {
      URL resource = DemoController.class.getClassLoader().getResource(fileLocation);
      if (resource == null) {
        throw new IllegalArgumentException(String.format("文件[%s]不存在", fileName));
      }
      URI uri = resource.toURI();
      File file = new File(uri);
      if (file.isDirectory()) {
        throw new IllegalArgumentException("目录不可下载");
      }

      try (InputStream fis = Files.newInputStream(file.toPath());
           BufferedInputStream bis = new BufferedInputStream(fis);
           // OutputStream 是文件写出流，讲文件下载到浏览器客户端
           OutputStream os = response.getOutputStream()) {
        // 设置 response 的下载响应头
//        response.reset();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.setContentType("application/octet-stream");
        // 注意，这里要设置文件名的编码，否则中文的文件名下载后不显示
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
        response.setContentLength((int) file.length());

        byte[] buffer = new byte[1024]; // 1KB缓冲区
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
          // 写出字节数组到输出流（直接写入的是缓存区）
          os.write(buffer, 0, bytesRead);
          // 刷新到客户端
          os.flush();
        }

        // 刷新输出流
        os.flush();
      } catch (Exception e) {
        e.printStackTrace();
      }

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

  }

  public void executeUpdate() {

  }

}
