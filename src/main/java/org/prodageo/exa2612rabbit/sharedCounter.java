// FBABLIB : package org.prodageo.exa2612lib;
package org.prodageo.exa2612rabbit;

import java.io.File;
// import java.nio.File;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;
// import java.nio.charset.Charset;

public class sharedCounter 
{
    public static int theSharedCounter ;

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






	public int incrementSharedMemoryCounter ()
    {
        // thePrivateSharedCounter = thePrivateSharedCounter + 1 ;
		
		/*  https://stackoverflow.com/questions/46343616/how-can-i-convert-a-char-to-int-in-java/46343671
		
			char x = '9';
			int y = Character.getNumericValue(x);   //use a existing function
			System.out.println(y + " " + (y + 1));  // 9  10
		*/

		
		char c1 = getLastStoredCharacter ( ) ;
		System.out.println(  "getLastStoredCharacter : " + c1 );
		
		// 		int i = c - '0' ;
		int i = Character.getNumericValue ( c1 ) ;
		
		int REDIX=10;//redix 10 is for decimal number, for hexa use redix 16  
		char c2 = Character.forDigit(i+1,REDIX);    		
		System.out.println(  "before put : " + c2 );
		put ( c2 ) ;

		
/*		
		char c ;
		char v ;		
		
		c = theSharedMemoryBuffer.get (0) ;
		System.out.println(  "view get(0)" + c );
		v = theSharedMemoryBuffer.get (1) ;
		System.out.println( "view get(1)" + v );
		v = theSharedMemoryBuffer.get (2) ;
		System.out.println( "view get(2)" + c );
		v = theSharedMemoryBuffer.get (3) ;
		System.out.println( "view get(3)" + c );
		
		// int i = c - '0' ;
		int i = c ;
		i = i + 1 ;
		c = (char) i ;
		
		String s = String.valueOf(c);
		System.out.println( "c increment : " + s );
		
		// char[] bufferChunk = s.toCharArray();
        theSharedMemoryBuffer.put( s );
		char cs = theSharedMemoryBuffer.get(0) ;
		System.out.println( "s after : " + cs );

		v = theSharedMemoryBuffer.get (1) ;
		System.out.println( "view get after (1)" + v );
		v = theSharedMemoryBuffer.get (2) ;
		System.out.println(  "view get(2) after" + v );
		
		cs = theSharedMemoryBuffer.get(i) ;
		System.out.println( "s after get(" + i + "): " + cs );			
*/

        return i ;
    }




/*
    public sharedCounter ( ) throws Throwable {
        // https://webdevdesigner.com/q/shared-memory-between-two-jvms-21766/

        // https://github.com/caplogic/Mappedbus/tree/master
        // In the code above the file "/tmp/test" is on disk and thus it's memory mapped by the library. To use the library with shared memory instead, point to a file in "/dev/shm", for example, "/dev/shm/test".
        File f = new File( "/dev/shm/test" );

        FileChannel channel = FileChannel.open( f.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE );

        MappedByteBuffer b = channel.map( MapMode.READ_WRITE, 0, 4096 );
        theSharedMemoryBuffer = b.asCharBuffer();

        char[] string = "0".toCharArray();
        theSharedMemoryBuffer.put( string );
    }
*/





    // constructor : set the shared memory
	private File             theSharedMemoryFile ;
	private File             theSharedMemoryRandomAccessFile ;	
    private CharBuffer       theSharedMemoryBuffer ;
	private MappedByteBuffer theSharedMemoryMappedBuffer ;

	private int theNextPutPosition = 0 ;


    private void put ( char c )
	{
		System.out.println( "theNextPutPosition begin : " + theNextPutPosition );
        // char[] string = c.toCharArray(); // char cannot be dereferenced
		String string = String.valueOf(c);
		System.out.println( "put " + string + " at " + theNextPutPosition );
        theSharedMemoryBuffer.put( string );
		theNextPutPosition = theNextPutPosition + 1 ;
		char c1 = theSharedMemoryBuffer.get( theNextPutPosition - 1 ) ;
		System.out.println( "check value : " + c1 );
		System.out.println( "theNextPutPosition end : " + theNextPutPosition );
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
		return getStoredCharacterAt ( theNextPutPosition - 1 ) ; // at this position, a value has already been stored
	}
	
	
    public sharedCounter ( char start ) throws Throwable {
        // https://webdevdesigner.com/q/shared-memory-between-two-jvms-21766/

		// https://github.com/AlexanderSchuetz97/Ivshmem4j : QEMU ivshmem (inter virtual machine shared memory) from a Java application running inside a JVM.
        // https://github.com/caplogic/Mappedbus/tree/master
        // In the code above the file "/tmp/test" is on disk and thus it's memory mapped by the library. To use the library with shared memory instead, point to a file in "/dev/shm", for example, "/dev/shm/test".
		

		if ( isWindows() )
		{
			theSharedMemoryFile = new File( "c:/tmp/mapped.txt" ) ;
			// RandomAccessFile memoryMappedFile = new RandomAccessFile("largeFile.txt", "rw");

			// https://www.javacodegeeks.com/2013/05/power-of-java-memorymapped-file.html
			// f.delete() ;
		}
		else
		{
			
			theSharedMemoryFile = new File( "/dev/shm/test" );
			// RandomAccessFile memoryMappedFile = new RandomAccessFile("largeFile.txt", "rw");			
			// f.delete() ;			
		}

        FileChannel channel = FileChannel.open( theSharedMemoryFile.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE );
		// theSharedMemoryFile
		
        MappedByteBuffer theSharedMemoryMappedBuffer = channel.map( MapMode.READ_WRITE, 0, 4096 );
        theSharedMemoryBuffer = theSharedMemoryMappedBuffer.asCharBuffer(); // est-ce nécessaire ?
		
        put( start );

/*
		// https://javarevisited.blogspot.com/2012/01/memorymapped-file-and-io-in-java.html#axzz6oSL19K7l
        // MappedByteBuffer out = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);
*/
		
		
/*		
        // MappedByteBuffer b = channel.map( MapMode.READ_WRITE, 0, 4096 );
		theSharedMemoryByteBuffer
		
        // theSharedMemoryBuffer = b.asCharBuffer();
*/
		/*
        char[] string = "0".toCharArray();
		System.out.println( "c init : " + string[0] );
        theSharedMemoryBuffer.put( string );
		*/

        /*

            // read shared memory
            char c;
            while( ( c = charBuf.get() ) != 0 ) {
                System.out.print( c );
            }
            System.out.println();
        */
		
    }



	private static String OS = System.getProperty("os.name").toLowerCase();

/*
	public static void main(String[] args) {

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
*/

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



}
