package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CommentVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class CommentAdapter extends CommonAdapter<CommentVO> {
	private LoadImage mLoadImage = new LoadImage(10);
	private AppClientLef mConnHelper = null;
	private ReplyInterface reply;

	public interface ReplyInterface {
		public void reply(String id, String userid,String name);
	}

	public CommentAdapter(Context context, List<CommentVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
		mConnHelper = AppClientLef.getInstance(context);
	}

	@Override
	public void convert(final ViewHolder helper, final CommentVO item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.ic_content, item.getContent());
		helper.setText(R.id.ic_people_company, item.getCompany());
		helper.setText(R.id.ic_people_name, item.getName());
		helper.setText(R.id.ic_people_position, item.getPosition());
		helper.setText(R.id.ic_time, item.getAddtime());
		helper.setImageResource(R.id.ic_people_image, R.drawable.ico_chat_card);
		mLoadImage.addTask(item.getUheader(),
				(ImageView) helper.getView(R.id.ic_people_image));
		mLoadImage.doTask();
		// 回复
		LinearLayout replyLayout = (LinearLayout) helper
				.getView(R.id.ic_reply_layout);
		replyLayout.setVisibility(View.GONE);
		if (item.getToId() != null) {
			replyLayout.setVisibility(View.VISIBLE);
			helper.setText(R.id.ic_reply_name, item.getToName()
					);
		}
		// 点赞
		helper.setImageResource(R.id.ic_praise, R.drawable.zhan_crowd_cmt);
		if (item.getIsPraise() != null
				&& item.getIsPraise().equals(CommentVO.praise)) {
			helper.setImageResource(R.id.ic_praise, R.drawable.zhan2_crowd_cmt);
		}
		helper.getView(R.id.ic_praise).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 点赞
						if (item.getIsPraise() != null
								&& !item.getIsPraise().equals(CommentVO.praise)){
							mConnHelper.collection(
									ZhuoCommHelper.getLikeincomment(), "id",
									item.getId(), "isLike", CommentVO.praise);
							item.setIsPraise(CommentVO.nopraise);
							helper.setImageResource(R.id.ic_praise, R.drawable.zhan2_crowd_cmt);
						}else{
							mConnHelper.collection(
									ZhuoCommHelper.getLikeincomment(), "id",
									item.getId(), "isLike", CommentVO.nopraise);
							item.setIsPraise(CommentVO.praise);
							helper.setImageResource(R.id.ic_praise, R.drawable.zhan_crowd_cmt);
						}
							
					}
				});
		helper.getView(R.id.ic_reply).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 回复
				if (reply != null) {
					reply.reply(item.getId(),item.getUserid(), item.getToName());
				}
			}
		});
	}

	public ReplyInterface getReply() {
		return reply;
	}

	public void setReply(ReplyInterface reply) {
		this.reply = reply;
	}

}
