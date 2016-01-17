package com.flysea.gles.com.flysea.gles.base;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * 3D网格中每个组的内容信息
 * Created by memoryemon on 2016/1/17.
 */
public class SubInfo {
    public String strSubsetName = ""; //出于读obj文件的理由 ,必须有子项名(与 obj mtl文件中相同)
    public FBInfo posInfo = null;
    public FBInfo normalInfo = null;
    public FBInfo uvInfo = null;
    public FBInfo colorInfo = null;
    public FBInfo trangentInfo = null;
    public FBInfo binormalInfo = null;

    public int idDTex; //texture的ID 没有 为-1
    public int idSTex;
    public int idNTex;
    public int iSubVertCount; //subset里的顶点数量

    //下面为临时存的使用的变量 在最后形成时应该抛弃临存变量 由系统回收
    public ArrayList<Float> arrV = new ArrayList<Float>(); /*临时存放pos*/
    public ArrayList<Float> arrVT = new ArrayList<Float>(); /*临时存放texcoord*/
    public ArrayList<Float> arrVN = new ArrayList<Float>(); /*临时存放normal*/

    public SubInfo(String strSubsetName) {
        this.strSubsetName = strSubsetName;
        idDTex = -1;
        idSTex = -1;
        idNTex = -1;
        iSubVertCount = 0;
    }

    public void PutPosFB(FloatBuffer fbuf) {
        posInfo = new FBInfo();
        posInfo._fbuf = fbuf;
        posInfo._iStride = 3 * 4;
    }

    public void PutNormalFB(FloatBuffer fbuf) {
        normalInfo = new FBInfo();
        normalInfo._fbuf = fbuf;
        normalInfo._iStride = 3 * 4;
    }

    public void PutUvFB(FloatBuffer fbuf) {
        uvInfo = new FBInfo();
        uvInfo._fbuf = fbuf;
        uvInfo._iStride = 2 * 4;
    }

    public void PutColorFB(FloatBuffer fbuf) {
        colorInfo = new FBInfo();
        colorInfo._fbuf = fbuf;
        colorInfo._iStride = 4 * 4;
    }

    public void PutTangentFB(FloatBuffer fbuf) {
        trangentInfo = new FBInfo();
        trangentInfo._fbuf = fbuf;
        trangentInfo._iStride = 3 * 4;
    }

    public void PutBinormalFB(FloatBuffer fbuf) {
        binormalInfo = new FBInfo();
        binormalInfo._fbuf = fbuf;
        binormalInfo._iStride = 3 * 4;
    }

    public int CommitSubInfo() {
        PutPosFB(Function.PushIntoFLoatBuffer(arrV));
        PutUvFB(Function.PushIntoFLoatBuffer(arrVT));
        PutNormalFB(Function.PushIntoFLoatBuffer(arrVN));
        iSubVertCount = arrV.size() / 3;

        if ( (arrVT.size() / 2 != iSubVertCount) || (arrVN.size()/3 != iSubVertCount)) {
            iSubVertCount = -1;
        }
        //释放临时数据
        arrV = null;
        arrVN = null;
        arrVT = null;
        return iSubVertCount;
    }
}