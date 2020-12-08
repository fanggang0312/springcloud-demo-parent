package com.example.demo.feign;

import com.example.demo.common.result.ResultVO;
import com.example.demo.feign.config.FeignConfiguration;
import com.example.demo.feign.hystrix.DemoFeignApiHystrix;
import com.example.demo.model.SysLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * value:调用服务的微服务名称(不支持下划线"_"，支持"-")
 * path:定义当前FeignClient的统一前缀
 * configuration:Feign配置类，可以自定义Feign的Encoder、Decoder、LogLevel、Contract
 * fallbackFactory:工厂类，用于生成fallback类示例(容错的处理类，当调用远程接口失败或超时时，会调用对应接口的容错逻辑，fallback指定的类必须实现@FeignClient标记的接口)
 *
 * @Author: fg
 * @Date: 2020/11/19
 */
@FeignClient(
        value = "DEMO-SERVICES-NAME"
        , path = "test/demo"
        , configuration = FeignConfiguration.class
        , fallbackFactory = DemoFeignApiHystrix.class
)
public interface DemoFeignApi {

    @GetMapping("/getDemo")
    ResultVO getDemo(@RequestParam(value = "demo") String demo);

    @PostMapping("/postDemo")
    ResultVO postDemo(@RequestBody SysLog sysLog);

}
