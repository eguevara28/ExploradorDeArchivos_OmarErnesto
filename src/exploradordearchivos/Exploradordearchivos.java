/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package exploradordearchivos;

import javax.swing.*;
import javax.swing.tree.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class Exploradordearchivos extends JFrame {

    private JTree fileTree;
    private DefaultMutableTreeNode rootNode;
    private JFileChooser fileChooser;
    private File currentDirectory;

    public Exploradordearchivos() {
        setTitle("Explorador de Archivos, Omar y Ernesto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        rootNode = new DefaultMutableTreeNode("Raiz");
        fileTree = new JTree(rootNode);
        JScrollPane treeScroll = new JScrollPane(fileTree);
        add(treeScroll, BorderLayout.CENTER);

        // Barra de menús
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");

        JMenuItem openItem = new JMenuItem("Abrir");
        openItem.addActionListener(e -> abrirdirectorio());
        fileMenu.add(openItem);

        JMenuItem renameItem = new JMenuItem("Renombrar");
        renameItem.addActionListener(e -> renombrararchivo());
        fileMenu.add(renameItem);

        JMenuItem createFileItem = new JMenuItem("Crear File");
        createFileItem.addActionListener(e -> creararchivo());
        fileMenu.add(createFileItem);

        JMenuItem createFolderItem = new JMenuItem("Crear Folder");
        createFolderItem.addActionListener(e -> crearfolder());
        fileMenu.add(createFolderItem);

        JMenuItem organizeItem = new JMenuItem("Organizar Archivos");
        organizeItem.addActionListener(e -> organizararchivos());
        fileMenu.add(organizeItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void abrirdirectorio() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            currentDirectory = fileChooser.getSelectedFile();
            rootNode.removeAllChildren();
            actualizararbol(currentDirectory);
            ((DefaultTreeModel) fileTree.getModel()).reload();
        }
    }

    private void actualizararbol(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
                rootNode.add(node);
            }
        }
    }

    private void renombrararchivo() {
        TreePath selectedPath = fileTree.getSelectionPath();
        if (selectedPath != null) {
            String oldName = selectedPath.getLastPathComponent().toString();
            String newName = JOptionPane.showInputDialog("Ingrese el nuevo nombre:", oldName);
            if (newName != null && !newName.isEmpty()) {
                File oldFile = new File(currentDirectory, oldName);
                File newFile = new File(currentDirectory, newName);
                if (oldFile.renameTo(newFile)) {
                    JOptionPane.showMessageDialog(this, "Archivo renombrado");
                    ((DefaultMutableTreeNode) selectedPath.getLastPathComponent()).setUserObject(newName);
                    ((DefaultTreeModel) fileTree.getModel()).reload();
                } else {
                    JOptionPane.showMessageDialog(this, "Error renombrando");
                }
            }
        }
    }

    private void creararchivo() {
        String fileName = JOptionPane.showInputDialog("Ingrese el nombre del archivo:");
        if (fileName != null && !fileName.isEmpty()) {
            File newFile = new File(currentDirectory, fileName + ".txt");
            try {
                if (newFile.createNewFile()) {
                    JOptionPane.showMessageDialog(this, "Archivo creado");
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFile.getName());
                    rootNode.add(newNode);
                    ((DefaultTreeModel) fileTree.getModel()).reload();
                } else {
                    JOptionPane.showMessageDialog(this, "Ya existe el archivo");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error creando el archivo");
            }
        }
    }

    private void crearfolder() {
        String folderName = JOptionPane.showInputDialog("Ingrese el nombre del folder:");
        if (folderName != null && !folderName.isEmpty()) {
            File newFolder = new File(currentDirectory, folderName);
            if (newFolder.mkdir()) {
                JOptionPane.showMessageDialog(this, "Folder creado correctamente");
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFolder.getName());
                rootNode.add(newNode);
                ((DefaultTreeModel) fileTree.getModel()).reload();
            } else {
                JOptionPane.showMessageDialog(this, "Error creando el folder");
            }
        }
    }

    private void organizararchivos() {
        File[] files = currentDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String extension = getFileExtension(file);
                    File folder = new File(currentDirectory, extension);
                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    file.renameTo(new File(folder, file.getName()));
                }
            }
            JOptionPane.showMessageDialog(this, "Archivos organizados");
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            return name.substring(dotIndex + 1).toLowerCase();
        }
        return "other";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Exploradordearchivos organizer = new Exploradordearchivos();
            organizer.setVisible(true);
        });
    }
}
