package com.cpstudio.zhuojiaren.model;

import java.util.List;

public class UserNewVO {
	//用户关系，对应字段relationType的去值.0:无关注  1:自己  2:单向关注  3:好友
	public enum USER_RELATION{RELATION_STRANGER,RELATION_MYSELF,RELATION_SINGL,RELATION_FRIENDS};
	
	private String signature;// 签名
	private String faith;
	private String dream;
	private List<PicNewVO> myPic;
	private int position;
	private String company;
	private String birthday;
	private int  spokesman;//<int> (代言人标识 0-不是代言人 1-是代言人) 
	private int isPhoneOpen;

	private int  constellation;

	private int hometown;

	private String hobby;

	private String registerTime;

	private int userType;// 0:普通用户 1:导师 2:代理商

	private int city;

	private int isBirthdayOpen;

	private String birthdayLunar;

	private int married;

	private String weixin;

	private int isEmailOpen;

	private int zodiac;

	private String email;

	private int isWeixinOpen;

	private String name;

	private String uheader;

	private int gender;

	private String travelCity;

	private String lastLoginTime;

	private String qq;

	private int isQqOpen;

	private String addtime;
	int industry;
	private String phone;
	private String userid;
	int isFree;// (0:收费用户 1:免费用户)
	int role;// 我在圈子中的身份类型

	String qrcode; // 二维码图片url
	int friendNum; // (好友个数 在动态页面中使用)
	int statusNum; // (动态/文章个数 在动态页面中使用)
	
	//还需增加一个字段:mType;
	
	int relation;//0:陌生人。1：好友。2：自己
	
	private List<PicNewVO> photo;// (照片 对多10张)

	//允许加入圈子时对应的圈子
	private String groupid;
	
	private String gname;
	
	private String zhuobi;
	private String bgpic;
	
	public String getBgpic() {
		return bgpic;
	}

	public void setBgpic(String bgpic) {
		this.bgpic = bgpic;
	}

	public String getZhuobi() {
		return zhuobi;
	}

	public int getSpokesman() {
		return spokesman;
	}

	public void setSpokesman(int spokesman) {
		this.spokesman = spokesman;
	}

	public void setZhuobi(String zhuobi) {
		this.zhuobi = zhuobi;
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

	public String getUserid() {
		return userid;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}

	public int getStatusNum() {
		return statusNum;
	}

	public void setStatusNum(int statusNum) {
		this.statusNum = statusNum;
	}

	public List<PicNewVO> getPhoto() {
		return photo;
	}

	public void setPhoto(List<PicNewVO> photo) {
		this.photo = photo;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getFaith() {
		return faith;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setFaith(String faith) {
		this.faith = faith;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<PicNewVO> getMyPic() {
		return myPic;
	}

	public void setMyPic(List<PicNewVO> myPic) {
		this.myPic = myPic;
	}

	public String getDream() {
		return dream;
	}

	public void setDream(String dream) {
		this.dream = dream;
	}

	public int getIndustry() {
		return industry;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setIsPhoneOpen(int isPhoneOpen) {
		this.isPhoneOpen = isPhoneOpen;
	}

	public int getIsPhoneOpen() {
		return this.isPhoneOpen;
	}

	public void setConstellation(int constellation) {
		this.constellation = constellation;
	}

	public int getConstellation() {
		return this.constellation;
	}

	public void setHometown(int hometown) {
		this.hometown = hometown;
	}

	public int getHometown() {
		return this.hometown;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getHobby() {
		return this.hobby;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getRegisterTime() {
		return this.registerTime;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getUserType() {
		return this.userType;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getCity() {
		return this.city;
	}

	public void setIsBirthdayOpen(int isBirthdayOpen) {
		this.isBirthdayOpen = isBirthdayOpen;
	}

	public int getIsBirthdayOpen() {
		return this.isBirthdayOpen;
	}

	public void setBirthdayLunar(String birthdayLunar) {
		this.birthdayLunar = birthdayLunar;
	}

	public String getBirthdayLunar() {
		return this.birthdayLunar;
	}

	public void setMarried(int married) {
		this.married = married;
	}

	public int getMarried() {
		return this.married;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getWeixin() {
		return this.weixin;
	}

	public void setIsEmailOpen(int isEmailOpen) {
		this.isEmailOpen = isEmailOpen;
	}

	public int getIsEmailOpen() {
		return this.isEmailOpen;
	}

	public void setZodiac(int zodiac) {
		this.zodiac = zodiac;
	}

	public int getZodiac() {
		return this.zodiac;
	}

	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}

	public int getIsFree() {
		return this.isFree;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setIsWeixinOpen(int isWeixinOpen) {
		this.isWeixinOpen = isWeixinOpen;
	}

	public int getIsWeixinOpen() {
		return this.isWeixinOpen;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	public String getUheader() {
		return this.uheader;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getGender() {
		return this.gender;
	}

	public void setTravelCity(String travelCity) {
		this.travelCity = travelCity;
	}

	public String getTravelCity() {
		return this.travelCity;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getQq() {
		return this.qq;
	}

	public void setIsQqOpen(int isQqOpen) {
		this.isQqOpen = isQqOpen;
	}

	public int getIsQqOpen() {
		return this.isQqOpen;
	}

}