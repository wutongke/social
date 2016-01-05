package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.AudioAdapter;
import com.cpstudio.zhuojiaren.adapter.EventListAdapter;
import com.cpstudio.zhuojiaren.adapter.GrouthAdapter;
import com.cpstudio.zhuojiaren.adapter.QuanziTopicListAdapter;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.GrouthVedio;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.ViewHolder;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;


/***
 * 
 * @author lef
 * 
 */
public class MyCollectionActivity extends BaseActivity {
	@InjectView(R.id.amc_pulldownview)
	PullDownView pullDownView;
	ListView listView;
	// 收藏录音
	private ArrayList<RecordVO> mDatas = new ArrayList<RecordVO>();
	BaseAdapter mAdapter;
	TableLayout table;
	private ArrayList<ToggleButton> buttonList;
	private int width = 720;
	private float times = 2;
	private int padding = 5;
	private int baseMargin = 19;
	private Activity mContext;

	private String url;
	private int handlerTag;
	// 分页
	private int mPage = 0;
	private AppClient appClientLef;
	private final int vedio = 1;
	private final int radio = 2;
	private final int event = 3;
	private final int topic = 4;
	private final int people = 5;
	private final int gong = 6;
	private final int xu = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		ButterKnife.inject(this);
		appClientLef = AppClient.getInstance(this);
		mContext = this;
		initTitle();
		title.setText(R.string.my_collect);
		initTableLayout();
	}

	private void initTableLayout() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(mContext,
				R.layout.head_collection);
		listView = pullDownView.getListView();
		table = (TableLayout) findViewById(R.id.amc_tableLayout);
		pullDownView.setShowHeader(false);
		pullDownView.setShowFooter(false);
		pullDownView.noFoot();
		mAdapter = new AudioAdapter(mContext, mDatas, R.layout.item_radio);
		listView.setAdapter(mAdapter);

		pullDownView.setOnPullDownListener(new OnPullDownListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadData();
			}

			@Override
			public void onMore() {
				// TODO Auto-generated method stub
				 loadData();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mAdapter != null)
					((AudioAdapter) mAdapter).stop();
				Intent intent = new Intent(MyCollectionActivity.this,
						AudioDetailActivity.class);
				intent.putExtra("id", mDatas.get(position - 1).getId());
				startActivity(intent);
			}
		});
		loadData();
		// 收藏列表，初始tablelayout
		buttonList = new ArrayList<ToggleButton>();
		LayoutInflater inflater = LayoutInflater
				.from(MyCollectionActivity.this);

		this.width = DeviceInfoUtil.getDeviceCsw(getApplicationContext());
		this.times = DeviceInfoUtil.getDeviceCsd(getApplicationContext());
		padding = (int) (padding * times);
		baseMargin = (int) (baseMargin * times);
		int restWidth = width - 2 * padding - 3 * baseMargin;
		int widthOne = restWidth / 4;
		baseMargin = (int) ((restWidth - widthOne * 4) / 3) + baseMargin;
		table.setPadding(padding, 0, padding, 0);
		String[] items = getResources().getStringArray(R.array.my_collection);
		TableRow tr = null;
		for (int i = 0; i < items.length; i++) {
			if (i % 4 == 0) {
				tr = new TableRow(MyCollectionActivity.this);
				table.addView(tr);
			}
			final ToggleButton tb = (ToggleButton) inflater.inflate(
					R.layout.item_toggle, null);
			buttonList.add(tb);
			tb.setTag(i);
			tb.setText(items[i]);
			tb.setTextOn(items[i]);
			tb.setTextOff(items[i]);
			/**
			 * 直接设置check不太好用，设置onclick测试可用
			 */
			tb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tb.setChecked(true);
					handlerTag = (Integer) (v.getTag()) + 1;
					url = ZhuoCommHelper.collectionUrls[handlerTag - 1];
					loadData();
				}
			});
			tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						for (ToggleButton temp : buttonList) {
							if (temp.getTag().equals(buttonView.getTag())) {
								buttonView.setTextColor(Color.rgb(0, 72, 255));
							} else {
								temp.setChecked(false);
							}
						}
					} else {
						buttonView.setTextColor(Color.rgb(52, 52, 52));
					}
				}
			});
			tr.addView(tb);
			TableRow.LayoutParams trlp = new TableRow.LayoutParams(widthOne,
					TableRow.LayoutParams.WRAP_CONTENT);
			trlp.rightMargin = baseMargin;
			trlp.bottomMargin = baseMargin * 3 / 8;
			trlp.topMargin = baseMargin * 3 / 8;
			tb.setLayoutParams(trlp);
		}
	}

	private void loadData() {
		switch(handlerTag){
		case vedio :
			appClientLef.getVedioCollectionList(null, null, mPage, 100, uiHandler,
					vedio, (Activity)mContext, true, null, null);
			break;
		case radio:
			appClientLef.getAudioListCollection(mPage, 100, uiHandler,
					radio,(Activity) mContext, true, null, null);
			break;
		case event:
			connHelper.getQuanEventListCollection(uiHandler, event,
					"1", null, mPage, 100);
			break;
		case topic:
			connHelper.getTopicListCollection(uiHandler, topic,
					mPage, 100);
			break;
		case people:
			connHelper.getPeopleListCollection(uiHandler, people,mPage,100);
			break;
		case gong:
			connHelper.getGongListCollection(uiHandler, gong, mPage, 100);
			break;
		case xu:
			connHelper.getGongListCollection(uiHandler, gong, mPage, 100);
			break;
		}
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pullDownView.finishLoadData(true);
			ResultVO res;
			if (JsonHandler.checkResult((String) msg.obj,
					MyCollectionActivity.this)) {
				res = JsonHandler.parseResult((String) msg.obj);
			} else {
				return;
			}
			String data = res.getData();
			JsonHandler nljh = new JsonHandler((String)msg.obj, mContext
					.getApplicationContext());
			switch (msg.what) {
			case radio:
				mDatas = JsonHandler_Lef
						.parseAudioList(data);
				mAdapter = new AudioAdapter(mContext, mDatas,
						R.layout.item_radio);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override 
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (mAdapter != null)
							((AudioAdapter) mAdapter).stop();
						Intent intent = new Intent(mContext,
								AudioDetailActivity.class);
						intent.putExtra("audio", mDatas.get(position-1));
						intent.putExtra("id", mDatas.get(position - 1).getId());
						startActivity(intent);
					}
				});
				break;
			case vedio:
				final ArrayList<GrouthVedio> list = JsonHandler_Lef
						.parseGrouthVedioList(data);
				mAdapter = new GrouthAdapter(mContext, list,
						R.layout.item_growth);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MyCollectionActivity.this,
								VedioActivity.class);
						intent.putExtra("vedio", list.get(position - 1).getId());
						startActivity(intent);
					}
				});
				break;
			case event:
				
				final ArrayList<EventVO> eventList = nljh.parseEventInfoList();
				
				mAdapter = new EventListAdapter(mContext, eventList);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position >= eventList.size())
							return;
						Intent i = new Intent(mContext, EventDetailActivity.class);
						i.putExtra("eventId", eventList.get(position).getActivityid());
						startActivity(i);
					}
				});
				break;
			case topic:
				final ArrayList<QuanTopicVO> topicList = nljh.parseQuanTopicList();
				mAdapter = new QuanziTopicListAdapter(mContext,topicList,2);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position >= topicList.size())
							return;
						Intent i = new Intent();
						QuanTopicVO vo = topicList.get(position);
						i.setClass(mContext, TopicDetailActivity.class);
						i.putExtra("topicid", vo.getTopicid());
						startActivity(i);
					}
				});
				break;
			case gong:
				final ArrayList<ResourceGXVO> gongList = (ArrayList<ResourceGXVO>) nljh
				.parseGongxuList();
				mAdapter = new MyResListAdapterListAdapter(
						mContext, gongList);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position >= 0) {
							Intent i = new Intent(mContext,
									GongXuDetailActivity.class);
							i.putExtra("msgid",gongList.get(position ).getSdid());
							startActivity(i);
						}
					}
				});
				break;
			case xu:
				final ArrayList<ResourceGXVO> xuList = (ArrayList<ResourceGXVO>) nljh
				.parseGongxuList();
				mAdapter = new MyResListAdapterListAdapter(
						mContext, xuList);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position >= 0) {
							Intent i = new Intent(mContext,
									GongXuDetailActivity.class);
							i.putExtra("msgid",xuList.get(position ).getSdid());
							startActivity(i);
						}
					}
				});
				break;
			case people:
				ArrayList<UserNewVO> mList = nljh.parseUserNewList();
				final LoadImage mLoad = LoadImage.getInstance();
				final BaseCodeData baseDataSet = 
						ConnHelper.getInstance(getApplicationContext()).getBaseDataSet();
				mAdapter = new CommonAdapter<UserNewVO>(mContext, mList,
						R.layout.item_myfriends_list) {
					@Override
					public void convert(ViewHolder helper, final UserNewVO item) {
						// TODO Auto-generated method stub
						helper.getConvertView().setTag(R.id.tag_id, item.getUserid());
						helper.setCheckBox(R.id.isChecked, false, View.GONE);
						helper.setText(R.id.izul_name, item.getName());
						String position = "";
						if (baseDataSet != null) {
							int pos = item.getPosition();
							if (pos != 0)
								pos--;
							position = ((baseDataSet.getPosition()).get(pos))
									.getContent();
						}
						helper.setText(R.id.izul_position, position);// 职位
						helper.setText(R.id.izul_company, item.getCompany());
						// CommonUtil.calcTimeToNow(time)
						helper.setText(R.id.tvTime, item.getRegisterTime());
						ImageView iv = helper.getView(R.id.izul_image);
						mLoad.beginLoad(item.getUheader(), iv);
					}
				};
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (id != -1) {
							Intent i = new Intent(mContext,
									ZhuoMaiCardActivity.class);
							i.putExtra("userid", (String) view.getTag(R.id.tag_id));
							startActivity(i);
						}
					}
				});
			}
		};
	};
	
}
