package com.atc.auto.common.response;


/**
 * @Author: kaKaXi
 * @Date: 2018/12/10 21:58
 * @Version 1.0
 * @Description:
 */
public enum ReturnCodeEnum {
	
	/** 成功 */
	OK("200", "查询成功"),
	CREATED("201", "新增成功"),
	ACCEPTED("202", "修改成功"),
	NO_CONTENT("204", "删除成功"),
	INTERNAL_SERVER_ERROR("500", "系统错误"),
	_500("500", "系统错误"),
	_FM01("FM01","一个小组只能添加一个规则"),
	_FM02("FM02","其他组长正在操作这条数据"),
	_FM03("FM03","这条信息你的上级已通过或者已拒绝或者已编辑"),
	_FM04("FM04","校验密码和登录密码不能重复"),
	_FM05("FM05","原密码不正确"),
	_FM06("FM06","登录名系统已存在"),
	_FM07("FM07","用户名或密码不能为空"),
	_FM08("FM08","用户不存在"),
	_FM09("FM09","登录密码错误"),
	_FM10("FM10","用户已存在"),
	_FM11("FM11","校验密码不正确"),
	_FM12("FM12","默认密码没有修改"),
	_FM13("FM13","参数为空"),
	_FM14("FM14","该银行卡物资信息已经被使用，请重新选择"),
	_FM15("FM15","该物资的唯一识别码已经存在了，请重新添加"),
	_FM16("FM16","该信息未结束不能交班"),
	_FM17("FM17","该信息组员未交班，不能进行交班确认"),
	_FM18("FM18","内部资金正在正常轮单，无法进行银行卡交班操作。请先进行内部资金暂停轮单，再尝试交班操作"),

	_FM30("FM30","该条记录已存在，请核对后添加"),

	/** 未知返回码 */
	UNKNOWN("RC99", "未知错误");
	
	
	/** 代码 */
	private final String code;

	/** 说明 */
	private final String desc;
	
	/**
	 * 构造方法
	 * 
	 * @param code 代码
	 * @param desc 说明
	 */
	ReturnCodeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * 根据代码获得描述
	 * 
	 * @param code 代码
	 * @return 描述
	 */
	public static String getDescByCode(String code) {
		String v = null;
		if (code != null) {
			ReturnCodeEnum[] enumArray = ReturnCodeEnum.values();
			for (ReturnCodeEnum enumItem : enumArray) {
				if (code.equals(enumItem.getCode())) {
					v = enumItem.getDesc();
					break;
				}
			}
		}
		// 没有找到错误信息,返回未知错误
		if (v == null) {
			v = ReturnCodeEnum.UNKNOWN.desc;
		}
		return v;
	}

	/**
	* @Title: getEnumByCode 
	* @Description: TODO(查询枚举对象，通过code) 
	* @param @param code
	* @param @return    设定文件 
	* @return ReturnCodeEnum    返回类型 
	* @throws
	 */
	public static ReturnCodeEnum getEnumByCode(String code){
		ReturnCodeEnum v = null;
		if (code != null) {
			ReturnCodeEnum[] enumArray = ReturnCodeEnum.values();
			for (ReturnCodeEnum enumItem : enumArray) {
				if (code.equals(enumItem.getCode())) {
					v = enumItem;
					break;
				}
			}
		}
		// 没有找到错误信息,返回未知错误
		if (v == null) {
			v = ReturnCodeEnum.UNKNOWN;
		}
		return v;
	}
}