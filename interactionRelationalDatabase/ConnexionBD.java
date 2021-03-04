package interactionRelationalDatabase;

import java.sql.*;



public class ConnexionBD {
	


		// méthode v�rifiant que le driver est bien installée
		
		
		int BD;
		Connection conn;
		// constructeur permettant de se connecter a la bonne base de données
		public ConnexionBD(int BD){
			 
			 conn= null;
			 try{
					//Class.forName("com.mysql.jdbc.Driver");
					try {
						Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
					} catch (InstantiationException | IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try{
						//Class.forName("com.mysql.jdbc.Driver");
						//Class.forName("com.mysql.cj.jdbc.Driver");
						//1 pour la base de donn�e locale
						if (BD==1){
							System.out.println("oups");
							conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/loinc2019?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
							System.out.println("connexion effectué avec succès félicitations!!!");
						}else if (BD==11){
							conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loinc?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
							System.out.println("connexion effectué avec succès félicitations!!!");
						}
						else if (BD==2){
						conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loincfr", "root", "");
						System.out.println("connexion au réseau effectué avec succès");
						}
						else if (BD==3){
							conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jnbase", "root", "");
						
						}
						//2 pour la base de donn�e rigel
						else {
							System.out.println("vous n'avez pas entrer de chiffre appropriée");
						}
					}
					catch (Exception e){
						System.out.println("échec de la connexion"+e.toString());
				
					}
			 }
			 catch(ClassNotFoundException e){	
				 System.out.println("Driver non retrouver");
			}
		}
		public Connection getconn() {
			return conn;
		}
		
		
		
	}


