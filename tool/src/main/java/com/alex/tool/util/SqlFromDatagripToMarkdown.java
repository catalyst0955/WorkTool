package com.alex.tool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SqlFromDatagripToMarkdown {

    private static final String SPLITER = "|";
    private static final String[] skipWord = {"constraint", "primary"};


    public static void main(String[] args) {
        String sql = "create table dbo.EOP_CaseData\n" +
                "(\n" +
                "    UniqId                nvarchar(50) not null\n" +
                "        constraint PK_EOP_CaseData\n" +
                "            primary key,\n" +
                "    Status                varchar(2),\n" +
                "    Type                  varchar(1),\n" +
                "    Process               varchar(20),\n" +
                "    SalaryAccountTw       varchar(16),\n" +
                "    SalaryAccountFore     varchar(16),\n" +
                "    IsTwTaxResident       varchar(1),\n" +
                "    Phone                 nvarchar(50),\n" +
                "    SetSecuritiesDelivery varchar(1),\n" +
                "    SecuritiesAddrZipCode varchar(5),\n" +
                "    SecuritiesAddr        nvarchar(50),\n" +
                "    SecuritiesCode        varchar(5),\n" +
                "    SecuritiesName        nvarchar(50),\n" +
                "    ProductId             varchar(2),\n" +
                "    ChtName               nvarchar(50),\n" +
                "    EngName               nvarchar(100),\n" +
                "    Idno                  nvarchar(20),\n" +
                "    Birthday              varchar(8),\n" +
                "    Gender                varchar(1),\n" +
                "    IdCardDate            datetime,\n" +
                "    IdCardLocation        nvarchar(50),\n" +
                "    IdCardRecord          varchar(2),\n" +
                "    ResAddrZipCode        nvarchar(6),\n" +
                "    ResAddr               nvarchar(200),\n" +
                "    CommAddrZipCode       nvarchar(6),\n" +
                "    CommAddr              nvarchar(200),\n" +
                "    Education             varchar(1),\n" +
                "    Marriage              varchar(1),\n" +
                "    EmailAddress          varchar(120),\n" +
                "    ResTelArea            nvarchar(4),\n" +
                "    ResTel                nvarchar(30),\n" +
                "    HomeTelArea           nvarchar(4),\n" +
                "    HomeTel               nvarchar(30),\n" +
                "    EstateType            varchar(1),\n" +
                "    PassBookTw            varchar(1),\n" +
                "    PassBookFore          varchar(1),\n" +
                "    CorpName              nvarchar(100),\n" +
                "    CorpTelArea           nvarchar(4),\n" +
                "    CorpTel               nvarchar(30),\n" +
                "    CorpAddrZipCode       nvarchar(6),\n" +
                "    CorpAddr              nvarchar(200),\n" +
                "    Occupation            varchar(10),\n" +
                "    JobTitle              nvarchar(100),\n" +
                "    YearlyIncome          int,\n" +
                "    OnBoardDate           char(10),\n" +
                "    IpAddress             varchar(20),\n" +
                "    SendType              varchar(1),\n" +
                "    BreakPointPage        varchar(200),\n" +
                "    D3AccountType         varchar(4),\n" +
                "    D3SubCategory         varchar(4),\n" +
                "    AMLResult             varchar(1),\n" +
                "    DataUse               varchar(1),\n" +
                "    EOPStatus             varchar(2),\n" +
                "    TrustStatus           varchar(1),\n" +
                "    CreateTime            datetime,\n" +
                "    UpdateTime            datetime,\n" +
                "    SendedTime            datetime,\n" +
                "    EndTime               datetime,\n" +
                "    ReceiveResultTime     datetime\n" +
                ")\n" +
                "go\n" +
                "\n";

        System.out.println(generate(sql));

    }


    private static String generate(String sql) {
        StringBuilder sb = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        try (BufferedReader pKeyReader = new BufferedReader(new StringReader(sql)); BufferedReader mainReader = new BufferedReader(new StringReader(sql))) {
            String str = null;
            List<String> pKeyList = null;
            String lastId = "";
            while ((str = pKeyReader.readLine()) != null) {
                if (str.substring(0, 1).equals("(")) {
                    continue;
                } else if (str.substring(0, 1).equals(")")) {
                    break;
                } else {
                    List<String> collect = Arrays.stream(str.split("\\s+")).filter(s -> s.trim().length() > 0).collect(Collectors.toList());
                    if (Arrays.asList(skipWord).contains(collect.get(0))) {
                    } else {
                        lastId = collect.get(0);
                    }
                }

                if (str.contains("primary key")) {

                    try {
                        String substring = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
                        pKeyList = Arrays.stream(substring.split(",")).map(String::trim).collect(Collectors.toList());
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        if (!lastId.isEmpty()) {
                            if (pKeyList == null) {
                                pKeyList = new ArrayList<>();
                            }
                            pKeyList.add(lastId);

                        }
                        continue;
                    }
                }
            }


            String dbName = "";

            while ((str = mainReader.readLine()) != null) {
                if (str.contains("create table")) {
                    dbName = str.substring(str.indexOf(".") + 1);
                    continue;
                }
                if (str.substring(0, 1).equals("(")) {
                    continue;
                } else if (str.substring(0, 1).equals(")")) {
                    break;
                } else {
                    List<String> collect = Arrays.stream(str.split("\\s+")).filter(s -> s.trim().length() > 0).collect(Collectors.toList());
//                    System.out.println(collect);
                    if (Arrays.asList(skipWord).contains(collect.get(0))) {
                        continue;
                    }
                    builder.append(SPLITER);
                    if (pKeyList != null && pKeyList.contains(collect.get(0))) {
                        builder.append(" V   ");
                    } else {
                        builder.append("     ");
                    }
                    builder.append(SPLITER);
                    if (collect.size() > 2) {
                        builder.append("     ");
                    } else {
                        builder.append(" V   ");
                    }
                    builder.append(SPLITER).append(collect.get(0).replaceAll(",","")).append(SPLITER).append(collect.get(1).replaceAll(",","")).append(SPLITER).append("   ").append(SPLITER).append("   ").append(SPLITER).append("\n");
                }
            }

            sb.append("### 上傳圖檔狀態資料表(").append(dbName).append(")").append("\n");
            sb.append(SPLITER).append("PK").append(SPLITER).append("NULL").append(SPLITER).append("欄位").append(SPLITER).append("型態").append(SPLITER).append("說明").append(SPLITER).append("代碼說明").append(SPLITER).append("\n");
            sb.append("| --- | --- | --- | --- | --- | --- |").append("\n");
            sb.append(builder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }
}
