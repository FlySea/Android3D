package com.flysea.gles;

import android.content.res.Resources;
import com.flysea.gles.com.flysea.gles.base.Function;
import com.flysea.gles.com.flysea.gles.base.MeshDrawable;
import com.flysea.gles.com.flysea.gles.base.MeshInfo;
import com.flysea.gles.com.flysea.gles.base.SubInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 3D网格
 * 暂时只能由  *.obj文件提取,并且要求 *.obj *.mtl 及纹理文件放在 assets里
 * 暂时本类提取的3D网格只支持位置,纹理座标,法线三个量
 * Created by Liangjun on 2016/1/18.
 */
public class Mesh implements MeshDrawable {

  MeshInfo _info = null;
  public Mesh(Resources resources,String strObjFileName) {
    // 按文件名生成网格，并填充至 _info
    ArrayList<Float> arr_v = new ArrayList<Float>();//用于临存 pos 索引元
    ArrayList<Float> arr_vt = new ArrayList<Float>();//临存 texcoord 索引元
    ArrayList<Float> arr_vn = new ArrayList<Float>();//临存 normal 索引元
    try {
      InputStream ins = resources.getAssets().open(strObjFileName);
      InputStreamReader inReader = new InputStreamReader(ins);
      BufferedReader br = new BufferedReader(inReader);
      String strTemp = null; //临存每行字符

      SubInfo curSub = null; //当前操作的SubInfo 用于填错临时数据
      _info = new MeshInfo();

      while ((strTemp = br.readLine())!= null ) {
        String[] arrStr = strTemp.split("[ ]+"); //按空格分割
        if (arrStr[0].trim().equals("o")) {
           _info.strMeshName = arrStr[1];
        } else if (arrStr[0].trim().equals("mtllib")) {
          //打开mtl文件,初始化图片(时机在sufaceview建立之后),并返回subset数量
          String strMtlFilename = arrStr[1];
          _info.iSubsets = SetupMaterial(resources,strMtlFilename);
        } else if (arrStr[0].trim().equals("v")) {
          //pos数据
          arr_v.add(Float.parseFloat(arrStr[1]));
          arr_v.add(Float.parseFloat(arrStr[2]));
          arr_v.add(Float.parseFloat(arrStr[3]));
        } else if (arrStr[0].trim().equals("vt")) {
          //texcoord数据
          arr_vt.add(Float.parseFloat(arrStr[1]));
          arr_vt.add(1.0f - Float.parseFloat(arrStr[2]));
        } else if (arrStr[0].trim().equals("vn")) {
          //normal数据
          arr_vn.add(Float.parseFloat(arrStr[1]));
          arr_vn.add(Float.parseFloat(arrStr[2]));
          arr_vn.add(Float.parseFloat(arrStr[3]));
        } else if (arrStr[0].trim().equals("usemtl")) {
          //读取面信息 并填充到SubTemp结构
          curSub = _info.GetSubset(arrStr[1]);
        } else if(arrStr[0].trim().equals("f")) {
          String[] arrVertex = null;
          int index = -1;
          for (int i=1; i <= 3; i++) {
            arrVertex = arrStr[i].split("/");  //本面的第i个点的信息
            index = Integer.parseInt(arrVertex[0])-1; //pos的索引
            curSub.arrV.add(arr_v.get(3*index));
            curSub.arrV.add(arr_v.get(3*index)+1);
            curSub.arrV.add(arr_v.get(3*index)+2);

            index = Integer.parseInt(arrVertex[1])-1; //uv的索引
            curSub.arrVT.add(arr_vt.get(2*index));
            curSub.arrVT.add(arr_vt.get(2*index)+1);

            index = Integer.parseInt(arrVertex[2]) - 1;
            curSub.arrVN.add(arr_vn.get(3*index));
            curSub.arrVN.add(arr_vn.get(3*index+1));
            curSub.arrVN.add(arr_vn.get(3*index+2));
          }
        }
      }
      _info.CommitMeshInfo();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 初始化Mesh材质信息
   * @param resources 系统资源
   * @param strMtlFilename 材质文件
   * @return Subset数量
   * @throws IOException
   */
  private int SetupMaterial(Resources resources,String strMtlFilename) throws IOException {
    //不考虑Ka kd ks
    InputStream ins = resources.getAssets().open(strMtlFilename);
    InputStreamReader insReader = new InputStreamReader(ins);
    BufferedReader br = new BufferedReader(insReader);
    String strTemp = null;
    int iSubCount = 0;
    SubInfo curSub = null;  //当前的subset info
    while ( (strTemp = br.readLine())!= null ) {
      String[] arrStr = strTemp.split("[ ]+");
      if (arrStr[0].trim().equals("newmtl")) {
        //标示材质
        iSubCount++;
        curSub = new SubInfo(arrStr[1]);
        _info.arrSubInfo.add(curSub);
      } else if (arrStr[0].trim().equals("map_Kd")) {
        //纹理名
        if ( curSub == null) {
          break;
        }
        curSub.idDTex = Function.CreateTexture(resources, arrStr[1]);
      }
    }
    return iSubCount;
  }

  @Override
  public MeshInfo getMeshInfo() {
    return _info;
  }

  public void SetRenderTech(int enumRENDER_TECH) {
    _info.enumRENDER_TECH = enumRENDER_TECH;
  }
}
