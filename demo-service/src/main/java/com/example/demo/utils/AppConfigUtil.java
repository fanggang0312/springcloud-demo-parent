package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 获取应用全局配置
 *
 * @author fg
 */
@Component
public final class AppConfigUtil {

    private static Environment envTemp;

    @Autowired
    private Environment env;

    private AppConfigUtil() {
    }

    @PostConstruct
    public void init() {
        envTemp = env;
    }

    private static String getProfile(String[] profiles) {
        if (profiles != null && profiles.length > 0) {
            return profiles[0];
        } else {
            return null;
        }
    }

    public static String getActiveProfile() {
        String profile = getProfile(envTemp.getActiveProfiles());
        if (StringUtils.isBlank(profile)) {
            profile = getProfile(envTemp.getDefaultProfiles());
            if (StringUtils.isBlank(profile)) {
                return "DEV";
            }
        }
        return profile;
    }

    public static boolean isProdEnv() {
        return "PROD".equalsIgnoreCase(getActiveProfile());
    }

    public static boolean isTestEnv() {
        return "TEST".equalsIgnoreCase(getActiveProfile());
    }

    public static boolean isDevEnv() {
        return "DEV".equalsIgnoreCase(getActiveProfile());
    }

}
