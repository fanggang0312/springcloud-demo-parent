package com.example.demo.controller;

import com.example.demo.common.result.ResultVO;
import com.example.demo.model.SysLog;
import com.example.demo.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: fg
 * @Date: 2020/11/09
 */
@Api(value = "demo", tags = "demo")
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private ISysLogService sysLogService;

    @ApiOperation("addSysLog接口")
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
        return new ResultVO(200,"success",new Date().getTime(),sysLog);
    }
}
