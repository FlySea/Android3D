package com.flysea.gles.com.flysea.gles.base;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * GLSurfaceView的abstract派生类,提供了
 * initModel() initcamera()的方法模型,方便着色器类进行标准化的调用
 * 本类并不独立使用，本类应由派生类产生实际作用
 * Created by Liangjun on 2016/1/18.
 */
public abstract class GameView extends GLSurfaceView {

  protected Context _context;

  public GameView(Context context) {
    super(context);
    _context = context;
    this.setEGLContextClientVersion(2); //标明GLES的版本号
  }

  /**这个函数的调用 是在 Render类里面的 （由于模型导入及camera初始都要在surface建成后）
   定个型先，在派生类中再重写
   */
  public abstract void InitModel();

  /**
   * 这个函数的调用 是在 Render类里面的 （由于模型导入及camera初始都要在surface建成后
   * 一般在 onSurfaceChange()里可以得到屏高屏宽）
   * 定个型先，在派生类中再重写
   */
  public abstract void InitCamera(int iWinWidth,int iWinHeight);

}
