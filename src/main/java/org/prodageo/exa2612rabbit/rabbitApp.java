package org.prodageo.exa2612rabbit;

// FBABLIB import org.prodageo.exa2612lib.sharedCounter ;

/*
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
*/

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class rabbitApp
{
	public static genericCounter theCounter ; 
    public static int secondsToSleep = 5 ; // 1 for rabbit
	
    public static void main( String[] args )
    {
		String cmd ;		
		char cmd0 = '-' ;
		char cmd1 = 's' ;

		String start ;		
		char start0 = '0' ;


		if ( args.length > 0 )
		{
			cmd  = args[0] ;
			cmd0 = cmd.charAt(0) ;
			cmd1 = cmd.charAt(1) ;			
			
			if ( args.length == 2 )
			{
				start = args[1] ;
				start0 = start.charAt(0) ;
			}
		}
		else
		{
			cmd0 = '-' ;
			cmd1 = 's' ;
			start0 = '0' ;
		}

		System.out.println(  "command : " + cmd0 + cmd1 + ' ' + start0 );

		try
		{		
			if ( cmd0 == '-' )
			{
				if ( cmd1 == 'l' ) // treat shared memory
				{
					theCounter = new sharedCounter ( '!' ) ;
				}
				else if ( cmd1 == 's' ) // list shared memory content
				{
					theCounter = new sharedCounter ( start0 ) ;
					count ( start0 ) ;
				}
				else  // treat heap memory
				{
					theCounter = new heapCounter ( start0 ) ;
					count ( start0 ) ;
				}
			}
		}
		catch ( Throwable e)
		{
			e.printStackTrace();
		}		
	}
		
	public static void count ( char start0 )
	{
		try
		{
			// loop to increment the counter (maybe concurrently with other thread !)
			int i = 0 ;
			int previous_c ;
			int current_c = Character.getNumericValue ( start0 ) ;			
			while ( i < 4 )
			{
				
				previous_c = current_c ;
				current_c = theCounter.incrementCounter( ) ;

				System.out.println( theCounter.currentTime () + " : " + previous_c + " -> " + current_c );

				// wait a little bit
				try
				{
					Thread.sleep(secondsToSleep * 1000);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
				i = i + 1 ;
			}
			
			// show the content of the shared memory
			theCounter.listContent() ;

		}        
		catch ( Throwable e)
		{
			e.printStackTrace();
		}
	}
 }