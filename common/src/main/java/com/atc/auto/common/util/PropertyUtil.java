package com.atc.auto.common.util;

/**
 * @Author: kaKaXi
 * @Date: 2018/12/7 15:09
 * @Version 1.0
 * @Description:
 */
public class PropertyUtil {

    /**
     * Integer常量
     */
    public static final Integer NUM_0 = 0;
    public static final Integer NUM_1 = 1;
    public static final Integer NUM_2 = 2;
    public static final Integer NUM_3 = 3;
    public static final Integer NUM_4 = 4;
    public static final Integer NUM_5 = 5;
    public static final Integer NUM_6 = 6;
    public static final Integer NUM_7 = 7;
    public static final Integer NUM_8 = 8;


    /**
     * long型常量
     */
    public static final Long NUM_0L = 0L;
    public static final Long NUM_1L = 1L;

    /**
     * 提案状态  0 未处理 1 处理中 2 待确认 3 已到帐 4 无法匹配  5 被退单 6 订单异常 7 部分到账
     */
    public static final Integer PROPOSAL_UNTREATED = 0;
    public static final Integer PROPOSAL_PROCESSING = 1;
    public static final Integer PROPOSAL_CONFIRMED = 2;
    public static final Integer PROPOSAL_ARRIVE = 3;
    public static final Integer PROPOSAL_REJECTED = 4;
    public static final Integer PROPOSAL_RETURNED = 5;
    public static final Integer PROPOSAL_EXCEPTION = 6;
    public static final Integer PROPOSAL_PART_ARRIVE = 7;

    /**
     * 数据删除状态
     */
    public static final Integer DELETE_STATUS_TRUE = 1;
    public static final Integer DELETE_STATUS_FALSE = 0;

    /**
     * 数据状态
     */
    public static final Integer DATA_STATUS_TRUE = 1;
    public static final Integer DATA_STATUS_FALSE = 0;

    /**
     * 处理状态 0 接单 1 待接单-加急 2 待出款 3 待出款-加急 4 待确认到账 5 已到帐 6 已拒绝 7 已退单 8 待分单 9 被退单待分单 10 异常
     */
    public static final Integer DEAL_WAITING = 0;
    public static final Integer DEAL_WAITING_URGENT = 1;
    public static final Integer DEAL_WAITING_PAY = 2;
    public static final Integer DEAL_WAITING_PAY_URGENT = 3;
    public static final Integer DEAL_CONFIRMED = 4;
    public static final Integer DEAL_ARRIVE = 5;
    public static final Integer DEAL_REJECT = 6;
    public static final Integer DEAL_BACK = 7;

    public static final Integer DEAL_DISTRIBUTE = 8;
    public static final Integer DEAL_REJECTED_DISTRIBUTE = 9;
    public static final Integer DEAL_EXCEPTION = 10;


    /**
     * 主动下发  0 待处理 1 待确认出款 2 待出款 3 已出款 4 无法匹配 5 异常终止 6.已完成
     */
    public static final Integer DISTRIBUTE_UNTREATED = 0;
    public static final Integer DISTRIBUTE_WAITING_SURE_PAY = 1;
    public static final Integer DISTRIBUTE_WAITING_PAY = 2;
    public static final Integer DISTRIBUTE_COMPLETE = 3;
    public static final Integer DISTRIBUTE_REJECTED = 4;
    public static final Integer DISTRIBUTE_EXCEPTION = 5;
    public static final Integer DISTRIBUTE_SUCCESS = 6;

    /**
     * 主动接受  0 未处理 1 处理中 2 已出款待确认 3 已完成 4 已撤单 5 已拒绝 6 异常终止 7.待分单 8. 被退单待分单
     */
    public static final Integer ACCEPT_UNTREATED = 0;
    public static final Integer ACCEPT_PROCESSING = 1;
    public static final Integer ACCEPT_SURE = 2;
    public static final Integer ACCEPT_COMPLETE = 3;
    public static final Integer ACCEPT_WITHDRAW = 4;
    public static final Integer ACCEPT_REJECTED = 5;
    public static final Integer ACCEPT_EXCEPTION = 6;
    public static final Integer ACCEPT_DISTRIBUTE = 7;
    public static final Integer ACCEPT_REJECTED_DISTRIBUTE = 8;

    public static final String DEFAULT_NULL = "--";
    public static final String TRADE_SPLIT = "-";

    public static final String TRANS_LS = "LS";
    public static final String TRANS_SQ = "SQ";
    public static final String TRANS_XF = "XF";
    public static final String TRANS_CS = "CS";
    public static final String TRANS_QT = "QT";
    public static final String TRANS_RK = "RK";
    public static final String TRANS_CK = "CK";
    public static final String TRANS_TA = "TA";

    /**
     * 流水类型   交易类别 0 主动下发 1 申请打款 2 测卡 3 其他入款 4其他出款
     */
    public static final Integer FLOW_DISTRIBUTE = 0;
    public static final Integer FLOW_PROPOSAL = 1;
    public static final Integer FLOW_TEST = 2;
    public static final Integer FLOW_ELSE = 3;
    public static final Integer FLOW_ELSE_OUT = 4;
    public static final Integer FLOW_IN = 5;
    public static final Integer FLOW_OUT = 6;

    /** 出款交易类型   0银行卡出款 1代理出款 2商户出款 3自动出款 4业务组银行卡出款 6业务组商户出款 7自动入款 8客户存款  */
    public static final Integer FLOW_CARD_OUT = 0;
    public static final Integer FLOW_PROXY_OUT = 1;
    public static final Integer FLOW_CARD_AUTO_OUT = 3;
    public static final Integer FLOW_OPERATION_OUT = 4;
    public static final Integer FLOW_CARDTRADE_IN = 6;
    public static final Integer FLOW_CUSTOMER_IN = 7;


    /**
     * 流水结果    0 失败 1 成功 2 待确认到账 3 订单异常
     */
    public static final Integer FLOW_RESULT_FAILURE = 0;
    public static final Integer FLOW_RESULT_SUCCESS = 1;
    public static final Integer FLOW_RESULT_WAITING = 2;
    public static final Integer FLOW_RESULT_EXCEPTION = 3;

    public static final String AMOUNT_NULL_DEFAULT_SHOW = "0.00";
    public static final String AMOUNT_HUNDRED = "100.00";

    /**
     * 123456经过MD5加密之后
     */
    public static final String PASS_WORD = "e10adc3949ba59abbe56e057f20f883e";
}
