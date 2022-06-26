import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Scanner;

public class Program {

public static void main(String[] args) {
		
		final String url = "jdbc:postgresql://localhost/biblio";
		final String user = "postgres";
		final String password = "root";
		Scanner inp = new Scanner(System.in);
		
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		
		//Lister les auteurs 
		var listeAuteurs = new ArrayList<Auteur>();
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, user, password);
			sta = con.createStatement();
			var req = "select * from Auteur";
			var res = sta.executeQuery(req);
			while (res.next()) {
				var tmp = new Auteur(res.getLong("id"),res.getString("nom"), res.getString("prenom"),res.getString("telephone"), res.getString("email"));
				listeAuteurs.add(tmp);
			}
			for(var item : listeAuteurs) {
				System.out.println(item);
			}
	    } catch(SQLException ex) {
	    	ex.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("-------------------");
		
		//Récupérer un auteur à partir d'un ID
		Auteur auteur = new Auteur();
		try {
			  Class.forName("org.postgresql.Driver");
			  con = DriverManager.getConnection(url, user, password);
			  System.out.print("Récuperer depuis l'ID :");
			  var id=inp.nextLong();
			  inp.nextLine();
			  sta = con.createStatement();
			  var req= String.format("select * from Auteur where id=%d",id);
			  System.out.println(req);
			  var res = sta.executeQuery(req);
			  if(res.next()) {
				  var result = new Auteur(id,res.getString("nom"), res.getString("prenom"),res.getString("telephone"), res.getString("email"));
				  System.out.println(result);
			  }
	    } catch(SQLException ex) {
	    	ex.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
	    } catch (InputMismatchException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		System.out.println("-------------------");
		
		//Ajouter un auteur
		auteur = new Auteur("Toto", "Tata", "0660606060", "toto@tata.fr");
		try {
			  Class.forName("org.postgresql.Driver");
			  con = DriverManager.getConnection(url, user, password);
			  sta = con.createStatement();
			  var req= String.format("INSERT INTO Auteur(nom,prenom,telephone,email) VALUES('%s','%s','%s','%s');",auteur.getNom(),auteur.getPrenom(),auteur.getTelephone(),auteur.getEmail());
			  System.out.println(req);
			  sta.executeUpdate(req, Statement.RETURN_GENERATED_KEYS);
			  var keys = sta.getGeneratedKeys();
			  if (keys.next()) {
				  auteur.setId(keys.getLong("id"));
			  }
	    } catch(SQLException ex) {
	    	ex.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		System.out.println("-------------------");
		
		//Modifier un auteur
		try {
			  Class.forName("org.postgresql.Driver");
			  con = DriverManager.getConnection(url, user, password);
			  sta = con.createStatement();
			  Optional<Auteur> item = listeAuteurs.stream().filter((x) -> x.getNom().equals("Goncalves") && x.getPrenom().equals("Antonio")).findFirst();
			  if(item.isPresent()) {
				  var goncalves = item.get();
				  var email = String.format("%s.%s@example.com",goncalves.getPrenom(),goncalves.getNom());
				  var req= String.format("UPDATE Auteur SET email='%s' where id=%d;",email, goncalves.getId());
				  System.out.println(req);
				  sta.executeUpdate(req);
			  }

	    } catch(SQLException ex) {
	    	ex.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		System.out.println("-------------------");
		
		//Supprimer un auteur
		try {
			  Class.forName("org.postgresql.Driver");
			  con = DriverManager.getConnection(url, user, password);
			  sta = con.createStatement();
			  long id =5;
			  var item = listeAuteurs.stream().filter((x) -> x.getId()==id).findFirst();
			  var req= String.format("DELETE FROM Auteur where id=%d;",auteur.getId());
			  System.out.println(req);
			  sta.executeUpdate(req);
			  if(item.isPresent()) {
				  listeAuteurs.remove(item.get());
			  }
			  
	    } catch(SQLException ex) {
	    	ex.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
