package com.alex.tool.pojo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Calendar;

@Getter
@Setter
public class PojoOrig {
    private String str = "123";
        private int i = 9;
    private Date date = new Date(Calendar.getInstance().getTimeInMillis());
    private String date2 = "20190908";
//    private String str2 = "2";
//    private String str3 = "3";
}
