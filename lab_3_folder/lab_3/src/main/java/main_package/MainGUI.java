package main_package;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.tree.DefaultTreeCellRenderer;

import Objects.Reactor;
import masters.ReactorsHolder;
import importers.*;

public class MainGUI {
    private ReactorsHolder reactorHolder = new ReactorsHolder();

    public static void main(String[] args) {
        showFrame();
    }

    public static void showFrame() {
        MainGUI mainFrame = new MainGUI();
        JFrame frame = new JFrame();
        frame.setTitle("Reactors Info");
        JLabel label = new JLabel("Выберите файл:");
        label.setForeground(Color.WHITE);
        JButton chooseButton = new JButton("Импортировать");
//        chooseButton.setBorder(BorderFactory.createLineBorder(Color.black));
//        chooseButton.setMargin(new Insets(30, 40, 0, 40));

        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON, XML, YAML Files", "json", "xml", "yaml");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        mainFrame.importFile(selectedFile);
                        mainFrame.displayReactorTree();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        Component verticalStrut1 = Box.createVerticalStrut(20);
        Component verticalStrut2 = Box.createVerticalStrut(20);
        panel.add(verticalStrut1);
        panel.add(label);
        panel.add(verticalStrut2);
        panel.add(chooseButton);
        frame.add(panel);
        frame.setSize(300, 150);
        panel.setOpaque(true);
        panel.setBackground(Color.DARK_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void importFile(File file) throws IOException {
        JsonImporter importerChain = new JsonImporter();
        XmlImporter xmlImporter = new XmlImporter();
        YamlImporter yamlImporter = new YamlImporter();
        importerChain.setNext(xmlImporter);
        xmlImporter.setNext(yamlImporter);
        importerChain.importFile(file, reactorHolder);
    }

    private void displayReactorTree() {
        JFrame treeFrame = new JFrame("Reactor Tree");
        
        treeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Reactor Types");
        for (String type : reactorHolder.getReactorMap().keySet()) {
            DefaultMutableTreeNode reactorNode = new DefaultMutableTreeNode(type);
            root.add(reactorNode);
        }

        JTree tree = new JTree(root);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.getUserObject() instanceof String) {
                    String reactorName = (String) selectedNode.getUserObject();
                    Reactor reactor = reactorHolder.getReactorMap().get(reactorName);
                    if (reactor != null) {
                        showReactorInfo(reactor);
                    }
                }
            }
            
        });

        JScrollPane scrollPane = new JScrollPane(tree);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        tree.setBackground(Color.DARK_GRAY);
        renderer.setTextSelectionColor(Color.WHITE);
        renderer.setTextNonSelectionColor(Color.WHITE);
        renderer.setBackgroundNonSelectionColor(Color.DARK_GRAY);
        renderer.setBackgroundSelectionColor(Color.ORANGE);
        treeFrame.add(scrollPane, BorderLayout.CENTER);
        treeFrame.pack();
        treeFrame.setLocationRelativeTo(null);
        treeFrame.setVisible(true);
    }

    private void showReactorInfo(Reactor reactor) {
        JFrame infoFrame = new JFrame("Reactor Information");
        
        JTextArea infoTextArea = new JTextArea(reactor.toOutString());
        infoTextArea.setBackground(Color.DARK_GRAY);        
        infoTextArea.setForeground(Color.WHITE);
        infoTextArea.setEditable(false);
        infoFrame.add(infoTextArea);
        infoFrame.pack();
        infoFrame.setLocationRelativeTo(null);
        infoFrame.setVisible(true);
    }
}
