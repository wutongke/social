package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class UserVO {
	private String userid;
	private String userpwd;
	private String username;
	/**
	 * 头像地址
	 */
	private String uheader;
	private String sex;
	private String company;
	/**
	 * 职位
	 */
	private String post;
	private String industry;
	private String city;
	private String hometown;
	private String travel_cities;
	private String birthday;
	private String birthday_type;
	private String constellation;
	private String maxim;
	private String hobby;
	private String email;
	private String learn_exp;
	private String website;
	private String join_zhuo_date;
	private String pinyin;
	private String startletter;
	private String level;
	private String productotal;
	private String familytotal;
	private String grouptotal;
	private String isfollow;
	private String isemailopen;
	private String isphoneopen;
	private String isworking;
	private String isisentrepreneurship;
	private String ismarry;
	private String offertotal;
	private String lastoffer;
	private String lastdemand;
	private String id;
	private String classissure;
	private String isbirthdayopen;
	private String activenum;
	private String fannum;
	private String follownum;
	private String age;
	private String isread;
	private String jifen;
	private String phone;
	private String mycustomer;
	private String isalert;
	private List<ProductVO> product = new ArrayList<ProductVO>();
	private List<UserVO> family = new ArrayList<UserVO>();
	private List<QuanVO> groups = new ArrayList<QuanVO>();
	private List<ZhuoInfoVO> growth = new ArrayList<ZhuoInfoVO>();
	private List<DreamVO> dream = new ArrayList<DreamVO>();
	private List<PicVO> pics = new ArrayList<PicVO>();

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public String getStartletter() {
		return startletter;
	}

	public void setStartletter(String startletter) {
		this.startletter = startletter;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getUheader() {
		return uheader;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getJifen() {
		return jifen;
	}

	public void setJifen(String jifen) {
		this.jifen = jifen;
	}

	public String getActivenum() {
		return activenum;
	}

	public void setActivenum(String activenum) {
		this.activenum = activenum;
	}

	public String getFannum() {
		return fannum;
	}

	public void setFannum(String fannum) {
		this.fannum = fannum;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIsworking() {
		return isworking;
	}

	public void setIsworking(String isworking) {
		this.isworking = isworking;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsisentrepreneurship() {
		return isisentrepreneurship;
	}

	public void setIsisentrepreneurship(String isisentrepreneurship) {
		this.isisentrepreneurship = isisentrepreneurship;
	}

	public String getIsbirthdayopen() {
		return isbirthdayopen;
	}

	public void setIsbirthdayopen(String isbirthdayopen) {
		this.isbirthdayopen = isbirthdayopen;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getTravelCities() {
		return travel_cities;
	}

	public void setTravelCities(String travelCities) {
		this.travel_cities = travelCities;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getMaxim() {
		return maxim;
	}

	public void setMaxim(String maxim) {
		this.maxim = maxim;
	}

	public List<DreamVO> getDream() {
		return dream;
	}

	public void setDream(List<DreamVO> dream) {
		this.dream = dream;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIsmarry() {
		return ismarry;
	}

	public void setIsmarry(String ismarry) {
		this.ismarry = ismarry;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<ProductVO> getProduct() {
		return product;
	}

	public void setProduct(List<ProductVO> product) {
		this.product = product;
	}

	public List<PicVO> getPics() {
		return pics;
	}

	public void setPics(List<PicVO> pics) {
		this.pics = pics;
	}

	public List<ZhuoInfoVO> getGrowth() {
		return growth;
	}

	public void setGrowth(List<ZhuoInfoVO> growth) {
		this.growth = growth;
	}

	public List<UserVO> getFamily() {
		return family;
	}

	public void setFamily(List<UserVO> family) {
		this.family = family;
	}

	public String getFamilytotal() {
		return familytotal;
	}

	public void setFamilytotal(String familytotal) {
		this.familytotal = familytotal;
	}

	public String getGrouptotal() {
		return grouptotal;
	}

	public void setGrouptotal(String grouptotal) {
		this.grouptotal = grouptotal;
	}

	public List<QuanVO> getGroups() {
		return groups;
	}

	public void setGroups(List<QuanVO> groups) {
		this.groups = groups;
	}

	public String getBirthdayType() {
		return birthday_type;
	}

	public void setBirthdayType(String birthdayType) {
		this.birthday_type = birthdayType;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIsfollow() {
		return isfollow;
	}

	public void setIsfollow(String isfollow) {
		this.isfollow = isfollow;
	}

	public String getJoinZhuoDate() {
		return join_zhuo_date;
	}

	public void setJoinZhuoDate(String joinZhuoDate) {
		this.join_zhuo_date = joinZhuoDate;
	}

	public String getIsphoneopen() {
		return isphoneopen;
	}

	public void setIsphoneopen(String isphoneopen) {
		this.isphoneopen = isphoneopen;
	}

	public String getIsemailopen() {
		return isemailopen;
	}

	public void setIsemailopen(String isemailopen) {
		this.isemailopen = isemailopen;
	}

	public String getFollownum() {
		return follownum;
	}

	public void setFollownum(String follownum) {
		this.follownum = follownum;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String password) {
		this.userpwd = password;
	}

	public String getLearn_exp() {
		return learn_exp;
	}

	public void setLearn_exp(String learn_exp) {
		this.learn_exp = learn_exp;
	}

	public String getProductotal() {
		return productotal;
	}

	public void setProductotal(String productotal) {
		this.productotal = productotal;
	}

	public String getOffertotal() {
		return offertotal;
	}

	public void setOffertotal(String offertotal) {
		this.offertotal = offertotal;
	}

	public String getLastoffer() {
		return lastoffer;
	}

	public void setLastoffer(String lastoffer) {
		this.lastoffer = lastoffer;
	}

	public String getLastdemand() {
		return lastdemand;
	}

	public void setLastdemand(String lastdemand) {
		this.lastdemand = lastdemand;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassissure() {
		return classissure;
	}

	public void setClassissure(String classissure) {
		this.classissure = classissure;
	}

	public String getMycustomer() {
		return mycustomer;
	}

	public void setMycustomer(String mycustomer) {
		this.mycustomer = mycustomer;
	}

	public String getIsalert() {
		return isalert;
	}

	public void setIsalert(String isalert) {
		this.isalert = isalert;
	}

}
