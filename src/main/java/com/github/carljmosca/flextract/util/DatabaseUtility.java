/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.util;

import com.github.carljmosca.flextract.props.InputProperties;
import com.github.carljmosca.flextract.props.TableReport;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class DatabaseUtility {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<TableReport> tableReports;

    @Autowired
    InputProperties inputProperties;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init() {
        tableReports = new ArrayList<>();
        initialReportTables();
    }

    public DatabaseType getDatabaseType() {
        String databaseProductName = "";
        try {
            DatabaseMetaData databaseMetaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            databaseProductName = databaseMetaData.getDatabaseProductName();
            if (databaseProductName.startsWith("Informix")) {
                return DatabaseType.INFORMIX;
            }
            if (databaseProductName.contains("MySQL")) {
                return DatabaseType.MYSQL;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        logger.warn("Unsupported database: " + databaseProductName);
        return DatabaseType.UNKNOWN;
    }

    public String addSkipLimitClause(String sql, int skip, int limit) {
        StringBuilder result = new StringBuilder(sql);
        if (skip <= 0) {
            skip = inputProperties.getSkipRecords();
        }
        if (limit <= 0) {
            limit = inputProperties.getLimitRecords();
        }
        switch (getDatabaseType()) {
            case INFORMIX:
                result.insert(0, " ");
                if (limit > 0) {
                    result.insert(0, limit).insert(0, " first ");
                }
                if (skip > 0) {
                    result.insert(0, skip).insert(0, " skip ");
                }
                break;
            case MYSQL:
                if (limit > 0) {
                    result.append(" limit ").append(limit);
                }
                if (skip > 0) {
                    result.append(" offset ").append(skip);
                }
                break;
        }
        result.insert(0, "select ");
        return result.toString();
    }
    
    public void reportTable(String tableName) {
        for (TableReport tableReport : tableReports) {
            if (tableReport.getTableName().equalsIgnoreCase(tableName)) {
                tableReport.setReferenced(true);
                break;
            }
        }
    }

    private void initialReportTables() {
        String sql = null;
        switch (getDatabaseType()) {
            case INFORMIX:
                sql = "select tabname tablename from systables where tabid > 99 and tabtype = 'T' order by tablename";
                break;
            case MYSQL:
                break;
        }
        if (sql != null) {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                tableReports.add(new TableReport(rowSet.getString("tablename").toLowerCase(), false));
            }
        }
    }

    public List<TableReport> getTableReports() {
        return tableReports;
    }
    
}
