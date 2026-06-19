package cn.addenda.porttrail.hostdemo.task;

import cn.addenda.porttrail.hostdemo.service.HttpTestService;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanTaskTest extends IJobHandler {

    @Autowired
    private HttpTestService httpTestService;

    @Override
    @XxlJob("beanTaskTest")
    public void execute() throws Exception {
        System.out.println("beanTaskTest");
        httpTestService.update2("3");
    }

}
