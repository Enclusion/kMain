package code.breakmc.legacy.utils;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Calvin on 5/9/2015.
 */
public class YoutubeChannel {

    private JSONObject json;
    private String channelName;
    private URL channelURL;
    private String channelInformation;

    public YoutubeChannel(String name) {
        channelName = name;
        try {
            channelURL = new URL("http://gdata.youtube.com/feeds/api/users/" + name + "?v=2&alt=json");
        } catch (MalformedURLException e) {
        }
        update();
    }

    public boolean update() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(channelURL.openStream()));
            channelInformation = br.readLine();
            br.close();
            try {
                JSONParser parser = new JSONParser();
                json = (JSONObject) parser.parse(channelInformation);
                json = (JSONObject) json.get("entry");
                channelName = (String) json.get("yt$name");
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
        }
        return false;
    }

    public String getName() {
        return channelName;
    }

    public String getDisplayName() {
        JSONObject json2 = (JSONObject) json.get("yt$username");
        return (String) json2.get("display");
    }

    public String getLocation() {
        JSONObject json2 = (JSONObject) json.get("yt$location");
        return (String) json2.get("$t");
    }

    public String getSummary() {
        JSONObject json2 = (JSONObject) json.get("summary");
        return (String) json2.get("$t");
    }

    public String getGooglePlusId() {
        JSONObject json2 = (JSONObject) json.get("yt$googlePlusUserId");
        return (String) json2.get("$t");
    }

    public int getSubscriberCount() {
        JSONObject json2 = (JSONObject) json.get("yt$statistics");
        String s = (String) json2.get("subscriberCount");
        int number = Integer.parseInt(s);
        return number;
    }

    public int getTotalViews() {
        JSONObject json2 = (JSONObject) json.get("yt$statistics");
        String s = (String) json2.get("totalUploadViews");
        int number = Integer.parseInt(s);
        return number;
    }
}