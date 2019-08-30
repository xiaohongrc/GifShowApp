package com.hongenit.gifshowapp.comment

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.util.DateUtil
import com.hongenit.gifshowapp.util.GlobalUtil

/**
 * Feed详情界面的热门评论所使用的adapter。
 *
 */
internal class HotCommentsAdapter(private val activity: Activity, private val comments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.CommentHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.hot_comment_item, parent, false)
        val holder = CommentsAdapter.CommentHolder(view)
        val avatar = view.findViewById<ImageView>(R.id.avatar)
        val nickname = view.findViewById<TextView>(R.id.nickname)
        val goodImage = view.findViewById<ImageView>(R.id.goodImage)
        val onUserClick = View.OnClickListener {
            val position = holder.adapterPosition
            val comment = comments[position]
//            UserHomePageActivity.actionStart(activity, avatar, comment.userId, comment.nickname, comment.avatar, comment.bgImage)
        }
        avatar.setOnClickListener(onUserClick)
        nickname.setOnClickListener(onUserClick)
        goodImage.setOnClickListener {
            val position = holder.adapterPosition
            val comment = comments[position]
            var goodsCount = comment.goodsCount
            if (comment.isGoodAlready) {
                comment.isGoodAlready = false
                goodsCount--
                if (goodsCount < 0) {
                    goodsCount = 0
                }
                comment.goodsCount = goodsCount
            } else {
                comment.isGoodAlready = true
                comment.goodsCount = ++goodsCount
            }
            notifyItemChanged(position)
//            GoodComment.getResponse(comment.commentId, null)
        }
        return holder
    }

    override fun onBindViewHolder(holder: CommentsAdapter.CommentHolder, position: Int) {
        val comment = comments[position]
        val avatar = comment.avatar
        Glide.with(activity)
            .load(avatar)
            .placeholder(R.drawable.loading_bg_circle)
            .error(R.drawable.avatar_default)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(holder.avatar)
        holder.nickname.text = comment.nickname
        holder.content.text = comment.content
        holder.postDate.text = DateUtil.getConvertedDate(comment.postDate)
        if (comment.goodsCount > 0) {
            holder.goodCount.visibility = View.VISIBLE
            holder.goodCount.text = GlobalUtil.getConvertedNumber(comment.goodsCount)
        } else {
            holder.goodCount.visibility = View.INVISIBLE
        }
        if (comment.isGoodAlready) {
            holder.goodImage.setImageResource(R.drawable.ic_gooded)
        } else {
            holder.goodImage.setImageResource(R.drawable.ic_good)
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

}