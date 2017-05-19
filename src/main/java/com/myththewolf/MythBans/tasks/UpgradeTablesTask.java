package com.myththewolf.MythBans.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.myththewolf.MythBans.commands.UpgradeTables;
import com.myththewolf.MythBans.lib.MythBans;

public class UpgradeTablesTask implements Runnable {
	private UpgradeTables instance;
	private MythBans srv;

	public UpgradeTablesTask(UpgradeTables ints, MythBans inst2) {
		instance = ints;
		srv = inst2;
	}

	@Override
	public void run() {
		try {
			this.restructTable("MythBans_PlayerStats");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void restructTable(String tableName) throws SQLException {
		Connection con = instance.con;
		PreparedStatement temp;
		ResultSet RealTable;
		ResultSet TempTable;
		ResultSetMetaData RealMeta;
		ResultSetMetaData TempMeta;
		String SQL_BUILD;
		System.out.println("Upgrading table: " + tableName + " Gathering table meta...");
		RealTable = con.prepareStatement("SELECT * FROM `" + tableName + "`").executeQuery();
		RealMeta = RealTable.getMetaData();
		System.out.println("Upgrading table: " + tableName + " Droping old temp table...");
		con.prepareStatement("DROP TABLE IF EXISTS `" + tableName + "TMP`").executeUpdate();
		System.out.println("Upgrading table: " + tableName + " Cloning table structure into new temp table...");
		con.prepareStatement("CREATE TABLE `" + tableName + "TMP` LIKE `" + tableName + "`;").executeUpdate();
		System.out.println("Upgrading table: " + tableName + " Reading table columns...");
		TempTable = con.prepareStatement("SELECT * FROM `" + tableName + "TMP`").executeQuery();
		TempMeta = TempTable.getMetaData();
		SQL_BUILD = "INSERT INTO `" + tableName + "TMP` (";
		String VARS = "(";
		System.out
				.println("Upgrading table: " + tableName + " Building SQL script to copy data with current columns...");
		for (int i = 1; i <= RealMeta.getColumnCount(); i++) {
			SQL_BUILD += "`" + RealMeta.getColumnName(i) + "`,";
			VARS += "?,";
		}
		SQL_BUILD = SQL_BUILD.substring(0, SQL_BUILD.length() - 1) + ")";
		VARS = VARS.substring(0, VARS.length() - 1) + ")";
		String FINAL_SQL = SQL_BUILD + " VALUES " + VARS + ";";

		System.out.println("Upgrading table: " + tableName + " Copying rows to temp table...");
		while (RealTable.next()) {
			temp = con.prepareStatement(FINAL_SQL);
			for (int i = 1; i <= RealMeta.getColumnCount(); i++) {
				temp.setString(i, RealTable.getString(i));
			}
			temp.executeLargeUpdate();
		}
		System.out.println("Upgrading table: " + tableName + " Droping current table...");
		con.prepareStatement("DROP TABLE IF EXISTS `" + tableName + "`").executeUpdate();
		System.out.println("Upgrading table: " + tableName + " Reload SQL engine to re-make table...");
		srv.loadMySQL();
		System.out.println("Upgrading table: " + tableName + " Re-Parsing table meta data...");
		RealTable = con.prepareStatement("SELECT * FROM `" + tableName + "`").executeQuery();
		RealMeta = RealTable.getMetaData();
		List<String> REAL_COLS = new ArrayList<>();
		List<String> TEMP_COLS = new ArrayList<>();
		for (int i = 1; i <= RealMeta.getColumnCount(); i++) {
			REAL_COLS.add(RealMeta.getColumnName(i));
		}
		for (int i = 1; i <= TempMeta.getColumnCount(); i++) {
			TEMP_COLS.add(TempMeta.getColumnName(i));
		}
		System.out.println("Upgrading table: " + tableName + " Building command to copy data from mutual colums...");
		SQL_BUILD = "";
		VARS = "(";

		List<String> MUT = new ArrayList<>();
		for (String S : REAL_COLS) {
			if (TEMP_COLS.contains(S)) {
				SQL_BUILD += "`" + S + "`,";
				VARS += "?,";
				
				MUT.add(S);
			}
		}
		SQL_BUILD = SQL_BUILD.substring(0, SQL_BUILD.length() - 1);
		VARS = VARS.substring(0, VARS.length() - 1) + ")";
		FINAL_SQL = SQL_BUILD + " VALUES " + VARS + ";";
		temp = con.prepareStatement("INSERT INTO `" + tableName + "` (" +SQL_BUILD + ") VALUES " + VARS + ";");
		
		PreparedStatement GET = con.prepareStatement("SELECT " + SQL_BUILD + " FROM `" + tableName + "TMP`;");
		ResultSet getRes = GET.executeQuery();
		System.out.println("Upgrading table: " + tableName + " Copying data from temp table to new table...");
		while (getRes.next()) {
			int pos = 1;
			for (String S : MUT) {
				
				temp.setString(pos, getRes.getString(S));
				pos++;
			}
			temp.executeLargeUpdate();
		}
		System.out.println("Upgrading table: " + tableName + " Cleaning up...");
		con.prepareStatement("DROP TABLE IF EXISTS `" + tableName + "TMP`").executeUpdate();
		System.out.println("Upgrading table: " + tableName + " Upgrade complete! Ajusted colums and re-imported data!");
	}

}
