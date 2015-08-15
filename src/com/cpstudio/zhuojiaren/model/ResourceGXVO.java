package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;


public class ResourceGXVO {

	// lz
	public static String RESOURCEGXTYPE = "resource_gongxu_type";// 寻找资源
	public static String RESOURCEGXFILTER_LOCATION = "resource_filter_location";//
	public static String RESOURCEGXFILTER_TYPE = "resource_filter_type";//
	// 供需 的各种类型
	public static int RESOURCE_FIND = 0;// 寻找资源
	public static int NEED_FIND = 1;// 发现需求
	public static int PUB_NEED_RESOURCE = 2;// 供需发布
	// "userid" : <string> (发布者用户ID) ,
	// "sdid" : <string> (供需ID) ,
	// "sdflag" : <int> (供需标识 0-资源 1-需求) ,
	// "type" : <int> (供需类型) ,
	// "title" : <string> (标题) ,
	// "content" : <string> (内容) ,
	// "picture" : <string> (第一张图片 在左侧显示) ,
	// "label" : <string> (供需标签) ,
	// "contacts" : <string> (联系人) ,
	// "phone" : <string> (联系电话) ,
	// "addtime" : <string> (添加时间)
	private String sdid;

	private int sdflag;

	private int type;

	private String title;

	private String content;

	private String label;

	private String contacts;

	private String phone;

	private String addtime;

	private String userid;

	private String name;

	private String uheader;
	
	private String picture;

	private int position;
	
	//需要添加公司，和是否收藏
	private String company="";
	private String isCollection="0";
	private ArrayList<Comment> commentList;
	private ArrayList<PicNewVO> sdPic;

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}
	public ArrayList<PicNewVO> getSdPic() {
		return sdPic;
	}
	public void setCommentList(ArrayList<Comment> commentList) {
		this.commentList = commentList;
	}
	public void setSdPic(ArrayList<PicNewVO> sdPic) {
		this.sdPic = sdPic;
	}
	public void setSdid(String sdid){
	this.sdid = sdid;
	}
	public String getSdid(){
	return this.sdid;
	}
	public void setSdflag(int sdflag){
	this.sdflag = sdflag;
	}
	public int getSdflag(){
	return this.sdflag;
	}
	public void setType(int type){
	this.type = type;
	}
	public int getType(){
	return this.type;
	}
	public void setTitle(String title){
	this.title = title;
	}
	public String getTitle(){
	return this.title;
	}
	public void setContent(String content){
	this.content = content;
	}
	public String getContent(){
	return this.content;
	}
	public void setLabel(String label){
	this.label = label;
	}
	public String getLabel(){
	return this.label;
	}
	public void setContacts(String contacts){
	this.contacts = contacts;
	}
	public String getContacts(){
	return this.contacts;
	}
	public void setPhone(String phone){
	this.phone = phone;
	}
	public String getPhone(){
	return this.phone;
	}
	public void setAddtime(String addtime){
	this.addtime = addtime;
	}
	public String getAddtime(){
	return this.addtime;
	}
	public void setUserid(String userid){
	this.userid = userid;
	}
	public String getUserid(){
	return this.userid;
	}
	public void setName(String name){
	this.name = name;
	}
	public String getPicture() {
		return picture;
	}
	public String getCompany() {
		return company;
	}
	public String getIsCollection() {
		return isCollection;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public void setIsCollection(String isCollection) {
		this.isCollection = isCollection;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getName(){
	return this.name;
	}
	public void setUheader(String uheader){
	this.uheader = uheader;
	}
	public String getUheader(){
	return this.uheader;
	}
	public void setPosition(int position){
	this.position = position;
	}
	public int getPosition(){
	return this.position;
	}

}
