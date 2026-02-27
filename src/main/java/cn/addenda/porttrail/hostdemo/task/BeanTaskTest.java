package cn.addenda.porttrail.hostdemo.task;

import cn.addenda.porttrail.hostdemo.service.DemoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanTaskTest extends IJobHandler {

  @Autowired
  private DemoService demoService;

  @Override
  @XxlJob("beanTaskTest")
  public ReturnT<String> execute(String s) throws Exception {
    System.out.println("beanTaskTest");
    demoService.update2("3");
    return ReturnT.SUCCESS;
  }

}
