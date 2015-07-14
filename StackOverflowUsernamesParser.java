package parser;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;

public class Parser {

    public static void main(String[] args) {    
    
    List<String> htmls = null;
    
        for(int i=1; i<1000; ++i){

            //-----------------------------------[SÃ³lo para coger html de la web]
            String content = null;
            URLConnection connection = null;
            try {
                connection =  new URL("http://stackoverflow.com/users/"+i).openConnection();
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\Z");
                content = scanner.next();
            //-----------------------------------[/SÃ³lo para coger html de la web]

                String comUser = "<title>User";
                String finUser = "- Stack Overflow"; 

                Integer comienzo = content.indexOf(comUser);
                Integer fin = content.indexOf(finUser);

                if(comienzo != -1 && fin != -1)
                    System.out.println(content.subSequence(comienzo+comUser.length(), fin).toString().trim());

            }catch ( Exception ex ) {
                System.out.println("ERROR - "+ex.toString());
            }
        }
    }
}