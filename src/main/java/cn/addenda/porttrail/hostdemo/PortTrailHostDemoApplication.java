package cn.addenda.porttrail.hostdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
 * -javaagent:C:\workspace\project\incubating\port-trail\dist\porttrail-agent.jar
 *
 * @author addenda
 * @since 2022/1/14 15:44
 */
@EnableTransactionManagement(order = Ordered.LOWEST_PRECEDENCE - 70)
@MapperScan("cn.addenda.porttrail.hostdemo")
@SpringBootApplication
public class PortTrailHostDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortTrailHostDemoApplication.class, args);
  }

}
