package createFrenchLoincTable;

import java.sql.SQLException;
import java.sql.Statement;

import interactionRelationalDatabase.ConnexionBD;

public class FrenchLoincTableCreation {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		String query= "create TABLE frenchLoinc AS SELECT * FROM belgianvariants " 
				  + "UNION "
				  + "SELECT * FROM canadianvariant "
				  + "UNION "
				  + "SELECT * FROM frenchvariant";
//		String query1= "create TABLE frenchLoinc2 AS SELECT * FROM belgianvariants " 
//				  + "UNION "
//				  + "SELECT * FROM canadianvariant ";
		ConnexionBD loinc2019DataBase= new ConnexionBD(1);
		System.out.println(query);
		
		Statement  stmt = loinc2019DataBase.getconn().createStatement();
	      stmt.executeUpdate(query);
	      System.out.println("finish ");
	

	}

}
