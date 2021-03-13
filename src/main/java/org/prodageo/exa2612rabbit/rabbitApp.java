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
    public static void main( String[] args )
    {
        int sc = sharedCounter.theSharedCounter ;
        int i = 0 ;
        int secondsToSleep = 1 ; // 1 for rabbit
		char start = '0' ;

		if ( args.length > 0 )
		{
			String arg0  = args[0] ;
			start = arg0.charAt(0) ;
		}
		
		if ( start == '-' )
		{

			try
			{
				sharedCounter smc = new sharedCounter ( '!' ) ;
				
				int j = 0 ;
				while ( j < 15 ) {
					char c = smc.getStoredCharacterAt ( j ) ;
					System.out.println( j + " : " + c );
					j = j + 1 ;
				}
			}        
			catch ( Throwable e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				sharedCounter smc = new sharedCounter ( start ) ;



				while ( i < 4 ) {

					// sc = sc + 1 ;
					sc = smc.incrementSharedMemoryCounter( ) ;

					System.out.println( currentTime () + " : " + sc );

					// wait a little bit
					i = i + 1 ;
					try {
						Thread.sleep(secondsToSleep * 10000);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}
				
				
				int j = 0 ;
				while ( j < 15 ) {
					char c = smc.getStoredCharacterAt ( j ) ;
					System.out.println( j + " : " + c );
					j = j + 1 ;
				}
				

			}        
			catch ( Throwable e)
			{
				e.printStackTrace();
			}
		}
    }

    private static String currentTime ()
    {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) ;
    }
 }