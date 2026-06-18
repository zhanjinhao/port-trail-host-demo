package cn.addenda.porttrail.hostdemo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

@Configuration
public class RequestDecompressionConfig {

  @Bean
  @Order(1)
  public FilterRegistrationBean<GzipDecompressionFilter> gzipDecompressionFilter() {
    FilterRegistrationBean<GzipDecompressionFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new GzipDecompressionFilter());
    registration.addUrlPatterns("/*");
    registration.setName("gzipDecompressionFilter");
    registration.setOrder(1);
    return registration;
  }

  public static class GzipDecompressionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

      HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
      String contentEncoding = httpRequest.getHeader("Content-Encoding");

      if ("gzip".equalsIgnoreCase(contentEncoding)) {
        GzipRequestWrapper wrappedRequest = new GzipRequestWrapper(httpRequest);
        filterChain.doFilter(wrappedRequest, servletResponse);
      } else if ("deflate".equalsIgnoreCase(contentEncoding)) {
        DeflateRequestWrapper wrappedRequest = new DeflateRequestWrapper(httpRequest);
        filterChain.doFilter(wrappedRequest, servletResponse);
      } else {
        filterChain.doFilter(servletRequest, servletResponse);
      }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
  }

  private static class GzipRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] decompressedData;

    public GzipRequestWrapper(HttpServletRequest request) throws IOException {
      super(request);
      this.decompressedData = decompressGzip(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
      ByteArrayInputStream bais = new ByteArrayInputStream(decompressedData);
      return new ServletInputStream() {
        @Override
        public boolean isFinished() {
          return bais.available() == 0;
        }

        @Override
        public boolean isReady() {
          return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }

        @Override
        public int read() {
          return bais.read();
        }
      };
    }

    @Override
    public BufferedReader getReader() {
      return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private byte[] decompressGzip(ServletInputStream inputStream) throws IOException {
      GZIPInputStream gis = new GZIPInputStream(inputStream);
      byte[] buffer = new byte[1024];
      java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
      int len;
      while ((len = gis.read(buffer)) > 0) {
        baos.write(buffer, 0, len);
      }
      return baos.toByteArray();
    }
  }

  private static class DeflateRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] decompressedData;

    public DeflateRequestWrapper(HttpServletRequest request) throws IOException {
      super(request);
      this.decompressedData = decompressDeflate(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
      ByteArrayInputStream bais = new ByteArrayInputStream(decompressedData);
      return new ServletInputStream() {
        @Override
        public boolean isFinished() {
          return bais.available() == 0;
        }

        @Override
        public boolean isReady() {
          return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }

        @Override
        public int read() {
          return bais.read();
        }
      };
    }

    @Override
    public BufferedReader getReader() {
      return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private byte[] decompressDeflate(ServletInputStream inputStream) throws IOException {
      InflaterInputStream iis = new InflaterInputStream(inputStream);
      byte[] buffer = new byte[1024];
      java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
      int len;
      while ((len = iis.read(buffer)) > 0) {
        baos.write(buffer, 0, len);
      }
      return baos.toByteArray();
    }
  }
}
