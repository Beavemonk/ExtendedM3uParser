package com.beavermonk.m3uParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M3UPlaylist 
{

// Uncomment for proper parsing of args
//   private static final String tagRegex = "[ ]+[^=]+=\"[^\"]*\"";
//   private static final String m3uRegex= "#[A-Z]+:([^ ,]+)(("+tagRegex + ")*),([^\\n]*)[\\n]*(http:.*)$";
   private static final String keyValueRegex = "([^=\"]+)=\"([^\"]*)\"";
   private static final String tagRegex = "[ ]+([^=]+=\"[^\"]*\")";
   private static final String m3uRegex= "#[A-Z]+:([^ ,]+)("+tagRegex + ")*,([^\\n]+)(.*)";

   private static final Pattern pattern = Pattern.compile(m3uRegex, Pattern.DOTALL);    
   private static final Pattern tagPattern = Pattern.compile(tagRegex);    
   private static final Pattern keyValuePattern = Pattern.compile(keyValueRegex);    

   
   private List<M3UPlaylistEntry> entries;
   
   private M3UPlaylist()
   {
      // use static builder
   }
   
   public List<M3UPlaylistEntry> getEntries() {
      return entries;
   }

   public void setEntries(List<M3UPlaylistEntry> entries) {
      this.entries = entries;
   }



   public static M3UPlaylist parse(URL m3uUrl) throws Exception
   {
      HttpURLConnection.setFollowRedirects(false);
      URLConnection uc = m3uUrl.openConnection();
      uc.connect();
      
      BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

      List<M3UPlaylistEntry> entries = new ArrayList<M3UPlaylistEntry>();
      M3UPlaylist returnPlayList = new M3UPlaylist();
      returnPlayList.setEntries(entries);
      
      String string = in.readLine();
      M3UPlaylistEntry lastCreatedEntry = null;
      
      while(string != null)
      {
         if(string.startsWith("#") && !string.startsWith("#EXTINF") || string.trim().length() == 0)
         {
            // we ignore these for now
         }
         else if(string.startsWith("#EXTINF"))
         {
            lastCreatedEntry = parseLine(string);
            entries.add(lastCreatedEntry);
         }
         else
         {
            // this is probably the URL
            try
            {
               lastCreatedEntry.setUrl(new URL(string.trim()));
            }
            catch(MalformedURLException e)
            {
               lastCreatedEntry.setUrl(new File(string.trim()).toURI().toURL());
            }
         }

         // Next line
         string = in.readLine();
      }

      
      return returnPlayList;
      
   }

   static M3UPlaylistEntry parseLine(String line)
   {
      Matcher matcher = pattern.matcher(line);
      M3UPlaylistEntry returnValue = new M3UPlaylistEntry();
      
      if(matcher.lookingAt())
      {
         for(int i=0; i<matcher.groupCount(); i++)
         {
            System.out.println("Group " + i + ": "+ matcher.group(i));
            
            if(i == 1)
            {
               returnValue.setTrackNumber(Integer.valueOf(matcher.group(i)));
            }
            else if(i == 2)
            {
               // We extract any meta info (key values)
               String tagLine = matcher.group(i);
               Map<String, String> keyValues = new HashMap<String, String>();
               returnValue.setTags(keyValues);
               if(tagLine != null && tagLine.trim().length() > 0)
               {
                  Matcher m = tagPattern.matcher(tagLine);

                  while(m.find())
                  {
                     Matcher keyValueMatcher = keyValuePattern.matcher(m.group(1));

                     if(keyValueMatcher.find())
                     {
                        System.out.println("Key: " + keyValueMatcher.group(1));
                        System.out.println("Value: " + keyValueMatcher.group(2));
                        keyValues.put(keyValueMatcher.group(1), keyValueMatcher.group(2));
                     }
                  }
               }
            }
            else if(i == 4)
            {
               returnValue.setName(matcher.group(i));
            }
               
         }
      }
      
      return returnValue;
   }
   
   
}
