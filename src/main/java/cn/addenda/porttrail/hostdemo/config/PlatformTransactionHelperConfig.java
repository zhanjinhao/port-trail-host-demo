package cn.addenda.porttrail.hostdemo.config;

import cn.addenda.component.transaction.PlatformTransactionHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PlatformTransactionHelperConfig {

  @Bean
  public PlatformTransactionHelper platformTransactionHelper(PlatformTransactionManager platformTransactionManager) {
    return new PlatformTransactionHelper(platformTransactionManager);
  }

}
