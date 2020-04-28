package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	private MeteoDAO dao;
	private List<Citta> soluzione;
	public int bestCosto;
	private int count;
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		dao=new MeteoDAO();
	}

	// of course you can change the SMeteoDAO dao=new MeteoDAO();
	public String getUmiditaMedia(int mese) {
		String temp="";
		temp= "L'umidità media del mese a Torino è "+dao.getMediaUmidita(mese, "Torino")+"\nL'umidità media del mese a Milano è "+dao.getMediaUmidita(mese, "Milano")+
				"\nL'umidità media a Genova del mese è "+ dao.getMediaUmidita(mese, "Genova");
		return temp;
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		bestCosto=0;
		count=0;
		soluzione= new ArrayList<>();
		List<Citta> parziale= new ArrayList<>();
		int livello=0;
		List<Citta> disponibili=  new ArrayList<>();
		Citta c1= new Citta("Torino", dao.getAllRilevamentiLocalitaMese(mese, "Torino"));
		Citta c2= new Citta("Genova", dao.getAllRilevamentiLocalitaMese(mese, "Genova"));
		Citta c3= new Citta("Milano", dao.getAllRilevamentiLocalitaMese(mese, "Milano"));
		disponibili.add(c1);
		disponibili.add(c2);
		disponibili.add(c3);	
		cerca(parziale, livello, disponibili);
		return soluzione;
	}
	
	private void cerca(List<Citta> parziale,int livello, List<Citta> disponibili) {
		
		if (parziale.size()== NUMERO_GIORNI_TOTALI) {
			if (bestCosto==0) {
				this.soluzione=new ArrayList<>(parziale);
		    	bestCosto=this.calcolaCosto(parziale);	
			}
			
			else if (this.calcolaCosto(parziale)<bestCosto) {
		    	this.soluzione=new ArrayList<>(parziale);
		    	bestCosto=this.calcolaCosto(parziale);
		    }
		  
		}
		else {
		for (int i=0; i<disponibili.size();i++) {
			boolean flag=true;
			Citta cTemp=disponibili.get(i);
			if(cTemp.getCounter()>=NUMERO_GIORNI_CITTA_MAX ) {
				flag=false;	
			}
			
			if ((flag==true) && this.aggiuntaValida(cTemp, parziale)) {
			disponibili.get(i).increaseCounter();
			parziale.add(cTemp);
			cerca(parziale, livello+1,disponibili);
			parziale.remove(cTemp);
			disponibili.get(i).setCounter(cTemp.getCounter()-1);
				}
			
			
			}
		}
	}
	
	
	public int calcolaCosto (List<Citta> soluzione) {
		int costo=0;
		for (int i=0; i<soluzione.size();i++) {
			if (i==0) {
				costo=soluzione.get(i).getRilevamenti().get(i).getUmidita();
			}
			
			else {
				if(soluzione.get(i).getNome().compareTo(soluzione.get(i-1).getNome())!=0) {
	    costo+=100;
			}
			costo+=soluzione.get(i).getRilevamenti().get(i).getUmidita();
			}
			
			}
		return costo;
}
	
 /* public boolean parzialeValida (Citta c, List<Citta> parziale, int livello) {
	  boolean flag=true;
	  if (parziale.size()==0) {
		  return true;
	  }
	  if (parziale.size()==1) {
		  if (!parziale.get(0).equals(c)) {
			  return false;
		  }
	  }
	  for (int i=livello; i>0;i--) {
		  if (parziale.get(i))
	  }
	  
	  
	  
	  if (parziale.size()<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
		  
		  
	  }
	  
	  
	  
	  
	  else {
	        for (int i=0;i<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN;i++) {
	        	if (parziale.get(i).equals(c))
	        }
		  
	  }
	  
	  for (int i=1; i<=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN;i++) {
		  if (!parziale.get(i).equals(c)) {
			  flag=false;
		  }	  
	  }
	  
		int count=0;
		for (int i=0; i<parziale.size();i++) {
			
			
		}
	}
	*/
private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
	boolean flag=true;
			
		// verifica dei giorni minimi
		if (parziale.size()==0) //primo giorno posso inserire qualsiasi città
				return true;
		if (parziale.size()==1 || parziale.size()==2) {
			//siamo al secondo o terzo giorno, non posso cambiare
			//quindi l'aggiunta è valida solo se la città di prova coincide con la sua precedente
			return parziale.get(parziale.size()-1).equals(prova); 
		}
		//nel caso generale, se ho già passato i controlli sopra, non c'è nulla che mi vieta di rimanere nella stessa città
		//quindi per i giorni successivi ai primi tre posso sempre rimanere
		if (parziale.get(parziale.size()-1).equals(prova))
			return true; 
		// se cambio città mi devo assicurare che nei tre giorni precedenti sono rimasto fermo
		for (int i=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN; i>1;i--) {
			if (!parziale.get(parziale.size()-i).equals(parziale.get(parziale.size()-(i-1)))) {
				flag=false;	
			}
		}
		/*if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) 
		&& parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;*/
			
		return flag;
		
	}
}
	
