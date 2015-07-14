package com.sinfonier.bolts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PuntoChatParser extends BaseSinfonierBolt {

    // Class variables
	String htmlfield;
	
    // Must implement constructor. Do not touch
    public PuntoChatParser(String path) {
        super(path);
    }

    @Override
    public void userprepare() {
        // Initialize your DB Connections or non-serializable classes.
        // This method will be executed once.
    	
    	this.htmlfield = this.getParam("htmlfield");
    }

    @Override
    public void userexecute() {

    	String htmlstring = (String) this.getField(this.htmlfield);
    	
    	try{
	    	//username parse
	    	String usernameBegin = "<h2 id=\"nick_scheda\" style=\"margin:0px;\">";
	    	
	    	Integer beginNick = htmlstring.indexOf(usernameBegin);
	    	String username = htmlstring.subSequence(beginNick + usernameBegin.length(), htmlstring.length()).toString();
	    	
	    	username = username.subSequence(0, username.indexOf("<")).toString();
	    	this.addField("username", username);
	    	//fin username parse
    	}catch(Exception ex){
    		ex.printStackTrace();
    		this.addField("username", "null");
    	}
	    	
    	try{
	    	//location parse
	    	String locationBegin = "class=\"superblu\">";
	    	
	    	Integer beginLocation = htmlstring.indexOf(locationBegin);
	    	String location = htmlstring.subSequence(beginLocation + locationBegin.length(), htmlstring.length()).toString();
	    	
	    	location = location.subSequence(0,location.indexOf("<")).toString();
	    	this.addField("location", location);
	    	//fin location parse
    	}catch(Exception ex){
    		ex.printStackTrace();
    		this.addField("location", "null");
    	}
	    	
    	try{
	    	//age parse
	    	String ageBegin = "Ho <b>";
	    	
	    	Integer beginAge = htmlstring.indexOf(ageBegin);
	    	String age = htmlstring.subSequence(beginAge + ageBegin.length(), htmlstring.length()).toString();
	    	
	    	age = age.subSequence(0,age.indexOf(" anni")).toString();
	    	this.addField("age", age);
	    	//fin age parse
    	}catch(Exception ex){
    		ex.printStackTrace();
    		this.addField("age", "null");
    	}
	    	
    	try{
	    	//lastConnection parse -
	    	String lastConnectionBegin = "Ultimo avvistamento: ";
	    	
	    	Integer beginLastConnection = htmlstring.indexOf(lastConnectionBegin);
	    	String lastConnection = htmlstring.subSequence(beginLastConnection + lastConnectionBegin.length(), htmlstring.length()).toString();
	    	
	    	lastConnection = lastConnection.subSequence(0,lastConnection.indexOf("<")).toString();

	    	//Fecha actual
	    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    	Calendar fechaActual = Calendar.getInstance();
	    	Calendar last_connection = null;
	    	
	    	//Calculo la fecha de la Ãºltima conexiÃ³n
	    	if(lastConnection == "Ormai una meteora!!")
	    		last_connection = fechaActual;
	    	else{
	    		if(lastConnection.contains("giorni")){
	    			Integer diaRestado = Integer.parseInt(lastConnection.substring(0, 1));
	    			fechaActual.add(Calendar.DAY_OF_YEAR, -diaRestado);
	    			
	    			last_connection = fechaActual;
	    		}
	    		if(lastConnection.contains("mesi")){
	    			Integer mesRestado = Integer.parseInt(lastConnection.substring(0, 1));
	    			fechaActual.add(Calendar.MONTH, -mesRestado);
	    			
	    			last_connection = fechaActual;
	    		}
	    		if(lastConnection.contains("Non disponibile")){
	    			last_connection = null;
	    		}
	    	}
	    	//end last connection parse
	    	this.addField("last_connection", last_connection);

	    }catch(Exception ex){
	    		ex.printStackTrace();
	    		this.addField("last_connection", "null");
	    }
	    	
    	try{
	    	//bio parse
	    	String bioBegin = "<span class=\"verd12\">";
	    	
	    	Integer beginBio = htmlstring.indexOf(bioBegin);
	    	String bio = htmlstring.subSequence(beginBio + bioBegin.length(), htmlstring.length()).toString();
	    	
	    	bio = bio.subSequence(0, bio.indexOf("<")).toString();
	    	//end bio parse
	    	this.addField("bio", bio);

    	}catch(Exception ex){
    		ex.printStackTrace();
    		this.addField("bio", "null");
    	}
	    	
	    this.emit();
    }

    @Override
    public void usercleanup() {
        // Close connections, clean resources
    }

    @Override
    public void tickTupleCase() {
        // Write tickTuple case. This method will be called every tickTuple.
        // Usually is used for flush data, send alarms, window event ...
        // If your module not use this feature leave empty
    }
}