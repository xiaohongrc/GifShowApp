package com.hongenit.gifshowapp.extension

import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.bean.Model
import kotlin.concurrent.thread

/**
 * 查询Model并回调集合中第一条符合给定参数条件元素的下标，如未查到则回调-1。
 */
fun <T : Model> findModelIndex(models: List<T>?, modelId: String, action: (index: Int) -> Unit) {
    thread {
        var index = -1
        if (models != null && models.isNotEmpty()) {
            for (i in models.indices) {
                val model = models[i]
                if (model.modelId == modelId) {
                    index = i
                    break
                }
            }
        }
        GlobalParam.handler.post {
            action(index)
        }
    }
}

/**
 * 查询Model并回调集合中第一条符合给定参数条件元素的下标，如未查到则不进行回调。
 */
fun <T : Model> searchModelIndex(models: List<T>?, modelId: String, action: (index: Int) -> Unit) {
    thread {
        var index = -1
        if (models != null && models.isNotEmpty()) {
            for (i in models.indices) {
                val model = models[i]
                if (model.modelId == modelId) {
                    index = i
                    break
                }
            }
        }
        if (index != -1) {
            GlobalParam.handler.post {
                action(index)
            }
        }
    }
}