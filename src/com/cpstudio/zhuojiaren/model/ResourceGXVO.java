package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;


public class ResourceGXVO {

	// lz
	public static String RESOURCEGXTYPE = "resource_gongxu_type";// Ѱ����Դ
	public static String RESOURCEGXFILTER_LOCATION = "resource_filter_location";//
	public static String RESOURCEGXFILTER_TYPE = "resource_filter_type";//
	// ���� �ĸ�������
	public static int RESOURCE_FIND = 0;// Ѱ����Դ
	public static int NEED_FIND = 1;// ��������
	public static int PUB_NEED_RESOURCE = 2;// ���跢��
	// "userid" : <string> (�������û�ID) ,
	// "sdid" : <string> (����ID) ,
	// "sdflag" : <int> (�����ʶ 0-��Դ 1-����) ,
	// "type" : <int> (��������) ,
	// "title" : <string> (����) ,
	// "content" : <string> (����) ,
	// "picture" : <string> (��һ��ͼƬ �������ʾ) ,
	// "label" : <string> (�����ǩ) ,
	// "contacts" : <string> (��ϵ��) ,
	// "phone" : <string> (��ϵ�绰) ,
	// "addtime" : <string> (���ʱ��)
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
	
	//��Ҫ��ӹ�˾�����Ƿ��ղ�
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
