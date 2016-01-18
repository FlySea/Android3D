package com.flysea.gles;


import android.opengl.Matrix;
import com.flysea.gles.com.flysea.gles.base.Function;
import java.util.ConcurrentModificationException;

/**摄像机类
 * Created by Liangjun on 2016/1/18.
 */
public class Camera {

  protected static float[] _mxTexp = new float[16];

  protected float[]	_right = new float[] {1.0f,0f,0f};
  protected float[] _up = new float[] {0f,1.0f,0f};
  protected float[]	_invLook = new float[] {0f,0f,1.0f};
  protected float[]	_pos = new float[]{0f,0f,0f};

  protected float[] _mxView = new float[16];
  protected float[] _mxProj = new float[16];

  public Camera (int iWinWidth, int iWinHeight, float zf, float zn) {
    float fRatio = (float) iWinWidth / iWinHeight;
    RefreshViewMatrix(this);
  }

  //按类内四个分量重新构建一个mxView
  private static void RefreshViewMatrix(Camera camera) {
    //look不变
    Function.glV3Normalize(camera._invLook);
    Function.glV3Cross(camera._right, camera._up, camera._invLook);
    Function.glV3Normalize(camera._right);

    Function.glV3Cross(camera._up, camera._invLook, camera._right);
    Function.glV3Normalize(camera._up);
    camera._mxView[0] = camera._right[0];camera._mxView[1] = camera._up[0]; camera._mxView[2] = camera._invLook[0];camera._mxView[3] = 0;
    camera._mxView[4] = camera._right[1];camera._mxView[5] = camera._up[1]; camera._mxView[6] = camera._invLook[1];camera._mxView[7] = 0;
    camera._mxView[8] = camera._right[2];camera._mxView[9] = camera._up[2]; camera._mxView[10] = camera._invLook[2];camera._mxView[11] = 0;
    camera._mxView[12] = -Function.glV3Dot(camera._right, camera._pos);
    camera._mxView[13] = -Function.glV3Dot(camera._up, camera._pos);
    camera._mxView[14] = -Function.glV3Dot(camera._invLook, camera._pos);
    camera._mxView[15] = 1.0f;
  }

  //取世界矩阵
  public void getWorldMatrix(float[] mxWorldOut) {
    if ( mxWorldOut.length != 16 ) {
      throw new RuntimeException("err in Camera::getMxWorld");
    }
    Matrix.invertM(mxWorldOut, 0, _mxView, 0);
    return;
  }

  public void setDirection(float[] v3Direction) {
    for (int i=0; i<3; i++ ) {
      _invLook[i] = -v3Direction[i];
    }
    Function.glV3Normalize(_invLook);
    RefreshViewMatrix(this);
  }

  public void setPosition(float[] v3Pos) {
    Function.glFArrayCopy(_pos, v3Pos);
    RefreshViewMatrix(this);
  }
  //TODO normal ?
  public void set_Up(float[] v3Up) {
    Function.glFArrayCopy(_up, v3Up);
    RefreshViewMatrix(this);
  }
  public void setLookTarget(float[] v3LookTarget) {
    Function.glV3Subtract(_invLook, _pos, v3LookTarget); //pos到target的向量
    Function.glV3Normalize(_invLook);
    RefreshViewMatrix(this);
  }
  public void setRight(float[] v3Right) {
    Function.glFArrayCopy(_right, v3Right);
    Function.glV3Normalize(_right);
    RefreshViewMatrix(this);
  }

  public void setProjMatrix(float[] mxProjSource) {
    Function.glFArrayCopy(_mxProj, mxProjSource);
  }

  public void getPosition(float[] v3PosOut){
    if(v3PosOut.length != 3)
      throw new RuntimeException("err in GetPosition");
    for(int i=0; i<3 ; i++){
      v3PosOut[i] = _pos[i];
    }
  }

  public void geRight(float[] v3RightOut){
    Function.glFArrayCopy(v3RightOut, _right);
  }

  public void getUp(float[] v3UpOut){
    Function.glFArrayCopy(v3UpOut, _up);
  }

  public void getLook(float[] v3LookOut){
    if(v3LookOut.length!=3)
      throw new RuntimeException("err in camera");
    for(int i=0; i<3; i++)
      v3LookOut[i] = -_invLook[i];
  }

  public void getInvLook(float[] v3InvLookOut){
    Function.glFArrayCopy(v3InvLookOut, _invLook);
  }

  public void getViewMatrix(float[] mxViewOut){
    Function.glFArrayCopy(mxViewOut, _mxView);
  }

  //不要先new 一个mx 再 mx = GetViewMatrix() ，
  //直接声明一个空指针的float[] 然后 运行函数
  public float[] getViewMatrix() {
    return _mxView;
  }

  public void getProjMatrix(float[] mxProjOut) {
    Function.glFArrayCopy(mxProjOut,_mxProj);
  }

  public float[] getProjMatrix() {
    return _mxProj;
  }

  //摄像机转动函数  left--right
  public void strafe(float foffset) {
    for (int i=0; i<3; i++) {
      _pos[i] += _right[i]*foffset;
    }
  }

  //up--down
  public void fly(float foffset) {
      for (int i=0; i<3; i++) {
        _pos[i] += _up[i]*foffset;
      }
  }

  //z+ z-
  public void walk(float foffset) {
      for (int i=0; i<3; i++) {
        _pos[i] += _invLook[i]*foffset;
      }
  }

  //以right为轴
  public void pitch(float fAngle) {
    //使用类内static 的TEMP量 _mxTemp;这里把_mxTemp作为旋转矩阵
    Function.glRotateAxis(_mxTexp, fAngle, _right);
    Function.glV3Transform(_up, _mxTexp);
    Function.glV3Transform(_invLook, _mxTexp);

    RefreshViewMatrix(this);
  }

  //up
  public void yaw(float fAngle) {
    //使用类内static 的TEMP量 _mxTemp;这里把_mxTemp作为旋转矩阵
    Function.glRotateAxis(_mxTexp, fAngle, _up);
    Function.glV3Transform(_right, _mxTexp);
    Function.glV3Transform(_invLook, _mxTexp);

    RefreshViewMatrix(this);
  }

  //以look为轴
  public void roll(float fAngle) {
    //使用类内static 的TEMP量 _mxTemp;这里把_mxTemp作为旋转矩阵
    Function.glRotateAxis(_mxTexp, fAngle, _invLook);
    Function.glV3Transform(_up, _mxTexp);
    Function.glV3Transform(_right, _mxTexp);

    RefreshViewMatrix(this);
  }

}
