// SDK=~/Library/Android/sdk; org.json from maven (json-20231013.jar):
// javac -implicit:none -cp "$SDK/platforms/android-34/android.jar:json.jar" -sourcepath src:build -d /tmp/history src/me/champdev/weblock/History.java test/HistoryTest.java && java -cp /tmp/history:json.jar me.champdev.weblock.HistoryTest
package me.champdev.weblock;

public class HistoryTest {
    static int fails;

    static void check(String name, Object got, Object want) {
        if (!String.valueOf(got).equals(String.valueOf(want))) { fails++; System.out.println("FAIL " + name + ": got " + got + " want " + want); }
    }

    public static void main(String[] a) {
        // a long number is an id, not an episode: no crash (it used to overflow an int), no episode
        check("huge episode id in the address", History.episode("https://x.tv/watch/episode-20261110123456", "Show"), "");
        check("huge episode number in the title", History.episode("https://x.tv/w", "Show Episode 99999999999"), "");
        check("huge season/episode query", History.episode("https://x.tv/w?s=99999999999&e=99999999999", "Show"), "");
        check("normal episode address still read", History.episode("https://x.tv/anime-1535-episode-3", "Show"), "E3");
        check("anime page", History.episodeUrl("https://animex.one/watch/anime-1535-episode-3", 3, 4), "https://animex.one/watch/anime-1535-episode-4");
        check("padding kept, query kept", History.episodeUrl("https://x.com/watch/show-ep-03?x=1", 3, 10), "https://x.com/watch/show-ep-10?x=1");
        check("the last one is swapped", History.episodeUrl("https://x.com/season-3/episode-3", 3, 4), "https://x.com/season-3/episode-4");
        check("not a longer number", History.episodeUrl("https://x.com/episode-31", 3, 4), null);
        check("no episode in the address", History.episodeUrl("https://x.com/movie/123", 3, 4), null);
        check("same episode", History.episodeUrl("https://x.com/episode-3", 3, 3), null);
        check("no address", History.episodeUrl(null, 3, 4), null);
        check("number from the address", History.episodeNumber("https://animex.one/watch/anime-1535-episode-3", ""), 3);
        check("number from the title", History.episodeNumber("https://x.com/watch/abc", "Death Note Episode 7"), 7);
        check("no number", History.episodeNumber("https://x.com/movie", "A Movie"), 0);
        System.out.println(fails == 0 ? "all passed" : fails + " failed");
        if (fails > 0) System.exit(1);
    }
}
