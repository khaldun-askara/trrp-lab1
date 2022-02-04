package com.github.scribejava.apis.examples;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DropboxApp extends JFrame {
    private JPanel contentPane;
    private JList<Object> FolderList;
    private JButton signInDropboxButton;
    private JPasswordField passwordField1;
    private JButton ОКButton;
    private JTextField textField1;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel signInPanel;
    private JPanel pincodePanel;
    private JPanel foldersPanel;
    private JButton выйтиИзАккаунтаButton;
    boolean isauth;
    Dropbox dropbox;

    public DropboxApp() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(signInDropboxButton);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        isauth = Save.IsAuthentificated();
        foldersPanel.setVisible(false);
        signInPanel.setVisible(!isauth);
        pincodePanel.setVisible(isauth);
        createButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);


        signInDropboxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dropbox = new Dropbox();
                    signInPanel.setVisible(false);
                    pincodePanel.setVisible(true);
                    setSize(640,320);
                } catch (Exception ex) {
                    MessageBox(ex.getMessage(), "Error");
                }
            }
        });
        ОКButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isauth)
                {
                    try{
                        Save.WriteSave(Save.encryptToken(dropbox.getrefToken(), new String(passwordField1.getPassword())));
                        pincodePanel.setVisible(false);
                        foldersPanel.setVisible(true);
                        setSize(450,640);
                        GetFolders();
                    }
                    catch (Exception er1){
                        MessageBox(er1.getMessage(), "Error");
                    }
                }
                else
                {
                    try{
                        Save save = Save.ReadSave();
                        String refTok = new String(Save.decryptToken(save.refreshTokenHash, new String(passwordField1.getPassword()), save.iv, save.keySalt));
                        dropbox = new Dropbox(refTok);
                        pincodePanel.setVisible(false);
                        foldersPanel.setVisible(true);
                        setSize(450,640);
                        GetFolders();
                    } catch (Exception er2){
                        MessageBox(er2.getMessage(), "Error111");
                    }
                }
            }
        });

        FolderList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonEnableCheck();
                deleteButton.setEnabled(!FolderList.isSelectionEmpty());
            }
        });
        textField1.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                createButtonEnableCheck();
                updateButtonEnableCheck();
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropbox.createFolder(textField1.getText());
                GetFolders();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropbox.updateFolder(FolderList.getSelectedValue().toString(), textField1.getText());
                GetFolders();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropbox.deleteFolder(FolderList.getSelectedValue().toString());
                GetFolders();
            }
        });

        выйтиИзАккаунтаButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Save.DeleteSave();
                onCancel();
            }
        });
    }
    private void createButtonEnableCheck(){
        boolean flag = textField1.getText() == null || textField1.getText().isEmpty();
        createButton.setEnabled(!flag);
    }
    private void updateButtonEnableCheck(){
        boolean flag = textField1.getText() == null || textField1.getText().isEmpty();
        updateButton.setEnabled(!flag && !FolderList.isSelectionEmpty());
    }

    private void GetFolders()
    {
        List<Object> list = new ArrayList<>(dropbox.getFolders());
        FolderList.setListData(list.toArray());
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void MessageBox(String content, String title){
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        JFrame dialog = new DropboxApp();
        dialog.pack();
        dialog.setVisible(true);
    }
}
