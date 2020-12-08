package com.example.demo.service;

import com.example.demo.model.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统操作日志表 服务类
 * </p>
 *
 * @author fg
 * @since 2020/11/09
 */
public interface ISysLogService extends IService<SysLog> {

    void insertSysLog(SysLog sysLogAdd);
}
