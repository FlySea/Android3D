package com.flysea.gles.com.flysea.gles.base;

import java.nio.FloatBuffer;

/**
 * 标准化每个进入着色器的浮点流
 * Created by memoryemon on 2016/1/17.
 */

public class FBInfo {
    public FloatBuffer _fbuf = null;
    public int _iStride = 0;  //步长 字节数
}
