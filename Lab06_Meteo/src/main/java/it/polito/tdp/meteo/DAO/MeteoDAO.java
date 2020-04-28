package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public double getMediaUmidita(int mese, String localita) {
		double media=0.0;
		final String sql ="SELECT AVG(Umidita) AS media "
				+ "FROM situazione"
				+ " WHERE MONTH(data)=? "
				+ "AND Localita=?";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, mese);
            st.setString(2, localita);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				media= rs.getDouble("media");
			}

			conn.close();
			return media;

		} 
		  catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}	
	}
	
	
	
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		List <Rilevamento> rilevamenti= new ArrayList<>();
		final String sql ="SELECT Data, Umidita "
				+ "FROM situazione "
				+ "WHERE MONTH(data)=? AND Localita=?"
				+ " ORDER BY Data ASC "
				+ "LIMIT 15";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, mese);
            st.setString(2, localita);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rilevamento rTemp= new Rilevamento (localita, rs.getDate("Data"),rs.getInt("Umidita"));
				rilevamenti.add(rTemp);
			}

			conn.close();
			return rilevamenti;

		} 
		  catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
	
	public List<Rilevamento> getAllRilevamentiMese(int mese) {
		List <Rilevamento> rilevamenti= new ArrayList<>();
		final String sql ="SELECT Data, Umidita,Localita "
				+ "FROM situazione "
				+ "WHERE MONTH(data)=?"
				+ " ORDER BY Data ASC "
				+ "LIMIT 15";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rilevamento rTemp= new Rilevamento (rs.getString("Localita"), rs.getDate("Data"),rs.getInt("Umidita"));
				rilevamenti.add(rTemp);
			}

			conn.close();
			return rilevamenti;

		} 
		  catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		
	}


}
