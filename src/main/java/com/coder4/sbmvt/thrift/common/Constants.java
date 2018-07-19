/**
 * @(#)Constants.java, Jul 19, 2018.
 * <p>
 * Copyright 2018 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.coder4.sbmvt.thrift.common;

/**
 * @author coder4
 */
public class Constants {

    // 服务监听端口
    public static final int THRIFT_PORT = 3000;

    // 16mb
    public static final int THRIFT_MAX_FRAME_SIZE = 1024 * 1024 * 16;

    // 4MB
    public static final int THRIFT_MAX_READ_BUF_SIZE = 4 * 1024 * 1024;

    // 服务队列长度
    public static final int THRIFT_TCP_BACKLOG = 5000;

    // 常驻线程
    public static final int THRIFT_CORE_THREADS = 64;

    // 最多线程
    public static final int THRIFT_MAX_THREADS = 256;

    // 事件驱动线程数
    public static final int THRIFT_SELECTOR_THREADS = 16;

    // 连接超时
    public static final int THRIFT_TIMEOUT = 5000;

}