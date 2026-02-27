package cn.addenda.porttrail.hostdemo.mapper;

import cn.addenda.porttrail.hostdemo.entity.Workflow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Workflow)表数据库访问层
 *
 * @author addenda
 * @since 2025-08-23 12:25:51
 */
public interface WorkflowMapper {
  /**
   * 新增数据
   */
  int insert(Workflow workflow);

  /**
   * 按ID更新数据
   */
  int updateById(Workflow workflow);

  /**
   * 按ID删除数据
   */
  int deleteById(@Param("id") Long id);

  /**
   * 按实体类删除数据
   */
  int deleteByEntity(Workflow workflow);

  /**
   * 按实体类查询数据
   */
  List<Workflow> queryByEntity(Workflow workflow);

  List<Workflow> queryAllByEntity(Workflow workflow);

  /**
   * 按ID查询数据
   */
  Workflow queryById(@Param("id") Long id);

  /**
   * 按ID集合查询数据
   */
  List<Workflow> queryByIdList(@Param("idList") List<Long> idList);

  /**
   * 按实体类计数数据
   */
  Long countByEntity(Workflow workflow);

}
