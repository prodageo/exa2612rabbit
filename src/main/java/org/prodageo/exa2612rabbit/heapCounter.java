// FBABLIB : package org.prodageo.exa2612lib;
package org.prodageo.exa2612rabbit;

public class heapCounter  extends genericCounter 
{
    public int theHeapCounter ;

    public heapCounter ( char c1 ) throws Throwable {

		theHeapCounter = Character.getNumericValue ( c1 ) ;
		System.out.println(  "theHeapCounter : " + theHeapCounter );
	}

	public int incrementCounter ()
	{
		theHeapCounter = theHeapCounter + 1 ;
		return theHeapCounter ;
	}

	public void listContent ()
	{
		System.out.println( "Content of heap memory : " ) ;
		System.out.println( 0 + " : " + theHeapCounter );		
	}
}
