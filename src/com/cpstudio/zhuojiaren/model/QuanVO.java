package com.cpstudio.zhuojiaren.model;


public class QuanVO {
	// lz 0801
	/**
	 * �˳�Ȧ��
	 */
	public final static int QUAN_QUIT = 0;
	/**
	 * ����Ȧ��
	 */
	public final static int QUAN_JOIN = 1;
	/*
	 * ����ĳ�˼���Ȧ��
	 */
	public final static int QUAN_PERMIT = 2;

	/*
	 * ����ĳ�˼���Ȧ�� ��ɫ 0-δ���� 1-��ͨ��Ա 2-����Ա 3-Ȧ��
	 */
	public final static String[] QUAN_ROLE_NAME={"������Ȧ","��ͨ��Ա","����Ա","Ȧ��"};
	
	public final static String QUANROLE = "quanzirole";
	public final static int QUAN_ROLE_NOTMEMBER = -1;
	public final static int QUAN_ROLE_YOUKE = 0;
	public final static int QUAN_ROLE_MEMBER = 1;
	public final static int QUAN_ROLE_MANAGER = 2;
	public final static int QUAN_ROLE_OWNER = 3;

	// Ȧ������
	public final static String QUANZITYPE = "quanzitype";
	public final static int QUANZIMYCTEATE = 6;
	public final static int QUANZIMYADD = 7;
	public final static int QUANZIRECOMMEND = 2;
	public final static int QUANZIQUERY = 3;

	// Ȧ����ҳ�б�����
	public final static String QUANZIMAINTYPE = "quanzitype";
	public final static int QUANZITOPIC = 7;
	public final static int QUANZIEVENT = 8;
	public final static int QUANZIMEMBER = 9;

	private String groupid;
	private String gname;
	private String gheader;
	private String gintro;

	private String gpub;

	private int gtype;

	private String addtime;
	private int memberCount;
	private int topicCount;
	private String lastmsgtime;
	private String alert;
	private String userid;
	private String name;
	private String uheader;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUheader() {
		return uheader;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	int city;// (���� ���б���)
	int followpms;// ����Ȩ�� 0:�����κ��˼��룬1:��Ҫ������ܼ���) ,
	int accesspms;// (����Ȩ�� 0:�����˶����Է��ʣ�1:����Ȧ�Ӳſ��Է���) ,

	int role;// ��ɫ 0-δ���� 1-��ͨ��Ա 2-����Ա 3-Ȧ��

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getGheader() {
		return gheader;
	}

	public void setGheader(String gheader) {
		this.gheader = gheader;
	}

	public String getGintro() {
		return gintro;
	}

	public void setGintro(String gintro) {
		this.gintro = gintro;
	}

	public String getGpub() {
		return gpub;
	}

	public void setGpub(String gpub) {
		this.gpub = gpub;
	}

	public int getGtype() {
		return gtype;
	}

	public void setGtype(int gtype) {
		this.gtype = gtype;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public int getTopicCount() {
		return topicCount;
	}

	public void setTopicCount(int topicCount) {
		this.topicCount = topicCount;
	}

	public String getLastmsgtime() {
		return lastmsgtime;
	}

	public void setLastmsgtime(String lastmsgtime) {
		this.lastmsgtime = lastmsgtime;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getFollowpms() {
		return followpms;
	}

	public void setFollowpms(int followpms) {
		this.followpms = followpms;
	}

	public int getAccesspms() {
		return accesspms;
	}

	public void setAccesspms(int accesspms) {
		this.accesspms = accesspms;
	}

}
