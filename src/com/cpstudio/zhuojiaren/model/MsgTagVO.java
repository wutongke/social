package com.cpstudio.zhuojiaren.model;

public class MsgTagVO {
	/**
	 * 初次加载首页数据
	 */
	public static final int DATA_LOAD = 0;
	/**
	 * 刷新
	 */
	public static final int DATA_REFRESH = 1;
	/**
	 * 加载更多
	 */
	public static final int DATA_MORE = 2;
	public static final int DATA_OTHER = 3;

	public static final int MSG_COLLECT = 7;
	public static final int MSG_FOWARD = 8;
	public static final int MSG_LIKE = 9;
	public static final int MSG_DEL = 15;
	public static final int MSG_RCMDUSER = 23;
	public static final int MSG_CMT = 24;

	public static final int ACCEPT_APPLY = 13;
	public static final int REFUSE_APPLY = 14;
	public static final int ACCEPT_INVENT = 16;
	public static final int REFUSE_INVENT = 17;
	public static final int ACCEPT_RCMD = 18;
	public static final int REFUSE_RCMD = 19;

	public static final int PUB_INFO = 5;
	/**
	 * 接受到新消息
	 */
	public static final int START_SEND = 21;

	public static final int UPDATE_LOCAL = 6;
	public static final int UPDATE = 20;

	public final static int INIT_SELECT = 4;
	public static final int USER_SELECT = 22;

	public static final int ADD_BACK = 10;
	public static final int PROSECUTE = 11;
	public static final int FOLLOW_QUAN = 12;

	public static final int SELECT_PICTURE = 25;
	public static final int SELECT_CAMER = 26;
	public static final int CAPTRUE_CAMER = 27;
	public static final int CAMERA_REQUEST = 28;
	public static final int ADD_USER = 29;
	/**
	 * 获取验证码和提交验证码
	 */
	public static final int GET_VERIFICATIONCODE = 1;
	public static final int SUBMIT_VERIFICATIONCODE = 2;
	/**
	 * 众筹
	 */
	public static final int INIT = 200;
	public static final int SCROLLVIEW = 201;

	public static final int HOT_WORD = 100;

	// lz供需，数据
	// 供需 的各种类型
	public static int RESOURCE_FIND = 1;// 寻找资源
	public static int NEED_FIND = 2;// 发现需求
	public static int PUB_NEED_RESOURCE = 3;// 供需发布
	
	public static final int FLIP = 99;// 广告图片滚动
	
	
	/**
	 * 从倬脉图片
	 */
	public static int ZHUOMAI_PIC = 401;// 供需发布
	/**
	 * 圈子操作
	 */
	public static final int disolve_quan = 501;
	public static final int out_quan = 502;
	public static final int join_quan = 503;
}
