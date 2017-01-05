/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.util;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class DatabaseUtility {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

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

    public String getSkipLimitClause(int skip, int limit) {
        StringBuilder result = new StringBuilder();
        switch (getDatabaseType()) {
            case INFORMIX:
                if (skip > 0) {
                    result.append(" skip ").append(skip);
                }
                if (limit > 0) {
                    result.append(" limit ").append(limit);
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
        return result.toString();
    }

}
