package com.jspiders.hrscode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelReservationSystem 
{
	private static final String url="jdbc:mysql://localhost:3306/hoteldb?";
	private static String user="root";
	private static String password="root";
	public static void main(String[] args)
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");  //1.Loading Driver//
		} 
		catch ( ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		try   //2.CREATE/ESTABLISH STATEMENT//
		{
			Connection con=DriverManager.getConnection(url, user, password);
			System.out.println();
			System.out.println("HOTEL MANAGEMENT SYSTEM");
			Scanner scn=new Scanner(System.in);
			System.out.println("1. Reserve a Room");
			System.out.println("2. View The Reservations");
			System.out.println("3. Get The Room Number");
			System.out.println("4. Update The Reservations");
			System.out.println("5. Delete The Reservations");
			System.out.println("0. Exit");
			System.out.println("Enter Your Choise");
			int choise=scn.nextInt();
			switch (choise)
			{
			case 1:reserveRoom(con,scn);
			       break;
			case 2:viewReservations(con);
				   break;
			case 3:getRoomNumber(con,scn);
			       break;
			case 4:updateReservation(con,scn);
			       break;
			case 5:deleteReservation(con,scn);
				   scn.close();
				   break;
			case 0:exit();
			       break;
		    default:System.out.println("Invalid Choise ...........Try Again!!!");
			}
		}
			
		catch (SQLException | InterruptedException e) 
		{
			e.printStackTrace();
		}
//		catch (InterruptedException e1)
//		{
//			e1.printStackTrace();
//			throw new RuntimeException(e1);
//		}
		}
	
	//1.method for adding the user//
	private static void reserveRoom(Connection con , Scanner scn) 
	{
		System.out.println("Enter The Guest Name");
		String name=scn.next();
		scn.nextLine();
		System.out.println("Enter The Room No");
		int roomno=scn.nextInt();
		System.out.println("Enter The Contact Number");
		long number=scn.nextLong();
		System.out.println("Enter The Date And Time ");
		

		//3.Issue The Query//
		String query1="INSERT INTO RESERVATION(GUESTNAME, ROOMNO, CONTACT)"+"VALUES('"+name+"',"+roomno+",'"+number+"')";
		Statement stmt;
		try 
		{
			//4.Create Or Establish Statement//
			stmt=con.createStatement();
			int count=stmt.executeUpdate(query1);
			//5.Execute Query//
			if (count>0) 
			{
				System.out.println("Reservation Successful!!!");	
			}
			else
			{
				System.out.println("Reservation Failed");
				
			}
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
	//2.Method For View The Reservation//
	private static void viewReservations(Connection con)
	{
		String query2="SELECT ID, GUESTNAME, ROOMNO, CONTACT, DATE FROM RESERVATION";
		try 
		{
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(query2);
			System.out.println("Current Reservation : ");
			System.out.println("+--------------|---------------|-------------|----------------|-----------------------+");
			System.out.println("|Reservation ID| GuestName     | RoomNumber  | ContactNumber  |       ReservationDate |");
			System.out.println("+--------------|---------------|-------------|----------------|----------------------+");
			while (rs.next()) 
			{
				int id =rs.getInt("ID");
				String name=rs.getString("GuestName");
				int roomno=rs.getInt("RoomNo");
				long contact=rs.getLong("Contact");
				String date=rs.getTimestamp("Date").toString();
				
				//Format And Display The Reservation Data In A Table Like Format//
				System.out.printf("| %-15d | %-11s | %-11d |  %-14s | %-20s | \n", id,name,roomno,contact,date );
			}
			System.out.println("+--------------------------------------------------------------------------------+");
			
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	//3.Method Getting The Room Details//
	private static void getRoomNumber(Connection con , Scanner scn) 
	{
		System.out.println("Enter Reservation ID : ");
		int id=scn.nextInt();
		System.out.println("Enter Guest Name : ");
		String name=scn.next();
		String query3="SELECT ROOMNO FROM RESERVATION WHERE ID="+id+" AND GUESTNAME = '"+name+"'";
		try 
		{
			Statement stmt=con.createStatement(); //Create Statement And Throws SQLException//
			ResultSet rs=stmt.executeQuery(query3);
			if (rs.next()) 
			{
				int room=rs.getInt("RoomNo");
				System.out.println("Room Number For Reservation ID "+id+" Guest Name "+name+" is "+room);	
			}
			else
			{
				System.out.println("Rservation Not Found For Given ID And GUESTNAME..............");
				
			}
			
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	//4.Method For Reservation Exist Checking//
	private static boolean reservationExists(Connection con ,int id) 
	{
		String query4="SELECT ID FROM RESERVATION WHERE ID="+id;
		try 
		{
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(query4);
			
			return rs.next();//If there is a result , the reservation wiLl exists
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
			
		}
		
	}
	//5.Method To Update The Reservation//
	private static void updateReservation(Connection con , Scanner scn)
	{
		System.out.println("Enter The Reservation ID To Update");
		int id=scn.nextInt();
		scn.nextLine();// consume the new line character//
		
		if (!reservationExists(con,id))
		{
			System.out.println("Reservation is not Found For Given ID");
			return;
			
		}
		System.out.println("Enter The New Guest Name : ");
		String newName=scn.nextLine();
		System.out.println("Enter The New Room Number");
		int newRoomNo=scn.nextInt();
		System.out.println("Enter The New Contact Number");
		long newContact=scn.nextLong();
		String query5="UPDATE RESERVATION "+"+SET GUESTNAME='"+newName+"'"+"ROOMNO="+newRoomNo+","+"CONTACT='"+newContact+","+"WHERE ID="+id;
		
		try 
		{
			Statement stmt=con.createStatement();
			int count=stmt.executeUpdate(query5);
			if (count>0)
			{
				System.out.println("Reservation Updated Successful!!...");
				
			}
			else
			{
				System.out.println("Reservation Updated Failed");
				
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
	//6.Method For Delete Reservation//
	private static void deleteReservation(Connection con , Scanner scn) 
	{
		System.out.println("Enter The Reservation ID To Delete");
		int id =scn.nextInt();
		
		if (!reservationExists(con,id)) 
		{
			System.out.println("Reservation is not found for the given ID");
			return;
		}
		String query6="DELETE FROM RESERVATION WHERE ID="+id;
		try 
		{
			Statement stmt=con.createStatement();
			int count=stmt.executeUpdate(query6);
			if (count>0) 
			{
				System.out.println("Reservation Deleted Successful!!!...");
				
			}
			else
			{
				System.out.println("Reservation Deleted Failed!!!...");
				
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	//7.Method For Exit The Reservation/
	private static void exit()throws InterruptedException
	{
		System.out.println("Exiting System");
		for (int i = 5; i >0; i--) 
		{
			System.out.println("..."+ i);
			try 
			{
				Thread.sleep(1000);
				
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
				// TODO: handle exception
			}
		}
//		int i=5;
//		while (i!=0) 
//		{
//			System.out.println("+++  ");
//			Thread.sleep(1000);
//			i--;
//		}
		System.out.println();
		System.out.println("ThankYou For Using Hotel Reservation System!!!...");
	}
	
}