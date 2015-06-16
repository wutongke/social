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
	private String fundingId;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 起步价
	 */
	private String minPrice;
	/**
	 * 完成率
	 */
	private String finishRate;
	/**
	 * 图片地址
	 */
	private String imageUrl;
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
	private String endDay;
	/**
	 * 已获取筹款
	 */
	private String moneyGet;
	/**
	 * 目标
	 */
	private String moneyAim;
	/**
	 * 喜欢数量
	 */
	private String likeCount;
	/**
	 * 支持数量
	 */
	private String supportCount;
	/**
	 * 发起人
	 */
	private UserVO boss;
	/**
	 * 项目描述
	 */
	private String des;
	public static String getCrowdfundingtype() {
		return CROWDFUNDINGTYPE;
	}
	public static void setCrowdfundingtype(String crowdfundingtype) {
		CROWDFUNDINGTYPE = crowdfundingtype;
	}
	public static int getCrowdfundingmy() {
		return CROWDFUNDINGMY;
	}
	public static void setCrowdfundingmy(int crowdfundingmy) {
		CROWDFUNDINGMY = crowdfundingmy;
	}
	public static int getCrowdfundingcreate() {
		return CROWDFUNDINGCREATE;
	}
	public static void setCrowdfundingcreate(int crowdfundingcreate) {
		CROWDFUNDINGCREATE = crowdfundingcreate;
	}
	public static int getCrowdfundingquery() {
		return CROWDFUNDINGQUERY;
	}
	public static void setCrowdfundingquery(int crowdfundingquery) {
		CROWDFUNDINGQUERY = crowdfundingquery;
	}
	public String getFundingId() {
		return fundingId;
	}
	public void setFundingId(String fundingId) {
		this.fundingId = fundingId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}
	public String getFinishRate() {
		return finishRate;
	}
	public void setFinishRate(String finishRate) {
		this.finishRate = finishRate;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTotalDay() {
		return totalDay;
	}
	public void setTotalDay(String totalDay) {
		this.totalDay = totalDay;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public String getMoneyGet() {
		return moneyGet;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public void setMoneyGet(String moneyGet) {
		this.moneyGet = moneyGet;
	}
	public String getMoneyAim() {
		return moneyAim;
	}
	public void setMoneyAim(String moneyAim) {
		this.moneyAim = moneyAim;
	}
	public String getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}
	public String getSupportCount() {
		return supportCount;
	}
	public void setSupportCount(String supportCount) {
		this.supportCount = supportCount;
	}
	public UserVO getBoss() {
		return boss;
	}
	public void setBoss(UserVO boss) {
		this.boss = boss;
	}
	
}
