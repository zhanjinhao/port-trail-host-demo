package cn.addenda.porttrail.hostdemo.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URL;

@Slf4j
@Configuration
public class XxlJobConfig {

  private String adminAddresses = "http://192.168.10.111:28192/xxl-job-admin";

  private String accessToken = "@1@3$5^7*9_5058";

  private String appname = "port-trail-host-demo";

  private String address;

  private String ip;

  private int port = 9988;

  private String logPath;

  private int logRetentionDays = 7;

  @Bean
  public XxlJobSpringExecutor xxlJobExecutor() {
    String classResourcePath = XxlJobConfig.class.getName().replaceAll("\\.", "/") + ".class";
    URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
    String urlString = resource.toString();
    int prefixLength = "file:".length();
    String classLocation = urlString.substring(
            prefixLength, urlString.length() - classResourcePath.length());

    logPath = classLocation.split("port-trail-host-demo")[0] + "port-trail-host-demo/log_xxljob";
    log.info(">>>>>>>>>>> xxl-job config init.");
    XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
    xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
    xxlJobSpringExecutor.setAppname(appname);
    xxlJobSpringExecutor.setAddress(address);
    xxlJobSpringExecutor.setIp(ip);
    xxlJobSpringExecutor.setPort(port);
    xxlJobSpringExecutor.setAccessToken(accessToken);
    xxlJobSpringExecutor.setLogPath(logPath);
    xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

    return xxlJobSpringExecutor;
  }

  /**
   * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
   *
   *      1、引入依赖：
   *          <dependency>
   *             <groupId>org.springframework.cloud</groupId>
   *             <artifactId>spring-cloud-commons</artifactId>
   *             <version>${version}</version>
   *         </dependency>
   *
   *      2、配置文件，或者容器启动变量
   *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
   *
   *      3、获取IP
   *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
   */

}