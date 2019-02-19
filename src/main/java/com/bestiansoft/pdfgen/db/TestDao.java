package com.bestiansoft.pdfgen.db;

import javax.sql.DataSource;

public class TestDao {
    DataSource ds = MyDataSourceFactory.getMariaDbDataSource();
}