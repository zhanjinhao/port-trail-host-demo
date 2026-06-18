package cn.addenda.porttrail.hostdemo.test.httpclient4;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

public class AbstractHttpClient4Test {

  protected static final String BASE_URL = "http://localhost:28893/porttrailhostdemo/httpTest";

  protected static final String REQUEST_ID_KEY = "request_id";
  protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  protected static byte[] gzipCompress(byte[] data) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
      gzos.write(data);
    }
    return baos.toByteArray();
  }

  protected static byte[] deflateCompress(byte[] data) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (DeflaterOutputStream dos = new DeflaterOutputStream(baos)) {
      dos.write(data);
    }
    return baos.toByteArray();
  }

  protected static byte[] gzipDecompress(byte[] compressedData) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (GZIPInputStream gzis = new GZIPInputStream(new java.io.ByteArrayInputStream(compressedData))) {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gzis.read(buffer)) > 0) {
        baos.write(buffer, 0, len);
      }
    }
    return baos.toByteArray();
  }

  protected static byte[] deflateDecompress(byte[] compressedData) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (InflaterInputStream iis = new InflaterInputStream(new java.io.ByteArrayInputStream(compressedData))) {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = iis.read(buffer)) > 0) {
        baos.write(buffer, 0, len);
      }
    }
    return baos.toByteArray();
  }

  protected static String decodeBody(byte[] bodyBytes, Header contentEncodingHeader) throws IOException {
    if (contentEncodingHeader == null) {
      return new String(bodyBytes, StandardCharsets.UTF_8);
    }

    String encoding = contentEncodingHeader.getValue().toLowerCase();

    switch (encoding) {
      case "gzip":
        byte[] decompressed = gzipDecompress(bodyBytes);
        return new String(decompressed, StandardCharsets.UTF_8);
      case "deflate":
        byte[] decompressedDeflate = deflateDecompress(bodyBytes);
        return new String(decompressedDeflate, StandardCharsets.UTF_8);
      case "identity":
      case "":
        return new String(bodyBytes, StandardCharsets.UTF_8);
      default:
        System.out.println("[Warning] Unsupported Content-Encoding: " + encoding + ", displaying raw bytes");
        return new String(bodyBytes, StandardCharsets.UTF_8);
    }
  }

}
