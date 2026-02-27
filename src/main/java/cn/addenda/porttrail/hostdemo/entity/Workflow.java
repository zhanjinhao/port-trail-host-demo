package cn.addenda.porttrail.hostdemo.entity;

import cn.addenda.component.base.jackson.deserializer.LocalDateTimeStrDeSerializer;
import cn.addenda.component.base.jackson.serializer.LocalDateTimeStryMdHmsSSerializer;
import cn.addenda.mybatisbasemodel.simple.SimpleBaseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (Workflow)实体类
 *
 * @author makejava
 * @since 2025-08-23 12:25:41
 */
@Setter
@Getter
@ToString
public class Workflow extends SimpleBaseModel implements Serializable {

  private static final long serialVersionUID = -37678640668316758L;

  public static final String NODE_TYPE_AND_COUNTERSIGN = "AND_COUNTERSIGN";
  public static final String NODE_TYPE_OR_COUNTERSIGN = "OR_COUNTERSIGN";
  public static final String NODE_TYPE_HANDLE = "HANDLE";

  public static final String NODE_STATE_BLOCKING = "BLOCKING";
  public static final String NODE_STATE_PASS = "PASS";
  public static final String NODE_STATE_REJECT = "REJECT";
  public static final String NODE_STATE_REVOKE_CONVERT_TO_COUNTERSIGN_REJECT = "REVOKE_CONVERT_TO_COUNTERSIGN_REJECT";
  public static final String NODE_STATE_REVOKE_TRANSFER_REJECT = "REVOKE_TRANSFER_REJECT";
  public static final String NODE_STATE_WAITING = "WAITING";
  public static final String NODE_STATE_OR_SKIP = "OR_SKIP";
  public static final String NODE_STATE_AND_SKIP = "AND_SKIP";
  public static final String NODE_STATE_READY = "READY";

  /**
   * 创建会签
   */
  public static final String INITIATION_TYPE_CREATE_COUNTERSIGN = "CREATE_COUNTERSIGN";
  /**
   * 转会签
   */
  public static final String INITIATION_TYPE_CONVERT_TO_COUNTERSIGN = "CONVERT_TO_COUNTERSIGN";
  /**
   * 转交
   */
  public static final String INITIATION_TYPE_TRANSFER = "TRANSFER";
  /**
   * 撤销转会签
   */
  public static final String INITIATION_TYPE_REVOKE_CONVERT_TO_COUNTERSIGN = "REVOKE_CONVERT_TO_COUNTERSIGN";
  /**
   * 撤销转交
   */
  public static final String INITIATION_TYPE_REVOKE_TRANSFER = "REVOKE_TRANSFER";


  private Long id;
  /**
   * 节点类型：AND会签节点、OR会签节点、处理节点
   */
  private String nodeType;
  /**
   * 依赖节点
   */
  private Long dependentNode;
  /**
   * 节点状态：会签节点（阻塞、通过、拒绝、等待、跳过）；处理节点（通过、拒绝、等待、跳过、Ready）
   */
  private String nodeState;
  /**
   * 父节点
   */
  private Long parentNode;
  /**
   * 发起类型：ROOT、转会签
   */
  private String initiationType;
  /**
   * 发起人
   */
  private String initiator;
  /**
   * 发起时间
   */
  @JsonDeserialize(using = LocalDateTimeStrDeSerializer.class)
  @JsonSerialize(using = LocalDateTimeStryMdHmsSSerializer.class)
  private LocalDateTime initiationDt;
  /**
   * 候选人
   */
  private String candidate;
  /**
   * 处理人
   */
  private String handler;
  /**
   * 处理时间
   */
  @JsonDeserialize(using = LocalDateTimeStrDeSerializer.class)
  @JsonSerialize(using = LocalDateTimeStryMdHmsSSerializer.class)
  private LocalDateTime handleTime;
  /**
   * 节点标题
   */
  private String nodeTitle;
  /**
   * 节点名称
   */
  private String nodeDescription;
  /**
   * 外挂表单ID
   */
  private String bizId;
  /**
   * 业务上的唯一ID
   */
  private String uuid;
  /**
   * 分组ID（一个流程实例一个）
   */
  private String groupId;
  /**
   * 原节点ID（用于转会签、转交）
   */
  private Long sourceNodeId;

  public static Workflow ofParam() {
    return new Workflow();
  }

}

