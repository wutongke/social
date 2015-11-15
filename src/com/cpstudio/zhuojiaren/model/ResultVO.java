package com.cpstudio.zhuojiaren.model;

public class ResultVO {
	/*# 0-99 ϵͳ��Ϣ
	0=�ɹ�
	1=ʧ��
	2=��ʱ
	3=���ݿ����
	4=�������
	5=�����ʽ����ȷ
	6=Ȩ�޲���

	# 100-200 �˺�ҵ��
	100=�ֻ��Ÿ�ʽ����ȷ
	101=�����ʽ����ȷ,������6-20λ����
	102=����Ϊ��
	103=�û�����ʽ����ȷ
	104=�������
	105=�û��Ѵ���
	106=�û�������
	107=sessionʧЧ
	200=Ȧ�Ӳ�����*/
	public static final int Success = 0;
	public static final int FAILURE = 1;
	public static final int OUTOFTIME = 2;
	public static final int DATABASEERROR = 3;
	public static final int CASHERROR = 4;
	public static final int REQUESFORMATERROR = 5;
	public static final int NOAUTHORITY = 6;
	public static final int PHONENUMBERFORMATERROR = 100;
	public static final int PASSWORDFORMATERROR = 101;
	public static final int NONAME = 102;
	public static final int PASSWORDERROR = 104;
	public static final int NOPHONENUMBER = 106;
	public static final int PHONENUMBER = 105;
	public static final int SESSIONOUT = 107;
	private String code;
	private String msg="";
	private String data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
