package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("httpTest")
public class HttpTestController {

  @Autowired
  private DemoService demoService;

  @PostMapping("testOnlyRequestParam")
  public Map<String, String> testOnlyRequestParam(@RequestParam("nodeType") String nodeType) {

    Map<String, String> map = new HashMap<>();
    map.put("testOnlyRequestParam", nodeType);
    return map;
  }

  @PostMapping("testRequestParamAndResponseBody")
  public Map<String, String> testRequestParamAndResponseBody(@RequestParam("nodeType") String nodeType, @RequestBody Map<String, String> postParam) {
    System.out.println(postParam);
    Map<String, String> map = new HashMap<>();
    map.put("testRequestParamAndResponseBody", nodeType);
    map.putAll(postParam);
    return map;
  }

  @GetMapping("testGet")
  public Map<String, String> testGet(@RequestParam(value = "id", required = false) String id,
                                     @RequestParam(value = "name", required = false) String name) {
    Map<String, String> map = new HashMap<>();
    map.put("method", "GET");
    map.put("id", id);
    map.put("name", name);
    return map;
  }

  @PutMapping("testPut")
  public Map<String, String> testPut(@RequestBody Map<String, String> putParam) {
    System.out.println(putParam);
    Map<String, String> map = new HashMap<>();
    map.put("method", "PUT");
    map.putAll(putParam);
    return map;
  }

  @DeleteMapping("testDelete/{id}")
  public Map<String, String> testDelete(@PathVariable("id") String id) {
    Map<String, String> map = new HashMap<>();
    map.put("method", "DELETE");
    map.put("deletedId", id);
    map.put("result", "success");
    return map;
  }

  @RequestMapping(value = "testHead", method = RequestMethod.HEAD)
  public Map<String, String> testHead() {
    Map<String, String> map = new HashMap<>();
    map.put("method", "HEAD");
    map.put("message", "HEAD response - no body, only headers returned by server");
    return map;
  }

  @RequestMapping(value = "testOptions", method = RequestMethod.OPTIONS)
  public Map<String, String> testOptions() {
    Map<String, String> map = new HashMap<>();
    map.put("method", "OPTIONS");
    map.put("allowedMethods", "GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE");
    return map;
  }

  @RequestMapping(value = "testTrace", method = RequestMethod.TRACE)
  public Map<String, String> testTrace() {
    Map<String, String> map = new HashMap<>();
    map.put("method", "TRACE");
    map.put("message", "TRACE echo - request is reflected back");
    return map;
  }

  @PatchMapping("testPatch")
  public Map<String, String> testPatch(@RequestBody Map<String, String> patchParam) {
    System.out.println(patchParam);
    Map<String, String> map = new HashMap<>();
    map.put("method", "PATCH");
    map.putAll(patchParam);
    return map;
  }

  @PostMapping("testPostStream")
  public Map<String, String> testPostStream(@RequestBody byte[] bytes) {
    System.out.println("bytes size: " + bytes.length);

    Map<String, String> map = new HashMap<>();
    map.put("method", "testPostStream");
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

  /**
   * request:multipart/form-data
   * response: application/json
   */
  @PostMapping(value = "testMultipartFormData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Map<String, String> testMultipartFormData(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("nodeType") String nodeType) {
    System.out.println("file=" + file.getName() + ",filename=" + file.getOriginalFilename() + ",size=" + file.getSize());
    // 将文件写入 target 目录
    try {
      File targetDir = new File(DemoController.class.getClassLoader().getResource("").toURI().getPath()).getParentFile();
      File dest = new File(targetDir, UUID.randomUUID().toString().replace("-", "") + "_" + file.getOriginalFilename());
      dest.createNewFile();
      InputStream inputStream = file.getInputStream();

      try (InputStream bufferedIn = new BufferedInputStream(inputStream);
           OutputStream fileOut = Files.newOutputStream(dest.toPath());
           BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut)) {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bufferedIn.read(buffer)) != -1) {
          bufferedOut.write(buffer, 0, bytesRead);
        }

        bufferedOut.flush();
      } catch (IOException e) {
        throw e;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // 处理文件和文本参数
    demoService.update(nodeType);
    Map<String, String> map = new HashMap<>();
    map.put("testMultipartFormDatas", "testMultipartFormData");
    map.put("file", file.getName());
    map.put("filename", file.getOriginalFilename());
    map.put("nodeType", nodeType);
    return map;
  }

  /**
   * request:application/x-www-form-urlencoded
   * response: application/json
   */
  @PostMapping(value = "testFormUrlencoded", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public Map<String, String> testFormUrlencoded(@RequestParam("nodeType") Integer nodeType, @RequestParam("f1") String f1) {
    System.out.println("f1=" + f1);
//    demoService.update("" + nodeType);
    Map<String, String> map = new HashMap<>();
    map.put("nodeType", String.valueOf(nodeType));
    map.put("f1", f1);
    map.put("testFormUrlencoded", "testFormUrlencoded");
    return map;
  }

}
