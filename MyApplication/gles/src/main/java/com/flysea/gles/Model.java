package com.flysea.gles;


import android.opengl.Matrix;
import com.flysea.gles.com.flysea.gles.base.MeshDrawable;
import com.flysea.gles.com.flysea.gles.base.MeshInfo;

/**空间模型类
 * Created by Liangjun on 2016/1/18.
 */
public class Model {

  protected MeshDrawable _mesh = null; //模型类内包含的网格体
  protected float[] _mxWorld = new float[16];

  void defultInit() {
    Matrix.setIdentityM(_mxWorld,0);
  }

  public Model (MeshDrawable mesh ) {
    _mesh = mesh;
    defultInit();
  }

  public MeshInfo getMeshInfo() {
    return _mesh.getMeshInfo();
  }

  public float[] getWorldMatrix() {
    return _mxWorld;
  }

  //// TODO: 2016/1/18  测试


}
