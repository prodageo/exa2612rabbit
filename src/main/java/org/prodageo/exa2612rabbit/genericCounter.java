// FBABLIB : package org.prodageo.exa2612lib;
package org.prodageo.exa2612rabbit;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class genericCounter 
{
	public abstract int incrementCounter () ;

	public abstract void listContent () ;
	
    static String currentTime ()
    {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) ;
    }	
}
