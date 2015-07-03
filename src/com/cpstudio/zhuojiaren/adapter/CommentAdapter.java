package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CommentVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class CommentAdapter extends CommonAdapter<CommentVO>{
	private LoadImage mLoadImage = new LoadImage(10);
	private ZhuoConnHelper mConnHelper = null;
	public CommentAdapter(Context context, List<CommentVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, CommentVO item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.ic_content, item.getContent());
		helper.setText(R.id.ic_people_company, item.getUser().getCompany());
		helper.setText(R.id.ic_people_name, item.getUser().getUsername());
		helper.setText(R.id.ic_people_position,item.getUser().getPost());
		helper.setText(R.id.ic_time, item.getTime());
		helper.setImageResource(R.id.ic_people_image, R.drawable.ico_chat_card);
		mLoadImage.addTask(item.getUser().getUheader(),(ImageView)helper.getView(R.id.ic_people_image));
		mLoadImage.doTask();
		//回复
		LinearLayout replyLayout = (LinearLayout)helper.getView(R.id.ic_reply_layout);
		replyLayout.setVisibility(View.GONE);
		if(item.getReplyUser()!=null){
			replyLayout.setVisibility(View.VISIBLE);
			helper.setText(R.id.ic_reply_name, item.getReplyUser().getUsername());
		}
		//点赞
		helper.setImageResource(R.id.ic_praise, R.drawable.zhan_crowd_cmt);
		if(item.getIsPraise()!=null&&item.getIsPraise().equals(CommentVO.praise)){
			helper.setImageResource(R.id.ic_praise, R.drawable.zhan2_crowd_cmt);
		}
		helper.getView(R.id.ic_praise).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点赞
			}
		});
		helper.getView(R.id.ic_reply).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//回复
			}
		});
	}

}
