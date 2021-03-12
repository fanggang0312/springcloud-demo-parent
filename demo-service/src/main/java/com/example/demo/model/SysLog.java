package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志表
 *
 * @author fg
 * @since 2020/11/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLog implements Serializable {

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
