package com.liepin.macaw.exception;

/**
 * 表示AB分层测试client初始化时的配置解析解析异常
 * Created by lixuancheng(formid) on 2017/5/24.
 */
public class ConfigException extends Exception {
    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
