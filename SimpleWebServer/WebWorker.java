/**
* Testing WebWorker.java push to repo
* 
*
* Web worker: an object of this class executes in its own new thread
* to receive and respond to a single HTTP request. After the constructor
* the object executes on its "run" method, and leaves when it is done.
*
* One WebWorker object is only responsible for one client connection.
* This code uses Java threads to parallelize the handling of clients:
* each WebWorker runs in its own thread. This means that you can essentially
* just think about what is happening on one client at a time, ignoring
* the fact that the entirety of the webserver execution might be handling
* other clients, too.
*
* This WebWorker class (i.e., an object of this class) is where all the
* client interaction is done. The "run()" method is the beginning -- think
* of it as the "main()" for a client interaction. It does three things in
* a row, invoking three methods in this class: it reads the incoming HTTP
* request; it writes out an HTTP header to begin its response, and then it
* writes out some HTML content for the response content. HTTP requests and
* responses are just lines of text (in a very particular format).
*
**/

import java.net.Socket;
import java.lang.Runnable;
import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.util.TimeZone;

    public class WebWorker implements Runnable
    {

    private Socket socket;

    /**
    * Constructor: must have a valid open socket
    **/
    public WebWorker(Socket s)
    {
        socket = s;
    }

    /**
    * Worker thread starting point. Each worker handles just one HTTP
    * request and then returns, which destroys the thread. This method
    * assumes that whoever created the worker created it with a valid
    * open socket object.
    **/
    public void run()
    {
	// Make a String called webAdd to hold the web address
        String webAdd = "";
        
        System.err.println("Handling connection...");
        try {
            InputStream  is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

	    // Set the web address to the outcome of the readHTTP method. This
	    // should give us the URL.
            webAdd = readHTTPRequest(is);
            
            // Because we added a third variable, we want to send the URL to the writeHTTP
	    // and writeContent methods.
            writeHTTPHeader(os, "text/html", webAdd);  
            writeContent(os, "text/html", webAdd);
            os.flush();
            socket.close();
        } catch (Exception e) {
            System.err.println("Output error: " + e);
        }
        System.err.println("Done handling connection.");
        return;
    }

    /**
    * Read the HTTP request header.
    **/

    // I wante to return the URL String, so I changed it to String instead of void.
    private String readHTTPRequest(InputStream is)
    {
        String line;
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
	String wa = "";

        while (true) {
            try {
                while (!r.ready()) Thread.sleep(1);
                line = r.readLine();

		// If the line contains GET, we want to create a substring of the address and get everything after
		// the http portion. Hence the substring. The for loop is then replacing the wa String with a substring
		// from element 0 to element i (wherever it finds a space).
                if (line.contains("GET ")) {
                    wa = line.substring(4); 
                  	  for(int i = 0; i < wa.length(); i++) {
                      		  if (wa.charAt(i) == ' '){
                          		  wa = wa.substring(0 , i);
			} // end 2nd if
                    }// end for
                }// end 1st if
                System.err.println("Request line: (" + line + ")");
                if (line.length() == 0)
                    break;
            } catch (Exception e) {
                System.err.println("Request error: " + e);
                break;
            }
        }
	// Return the Web address.
        return wa;
    }

    /**
    * Write the HTTP header lines to the client network connection.
    * @param os is the OutputStream object to write to
    * @param contentType is the string MIME content type (e.g. "text/html")
    **/
    private void writeHTTPHeader(OutputStream os, String contentType, String webAdd) throws Exception
    {
        Date d = new Date();
        DateFormat df = DateFormat.getDateTimeInstance();
        df.setTimeZone( TimeZone.getTimeZone("GMT-6") );

	// Creating a new string (wac = Web Address Copy lol) starting with a period before the web address.
	// Creating a new file named inp.
	String wac = '.' + webAdd;
	File inp = new File(wac);

        //if the file doesn't exist, change HTTP status.
        try {
            FileReader file = new FileReader(inp);
            BufferedReader r = new BufferedReader(file);
        } catch(FileNotFoundException e) {
            System.out.println("File not found: " + webAdd);
            os.write("HTTP/1.1 404 Error: Not Found\n".getBytes());
        }
        // If everything is fine, keep 200 OK status.
        os.write("HTTP/1.1 200 OK\n".getBytes());
        os.write("Date: ".getBytes());
        os.write((df.format(d) ).getBytes());
        os.write("\n".getBytes());
        os.write("Server: Gabby's very own server\n".getBytes());
        os.write("Connection: close\n".getBytes());
        os.write("Content-Type: ".getBytes());
        os.write(contentType.getBytes());
        os.write("\n\n".getBytes()); // HTTP header ends with 2 newlines
        return;
    }

    /**
    * Write the data content to the client network connection. This MUST
    * be done after the HTTP header has been written out.
    * @param os is the OutputStream object to write to
    **/
    private void writeContent(OutputStream os, String contentType, String webAdd) throws Exception
    {
	// Added date information since cs371date needs to print if found.
        Date d = new Date();
        DateFormat df = DateFormat.getDateTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("GMT-6"));

	// Create a string specifically for the file content and another webAdd copy starting with a period.
	// Also creating a new file named inp again.
        String contents = "";
        String wac = "." + webAdd.substring(0, webAdd.length());
        String date = df.format(d);
        File inp = new File(wac);

        // While the contrents in the file aren't NULL, write the contents of the file in contents string.
	// If the contents of the file contain "<cs371date>", write the date. If it contains "<cs371server>",
	// write my identification string. If file not found, throw File Not Found exception.
            try{
                FileReader iRead = new FileReader(inp);
                BufferedReader iBuff = new BufferedReader(iRead);
             	   while((contents = iBuff.readLine()) != null) {
               	     os.write(contents.getBytes());
               	     os.write("\n".getBytes());
                	    if (contents.contains("<cs371date>")) {
                      		  os.write(date.getBytes());
                   	 } // end if
                   	    if (contents.contains("<cs371server>")){
                        os.write("Ayeeeee my server works! brb playing WoW classic.\n".getBytes());
                }// end if
	} // end while
                
            } catch(FileNotFoundException e) {
                System.err.println("File not found: " + webAdd);
                os.write("<h1>Error: 404 Not found<h1>\n".getBytes());
            } // end try-catch
        
    } // end method

} // end class
