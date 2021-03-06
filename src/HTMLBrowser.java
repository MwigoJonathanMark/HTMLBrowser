
import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MWIGO JON MARK
 */
public class HTMLBrowser extends JFrame
{
    private static final long serialVersionUID = 1L;
    JLabel urlLabel = new JLabel("URL:");
    JTextField urlTextField = new JTextField(40);
    JButton urlGoButton = new JButton("Go");
    JEditorPane editorPane = new JEditorPane();
    JLabel statusLabel = new JLabel("Ready");
    
    public HTMLBrowser(String title)
    {
        super();
        initFrame();
    }
    
    // Initialize the JFrame and add components to it
    private void initFrame()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container contentPane = this.getContentPane();
        
        Box urlBox = this.getURLBox();
        Box editorPaneBox = this.getEditPaneBox();
        
        contentPane.add(urlBox, BorderLayout.NORTH);
        contentPane.add(editorPaneBox, BorderLayout.CENTER);
        contentPane.add(statusLabel, BorderLayout.SOUTH);
    }
    
    private Box getURLBox()
    {
        // URL Box consists of a JLabel, a JTextField and a JButton
        Box urlBox = Box.createHorizontalBox();
        urlBox.add(urlLabel);
        urlBox.add(urlTextField);
        urlBox.add(urlGoButton);
        
        // Add an action listener to urlTextField, so when the user enters a url
        // and presses the enter key, the appplication navigates to the new URL.
        urlTextField.addActionListener(e ->
        {
            String urlString = urlTextField.getText();
            go(urlString);
        });
        
        // Add an action listener to the Go button
        urlGoButton.addActionListener(e -> go());
        return urlBox;
    }
    
    private Box getEditPaneBox()
    {
        // To display HTML, you must make the editor pane non-editable.
        // Otherwise, you will see an editable HTML page that doesnot look nice.
        editorPane.setEditable(false);

        // URL Box consists of a JLabel, a JTextField and a JButton
        Box editorBox = Box.createHorizontalBox();

        // Add a JEditorPane inside a JScrollPane to provide scolling
        editorBox.add(new JScrollPane(editorPane));

        // Add a hyperlink listener to the editor pane, so that it
        // navigates to a new page, when the user clicks a hyperlink
        editorPane.addHyperlinkListener((HyperlinkEvent event) ->
        {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
            {
            go(event.getURL());
            }
            else if (event.getEventType() == HyperlinkEvent.EventType.ENTERED)
            {
                statusLabel.setText("Please click this link to visit the page");
            }
            else if (event.getEventType() == HyperlinkEvent.EventType.EXITED)
            {
                statusLabel.setText("Ready");
            }
        });

        // Add a property change listener, so we can update
        // the URL text field with url of the new page
        editorPane.addPropertyChangeListener((PropertyChangeEvent e) ->
        {
            String propertyName = e.getPropertyName();

            if (propertyName.equalsIgnoreCase("page"))
            {
                URL url = editorPane.getPage();
                urlTextField.setText(url.toExternalForm());
            }
        });

        return editorBox;
    }
    
    // Navigates to the url entered in the URL JTextField
    public void go()
    {
        try
        {
            URL url = new URL(urlTextField.getText());
            this.go(url);
        }
        catch (MalformedURLException e)
        {
            setStatus(e.getMessage());
        }
    }
    // Navigates to the specified URL
    public void go(URL url)
    {
        try
        {
            editorPane.setPage(url);
            
            urlTextField.setText(url.toExternalForm());
            
            setStatus("Ready");
        }
        catch (IOException e)
        {
            setStatus(e.getMessage());
            //editorPane.setText("Ooh, Snap!!!\nSomething Happened.");
        }
    }
    
    // Navigates to the specified URL specified as a string
    public void go(String urlString)
    {
        try
        {
            URL url = new URL(urlString);
            
            go(url);
        }
        catch (IOException e)
        {
            setStatus(e.getMessage());
        }
    }
    
    private void setStatus(String status)
    {
        statusLabel.setText(status);
    }
    
    public static void main(String[] args)
    {
        HTMLBrowser browser = new HTMLBrowser("HTML Browser");
        
        browser.setSize(700, 500);
        
        browser.setVisible(true);
        
        // Let us visit yahoo.com
        browser.go("http://www.yahoo.com");
    }
}
