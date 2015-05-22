package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.TeacherVO;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

public class TeacherInfoActivity extends Activity {
	private String id = "";
	private ZhuoConnHelper mConnHelper = null;
	private LoadImage mLoadImage = new LoadImage();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_info);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		initClick();
		loadData();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TeacherInfoActivity.this.finish();
			}
		});

	}

	public void loadData() {
		String params = ZhuoCommHelper.getUrlTeacher() + "?id=" + id;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
				TeacherInfoActivity.this, true, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						TeacherInfoActivity.this.finish();
					}
				});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (msg.obj != null && !((String) msg.obj).equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					TeacherVO item = nljh.parseTeacher();
					String name = item.getName();
					String sex = item.getSex();
					final String userid = item.getUserid();
					String level = item.getLevel();
					final String intro = item.getIntro();
					final String zjkc = item.getZjkc();
					final String skfg = item.getSkfg();
					final String jzzz = item.getJzzz();
					final String dsyl = item.getDsyl();
					final String dsmx = item.getDsmx();
					final List<PicVO> images = item.getDsfc();
					((TextView) findViewById(R.id.textViewSex)).setText(sex);
					((TextView) findViewById(R.id.textViewUsername))
							.setText(name);
					((TextView) findViewById(R.id.textViewLevel))
							.setText(level);
					if (!userid.equals(ResHelper.getInstance(
							getApplicationContext()).getUserid())) {
						findViewById(R.id.buttonChat).setVisibility(
								View.VISIBLE);
						findViewById(R.id.buttonChat).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												TeacherInfoActivity.this,
												ChatActivity.class);
										intent.putExtra("userid", userid);
										startActivity(intent);
									}
								});
					}

					findViewById(R.id.relativeLayoutIntro).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											TeacherInfoActivity.this,
											TeacherDetailActivity.class);
									intent.putExtra(
											"title",
											getString(R.string.label_teacher_intro));
									intent.putExtra("info", intro);
									startActivity(intent);
								}
							});

					findViewById(R.id.relativeLayoutLession)
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											TeacherInfoActivity.this,
											TeacherDetailActivity.class);
									intent.putExtra(
											"title",
											getString(R.string.label_teacher_lession));
									intent.putExtra("info", zjkc);
									startActivity(intent);
								}
							});

					findViewById(R.id.relativeLayoutType).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											TeacherInfoActivity.this,
											TeacherDetailActivity.class);
									intent.putExtra(
											"title",
											getString(R.string.label_teacher_type));
									intent.putExtra("info", skfg);
									startActivity(intent);
								}
							});

					findViewById(R.id.relativeLayoutProposition)
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											TeacherInfoActivity.this,
											TeacherDetailActivity.class);
									intent.putExtra(
											"title",
											getString(R.string.label_teacher_prop));
									intent.putExtra("info", jzzz);
									startActivity(intent);
								}
							});

					findViewById(R.id.relativeLayoutQuotations)
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											TeacherInfoActivity.this,
											TeacherDetailActivity.class);
									intent.putExtra(
											"title",
											getString(R.string.label_teacher_quot));
									intent.putExtra("info", dsyl);
									startActivity(intent);
								}
							});

					findViewById(R.id.relativeLayoutDream).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											TeacherInfoActivity.this,
											TeacherDetailActivity.class);
									intent.putExtra(
											"title",
											getString(R.string.label_teacher_dream));
									intent.putExtra("info", dsmx);
									startActivity(intent);
								}
							});

					if (images != null) {
						LinearLayout imagesContainer = (LinearLayout) findViewById(R.id.linearLayoutPicContainer);
						int w = imagesContainer.getMeasuredWidth();
						if (w == 0) {
							return;
						}
						int width = (w - 10) / 3;
						int marginRight = (w - width * 3) / 2;
						LayoutParams ivlp = new LayoutParams(width, width);
						ivlp.rightMargin = marginRight;
						ivlp.bottomMargin = marginRight;
						for (int i = 0; i < images.size() && i < 6; i++) {
							if (i % 3 == 0) {
								LinearLayout ll = new LinearLayout(
										TeacherInfoActivity.this);
								LayoutParams lp = new LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								ll.setLayoutParams(lp);
								imagesContainer.addView(ll);
							}
							ImageView iv = new ImageView(
									TeacherInfoActivity.this);
							iv.setLayoutParams(ivlp);
							String headurl = images.get(i).getUrl();
							final String orgUrl = images.get(i).getOrgurl();
							iv.setTag(headurl);
							iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
							iv.setImageResource(R.drawable.default_image);
							mLoadImage.addTask(headurl, iv);
							((LinearLayout) imagesContainer.getChildAt(i / 3))
									.addView(iv);
							iv.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											TeacherInfoActivity.this,
											PhotoViewMultiActivity.class);
									ArrayList<String> orgs = new ArrayList<String>();
									for (int j = 0; j < images.size(); j++) {
										orgs.add(images.get(j).getOrgurl());
									}
									intent.putStringArrayListExtra("pics", orgs);
									intent.putExtra("pic", orgUrl);
									startActivity(intent);
								}
							});
						}
						mLoadImage.doTask();
					}
				}
				break;
			}
			}
		}
	};

}
