/*
 *
 * Created on May 12, 2005 by iKnow
 *
  */

package pt.iflow.api.userdata;

import pt.iflow.api.userdata.IMappedData;


/**
 * 
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2005 iKnow</p>
 * 
 * @author iKnow
 */

public interface UserData extends IMappedData {
  
  public static final String ID = "ID";
  public static final String USERNAME = "USERNAME";
  public static final String FULL_NAME = "FULL_NAME";
  public static final String EMAIL = "EMAIL";
  public static final String EMPLOYEE_NUMBER = "EMPLOYEE_NUMBER";
  public static final String ORG_NAME = "ORG_NAME";
  public static final String ORG_ID = "ORG_ID";
  public static final String UNIT_NAME = "UNIT_NAME";
  public static final String UNIT_ID= "UNITID";
  public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
  public static final String ORGADM = "ORGADM";
  public static final String PASSWORD_RESET = "PASSWORD_RESET";
  
  public abstract String getEmail();
  public abstract String getUsername();
  public abstract String getName();
  public abstract String getNumber();
  public abstract String getUnit();
  public abstract String getUnitId();
  public abstract String getMobileNumber();
  public abstract String getOrgAdm();
}
