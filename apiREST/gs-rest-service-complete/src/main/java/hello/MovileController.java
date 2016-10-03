package hello;



import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovileController {
	
    static final String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    //"com.mysql.jdbc.Driver";
	static final String conexao="jdbc:sqlserver://regulus:1433;databasename=BDu14175";
	static final String usuarioBD="BDu14175";
	static final String senha="vermelho291298";
	

	@CrossOrigin //essa anotação é padrão para não ocorrer erro de permissão do browser
	@RequestMapping(value = "/api/query/1", method = RequestMethod.GET)
	private ResponseEntity calculaBaseAtiva(@PathVariable(value="day") String dia){
    	//pega do banco
		
		 try
		   {
			   AdaptedPreparedStatement bd = new AdaptedPreparedStatement(driver, conexao, usuarioBD, senha);  	       
			   ArrayList<BaseAtiva> bases = new ArrayList<BaseAtiva>();			   
						  
			   
			   AdaptedResultSet resultado = null;		        
		        try
		        {
		            String sql = "select active_base_total, carrier_name from dashboard_daily where inserted_date = '"+dia+"' group by carrier_name, active_base_total";		            
		            bd.prepareStatement (sql);
		            resultado = (AdaptedResultSet)bd.executeQuery();
		        }
		        catch (SQLException erro)
		        {
		            throw new Exception ("Erro ao recuperar base ativa");
		        }
		        		        
		       
		        String opAtual, opAnterior;
				opAtual = opAnterior  = "";
				int indice  = 0;
				
		        while(resultado.next()){		        			        	
                    opAtual = resultado.getString("carrier_name");
		        	
		        	if (!opAtual.equals(opAnterior))
		        	{
		        		indice++;		
		        		bases.add(new BaseAtiva(opAtual));		        				        	
		        	}
		        			        			        
		        	bases.get(indice).setQtd(bases.get(indice).getQtd()+resultado.getInt("active_base_total"));	        			        				        			        	
		        }
		        		        			          		        			  			   
			   bd.close();
			   
			   return new ResponseEntity(bases, HttpStatus.OK);
		   }
		   catch(Exception e)
		   {
			   return new ResponseEntity(0, HttpStatus.INTERNAL_SERVER_ERROR); 
		   }		     	
    }
	
	@CrossOrigin
	@RequestMapping(value="/api/query/2")
	private ResponseEntity cancelaServico(){
		String opAtual, opAnterior;
		opAtual = opAnterior  = "";
		int indice  = 0;
		
		   
		   try
		   {
			   AdaptedPreparedStatement bd = new AdaptedPreparedStatement(driver, conexao, usuarioBD, senha);  	       
			   ArrayList<Cancelamento> cancelamentos = new ArrayList<Cancelamento>();			   
						   			
			   AdaptedResultSet resultado = null;		        
		        try
		        {
		            String sql = "select cancelation_total, cancelation_type, carrier_id from dashboard_daily group by carrier_name, cancelation_total, cancelation_type";		            
		            bd.prepareStatement (sql);
		            resultado = (AdaptedResultSet)bd.executeQuery();
		        }
		        catch (SQLException erro)
		        {
		            throw new Exception ("Erro ao recuperar cancelamentos");
		        }
		        		        

		        while(resultado.next()){
		        	opAtual = resultado.getString("carrier_name");
		        	
		        	if (!opAtual.equals(opAnterior))
		        	{
		        		indice++;		        		
		        		cancelamentos.add(new Cancelamento(opAtual));		        		
		        	}
		        	
		        	
		        	if (resultado.getString("cancelation_type").toUpperCase().equals("V"))
	        			cancelamentos.get(indice).addVoluntario();	        		
	        		else
	        			cancelamentos.get(indice).addInvoluntario();
	        			
		        	
		        	opAnterior = opAtual;
		        }
		        		        			          		        			  
			   
			   bd.close();
			   
			   return new ResponseEntity(cancelamentos, HttpStatus.OK);
		   }
		   catch(Exception e)
		   {
			   return new ResponseEntity(0, HttpStatus.INTERNAL_SERVER_ERROR); 
		   }
		   	       	       		       
    }
	
	/*@CrossOrigin
	@RequestMapping("/api/query/3")
    private double calculaARPU(){
    	
	
		   	   
    }*/
	
}

