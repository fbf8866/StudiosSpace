package com.example.magic.myapplication.bean;

/**
 * Created by Administrator on 2017/3/24.
 */
public class CookieBean {
    /**
     * SendSmsBean : {"resultCode":"200","messageInfo":"成功","sessionId":"naT_Zis9_YJ4vzU3cqZ_J06OJVrZnq__83llbDEbceThsUStmcnh!-807880201!1490343570237"}
     */

    private SendSmsBeanCookieBean SendSmsBean;

    public SendSmsBeanCookieBean getSendSmsBean() {
        return SendSmsBean;
    }

    public void setSendSmsBean(SendSmsBeanCookieBean SendSmsBean) {
        this.SendSmsBean = SendSmsBean;
    }

    public static class SendSmsBeanCookieBean {
        /**
         * resultCode : 200
         * messageInfo : 成功
         * sessionId : naT_Zis9_YJ4vzU3cqZ_J06OJVrZnq__83llbDEbceThsUStmcnh!-807880201!1490343570237
         */

        private String resultCode;
        private String messageInfo;
        private String sessionId;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getMessageInfo() {
            return messageInfo;
        }

        public void setMessageInfo(String messageInfo) {
            this.messageInfo = messageInfo;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}
