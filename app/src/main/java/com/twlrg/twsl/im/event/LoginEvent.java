package com.twlrg.twsl.im.event;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2018/05/16 5.6.6 
 */

public class LoginEvent {
    public static final String STATUS_LOGIN="login";
    public static final String STATUS_LOGOUT="logout";
    String newStatus;

    public LoginEvent(String newStatus) {
        this.newStatus = newStatus;
    }
}
