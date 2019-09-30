package com.hongenit.gifshowapp.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import com.hongenit.gifshowapp.R
import kotlinx.android.synthetic.main.dialog_edit_remark.*


/**
 * Created by Xiaohong on 2019/4/16.
 * desc:
 */
class EditRemarkDialog(context: Context, val oldName: String?) : Dialog(context, R.style.Dialog_FS),
    View.OnClickListener, TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val empty = TextUtils.isEmpty(et_change_name.text.toString())

        ib_clear_content.visibility = if (empty) View.GONE else View.VISIBLE
        count_tip.text =
            "${et_change_name.text.toString().length}/${context.resources.getInteger(R.integer.username_max_length)}"
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.nickname_confirm -> {
                listener?.confirm(et_change_name.text.toString().trim())
                et_change_name.setText("")
                dismiss()
            }
            R.id.ib_clear_content -> {
                et_change_name.setText("")
                ib_clear_content.visibility = View.GONE
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_remark)
        initView()

    }

    private fun initView() {
        ib_clear_content.setOnClickListener(this)
        nickname_confirm.setOnClickListener(this)
        et_change_name.requestFocus()
        et_change_name.addTextChangedListener(this)
        et_change_name.setSelection(0)
        if (oldName != null) {
            et_change_name.setText(oldName)
            val maxNameLength = context.resources.getInteger(R.integer.username_max_length)
            et_change_name.setSelection(if (oldName.length > maxNameLength) maxNameLength else oldName.length)
        }
        setCanceledOnTouchOutside(true)
    }


    interface IActionListener {
        fun close()

        fun confirm(nickname: String)
    }

    private var listener: IActionListener? = null

    fun setOnIActionListener(listener: IActionListener) {
        this.listener = listener
    }

}