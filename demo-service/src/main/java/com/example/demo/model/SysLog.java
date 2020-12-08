package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 系统操作日志表
 * </p>
 *
 * @author fg
 * @since 2020/11/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysLog extends Model {

    private static final long serialVersionUID = 1L;

    /**
     * 日志编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账户
     */
    private String account;

    /**
     * 日志摘要
     */
    private String content;

    /**
     * 日志类型
     */
    private String type;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 入参
     */
    private String param;

    /**
     * 操作结果
     */
    private String result;

    /**
     * 创建时间
     */
    private Date createTime;

}
