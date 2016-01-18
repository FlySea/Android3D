package com.flysea.gles.com.flysea.gles.base;

import android.opengl.GLES20;
import android.content.res.Resources;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 加载定点shader与片元shader
 * 提供着色器的初始化方法
 * Created by Liangjun on 2016/1/18.
 */
public class ShaderUtil {

  /**
   * 加载制定shader的方法
   * @param shaderType shader的类型  GLES20.GL_VERTEX_SHADER(顶点)   GLES20.GL_FRAGMENT_SHADER(片元)
   * @param source shader的脚本字符串
   * @return
   */
  public static int loadShader (int shaderType,String source) {
    //创建一个新的shader
    int shader = GLES20.glCreateShader(shaderType);
    if ( shader != 0) {
      GLES20.glShaderSource(shader, source);
      GLES20.glCompileShader(shader);
      int[] compiled = new int[1];
      GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
      if (compiled[0] == 0) {
        Log.e("ES20_ERROR","Could not compile shader " + shaderType + GLES20.glGetShaderInfoLog(shader));
        GLES20.glDeleteShader(shader);
        shader = 0;
      }
    }
    return shader;
  }

  //创建shader程序的方法
  public static int createProgram (String vertexSource,String fragmentSource) {

    int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
    if ( vertexShader == 0) {
      return 0;
    }

    int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
    if ( pixelShader == 0) {
      return 0;
    }

    int program = GLES20.glCreateProgram();
    if (program != 0) {
      GLES20.glAttachShader(program, vertexShader);
      checkGLError("glAttachShader");
      GLES20.glAttachShader(program, pixelShader);
      checkGLError("glAttachShader");
      GLES20.glLinkProgram(program);
      int[] linkStatus = new int[1];
      GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
      if (linkStatus[0]!= GLES20.GL_TRUE) {
        Log.e("ES20_ERROR", "Could not link program: ");
        Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
        GLES20.glDeleteProgram(program);
        program = 0;
      }
    }
    return program;
  }

  private static void checkGLError(String op) {
    int error;
    while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
      Log.e("ES20_ERROR",op + ": glError" + error);
      throw new RuntimeException(op + ": glError" +error);
    }
  }

  //从AssetsFile加载shader
  public static String loadFromAssetsFile(String fname, Resources r) {
    String result = null;
    try {
      InputStream in = r.getAssets().open(fname);
      int ch = 0;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while ( (ch=in.read())!=-1) {
        baos.write(ch);
      }
      byte[] buff = baos.toByteArray();
      baos.close();
      in.close();
      result = new String(buff,"UTF-8");
      result = result.replaceAll("\\r\\n","\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}
