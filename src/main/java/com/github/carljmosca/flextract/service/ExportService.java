/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.service;

import com.github.carljmosca.flextract.props.InputProperties;
import com.github.carljmosca.flextract.props.InputTable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class ExportService {

    @Autowired
    private InputProperties inputProperties;
    @Autowired
    private EntityManager entityManager;

    public void process() {
        inputProperties.getInputTables().forEach((InputTable inputTable) -> {
            try {
                createInsertStatements(inputTable, null);
            } catch (SQLException e) {

            }
        });
    }

    private void createInsertStatements(InputTable inputTable, ResultSet parentResultSet) throws SQLException {

        StringBuilder insertStatement = new StringBuilder();
        Connection connection = entityManager.unwrap(java.sql.Connection.class);
        Statement st = connection.createStatement();
        ResultSet resultSet = null;
        resultSet = st.executeQuery(buildQuery(inputTable, resultSet, parentResultSet));
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            insertStatement.setLength(0);
            insertStatement.append("insert into ").append(inputTable.getName()).append(" (");
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                if (i > 0) {
                    insertStatement.append(", ");
                }
                insertStatement.append(metaData.getColumnName(i));
            }
            insertStatement.append(") values (");
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                insertStatement.append(getFieldValue(resultSet, i, metaData.getColumnType(i)));
            }
            insertStatement.append(")");
            System.out.println(insertStatement.toString());
            if (inputTable.getInputRelatedTables() != null) {
                for (InputTable iTable : inputTable.getInputRelatedTables()) {
                    createInsertStatements(iTable, resultSet);
                }
            }
        }
    }

    private String buildQuery(InputTable inputTable, ResultSet resultSet, 
            ResultSet parentResultSet) throws SQLException {
        StringBuilder result = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();
        result.append("select * from ").append(inputTable.getName());
        if (inputTable.getWhereClause() != null && !inputTable.getWhereClause().trim().isEmpty()) {
            whereClause.append(inputTable.getWhereClause());
        }
        if (inputTable.getTableReferences() != null) {
            ResultSetMetaData parentMetaData = parentResultSet.getMetaData();
            inputTable.getTableReferences().forEach((tableReference) -> {
                if (whereClause.length() > 0) {
                    whereClause.append(" and ");
                }
                try {
                int columnIndex = parentResultSet.findColumn(tableReference.getPrimaryKeyColumnName());
                whereClause.append(tableReference.getForeignKeyColumnName())
                        .append(" = ")
                        .append(getFieldValue(parentResultSet, columnIndex,                                
                                parentMetaData.getColumnType(columnIndex)));
                } catch (SQLException e) {
                    
                }
            });
        }
        if (whereClause.length() > 0) {
            result.append(" where ").append(whereClause.toString());
        }
        if (inputTable.getSkipRecords() > 0) {
            result.append(" skip ").append(inputTable.getSkipRecords());
        }
        if (inputTable.getLimitRecords() > 0) {
            result.append(" limit ").append(inputTable.getLimitRecords());
        }
        return result.toString();
    }

    private String getFieldValue(ResultSet resultSet, int columnIndex, int columnType) {
        String result;
        try {
            switch (columnType) {
                case java.sql.Types.CHAR:
                case java.sql.Types.VARCHAR:
                    result = resultSet.getString(columnIndex);
                    break;
                case java.sql.Types.INTEGER:
                    result = Long.toString(resultSet.getLong(columnIndex));
                    break;
                case java.sql.Types.FLOAT:
                case java.sql.Types.DECIMAL:
                case java.sql.Types.DOUBLE:
                    result = resultSet.getBigDecimal(columnIndex).toPlainString();
                    break;
                default:
                    result = "null";
            }
        } catch (SQLException e) {
            result = "null";
        }
        return result;
    }
}
