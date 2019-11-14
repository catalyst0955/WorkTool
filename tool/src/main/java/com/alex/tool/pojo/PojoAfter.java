package com.alex.tool.pojo;

import com.alex.tool.annotation.DateFormat;
import com.alex.tool.annotation.MergeAlias;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class PojoAfter {
    @MergeAlias(value = {"str"})
    private String sss;
    @MergeAlias(value = "i")
    private int i;

    @MergeAlias(value = "date",dateToString = "yyyyMMdd")
    private String d;

    @MergeAlias(value = "date2",stringToDate = "yyyyMMdd")
    private Date newDate;
//    @MergeAlias(value = {"st2"})
//    private String sss2;
//    @MergeAlias(value = {"str3"})
//    private String sss3;

}
