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
    private EntityManager em;

    public void process() {
        inputProperties.getInputTables().forEach((InputTable inputTable) -> {
            System.out.println(inputTable.getName());
            if (inputTable.getInputRelatedTables() != null) {
                inputTable.getInputRelatedTables().forEach((inputRelatedTable) -> {
                    System.out.println(inputRelatedTable.getName());
                });
            }
        });
    }

    private void runQuery(String sql) throws SQLException {

        Connection connection = em.unwrap(java.sql.Connection.class);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
  
                switch (metaData.getColumnType(i)) {
                    case java.sql.Types.CHAR :
                    case java.sql.Types.VARCHAR :
                        break;
                }
            }    
        }

    }
}
