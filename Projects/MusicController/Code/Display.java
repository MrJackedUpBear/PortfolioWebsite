import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Display {

	/*
	 * This includes the different security questions allowed in the account creation screen.
	 */
	private static String[] securityQuestions = {"What is your favorite color?",
												"What was your first pet's name?",
												"Where were you born?",
												"What is your home address?",
												"What is your mother's maiden name?"};
	private static String currentUser;

	/*
	 * This is the button for the Music Controller Page
	 */
	static JButton musicControllerButton = new JButton("Continue");
	static JButton backToMainButton = new JButton("Back");

	/*
	 * These three lines inlcude the main frame used, the buttons and panels used in mainPage()
	 */
	private static JFrame frame = new JFrame("Music Controller");
	private static JButton createAccountButton = new JButton("Create account");
	private static JPanel createAccountPanel = new JPanel();
	private static JPanel userPanel = new JPanel();
	private static JLabel userPanelLabel = new JLabel("Users");

	/*
	 * This code creates a static guest and user account
	 */
	public static Account guest = new Account();
	public static Account user = new Account();

	/*
	 * This code creates labels for the account creation screen including the username,
	 * passowrd, and question labels.
	 */
	static JLabel usernameLabel = new JLabel("Enter your username: ");
	static JLabel passwordLabel = new JLabel("Enter your password: ");
	static JLabel questionLabels = new JLabel("Enter your security questions and answers: ");
	
	/*
	 * This code includes the text fields for the account creation screen.
	 */
	public static JTextField usernameText = new JTextField(16);
	public static JPasswordField passwordText = new JPasswordField(16);
	private static JTextField question1Text = new JTextField(16);
	private static JTextField question2Text = new JTextField(16);
	private static JTextField question3Text = new JTextField(16);
	
	/*
	 * This code includes the dropdown boxes for the security question part of the account creation
	 * screen.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static JComboBox questionOptions1 = new JComboBox(securityQuestions);
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static JComboBox questionOptions2 = new JComboBox(securityQuestions);
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static JComboBox questionOptions3 = new JComboBox(securityQuestions);
	
	/*
	 * This creates a submit button and back button for the account creation screen.
	 */
	static JButton submitButton = new JButton("Submit");
	static JButton backButton = new JButton("Back");
	
	/*
	 * This creates the panels for the buttons, text fields, and labels created above
	 */
	static JPanel usernamePanel = new JPanel();
	static JPanel passwordPanel = new JPanel();
	static JPanel securityQuestionsPanel = new JPanel();
	static JPanel submitPanel = new JPanel();
	static JPanel finalPanel = new JPanel(new BorderLayout());
	static JPanel backPanel = new JPanel(new BorderLayout());

	/*
	 * Submit button for the login screen.
	 */
	static JButton loginSubmitButton = new JButton("Submit");
	static JPasswordField loginPasswordField = new JPasswordField();

	/*
	 * Music Sources buttons for user accounts
	 */
	static JButton youtubeButton = new JButton("Youtube Music");
	static JButton spotifyButton = new JButton("Spotify");
	static JButton iHeartButton = new JButton("IHeartRadio");
	static JButton soundcloudButton = new JButton("SoundCloud");

	@SuppressWarnings("unused")
	public static void firstSetup() throws IOException{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(1100, 800);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		createAccountButton.addActionListener(e -> {
			try {
				accountCreationScreen();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		submitButton.addActionListener(e -> {
			try {
				user.createAccount(usernameText, passwordText, questionOptions1, questionOptions2, questionOptions3, question1Text, question2Text, question3Text);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		backButton.addActionListener(e -> {
			try {
				Display.mainPage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		loginSubmitButton.addActionListener(e -> {
			try {
				user.checkPassword(currentUser, loginPasswordField.getPassword());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Could not open");
			}
		});

		youtubeButton.addActionListener(e -> {
			try {
				musicSources.openYoutubeMusic();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		spotifyButton.addActionListener(e -> {
			try {
				musicSources.openSpotifyMusic();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		iHeartButton.addActionListener(e -> {
			try {
				musicSources.openIHeartRadio();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		soundcloudButton.addActionListener(e -> {
			try {
				musicSources.openSoundCloud();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		musicControllerButton.addActionListener(e -> {
			try {
				mainPage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		backToMainButton.addActionListener(e -> musicControllerPage());

		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

		userPanelLabel.setPreferredSize(new Dimension(50, 25));
		userPanelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		userPanelLabel.setFont(new Font("Serif", Font.BOLD, 30));

		createAccountButton.setPreferredSize(new Dimension(200, 25));

		backToMainButton.setPreferredSize(new Dimension(100, 25));
		backToMainButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		try
		{
			loadAccounts();
		}
		catch (IOException e)
		{
			System.out.println("Cannot load acccounts.");
		}

		musicControllerPage();
	}

	public static void musicControllerPage()
	{
		frame.getContentPane().removeAll();
		
		String description = "<html>Welcome to my music controller application. This application will allow" +
		" you, the user, to create an account, log in and play from a variety of music sources." + 
		" This is my third version and may be the last of my Music Controller applications." +
		" I will be adding API calls or call your default browser to play selected music." +
		" I will also allow you to save your logins for each account, and control how the music" +
		" is played throughout your space. I hope you enjoy and let me know if there are any issues.<html>";

		JPanel musicControllerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel();
		JLabel musicControllerTitle = new JLabel("Music Controller Application");
		JLabel musicControllerDescription = new JLabel(description);

		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		musicControllerDescription.setFont(new Font("Serif", Font.PLAIN, 15));
		musicControllerTitle.setFont(new Font("Serif", Font.BOLD, 50));
		musicControllerButton.setFont(new Font("Serif", Font.BOLD, 40));

		musicControllerButton.setPreferredSize(new Dimension(200, 50));

		musicControllerTitle.setHorizontalAlignment(JLabel.CENTER);
		musicControllerButton.setHorizontalAlignment(JButton.CENTER);
		musicControllerTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		musicControllerButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		musicControllerDescription.setHorizontalAlignment(JLabel.CENTER);

		topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		topPanel.add(musicControllerTitle);
		topPanel.add(Box.createVerticalStrut(300));
		topPanel.add(musicControllerButton);

		musicControllerPanel.add(topPanel, BorderLayout.NORTH);
		musicControllerPanel.add(musicControllerDescription, BorderLayout.SOUTH);

		topPanel.setBackground(Color.gray);
		musicControllerPanel.setBackground(Color.gray);

		frame.add(musicControllerPanel, BorderLayout.CENTER);

		frame.revalidate();
		frame.repaint();
	}
	
	public static void mainPage() throws IOException{

        frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		createAccountPanel.add(createAccountButton);
		JPanel backToMainPanel = new JPanel(new BorderLayout());

		backToMainPanel.add(backToMainButton, BorderLayout.WEST);

		frame.add(backToMainPanel, BorderLayout.NORTH);
		frame.add(userPanel, BorderLayout.CENTER);
		frame.add(createAccountPanel, BorderLayout.SOUTH);

		frame.revalidate();
		frame.repaint();
    }

	public static void loadAccounts() throws IOException
	{
		userPanel.removeAll();
		userPanel.add(userPanelLabel);
		userPanel.add(Box.createVerticalStrut(200));

		guestAccount();

		Account.getDifferentAccounts();

		for (String user : Account.differentAccounts)
		{
			createButton(user);
		}
	}

	@SuppressWarnings("unused")
	public static void guestAccount()
	{
		guest.setUsername("Guest");
		String guestUsername = guest.getUsername();

		JButton accountButton = new JButton(guestUsername);
		accountButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		accountButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
		accountButton.addActionListener(e -> mainUserPage(guestUsername));
		accountButton.setPreferredSize(new Dimension(50, 50));
		accountButton.setMaximumSize(new Dimension(300, 50));
		userPanel.add(accountButton, BorderLayout.CENTER);
	}

	@SuppressWarnings("unused")
	public static void createButton(String text)
	{
		JButton accountButton = new JButton(text);
		accountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		accountButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		accountButton.addActionListener(e -> loginScreen(text));
		accountButton.setPreferredSize(new Dimension(50, 50));
		accountButton.setMaximumSize(new Dimension(300,50));
		userPanel.add(accountButton, BorderLayout.CENTER);
	}

    public static void accountCreationScreen() throws IOException{
		if (Account.numberUsers >= 5)
		{
			JOptionPane.showMessageDialog(null, "You can only create 5 accounts");
			return;
		}
	
		frame.getContentPane().removeAll();
	
		frame.setLayout(new BorderLayout());

		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameText);

		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordText);

		securityQuestionsPanel.add(questionLabels);
		securityQuestionsPanel.add(questionOptions1);
		securityQuestionsPanel.add(question1Text);
		securityQuestionsPanel.add(questionOptions2);
		securityQuestionsPanel.add(question2Text);
		securityQuestionsPanel.add(questionOptions3);
		securityQuestionsPanel.add(question3Text);
		
		submitPanel.add(submitButton);

		backPanel.add(backButton, BorderLayout.WEST);

		BoxLayout boxlayout = new BoxLayout(finalPanel, BoxLayout.Y_AXIS);
		finalPanel.setLayout(boxlayout);

		usernamePanel.setMaximumSize(new Dimension(300, 100));
		finalPanel.add(usernamePanel);
		passwordPanel.setMaximumSize(new Dimension(300, 100));
		finalPanel.add(passwordPanel);
		securityQuestionsPanel.setMaximumSize(new Dimension(300, 400));
		finalPanel.add(securityQuestionsPanel);
		submitPanel.setMaximumSize(new Dimension(300, 100));
		finalPanel.add(submitPanel);
		finalPanel.setPreferredSize(new Dimension(300, 700));

		frame.add(finalPanel, BorderLayout.SOUTH);
		frame.add(backPanel, BorderLayout.NORTH);
		frame.revalidate();
		frame.repaint();
    }

	public static void loginScreen(String user)
	{
		frame.getContentPane().removeAll();

		currentUser = user;
		JPanel loginPanel = new JPanel();
		JLabel loginLabel = new JLabel("Enter the password for " + currentUser);

		loginLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		loginPasswordField.setAlignmentX(JPasswordField.CENTER_ALIGNMENT);
		loginSubmitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		loginPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		loginPasswordField.setMaximumSize(new Dimension(400, 25));

		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

		loginPanel.add(loginLabel);
		loginPanel.add(loginPasswordField);
		loginPanel.add(loginSubmitButton);

		backPanel.add(backButton, BorderLayout.WEST);

		frame.add(backPanel, BorderLayout.NORTH);
		frame.add(loginPanel);

		frame.revalidate();
		frame.repaint();
	}

	public static void mainUserPage(String user)
	{
		frame.getContentPane().removeAll();

		JPanel welcomePanel = new JPanel();
		JPanel musicSourcesPanel = new JPanel(new BorderLayout());
		JPanel totalPanel = new JPanel();
		musicSourcesPanel.setLayout(new BoxLayout(musicSourcesPanel, BoxLayout.Y_AXIS));
		totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));

		JLabel welcomeLabel = new JLabel("Welcome " + user + "!");
		welcomePanel.setLayout(new BorderLayout());

		welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
		welcomeLabel.setPreferredSize(new Dimension(50, 100));
		
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		welcomePanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		spotifyButton.setMaximumSize(new Dimension(300, 50));
		youtubeButton.setMaximumSize(new Dimension(300, 50));
		iHeartButton.setMaximumSize(new Dimension(300, 50));
		soundcloudButton.setMaximumSize(new Dimension(300, 50));

		backPanel.add(backButton, BorderLayout.WEST);
		welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
		musicSourcesPanel.add(youtubeButton, BorderLayout.NORTH);
		musicSourcesPanel.add(spotifyButton, BorderLayout.NORTH);
		musicSourcesPanel.add(iHeartButton, BorderLayout.NORTH);
		musicSourcesPanel.add(soundcloudButton, BorderLayout.NORTH);

		musicSourcesPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		musicSourcesPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);

		totalPanel.add(backPanel);
		totalPanel.add(welcomePanel);
		totalPanel.add(musicSourcesPanel);

		frame.add(totalPanel, BorderLayout.NORTH);
		
		frame.revalidate();
		frame.repaint();
	}
}
