package RandomGenerator2.Tools;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static RandomGenerator2.RT2.runFile;
import static RandomGenerator2.RT2.saikidou;

public class EditWmx {
    public static JFrame frame = new JFrame("w.mx 解密与编辑工具");
    private static final int KEY = 5275;
    private static final byte[] KEY_BYTES = {
            (byte) (KEY & 0xFF),
            (byte) ((KEY >> 8) & 0xFF),
            (byte) ((KEY >> 16) & 0xFF),
            (byte) ((KEY >> 24) & 0xFF)
    };
    private static final File FILE = new File("resources/w.mx");

    //main
    public static void wmxkidou() {
        SwingUtilities.invokeLater(EditWmx::createAndShowGUI);
    }


    private static void createAndShowGUI() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton reloadButton = new JButton("重新加载");
        JButton saveButton = new JButton("保存并加密");
        bottomPanel.add(reloadButton);
        bottomPanel.add(saveButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        loadAndDecrypt(textArea);

        reloadButton.addActionListener(e -> loadAndDecrypt(textArea));
        saveButton.addActionListener(e -> saveAndEncrypt(textArea.getText(), frame));

        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    private static void loadAndDecrypt(JTextArea textArea) {
        if (!FILE.exists()) {
            JOptionPane.showMessageDialog(null, "file not found", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            byte[] encryptedBytes = Files.readAllBytes(FILE.toPath());
            byte[] decryptedBytes = xorCrypt(encryptedBytes);
            String content = new String(decryptedBytes, StandardCharsets.UTF_8);
            textArea.setText(content);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "读取失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static void saveAndEncrypt(String content, JFrame frame) {
        try {
            if (FILE.getParentFile() != null && !FILE.getParentFile().exists()) {
                FILE.getParentFile().mkdirs();
            }

            byte[] plainBytes = content.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = xorCrypt(plainBytes);
            Files.write(FILE.toPath(), encryptedBytes);

            JOptionPane.showMessageDialog(frame, "保存并加密成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "保存失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static byte[] xorCrypt(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ KEY_BYTES[i % KEY_BYTES.length]);
        }
        return result;
    }
}