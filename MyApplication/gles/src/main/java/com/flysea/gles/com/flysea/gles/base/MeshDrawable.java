package com.flysea.gles.com.flysea.gles.base;

/**
 * 所有要进入管线的模型,都要派生自这个接口类
 * Created by memoryemon on 2016/1/17.
 */
public interface MeshDrawable {

    //取出VertInfo 供Render用 (用于确定向GPU发送哪些顶点属性
    //由于里面包含了所有要绘制的信息 (不算矩阵) 所以省去了 drawMesh
    public MeshInfo GetMeshInfo();
}
