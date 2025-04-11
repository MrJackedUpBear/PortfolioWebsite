import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

public class musicSources {
    public static void openYoutubeMusic() throws URISyntaxException
    {
        try
        {
            Desktop.getDesktop().browse(new URI("https://music.youtube.com/"));
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Did not work.");
        }
    }

    public static void openSpotifyMusic() throws URISyntaxException
    {
        try
        {
            Desktop.getDesktop().browse(new URI("https://open.spotify.com/"));
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Did not work.");
        }
    }

    public static void openIHeartRadio() throws URISyntaxException
    {
        try
        {
            Desktop.getDesktop().browse(new URI("https://www.iheart.com/"));
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Did not work.");
        }
    }

    public static void openSoundCloud() throws URISyntaxException
    {
        try
        {
            Desktop.getDesktop().browse(new URI("https://soundcloud.com/discover"));
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Did not work.");
        }
    }
}
