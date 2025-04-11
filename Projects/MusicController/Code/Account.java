import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Account{
	/* 
	public void updateSecurityQuestions();
	public void changePassword();
	public void setPlaybackSettings();
	public void updatePlaylists();
	public void setDefaultPlayback();
	public void setNumSpeakers();
	public void setSavedConfigs();
	public void setDownloadedMusic();
	public void setMusicAccounts();
	public void setSpeakerAreas();
	public void setVolume();
	private boolean checkSecurityQuestions();
	public void getDefaultPlaybackSettings();
	public void getCurrentPlaybackSettings();
	public void getPlaylists();
	public void getNumSpeakers();
	public void getSavedConfigs();
	public void getDownloadedMusic();
	public void getMusicAccounts();
	public void getGuestAccounts();
	public void getSpeakerAreas();
	public float getSpeakerVolume();
	*/
	private String username;
	private char[] password;
	private HashMap<String, String> securityQuestionsAndAnswers = new HashMap<String, String>();
	private String[] question = {"", "", ""};
	private static int fileSize = 0;
	public static int numberUsers;
	public static ArrayList<String> differentAccounts = new ArrayList<>();
	/* 
	private String[] musicAccounts;
	private String[] guestAccount;
	private String[] speakerAreas;
	private float speakerVolume;
	private String[] defaultPlaybackSettings;
	private String[] currentPlaybackSettings;
	private String[] accountPlaylists;
	private int numSpeakers;
	private String[] savedConfigs;
	private String[] downloadedMusic;
	*/
	
	/*
	* This method has the user create a username and password.
	* After creating the username and password, the user must create
	* security questions for their account.
	*/
	
	@SuppressWarnings({ "rawtypes" })
	public void createAccount(JTextField usernameText, JPasswordField passwordText, JComboBox questionOptions1, JComboBox questionOptions2, JComboBox questionOptions3, JTextField question1Text, JTextField question2Text, JTextField question3Text) throws IOException{
		username = usernameText.getText();
		password = passwordText.getPassword();
		securityQuestionsAndAnswers.put((String) questionOptions1.getSelectedItem(), question1Text.getText());
		securityQuestionsAndAnswers.put((String) questionOptions2.getSelectedItem(), question2Text.getText());
		securityQuestionsAndAnswers.put((String) questionOptions3.getSelectedItem(), question3Text.getText());
		question[0] = (String) questionOptions1.getSelectedItem();
		question[1] = (String) questionOptions2.getSelectedItem();
		question[2] = (String) questionOptions3.getSelectedItem();

		if (username.equals("") || Arrays.equals(password, "".toCharArray()) || question1Text.getText().equals("") || question2Text.getText().equals("") || question3Text.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please fill out all boxes.");
			return;
		}
		else if (questionOptions1.getSelectedItem().equals(questionOptions2.getSelectedItem()) || questionOptions1.getSelectedItem().equals(questionOptions3.getSelectedItem()) || questionOptions2.getSelectedItem().equals(questionOptions3.getSelectedItem()))
		{
			JOptionPane.showMessageDialog(null, "Please choose 3 unique security questions.");
			return;
		}

		usernameText.setText("");

		passwordText.setText("");

		questionOptions1.setSelectedItem("What is your favorite color?");
		questionOptions2.setSelectedItem("What is your favorite color?");
		questionOptions3.setSelectedItem("What is your favorite color?");

		question1Text.setText("");
		question2Text.setText("");
		question3Text.setText("");

		//Need to create file and save password, username, and security questions to it.
		File users = new File("users.txt");
		File tmp = new File("tmp.txt");

		boolean check = false;

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(users));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));
			
			for (int i = 0; i < fileSize; i++)
			{
				writer.write(reader.readLine());
				writer.newLine();
			}
			reader.close();
			
			writer.write("{");
			writer.newLine();
			writer.write("\t" + username);
			writer.newLine();
			writer.write("\t");
			writer.write(password);
			writer.newLine();
			
			for (Entry<String, String> entry : securityQuestionsAndAnswers.entrySet())
			{
				writer.write("\t" + entry.getKey() + ": " + entry.getValue());
				writer.newLine();
			}
			writer.write("}");

			writer.close();

			boolean delete = users.delete();
			boolean rename = tmp.renameTo(users);

			if (!delete || !rename)
			{
				JOptionPane.showMessageDialog(null, "Could not edit file.");
			}
		}
		catch(IOException e)
		{
			
			check = true;
		}

		if (check)
		{
			try
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(users));
				writer.write("{");
				writer.newLine();
				writer.write("\t" + username);
				writer.newLine();
				writer.write("\t");
				writer.write(password);
				writer.newLine();
				
				for (Entry<String, String> entry : securityQuestionsAndAnswers.entrySet())
				{
					writer.write("\t" + entry.getKey() + ": " + entry.getValue());
					writer.newLine();
				}
				writer.write("}");

				writer.close();

			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "Something went wrong.");
			}
		}

		JOptionPane.showMessageDialog(null, "You have successfully created an account with the username " + username);

		securityQuestionsAndAnswers.clear();

		Display.loadAccounts();

		Display.mainPage();
	}
	
	public static void getDifferentAccounts() throws IOException
	{
		differentAccounts.clear();

		fileSize = 0;

		File users = new File("users.txt");

		BufferedReader temp = new BufferedReader(new FileReader(users));
		while(temp.readLine() != null)
		{
			fileSize ++;
		}

		temp.close();

		numberUsers = fileSize / 7;

		BufferedReader reader = new BufferedReader(new FileReader(users));

		for (int i = 0; i < numberUsers; i++)
		{
			reader.readLine();
			differentAccounts.add(reader.readLine());
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
		}

		reader.close();
	}

	public void checkPassword(String user, char[] pass) throws IOException
	{
		username = user;
		getPassword();

		boolean isEqual = false;

		if (new String(password).length() - 1 != new String(pass).length())
		{
			isEqual = false;
		}
		else
		{
			for (int i = 1; i < new String(password).length(); i++)
			{
				if (new String(password).charAt(i) != new String(pass).charAt(i - 1))
				{
					isEqual = false;
					break;
				}
				else
				{
					isEqual = true;
				}
			}
		}

		if (isEqual)
		{
			Display.loginPasswordField.setText("");
			password = "******".toCharArray();
			Display.mainUserPage(username);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Password incorrect. Try again.");
			Display.loginPasswordField.setText("");
			Display.loginScreen(username);
		}
	}

	private static void getPassword() throws IOException
	{
		File users = new File("users.txt");

		BufferedReader reader = new BufferedReader(new FileReader(users));

		for (int i = 0; i < fileSize - 1; i++)
		{
			if (reader.readLine().equals(Display.user.username))
			{
				Display.user.password = reader.readLine().toCharArray();
				break;
			}
		}

		reader.close();
	}

	public void setUsername(String user)
	{
		username = user;
	}

	public String getUsername()
	{
		return username;
	}
}