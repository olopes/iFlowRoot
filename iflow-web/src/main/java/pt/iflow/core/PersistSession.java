package pt.iflow.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpSession;
import pt.iflow.api.db.DatabaseInterface;
import pt.iflow.api.utils.Logger;
import pt.iflow.api.utils.UserInfoInterface;

public class PersistSession {

  
  public void getSession(UserInfoInterface userInfo, HttpSession session){
    String userid = userInfo.getUtilizador();    
    Connection db = null;
    Statement st = null;
    ResultSet rs = null;
    Object[][] valores = new Object[0][2]; 
    byte[] buf = null;
    
    try {
      db = DatabaseInterface.getConnection(userInfo);
      st = db.createStatement();
      rs = st.executeQuery("select session from user_session where userid = '"+userid+"'");
      
      if (rs.next()) {
        buf = rs.getBytes("session");
      }
      
      if (buf != null) {
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ObjectInputStream objectIn = new ObjectInputStream(bais);
        valores = (Object[][]) objectIn.readObject();
        
      }
      
      rs.close();
      rs = null;
    } catch (SQLException sqle) {
        Logger.error(userid, this, "getSession","caught sql exception: " + sqle.getMessage(), sqle);
    } catch (Exception e) {
        Logger.error(userid, this, "getSession","caught exception: " + e.getMessage(), e);
    } finally {
        DatabaseInterface.closeResources(db, st, rs);
    }

    for(int i=0; i < valores.length; i++){
      session.setAttribute((String)valores[i][0], valores[i][1]);
    }
  }
  
  
  public void setSession(UserInfoInterface userInfo, HttpSession session){
    String[] chaves = session.getValueNames();
    Object[][] valores = new Object[chaves.length][2];
    int rows = 0;
    
    for(int i = 0; i < valores.length; i++){
      valores[i][0] = chaves[i];
      valores[i][1] = session.getAttribute(chaves[i]);
    }
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream oout = new ObjectOutputStream(baos);
      oout.writeObject(valores);
      oout.close();
      } catch (IOException e) {
        Logger.error(userInfo.getUtilizador(), this, "setSession","caught sql exception: "+ e.getMessage());
        return;
      }
        
    Connection db = null;
    PreparedStatement pst = null;
    try {
      db = DatabaseInterface.getConnection(userInfo);
      pst = db.prepareStatement("Update user_session set session=? where userid='"+userInfo.getUtilizador()+"'");
      pst.setBytes(1, baos.toByteArray());
      rows = pst.executeUpdate();

      if(rows <= 0){      
        db = DatabaseInterface.getConnection(userInfo); 
        pst = db.prepareStatement("insert into user_session (userid, session) values ('"+userInfo.getUtilizador()+"',?)");
        pst.setBytes(1, baos.toByteArray());
        pst.execute();
      }
    } catch (SQLException sqle) {
        Logger.error(userInfo.getUtilizador(), this, "setSession","caught sql exception: " + sqle.getMessage(), sqle);
    } finally {
        DatabaseInterface.closeResources(db, pst);
    }
  }
  
}
