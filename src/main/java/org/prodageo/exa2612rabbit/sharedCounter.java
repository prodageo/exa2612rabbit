// FBABLIB : package org.prodageo.exa2612lib;
package org.prodageo.exa2612rabbit;

import java.io.File;
// import java.nio.File;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;

public class sharedCounter    extends genericCounter 
{
    public static int theSharedCounter ;


/*
    // a static variable : the same for all objects instances of the sharedCounter class
    private static int thePrivateStaticSharedCounter ;

    public static int getPrivateStaticSharedCounter ()
    {
        return thePrivateStaticSharedCounter ;
    }

    public int incrementPrivateStaticSharedCounter ()
    {
        thePrivateStaticSharedCounter = thePrivateStaticSharedCounter + 1 ;
        return thePrivateStaticSharedCounter ;
    }
*/




    // a non-static variable : specific to instance object in which they are created. 
    private int thePrivateSharedCounter ;

    public int getPrivateSharedCounter ()
    {
        return thePrivateSharedCounter ;
    }

    public int incrementPrivateSharedCounter ()
    {
        thePrivateSharedCounter = thePrivateSharedCounter + 1 ;
        return thePrivateSharedCounter ;
    }



	public int incrementCounter ()
    {
		return incrementSharedMemoryCounter () ;
	}


	public int incrementSharedMemoryCounter ()
    {
	
		char c1 = getLastStoredCharacter ( ) ;
		// System.out.println(  "getLastStoredCharacter : " + c1 );
		
		int i = Character.getNumericValue ( c1 ) ;
		
		int REDIX=10; //redix 10 is for decimal number, for hexa use redix 16  
		char c2 = Character.forDigit(i+1,REDIX);    		
		// System.out.println(  "incrementSharedMemoryCounter before put : " + c2 );
		put ( c2 ) ;
		
		i = Character.getNumericValue ( c2 ) ;

        return i ;
    }






    // constructor : set the shared memory
	private File             theSharedMemoryFile ;
	private FileChannel      theSharedMemoryChannel  ;
    private CharBuffer       theSharedMemoryBuffer ;




    private void put ( char c )
	{
		String string = String.valueOf(c);

		
		int thePositionBefore = theSharedMemoryBuffer.position() ;
		// System.out.println( "position before put : " + thePositionBefore ) ;
        theSharedMemoryBuffer.put( string );
		int thePositionAfter = theSharedMemoryBuffer.position() ;
		// System.out.println( "position after put : " + thePositionAfter ) ;
		
		theSharedMemoryBuffer.rewind(); // flip() position returns to 0 (in reading mode)

		int thePositionAfterFlip = theSharedMemoryBuffer.position() ;
		// System.out.println( "position after flip : " + thePositionAfterFlip ) ;
		
		char c1 = theSharedMemoryBuffer.get() ;
		// after each get, rewind to 0 !!!
		theSharedMemoryBuffer.rewind(); // flip() position returns to 0 (in reading mode)
		// System.out.println( "check value : " + c1 );
		// System.out.println( "theNextPutPosition end : " + theNextPutPosition );
	}
	

    private int getCurrentCursorPosition ( )
	{
		// put cursor position can not but set => manage a cursor locally !
		// return theNextPutPosition - 1 ; // at this position, a value has already been stored
		
		// http://tutorials.jenkov.com/java-nio/buffers.html#buffertypes
		return theSharedMemoryBuffer.position() ;
	}

    public char getStoredCharacterAt ( int p )
	{
		// put cursor position can not but set => manage a cursor locally !
		return theSharedMemoryBuffer.get( p ) ; // at this position, a value has already been stored
	}

    private char getLastStoredCharacter ( )
	{
		// SHOULD RAISE AN EXCEPTION IF theNextPutPosition == 0 !
		return getStoredCharacterAt ( 0 ) ; // at this position, a value has already been stored
	}
	
	
    public sharedCounter ( char start ) throws Throwable {
  
		// https://webdevdesigner.com/q/shared-memory-between-two-jvms-21766/

		// https://github.com/AlexanderSchuetz97/Ivshmem4j : 
		// 		QEMU ivshmem (inter virtual machine shared memory) from a Java application running inside a JVM.
        // https://github.com/caplogic/Mappedbus/tree/master
        // 		In the code above the file "/tmp/test" is on disk and thus it's memory mapped by the library.
		//		To use the library with shared memory instead, point to a file in "/dev/shm", for example, "/dev/shm/test".
		

		if ( isWindows() )
		{
			theSharedMemoryFile = new File( "c:/tmp/mapped.txt" ) ;
		}
		else
		{
			
			theSharedMemoryFile = new File( "/dev/shm/test" );
		}

        FileChannel theSharedMemoryChannel = FileChannel.open( theSharedMemoryFile.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE );
		
        MappedByteBuffer theSharedMemoryMappedBuffer = theSharedMemoryChannel.map( MapMode.READ_WRITE, 0, 4096 );
        theSharedMemoryBuffer = theSharedMemoryMappedBuffer.asCharBuffer(); // est-ce nécessaire ? oui

		// in case the exec is called to list the content of the Shared Memory
		// mvn exec:java -Dexec.args="-l"
		if ( start != '!' )
		{
			put( start );
		}
    }



	private static String OS = System.getProperty("os.name").toLowerCase();

	public static void print_os ()
	{
		System.out.println(OS);

		if (isWindows()) {
			System.out.println("This is Windows");
		} else if (isMac()) {
			System.out.println("This is Mac");
		} else if (isUnix()) {
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
	}

	public static boolean isWindows() {
		return OS.contains("win");
	}

	public static boolean isMac() {
		return OS.contains("mac");
	}

	public static boolean isUnix() {
		return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	}

	public static boolean isSolaris() {
		return OS.contains("sunos");
	}
	public static String getOS(){
		if (isWindows()) {
			return "win";
		} else if (isMac()) {
			return "osx";
		} else if (isUnix()) {
			return "uni";
		} else if (isSolaris()) {
			return "sol";
		} else {
			return "err";
		}
	}		

	public void listContent ()
	{
			try
			{
		
				int j = 0 ;
				while ( j < 1 ) {
					char c = getStoredCharacterAt ( j ) ;
					System.out.println( "Content of shared memory : " ) ;
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
