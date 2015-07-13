/*
The MIT License (MIT)

Copyright (c) 2015 sinfonier-project

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

*/

package com.sinfonier.bolts;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;

public class LastFMBolt extends BaseSinfonierBolt {

    // Class variables
	String apikey;
	String user;
	
    // Must implement constructor. Do not touch
    public LastFMBolt(String path) {
        super(path);
    }

    @Override
    public void userprepare() {
        // Initialize your DB Connections or non-serializable classes.
        // This method will be executed once.
    	
    	this.apikey = this.getParam("apikey");

    }

    @Override
    public void userexecute() {

	this.user = (String) this.getField("username");
    	
    	 String content = null;
         URLConnection connection = null;
         try{
             connection =  new URL("http://ws.audioscrobbler.com/2.0/?method=user.getinfo&user="+this.user+"&api_key="+this.apikey+"&format=json").openConnection();
             Scanner scanner = new Scanner(connection.getInputStream());
             scanner.useDelimiter("\\Z");
             content = scanner.next();
        
         //"Limpio" el JSON original
         content = content.subSequence(8, content.length()-1).toString();
         }catch(Exception e){
        	 e.printStackTrace();
         }
         
         Map<String, Object> map = new HashMap<String, Object>();
         
         JSONObject j = new JSONObject(content);
         
         map.put("name", j.getString("name"));
         map.put("realname", j.getString("realname"));
         map.put("urlLastFM", j.getString("url"));
         map.put("country", j.getString("country"));
         map.put("age", j.getString("age"));
         map.put("gender", j.getString("gender"));
         map.put("playcount", j.getString("playcount"));

         this.addField("lastFM", map);
         
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