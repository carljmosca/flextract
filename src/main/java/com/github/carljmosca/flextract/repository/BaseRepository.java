/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.repository;

import com.github.carljmosca.flextract.props.InputProperties;
import com.github.carljmosca.flextract.props.InputTable;
import com.github.carljmosca.flextract.util.DatabaseUtility;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author moscac
 */
@Repository
public class BaseRepository {

    private final String NULL_STRING_VALUE = "null";
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    InputProperties inputProperties;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DatabaseUtility databaseUtility;

    @Transactional(readOnly = true)
    public SqlRowSet executeQuery(InputTable inputTable, SqlRowSet parentSqlRowSet) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet(buildQuery(inputTable, parentSqlRowSet));
        return rs;
    }

    public String getDatabaseName() {
        try {
            return jdbcTemplate.getDataSource().getConnection().getCatalog();
        } catch (SQLException ex) {
            logger.warn(ex.getMessage());
        }
        return "flextract";
    }

    public List<String> getTables() {
        List<String> result = new ArrayList<>();
        
        return result;
    }
    
    private String buildQuery(InputTable inputTable,
            SqlRowSet parentResultSet) {
        StringBuilder result = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();
        result.append("* from ").append(inputTable.getName());
        if (inputTable.getWhereClause() != null && !inputTable.getWhereClause().trim().isEmpty()) {
            whereClause.append(inputTable.getWhereClause());
        }
        if (inputTable.getTableReferences() != null) {
            SqlRowSetMetaData parentMetaData = parentResultSet.getMetaData();
            inputTable.getTableReferences().forEach((tableReference) -> {
                if (whereClause.length() > 0) {
                    whereClause.append(" and ");
                }
                int columnIndex = parentResultSet.findColumn(tableReference.getPrimaryKeyColumnName());
                whereClause.append(tableReference.getForeignKeyColumnName())
                        .append(" = ")
                        .append(getFieldValue(parentResultSet, columnIndex,
                                parentMetaData.getColumnType(columnIndex)));
            });
        }
        if (whereClause.length() > 0) {
            result.append(" where ").append(whereClause.toString());
        }
        return databaseUtility.addSkipLimitClause(result.toString(), inputTable.getSkipRecords(), 
                inputTable.getLimitRecords());
    }

    public String getFieldValue(SqlRowSet resultSet, int columnIndex, int columnType) {
        if (resultSet.getObject(columnIndex) == null) {
            return NULL_STRING_VALUE;
        }
        String result;
        SimpleDateFormat sdf;
        switch (columnType) {
            case java.sql.Types.CHAR:
            case java.sql.Types.VARCHAR:
                result = resultSet.getString(columnIndex) == null ? "null"
                        : "\"" + resultSet.getString(columnIndex).trim() + "\"";
                break;
            case java.sql.Types.SMALLINT:
            case java.sql.Types.INTEGER:
                result = Long.toString(resultSet.getLong(columnIndex));
                break;
            case java.sql.Types.FLOAT:
            case java.sql.Types.DECIMAL:
            case java.sql.Types.DOUBLE:
                result = resultSet.getBigDecimal(columnIndex).toPlainString();
                break;
            case java.sql.Types.DATE:
                sdf = new SimpleDateFormat(inputProperties.getDateFormat());
                result = sdf.format(resultSet.getDate(columnIndex));
                break;
            case java.sql.Types.TIME:
                sdf = new SimpleDateFormat(inputProperties.getTimeFormat());
                result = sdf.format(resultSet.getDate(columnIndex));
                break;
            case java.sql.Types.TIMESTAMP:
                sdf = new SimpleDateFormat(inputProperties.getDateTimeFormat());
                result = sdf.format(resultSet.getDate(columnIndex));
                break;
            default:
                result = NULL_STRING_VALUE;
        }
        return result;
    }
}
