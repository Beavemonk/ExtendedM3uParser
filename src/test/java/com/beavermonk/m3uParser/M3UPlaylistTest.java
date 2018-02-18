package com.beavermonk.m3uParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

public class M3UPlaylistTest 
{
   @Test
   public void testSingleLine() throws IOException
   {
      String contents = new String(Files.readAllBytes(Paths.get(M3UPlaylistTest.class.getResource("withTags.m3u").getFile())));
 
      M3UPlaylistEntry entry = M3UPlaylist.parseLine(contents);
      assertEquals("The Star", entry.getName());
      assertEquals("Family & Kids", entry.getTags().get("group-title"));
   }

   @Test
   public void testSimpleLine() throws IOException
   {
      String contents = new String(Files.readAllBytes(Paths.get(M3UPlaylistTest.class.getResource("noTags.m3u").getFile())));
 
      M3UPlaylistEntry entry = M3UPlaylist.parseLine(contents);
      assertEquals("The Jerk", entry.getName());
   }

   @Test
   public void testAliceInChains() throws Exception
   {
      M3UPlaylist list = M3UPlaylist.parse(M3UPlaylistTest.class.getResource("aliceChains.m3u"));
      
      assertNotNull(list);
      assertEquals(7, list.getEntries().size());
   }
   
}
