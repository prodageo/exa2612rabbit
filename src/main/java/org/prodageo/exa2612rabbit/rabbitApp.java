package org.prodageo.exa2612rabbit;
import org.prodageo.exa2612lib.sharedCounter ;

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

        while ( i < 10 ) {

            sc = sc + 1 ;
            System.out.println( currentTime () + " : " + sc );

            // wait a little bit
            i = i + 1 ;
            try {
                Thread.sleep(secondsToSleep * 1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static String currentTime ()
    {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) ;
    }
 }