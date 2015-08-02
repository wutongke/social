package com.cpstudio.zhuojiaren.model;

public class CrowdFundingVO {
	// 众筹类型
	public static String CROWDFUNDINGTYPE = "crowdfunding";
	public static String CROWDFUNDINGID = "crowdfundingid";
	//众筹 的各种类型
	public static int CROWDFUNDINGMY = 1;
	public static int CROWDFUNDINGCREATE = 2;
	public static int CROWDFUNDINGQUERY = 3;
	public static int CROWDFUNDINGTECH = 4;
	public static int CROWDFUNDINGPUBLISH = 5;
	public static int CROWDFUNDIGAMUSEMENT = 6;
	public static int CROWDFUNDINGART = 7;
	public static int CROWDFUNDINGAGRICULTURE = 8;
	public static int CROWDFUNDINGCRAFT = 9;
	
	//handler 消息类型
	
	
	public static String[] typeStr = {"","我发起","我投资","科技","出版","娱乐","艺术","农业","工艺"};
	/**
	 * id
	 */
	private String id;
	/**
	 * 名字
	 */
	private String title;
	/**
	 * 起步价
	 */
	private String minSupport;
	/**
	 * 完成率
	 */
	private String finishRate;
	/**
	 * 图片地址
	 */
	private String thumbPic;
	/**
	 * 类型：是否是我发起的、我投资的
	 */
	private String type;
	/**
	 * 完成状态
	 */
	private String state;
	/**
	 * 是否完成
	 */
	private String isFinish;
	/**
	 * 总共多少天
	 */
	private String totalDay;
	/**
	 * 还有多少天完成
	 */
	private String remainDay;
	/**
	 * 已获取筹款
	 */
	private String reach;
	/**
	 * 目标
	 */
	private String targetZb;
	/**
	 * 喜欢数量
	 */
	private String likeNum;
	/**
	 * 支持数量
	 */
	private String supportNum;
	/**
	 * 项目描述
	 */
	private String description;
	
	private String company;
	private String name;
	private String uheader;
	private String position;
	
	public String getCompany() {
		return company;
	}
	public String getName() {
		return name;
	}
	public String getUheader() {
		return uheader;
	}
	public String getPosition() {
		return position;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUheader(String uheader) {
		this.uheader = uheader;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public static String[] getTypeStr() {
		return typeStr;
	}
	public String getTitle() {
		return title;
	}
	public String getMinSupport() {
		return minSupport;
	}
	public String getFinishRate() {
		return finishRate;
	}
	public String getThumbPic() {
		return thumbPic;
	}
	public String getType() {
		return type;
	}
	public String getState() {
		return state;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public String getTotalDay() {
		return totalDay;
	}
	public String getRemainDay() {
		return remainDay;
	}
	public String getReach() {
		return reach;
	}
	public String getTargetZb() {
		return targetZb;
	}
	public String getLikeNum() {
		return likeNum;
	}
	public String getSupportNum() {
		return supportNum;
	}
	public String getDescription() {
		return description;
	}
	public static void setTypeStr(String[] typeStr) {
		CrowdFundingVO.typeStr = typeStr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setMinSupport(String minSupport) {
		this.minSupport = minSupport;
	}
	public void setFinishRate(String finishRate) {
		this.finishRate = finishRate;
	}
	public void setThumbPic(String thumbPic) {
		this.thumbPic = thumbPic;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	public void setTotalDay(String totalDay) {
		this.totalDay = totalDay;
	}
	public void setRemainDay(String remainDay) {
		this.remainDay = remainDay;
	}
	public void setReach(String reach) {
		this.reach = reach;
	}
	public void setTargetZb(String targetZb) {
		this.targetZb = targetZb;
	}
	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}
	public void setSupportNum(String supportNum) {
		this.supportNum = supportNum;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
