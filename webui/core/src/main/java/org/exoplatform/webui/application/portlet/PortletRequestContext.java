package org.exoplatform.webui.application.portlet;

import java.io.Writer;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;

import org.exoplatform.web.application.URLBuilder;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.lifecycle.HtmlValidator;

public class PortletRequestContext extends WebuiRequestContext {
  
  static public int VIEW_MODE =  0 ;
  static public int EDIT_MODE =  1 ;
  static public int HELP_MODE =  2 ;
  static public int CONFIG_MODE = 3 ;
  
  private int applicationMode_ ;
  private PortletRequest request_; 
  private PortletResponse response_ ;
  private Writer writer_ ;
  private boolean hasProcessAction_ = false ;
  
  public PortletRequestContext(WebuiApplication app, Writer writer, 
                               PortletRequest req, PortletResponse res) {
    super(app) ;
    init(writer, req, res) ;
    setSessionId(req.getPortletSession(true).getId()) ;
    PortletMode mode = req.getPortletMode() ;
    if(mode.equals(PortletMode.VIEW))  applicationMode_ = VIEW_MODE ;
    else if(mode.equals(PortletMode.EDIT))  applicationMode_ = EDIT_MODE ;
    else if(mode.equals(PortletMode.HELP))  applicationMode_ = HELP_MODE ;
    else  applicationMode_ = VIEW_MODE ;
    
    urlBuilder = new PortletURLBuilder();
  }
  
  public void init(Writer writer,  PortletRequest req, PortletResponse res) {
    request_ = req ;
    response_ =  res ;
    writer_ = new HtmlValidator(writer) ;
//    writer_ =  writer ;
  }

  public void  setUIApplication(UIApplication uiApplication) throws Exception { 
    uiApplication_ = uiApplication ;
    appRes_ = getApplication().getResourceBundle(getParentAppRequestContext().getLocale()) ;
  }
  
  final public String getRequestParameter(String name) {
    return request_.getParameter(name);
  }

  final public String[] getRequestParameterValues(String name) {
    return request_.getParameterValues(name);
  }

  public String getRequestContextPath() { return  request_.getContextPath(); }
  
  @SuppressWarnings("unchecked")
  public PortletRequest  getRequest() { return request_ ; }
  
  @SuppressWarnings("unchecked")
  public PortletResponse getResponse() {  return response_ ; }
  
  public String getRemoteUser() { return parentAppRequestContext_.getRemoteUser() ; }
  
  final public boolean isUserInRole(String roleUser){ return request_.isUserInRole(roleUser); }
  
//  final public boolean isLogon() { return getParentAppRequestContext().isLogon(); }
  
  public int getApplicationMode() { return applicationMode_ ; }
  
  public void setApplicationMode(int mode) { applicationMode_ = mode; }
  
  public Writer getWriter() throws Exception {  return writer_ ; }

  final public boolean useAjax() { return getParentAppRequestContext().useAjax(); } 
  
  public  boolean hasProcessAction() { return hasProcessAction_ ;}
  
  public  void    setProcessAction(boolean b) { hasProcessAction_ = b ; }
  
  public URLBuilder getURLBuilder() {
    RenderResponse renderRes = (RenderResponse)  response_ ;
    urlBuilder.setBaseURL(renderRes.createActionURL().toString());
    return urlBuilder ;
  }
  
}