package com.flysea.gles;

import java.util.ArrayList;

/**
 * 模型对了 为引擎实现标准数据格式
 * Created by Liangjun on 2016/1/18.
 */
public class ModelCollection {
  ArrayList<Model> _arrModel = new ArrayList<Model>();
  public ModelCollection() {

  }

  public void AddModel(Model model) {
    _arrModel.add(model);
  }

  public ArrayList<Model> getModelList() {
    return _arrModel;
  }
}
