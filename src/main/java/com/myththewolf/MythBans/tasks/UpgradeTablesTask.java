 package com.myththewolf.MythBans.tasks;
 
 import com.myththewolf.MythBans.commands.UpgradeTables;
 import com.myththewolf.MythBans.lib.MythBans;
 import java.io.PrintStream;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.util.List;
 
 public class UpgradeTablesTask implements Runnable
 {
   private UpgradeTables instance;
   private MythBans srv;
   
   public UpgradeTablesTask(UpgradeTables ints, MythBans inst2)
   {
     this.instance = ints;
     this.srv = inst2;
   }
   
   public void run()
   {
     try {
       restructTable("MythBans_PlayerStats");
     }
     catch (SQLException e) {
       e.printStackTrace();
     }
     com.myththewolf.MythBans.lib.feilds.AbstractMaps.setUpgrade(false);
   }
   
   public void restructTable(String tableName) throws SQLException {
     Connection con = this.instance.con;
     
 
 
 
 
 
     System.out.println("Upgrading table: " + tableName + " Gathering table meta...");
     ResultSet RealTable = con.prepareStatement("SELECT * FROM `" + tableName + "`").executeQuery();
     ResultSetMetaData RealMeta = RealTable.getMetaData();
     System.out.println("Upgrading table: " + tableName + " Droping old temp table...");
     con.prepareStatement("DROP TABLE IF EXISTS `" + tableName + "TMP`").executeUpdate();
     System.out.println("Upgrading table: " + tableName + " Cloning table structure into new temp table...");
     con.prepareStatement("CREATE TABLE `" + tableName + "TMP` LIKE `" + tableName + "`;").executeUpdate();
     System.out.println("Upgrading table: " + tableName + " Reading table columns...");
     ResultSet TempTable = con.prepareStatement("SELECT * FROM `" + tableName + "TMP`").executeQuery();
     ResultSetMetaData TempMeta = TempTable.getMetaData();
     String SQL_BUILD = "INSERT INTO `" + tableName + "TMP` (";
     String VARS = "(";
     System.out
       .println("Upgrading table: " + tableName + " Building SQL script to copy data with current columns...");
     for (int i = 1; i <= RealMeta.getColumnCount(); i++) {
       SQL_BUILD = SQL_BUILD + "`" + RealMeta.getColumnName(i) + "`,";
       VARS = VARS + "?,";
     }
     SQL_BUILD = SQL_BUILD.substring(0, SQL_BUILD.length() - 1) + ")";
     VARS = VARS.substring(0, VARS.length() - 1) + ")";
     String FINAL_SQL = SQL_BUILD + " VALUES " + VARS + ";";
     
     System.out.println("Upgrading table: " + tableName + " Copying rows to temp table...");
     while (RealTable.next()) {
       PreparedStatement temp = con.prepareStatement(FINAL_SQL);
       for (int i = 1; i <= RealMeta.getColumnCount(); i++) {
         temp.setString(i, RealTable.getString(i));
       }
       temp.executeLargeUpdate();
     }
     System.out.println("Upgrading table: " + tableName + " Droping current table...");
     con.prepareStatement("DROP TABLE IF EXISTS `" + tableName + "`").executeUpdate();
     System.out.println("Upgrading table: " + tableName + " Reload SQL engine to re-make table...");
     this.srv.loadMySQL();
     System.out.println("Upgrading table: " + tableName + " Re-Parsing table meta data...");
     RealTable = con.prepareStatement("SELECT * FROM `" + tableName + "`").executeQuery();
     RealMeta = RealTable.getMetaData();
     List<String> REAL_COLS = new java.util.ArrayList();
     List<String> TEMP_COLS = new java.util.ArrayList();
     for (int i = 1; i <= RealMeta.getColumnCount(); i++) {
       REAL_COLS.add(RealMeta.getColumnName(i));
     }
     for (int i = 1; i <= TempMeta.getColumnCount(); i++) {
       TEMP_COLS.add(TempMeta.getColumnName(i));
     }
     System.out.println("Upgrading table: " + tableName + " Building command to copy data from mutual colums...");
     SQL_BUILD = "";
     VARS = "(";
     
     List<String> MUT = new java.util.ArrayList();
     for (String S : REAL_COLS) {
       if (TEMP_COLS.contains(S)) {
         SQL_BUILD = SQL_BUILD + "`" + S + "`,";
         VARS = VARS + "?,";
         
         MUT.add(S);
       }
     }
     SQL_BUILD = SQL_BUILD.substring(0, SQL_BUILD.length() - 1);
     VARS = VARS.substring(0, VARS.length() - 1) + ")";
     FINAL_SQL = SQL_BUILD + " VALUES " + VARS + ";";
     PreparedStatement temp = con.prepareStatement("INSERT INTO `" + tableName + "` (" + SQL_BUILD + ") VALUES " + VARS + ";");
     
     PreparedStatement GET = con.prepareStatement("SELECT " + SQL_BUILD + " FROM `" + tableName + "TMP`;");
     ResultSet getRes = GET.executeQuery();
     System.out.println("Upgrading table: " + tableName + " Copying data from temp table to new table...");
     while (getRes.next()) {
       int pos = 1;
       for (String S : MUT)
       {
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


