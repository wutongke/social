package com.cpstudio.zhuojiaren.model;

public class CrowdFundingVO {
	// �ڳ�����
	public static String CROWDFUNDINGTYPE = "crowdfunding";
	public static int CROWDFUNDINGMY = 1;
	public static int CROWDFUNDINGCREATE = 2;
	public static int CROWDFUNDINGQUERY = 3;
	public static int CROWDFUNDINGTECH = 4;
	public static int CROWDFUNDINGPUBLISH = 5;
	public static int CROWDFUNDIGAMUSEMENT = 6;
	public static int CROWDFUNDINGART = 7;
	public static int CROWDFUNDINGAGRICULTURE = 8;
	public static int CROWDFUNDINGCRAFT = 9;
	
	public static String[] typeStr = {"","�ҷ���","��Ͷ��","","�Ƽ�","����","����","����","ũҵ","����"};
	
	private String fundingId;
	private String name;
	/**
	 * �𲽼�
	 */
	private String minPrice;
	/**
	 * �����
	 */
	private String finishRate;
	/**
	 * ͼƬ��ַ
	 */
	private String imageUrl;
	/**
	 * ���ͣ��Ƿ����ҷ���ġ���Ͷ�ʵ�
	 */
	private String type;
	private String isfinish;
	private String totalDay;
	/**
	 * ���ж��������
	 */
	private String endDay;
	/**
	 * �ѻ�ȡ���
	 */
	private String moneyGet;
	/**
	 * Ŀ��
	 */
	private String moneyAim;
	private String likeCount;
	private String supportCount;
	private UserVO boss;
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
	public String getIsfinish() {
		return isfinish;
	}
	public void setIsfinish(String isfinish) {
		this.isfinish = isfinish;
	}
	public String getTotalDay() {
		return totalDay;
	}
	public void setTotalDay(String totalDay) {
		this.totalDay = totalDay;
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
