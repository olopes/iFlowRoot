package pt.iflow.blocks;

import com.infosistema.OERIflow.IflowServicoMain;
import com.infosistema.oerrexiflowlib.data.OERAssetServico;
import com.infosistema.oerrexiflowlib.data.OERResponseData;
import com.infosistema.oerrexiflowlib.utils.Config;

import pt.iflow.api.blocks.Port;
import pt.iflow.api.processdata.ProcessData;
import pt.iflow.api.processdata.ProcessListVariable;
import pt.iflow.api.utils.Logger;
import pt.iflow.api.utils.UserInfoInterface;
import pt.iknow.utils.StringUtilities;

/**
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: Infosistema
 * </p>
 * 
 * @author
 */

public class BlockP15032ServicePasso4 extends BlockP15032OER {

  public BlockP15032ServicePasso4(int anFlowId, int id, int subflowblockid,
			String filename) {
		super(anFlowId, id, subflowblockid, filename);
		// TODO Auto-generated constructor stub
	}

/**
   * Executes the block main action
   * 
   * @param dataSet
   *          a value of type 'DataSet'
   * @return the port to go to the next block
   */
  public Port after(UserInfoInterface userInfo, ProcessData procData) {
	    Port outPort = portSuccess;
	    String login = userInfo.getUtilizador();
	    StringBuffer logMsg = new StringBuffer();

	    String sIdVar = getParsedAttribute(userInfo, "Id", procData);
	    
	    String sComplexidadeVar = getParsedAttribute(userInfo, "Complexidade", procData);
	    String sEstimativaVar = getParsedAttribute(userInfo, "Estimativa", procData);
	    String sTipoDeImpactoVar = getParsedAttribute(userInfo, "TipoDeImpacto", procData);
	    String sAnaliseDeImpactoVar = getParsedAttribute(userInfo, "AnaliseDeImpacto", procData);
	    String sTipoDeRiscoVar = getParsedAttribute(userInfo, "TipoDeRisco", procData);
	    String sAnaliseDeRiscoVar = getParsedAttribute(userInfo, "AnaliseDeRisco", procData);	    
	    
	    ProcessListVariable areasListVar = procData.getList(getAttribute("AreasList"));
	    ProcessListVariable aplicacoesListVar = procData.getList(getAttribute("AplicacoesList"));
	    	 	    
	      try {	    	  
	    	  com.infosistema.oerrexiflowlib.data.OERAssetServico asset = new OERAssetServico();
	    	  
	    	  asset.setId(Long.parseLong(sIdVar));
	    	  asset.setComplexidade(sComplexidadeVar);
	    	  asset.setEstimativa(Double.parseDouble(sEstimativaVar));
	    	  asset.setTipoDeImpacto(sTipoDeImpactoVar);
	    	  asset.setAnaliseDeImpacto(sAnaliseDeImpactoVar);
	    	  asset.setTipoDeRisco(sTipoDeRiscoVar);
	    	  asset.setAnaliseDeRisco(sAnaliseDeRiscoVar);
	    	  asset.setAreasList(turnToStringArray(areasListVar));
	    	  asset.setAplicacoesList(turnToAssetList(aplicacoesListVar));
	    	  
	    	  IflowServicoMain tester = new IflowServicoMain();
	    	  OERResponseData result = tester.runPasso(Config.getOERUsername(), Config.getOERPassword(), asset, 4);
	    	  if(result.getResponse()==null){
					outPort =portError;
					procData.set(getAttribute("OUT_Error"),result.getError());
				}
	    	  
	      } catch (Exception e) {
	        Logger.error(login, this, "after", procData.getSignature() + "caught exception: " + e.getMessage(), e);
	        outPort = portError;
	      }

	    logMsg.append("Using '" + outPort.getName() + "';");
	    Logger.logFlowState(userInfo, procData, this, logMsg.toString());
	    return outPort;
	  }

}
