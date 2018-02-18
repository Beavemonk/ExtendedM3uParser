package com.beavermonk.m3uParser;

import java.net.URL;
import java.util.Map;

public class M3UPlaylistEntry 
{
   private URL url;
   private String name;
   private int trackNumber;
   private Map<String, String> tags;
   
   public M3UPlaylistEntry()
   {
      
   }

   public URL getUrl() {
      return url;
   }

   public void setUrl(URL url) {
      this.url = url;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getTrackNumber() {
      return trackNumber;
   }

   public void setTrackNumber(int trackNumber) {
      this.trackNumber = trackNumber;
   }

   public Map<String, String> getTags() {
      return tags;
   }

   public void setTags(Map<String, String> tags) {
      this.tags = tags;
   }

   @Override
   public String toString() {
      return "M3UPlaylistEntry [url=" + url + ", name=" + name + ", trackNumber=" + trackNumber + ", tags=" + tags
            + "]";
   }

}
