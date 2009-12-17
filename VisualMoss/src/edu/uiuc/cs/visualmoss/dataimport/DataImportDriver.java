package edu.uiuc.cs.visualmoss.dataimport;

import java.sql.SQLException;

import prefuse.data.io.DataIOException;

public class DataImportDriver {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, DataIOException
	{
		new DataImport();
	}
}
