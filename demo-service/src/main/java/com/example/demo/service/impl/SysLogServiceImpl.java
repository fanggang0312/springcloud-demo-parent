package com.example.demo.service.impl;

import com.example.demo.model.SysLog;
import com.example.demo.mapper.SysLogMapper;
import com.example.demo.service.ISysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 系统操作日志表 服务实现类
 * </p>
 *
 * @author fg
 * @since 2020/11/09
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public void insertSysLog(SysLog sysLogAdd) {
        sysLogMapper.insert(sysLogAdd);
    }
}
