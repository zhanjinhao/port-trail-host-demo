package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.component.transaction.PlatformTransactionHelper;
import cn.addenda.porttrail.hostdemo.entity.Workflow;
import cn.addenda.porttrail.hostdemo.mapper.WorkflowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DemoService {

  @Autowired
  private WorkflowMapper workflowMapper;

  @Autowired
  private PlatformTransactionHelper platformTransactionHelper;

  public List<Workflow> query(String nodeType) {
    log.info("HelloService: classLoader={}", log.getClass().getClassLoader());

    Workflow param = Workflow.ofParam();
    param.setNodeType(nodeType);
    return workflowMapper.queryByEntity(param);
  }

  @Transactional(rollbackFor = Exception.class)
  public void update(String nodeType) {
    log.error("HelloService: classLoader={}", log.getClass().getClassLoader());

//    workflowMapper.queryAllByEntity(Workflow.ofParam());

    Workflow param = Workflow.ofParam();
    param.setNodeType(nodeType);
    workflowMapper.updateById(param);

    param.setNodeType(nodeType);
    workflowMapper.updateById(param);
  }

  public void update2(String nodeType) {
    platformTransactionHelper.doTransaction(() -> {
      log.error("HelloService: classLoader={}", log.getClass().getClassLoader());

      workflowMapper.queryAllByEntity(Workflow.ofParam());

      Workflow param = Workflow.ofParam();
      param.setNodeType(nodeType);
      workflowMapper.updateById(param);

      param.setNodeType(nodeType);
      workflowMapper.updateById(param);
    });

  }


}
