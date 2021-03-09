package com.example.demo.service;

import com.example.demo.model.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    String doUploadFile(HttpServletRequest request, HttpServletResponse response);

    void doDownloadFile(HttpServletRequest request, HttpServletResponse response);
}
