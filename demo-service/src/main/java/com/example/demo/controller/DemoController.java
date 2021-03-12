package com.example.demo.controller;

import com.example.demo.common.result.ResultVO;
import com.example.demo.model.SysLog;
import com.example.demo.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: fg
 * @Date: 2020/11/09
 */
@Api(value = "demo测试接口controller")
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private ISysLogService sysLogService;

    @ApiOperation(value = "新增日志", notes = "新增日志新增日志新增日志新增日志新增日志")
    @GetMapping("/addSysLog")
    public ResultVO addSysLog() {
        SysLog sysLog = new SysLog();
        sysLog.setAccount("Account");
        sysLog.setContent("Content");
        sysLog.setType("Type");
        sysLog.setIp("127.0.0.1");
        sysLog.setParam("Param");
        sysLog.setResult("Result");
        sysLog.setCreateTime(new Date());

        sysLogService.insertSysLog(sysLog);
        return new ResultVO(200, "success", new Date().getTime(), sysLog);
    }

    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "文件上传", notes = "文件上传文件上传文件上传文件上传文件上传")
    @PostMapping("/doUploadFile")
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) {
        return sysLogService.doUploadFile(request, response);
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "文件下载", notes = "文件下载文件下载文件下载文件下载文件下载文件下载")
    @PostMapping("/doDownloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        sysLogService.doDownloadFile(request, response);
    }
}
