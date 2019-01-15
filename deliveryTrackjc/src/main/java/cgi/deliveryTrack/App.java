package cgi.deliveryTrack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import cgi.deliveryTrack.bean.Context;
import cgi.deliveryTrack.enumeration.ApiEnum;
import cgi.deliveryTrack.service.Service;
import cgi.deliveryTrack.utils.FileDrop;

/**
 * Réalisé par Julien GRIFFAULT
 * CGI - France
 */

public class App {
	private static String Livrer;
	private static String Valider;
	private static String Invalider ;
	private static String Archiver ;
	
	
		
    public static void main( String[] args ){	
    	
    	Livrer = "C:\\test\\Livrer\\";
    	Valider = "C:\\test\\Valider\\";
    	Invalider = "C:\\test\\Invalider\\";
    	Archiver = "C:\\test\\Archiver\\";

    	// Initialisation des services
    	final Service service = new Service();
    	
    	// Plan de classement	
    	// Initialisation de l'interface graphique Swing et de ses composants.
        final JFrame frame = new JFrame( "DeliveryTrack" );
        
        JPanel dndContainer = new JPanel();
        dndContainer.setLayout(new GridLayout(1,4));
        
        final JTextArea dndLivraison = new JTextArea();
        dndLivraison.setBorder(BorderFactory.createLineBorder(Color.black));
        JTextArea dndValidation = new JTextArea();
        dndValidation.setBorder(BorderFactory.createLineBorder(Color.black));
        JTextArea dndInvalidation = new JTextArea();
        dndInvalidation.setBorder(BorderFactory.createLineBorder(Color.black));
        JTextArea dndArchivage = new JTextArea();
        dndArchivage.setBorder(BorderFactory.createLineBorder(Color.black));
        
        // Scroll down automatically
        DefaultCaret caret = (DefaultCaret)dndLivraison.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        DefaultCaret caret2 = (DefaultCaret)dndValidation.getCaret();
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        DefaultCaret caret3 = (DefaultCaret)dndInvalidation.getCaret();
        caret3.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        DefaultCaret caret4 = (DefaultCaret)dndArchivage.getCaret();
        caret4.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        // Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("Gestion du plan de classement");
        mb.add(m1);
        JMenuItem m11 = new JMenuItem("Changer l'adresse de livraison");
        JMenuItem m22 = new JMenuItem("Changer l'adresse de validation");
        JMenuItem m33 = new JMenuItem("Changer l'adresse de invalidation");
        JMenuItem m44 = new JMenuItem("Changer l'adresse d'archivage");
        JMenuItem m55 = new JMenuItem("Changer l'adresse des logs");
        m1.add(m11);
        m1.add(m22);
        m1.add(m33);
        m1.add(m44);
        m1.add(m55);
        
        // Création du Bouton RollBack
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton cancel = new JButton("Annuler la dernière action");
        panel.add(cancel);
        
        dndContainer.add(dndLivraison);
        dndContainer.add(dndValidation);
        dndContainer.add(dndInvalidation);
        dndContainer.add(dndArchivage);
        
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, dndContainer);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        
        dndLivraison.append("Livrer un document \n-------------------\n"
        		+ "Déposer ici un fichier à livrer.(Copie)\n"
        		+ "Celui ci doit être NON versionné \non versionné au format vX.X uniquement.\n\n"
        		+ "Incrémentation du versionning automatique.\n"
        		+ "Exemple : 2 versements du fichier exemple.doc = exemple_v1.0.doc \net exemple_v1.1.doc."
        		+ "\n-------------------\n");
        dndValidation.append("Valider un document \n-------------------\n"
        		+ "Déposer ici un fichier à valider.\n(Déplacement)"
        		+ "\n-------------------\n");
        dndInvalidation.append("Invalider un document \n-------------------\n"
        		+ "Déposer ici un fichier à invalider.\n(Déplacement)"
        		+ "\n-------------------\n");
        dndArchivage.append("Archiver un document \n-------------------\n"
        		+ "Déposer ici un fichier à archiver.\n(Déplacement)"
        		+ "\n-------------------\n");
        
        dndLivraison.setEditable(false);
        dndValidation.setEditable(false);
        dndInvalidation.setEditable(false);
        dndArchivage.setEditable(false);
        
        dndLivraison.setFont(dndLivraison.getFont().deriveFont(15f));
        dndValidation.setFont(dndValidation.getFont().deriveFont(15f));
        dndInvalidation.setFont(dndInvalidation.getFont().deriveFont(15f));
        dndArchivage.setFont(dndArchivage.getFont().deriveFont(15f));
        
        Color gris = new Color(207,207,207);
        Color vert = new Color(0,227,151);
        Color rouge = new Color(225,151,151);
        Color bleu = new Color(121,121,255);
        
        dndLivraison.setBackground(gris);
        dndValidation.setBackground(vert);
        dndInvalidation.setBackground(rouge);
        dndArchivage.setBackground(bleu);
        
        dndLivraison.setLineWrap(true);
        dndValidation.setLineWrap(true);
        dndInvalidation.setLineWrap(true);
        dndArchivage.setLineWrap(true);
        
        // LIVRAISON BOX
        new FileDrop( System.out, dndLivraison, /*dragBorder,*/ new FileDrop.Listener() {   
        	public void filesDropped( File[] files ) {   
	        	for( int i = 0; i < files.length; i++ ) {   
	        		File file = files[i];
	        		Context context = new Context(ApiEnum.LIVRAISON, file);
	        		dndLivraison.append(service.fileTransfert(context));
	        		if(file.isFile()) {
	        			//////////////fileNewName=
	        		}

            	}   // end for: through each dropped file
        	}   // end filesDropped
        }); // end FileDrop.Listener
        
        // VALIDATION BOX
        new FileDrop( System.out, dndValidation, /*dragBorder,*/ new FileDrop.Listener() {   
        	public void filesDropped( File[] files ) {   
        		for( int i = 0; i < files.length; i++ ) {

                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener
        
        // INVALIDATION BOX
        new FileDrop( System.out, dndInvalidation, /*dragBorder,*/ new FileDrop.Listener() {   
        	public void filesDropped( File[] files ) {   
        		for( int i = 0; i < files.length; i++ ) {   

                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener  
        
        // ARCHIVAGE BOX
        new FileDrop( System.out, dndArchivage, /*dragBorder,*/ new FileDrop.Listener() {   
        	public void filesDropped( File[] files ) {   
        		for( int i = 0; i < files.length; i++ ) {   

                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener
        
        m11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	String livrer = JOptionPane.showInputDialog(frame,
                        "Où dois-je livrer ?", null);
            }
        });
        
        m22.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		String valider = JOptionPane.showInputDialog(frame,
        				"Où dois-je valider ?", null);
        	}
        });
        
        m33.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		String invalider = JOptionPane.showInputDialog(frame,
        				"Où dois-je invalider ?", null);
        	}
        });
        
        m44.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		String archiver = JOptionPane.showInputDialog(frame,
        				"Où dois-je archiver ?", null);
        	}
        });
        
        m55.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		String logs = JOptionPane.showInputDialog(frame,
        				"Où dois-je stocker les logs ?", null);
        	}
        });
        
    
        //ajouter l'action du listener c a dire cancel
        cancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {

        	}
        });

        frame.setSize(1200,400);
//        frame.setBounds( 100, 100, 100, 100 );
        frame.setDefaultCloseOperation( frame.EXIT_ON_CLOSE );
        frame.setVisible(true);
    }   // end main
}
