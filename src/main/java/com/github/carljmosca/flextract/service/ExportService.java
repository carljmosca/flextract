/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.service;

import com.github.carljmosca.flextract.props.InputProperties;
import com.github.carljmosca.flextract.props.InputTable;
import com.github.carljmosca.flextract.props.InputTableColumn;
import com.github.carljmosca.flextract.props.TableReport;
import com.github.carljmosca.flextract.repository.BaseRepository;
import com.github.carljmosca.flextract.util.DatabaseUtility;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
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
    BaseRepository baseRepository;
    @Autowired
    DatabaseUtility databaseUtility;
    BufferedWriter bfInsertStatements;
    BufferedWriter bfReport;
    List<TableReport> tableReports;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void process() {
        initialize();
        inputProperties.getInputTables().forEach((InputTable inputTable) -> {
            try {
                createInsertStatements(inputTable, null);
            } catch (SQLException | IOException e) {
                logger.error(e.getMessage());
            }
        });
        finish();
    }

    private void initialize() {
        try {
            bfInsertStatements = new BufferedWriter(new FileWriter(
                    inputProperties.getOutputDirectory() + "/"
                    + baseRepository.getDatabaseName() + ".sql", true));
            bfReport = new BufferedWriter(new FileWriter(
                    inputProperties.getOutputDirectory() + "/Flextract.rpt", true));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void finish() {
        try {
            bfReport.write("Tables not referenced:");
            bfReport.newLine();
            for (TableReport tableReport : databaseUtility.getTableReports()) {
                if (!tableReport.isReferenced()) {
                    bfReport.write(tableReport.getTableName());
                    bfReport.newLine();
                }
            }
            bfReport.close();
            bfInsertStatements.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void createInsertStatements(InputTable inputTable, SqlRowSet parentResultSet) throws SQLException, IOException {

        databaseUtility.reportTable(inputTable.getName());
        StringBuilder insertStatement = new StringBuilder();
        SqlRowSet sqlRowSet;
        sqlRowSet = baseRepository.executeQuery(inputTable, parentResultSet);
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        while (sqlRowSet.next()) {
            insertStatement.setLength(0);
            insertStatement.append("insert into ").append(inputTable.getName()).append(" (");
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if (i > 1) {
                    insertStatement.append(", ");
                }
                insertStatement.append(metaData.getColumnName(i));
            }
            insertStatement.append(") values (");
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if (i > 1) {
                    insertStatement.append(", ");
                }
                insertStatement.append(baseRepository.getFieldValue(sqlRowSet, i,
                        metaData.getColumnType(i), getColumnFilter(
                                metaData.getColumnName(i), inputTable.getColumns())));
            }
            insertStatement.append(");");
            bfInsertStatements.write(insertStatement.toString());
            bfInsertStatements.newLine();
            if (inputTable.getInputRelatedTables() != null) {
                for (InputTable iTable : inputTable.getInputRelatedTables()) {
                    createInsertStatements(iTable, sqlRowSet);
                }
            }
        }
    }

    private String getColumnFilter(String columnName, List<InputTableColumn> columns) {
        String result = null;
        if (columns != null) {
            for (InputTableColumn inputTableColumn : columns) {
                if (columnName.equalsIgnoreCase(inputTableColumn.getName())) {
                    return inputTableColumn.getFilter();
                }
            }
        }
        return result;
    }

}
