package com.alex.tool.util.sqlservertomk.dao;

import com.alex.tool.util.sqlservertomk.dto.TableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DbDao {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(dataSource);
    }
    private NamedParameterJdbcTemplate getNamedJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource);
    }

    public List<TableDto> getTableSchema(String tableName){
        NamedParameterJdbcTemplate jdbcTemplate = getNamedJdbcTemplate();
//        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        String sql = "select " +
                "       c.COLUMN_NAME ColumnName, " +
                "       case c.IS_NULLABLE when 'YES' then 'V' else '' end IsNullable, " +
                "       c.DATA_TYPE DataType , " +
                "       c.CHARACTER_MAXIMUM_LENGTH  Length, " +
                "       case when c.COLUMN_NAME = k.COLUMN_NAME then 'V' else '' end KeyColumn, " +
                "       (SELECT sc.is_identity FROM sys.columns sc left join sys.extended_properties sep on  sc.column_id = sep.minor_id where object_id=object_id(:tableName) and sc.name = c.COLUMN_NAME) AutoIncrement, " +
                "       isnull((SELECT sep.value FROM sys.columns sc left join sys.extended_properties sep on  sc.column_id = sep.minor_id where object_id=object_id('ED3_CaseData') and sc.name = c.COLUMN_NAME),'') Description " +
                "from INFORMATION_SCHEMA.COLUMNS c " +
                "         left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE k on c.TABLE_NAME = k.TABLE_NAME " +
                "where c.TABLE_NAME = :tableName";
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        return jdbcTemplate.query(sql, map, new BeanPropertyRowMapper<>(TableDto.class));
//        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(TableDto.class),tableName,tableName,tableName);
    }
}
