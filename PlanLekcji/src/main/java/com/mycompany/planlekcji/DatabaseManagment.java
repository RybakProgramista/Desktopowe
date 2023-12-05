/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.planlekcji;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author kkile
 */
public class DatabaseManagment {
    private Connection con;
    
    public DatabaseManagment(){
        try {
            Class.forName("org.sqlite.JDBC");
            
            URL resource = DatabaseManagment.class.getResource("/plany.db");
            String url = "jdbc:sqlite:" + URLDecoder.decode(resource.getPath(), "UTF-8");
            
            con = DriverManager.getConnection(url);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DatabaseManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int getInt(String query){
        int out = 0;
        
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()){
                out = rs.getInt(1);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return out;
    }
    
    public void commitQuery(String query){
        try {
            Statement st = con.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getString(String query, String fieldName){
        String out = "";
        
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()){
                out = rs.getString(fieldName);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return out;
    }
    
    public List<String> getList(String query){
        List<String> out = new ArrayList<String>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()){
                for(int x = 1; x < 5; x++){
                    out.add(rs.getString(x));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return out;
    }
    
    public List<String> getTables(){
        List<String> out = new ArrayList<>();
        
        try {
            DatabaseMetaData md = con.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
              if(rs.getString(3).contains("Plan")){
                  out.add(rs.getString(3));
              }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return out;
    }
}
