package com.alex.tool.util.sqlservertomk.service;

import com.alex.tool.util.sqlservertomk.dao.DbDao;
import com.alex.tool.util.sqlservertomk.dto.TableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbService {
    private static final String SPLITER = "|";
    @Autowired
    private DbDao dao;

    public String generateMDString(String tableName){
        List<TableDto> tableSchema = dao.getTableSchema(tableName);
        StringBuilder sb = new StringBuilder();

        sb.append("### (").append(tableName).append(")").append("\n");
        sb.append(SPLITER).append("PK").append(SPLITER).append("NULL").append(SPLITER).append("欄位").append(SPLITER).append("型態").append(SPLITER).append("說明").append(SPLITER).append("代碼說明").append(SPLITER).append("\n");
        sb.append("| --- | --- | --- | --- | --- | --- |").append("\n");
        tableSchema.stream().forEach(dto->{
            sb.append(SPLITER).append(dto.getKeyColumn()).append(SPLITER).append(dto.getIsNullable()).append(SPLITER).append(dto.getColumnName()).append(SPLITER).append(dto.getDataType());

            if(dto.getLength()!=null && !dto.getLength().equals("")){
                if(dto.getLength().equals("-1")){
                    sb.append(" (MAX) ");
                }else{
                    sb.append(" (").append(dto.getLength()).append(") ");
                }
            }
            sb.append(SPLITER).append(dto.getDescription()).append(SPLITER).append("  ").append(SPLITER).append("\n");
        });

        return sb.toString();
    }
}
