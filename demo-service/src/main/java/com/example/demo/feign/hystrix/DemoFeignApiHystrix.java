package com.example.demo.feign.hystrix;

import com.example.demo.common.result.ResultVO;
import com.example.demo.feign.DemoFeignApi;
import com.example.demo.model.SysLog;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: fg
 * @Date: 2020/11/19
 */
@Slf4j
@Component
public class DemoFeignApiHystrix implements FallbackFactory<DemoFeignApi> {
    @Override
    public DemoFeignApi create(Throwable throwable) {
        log.info(">>>>>>【降级原因】：{}", throwable.getMessage());
        return new DemoFeignApi() {
            @Override
            public ResultVO getDemo(String demo) {
                return null;
            }

            @Override
            public ResultVO postDemo(SysLog sysLog) {
                return null;
            }
        };
    }
}
