package com.flysea.gles.com.flysea.gles.base;

import android.util.Log;

import java.util.ArrayList;

/**
 * 3D 网格的数据包装类
 * Created by memoryemon on 2016/1/17.
 */
public class MeshInfo {
    public String strMeshName = "";
    public int iSubsets = 0;
    public int  enumRENDER_TECH = -1;
    public ArrayList<SubInfo> arrSubInfo = new ArrayList<SubInfo>();

    public int GetSubIndex(String strSubName) {
        for (int i=0; i < arrSubInfo.size(); i++) {
            if ( (arrSubInfo.get(i).strSubsetName)==strSubName)
                return i;
        }
        return -1;
    }

    public SubInfo GetSubset(String strMeshName) {
        for (int i=0; i<arrSubInfo.size();i++) {
            if ( (arrSubInfo.get(i).strSubsetName.trim().equals(strMeshName))) {
                return arrSubInfo.get(i);
            }
        }
        return null;
    }

    public SubInfo GetSubset(int index) {
        if (index < arrSubInfo.size()) {
            return arrSubInfo.get(index);
        } else {
            Log.e("ERR","in MeshInfo::GetSubSet");
            return null;
        }
    }

    //生成mesh info后最后一步 检测并完成整个mesh info 返回总顶点数
    public int CommitMeshInfo() {
        iSubsets = arrSubInfo.size();
        int iVertCount = 0;
        for ( int i=0; i < iSubsets; i++) {
            int iSubVert = arrSubInfo.get(i).CommitSubInfo();
            if (iSubVert < 0) {
                throw new RuntimeException("Mesh subset error");
            }
            iVertCount += iSubVert;
        }
        return  iVertCount;
    }
}
