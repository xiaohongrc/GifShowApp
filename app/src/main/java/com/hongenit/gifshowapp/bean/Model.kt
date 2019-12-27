package com.hongenit.gifshowapp.bean

import org.litepal.crud.LitePalSupport

/**
 * 所有网络通讯数据模型实体类的基类。
 *
 */
abstract class Model : LitePalSupport() {

    /**
     * 获取当前实体类的实体数据id。比如User类就获取userId，Comment类就获取commentId。
     * @return 当前实体类的实体数据id。
     */
    abstract val modelId: String

}