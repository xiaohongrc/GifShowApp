package com.hongenit.gifshowapp.widgets

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.TextUtils

import com.hongenit.gifshowapp.R


/**
 * Created by hongenit on 2019/9/30.
 * desc: 系统弹窗的封装类
 */
class SystemDialog {


    class Builder(private val mContext: Context) {
        private var mTitle: CharSequence? = null
        private var mMessage: CharSequence? = null
        private var mPositive: CharSequence? = null
        private var mNegative: CharSequence? = null
        private var mPositiveListener: DialogInterface.OnClickListener? = null
        private var mNegativeListener: DialogInterface.OnClickListener? = null
        private var cancelable: Boolean = false

        init {
            mPositive = mContext.getString(R.string.confirm)
            mNegative = mContext.getString(R.string.cancel)
        }

        fun setTitle(title: CharSequence): SystemDialog.Builder {
            mTitle = title
            return this
        }

        fun setMessage(message: CharSequence): SystemDialog.Builder {
            mMessage = message
            return this
        }

        fun setCancelable(cancelable: Boolean): SystemDialog.Builder {
            this.cancelable = cancelable
            return this
        }

        fun setPositive(
            positive: CharSequence,
            listener: DialogInterface.OnClickListener
        ): SystemDialog.Builder {
            mPositive = positive
            mPositiveListener = listener
            return this
        }

        fun setNegative(
            negative: CharSequence,
            listener: DialogInterface.OnClickListener
        ): SystemDialog.Builder {
            mNegativeListener = listener
            mNegative = negative
            return this
        }

        fun setTitle(title: Int): SystemDialog.Builder {
            return setTitle(mContext.getString(title))
        }

        fun setMessage(message: Int): SystemDialog.Builder {
            return setMessage(mContext.getString(message))
        }

        fun setPositive(
            positive: Int,
            listener: DialogInterface.OnClickListener
        ): SystemDialog.Builder {
            return setPositive(mContext.getString(positive), listener)
        }

        fun setNegative(
            negative: Int,
            listener: DialogInterface.OnClickListener
        ): SystemDialog.Builder {
            return setNegative(mContext.getString(negative), listener)
        }

        fun setButtonListener(
            positiveListener: DialogInterface.OnClickListener,
            negativeListener: DialogInterface.OnClickListener
        ): SystemDialog.Builder {
            mPositiveListener = positiveListener
            mNegativeListener = negativeListener
            return this
        }

        fun build(): AlertDialog {
            val builder = AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog)
            builder.setCancelable(cancelable)
            if (!TextUtils.isEmpty(mTitle)) {
                builder.setTitle(mTitle)
            }
            if (!TextUtils.isEmpty(mMessage)) {
                builder.setMessage(mMessage)
            }
            if (!TextUtils.isEmpty(mNegative) && mNegativeListener != null) {
                builder.setNegativeButton(mNegative, mNegativeListener)
            }
            if (!TextUtils.isEmpty(mPositive) && mPositiveListener != null) {
                builder.setPositiveButton(mPositive, mPositiveListener)
            }
            return builder.create()
        }
    }
}
