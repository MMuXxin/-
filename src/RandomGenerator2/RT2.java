package RandomGenerator2;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.PlainDocument;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static RandomGenerator2.Tools.EditWmx.frame;
import static RandomGenerator2.Tools.EditWmx.wmxkidou;
import static RandomGenerator2.Tools.SimpleWeatherApp.getWeatherMapss;

public class RT2 {
    public static final ArrayList<Integer> baseList;
    public static int tempKEY;
    public static final int KEY = 5275; //核心密码，写死的
    public static String[] zh;
    public static String[] ja;
    public static final String sserial;
    public static String initStr;
    public static int currentLang;
    public static String zhLog;
    public static String jpLog;
    public static int resultAreaR;
    public static int resultAreaG;
    public static int resultAreaB;
    public static int resultAreaBgR;
    public static int resultAreaBgG;
    public static int resultAreaBgB;
    public static int scrollBorderR;
    public static int scrollBorderG;
    public static int scrollBorderB;
    public static File backgroundFile;
    public static float transparency;
    public static Properties p;
    public static ParseWmx parseWmx;
    public static JButton generateButton;
    public static JButton settingsButton;
    public static PartialMaskFilter maskFilter;
    public static String settingIconPath;
    public static String settingLightIconPath;
    public static MenuItem exitItem;
    public static MenuItem subscribeItem;
    public static MenuItem menunoItem;
    public static MenuItem saikidouItem;
    public static JLabel langPanelLabel;
    public static JButton langBtn;
    public static JLabel stepPanelLabel;
    public static JLabel removeHistoryLabel;
    public static JButton removeHistoryBtn;
    public static JLabel mennsekiLabel;
    public static JLabel linkLabel;
    public static JLabel authorLink;
    public static JLabel updateLogLabel;
    public static JLabel updateLogLink;
    public static JDialog logDialog;
    public static JTextArea taLog;
    public static JLabel searchLabel;
    public static JButton chooseBackgroundBtn;
    public static JLabel chooseBackgroundLabel;
    public static BackgroundPanel backgroundPanel;
    public static JFrame mainFrame;
    public static JLabel label;
    public static final Font smallerFont = getF(11, false);
    public static final Font baseFont = getF(16, false);
    public static final Font generateButtonFont = getF(24, true);
    public static final Font generateButtonPlainFont = getF(24, false);
    public static final Font logButtonFont = getF(14, true);
    public static final Font propTextAreaFont = getF(15, false);
    public static final Font badappleFont;
    public static JTextArea resultArea;
    public static JTextField countField;
    public static JDialog menuDialog;
    public static int colorInternal;
    public static File runFile = new File("运行.bat");
    public static int wwwerCOUNT;       //"/gb"指令需要
    public static int typetextinterval;
    public static int resultAreaDefaultFontSize;
    public static Font wocaonimaFont;
    public static volatile boolean isStopRequested = false;
    public static final String HISTORY_FILE = "out.wmx";
    public static LinkedList<Integer> recentHistory = new LinkedList<>();
    public static int stepNum = 0;
    public static String iconnoPath;
    private static java.net.ServerSocket serverSocketLock;
    private static Timer typingTimer;
    private static final File file = new File("resources/w.mx");
    private static final byte[] KEY_BYTES = {(byte) (KEY & 0xFF), (byte) ((KEY >> 8) & 0xFF), (byte) ((KEY >> 16) & 0xFF), (byte) ((KEY >> 24) & 0xFF)};
    private static final int modifierStep = 5;  //淘汰的作弊菜单的栏目数量
    private static int generateRound = 0;   //指定次数强制换成特定学号，记录次数
    public static File pf;  //properties文件
    public static String pPath; //properties路径
    private static String trayIconPath;
    //天气组件相关的
    private static JLabel simpleWeatherDisplayLabel;
    private static JDialog weatherDisplayDialog;
    private static JTextArea weatherDisplayTextArea;
    private static String currentCityName;
    private static JLabel todayLabel;
    private static JLabel tomorrowLabel;
    private static JLabel afterTomorrowLabel;

    public static final Logger l = Logger.getLogger(RT2.class.getName());
    static {
        FileHandler fh = null;
        try {
            fh = new FileHandler("log.wwwer",true);
        } catch (IOException e) {System.err.println("读取log.wwwer失败");}
        fh.setFormatter(new SimpleFormatter());
        l.addHandler(fh);


        pPath = "resources/setting.properties";
        pf = new File(pPath);
        p = new Properties();
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(pPath), StandardCharsets.UTF_8)) {
            p.load(isr);
        } catch (IOException e) {
            l.severe("配置文件加载失败!"+e.getMessage());
        }
        sserial = p.getProperty("serial", "1.0.0");
        String baseStr = p.getProperty("baseList", "1,2,3,4,5,6,8,9," +
                "12,15,17,18,19," +
                "20,21,22,23,25,26,27,28,29," +
                "30,31,32,33,34,35,37,38," +
                "40,41,42,43,44,45,46,47,48,49," +
                "50,51,52,65");     //默认采用作者班级的学号，请修改prop里的baseStr
        String[] parts = baseStr.split(",");
        ArrayList<Integer> base = new ArrayList<>();
        for (String part : parts) {
            base.add(Integer.parseInt(part.trim()));
        }
        baseList = base;
        backgroundFile = new File(p.getProperty("backgroundImage", null));
        transparency = Float.parseFloat(p.getProperty("transparency", "0.8"));
        currentLang = Integer.parseInt(p.getProperty("defaultLanguage", "0"));
        zhLog = p.getProperty("zhLog", null);
        jpLog = p.getProperty("jpLog", null);
        initStr = p.getProperty("initStr", null);
        resultAreaR = Integer.parseInt(p.getProperty("color.resultArea.r", "160"));
        resultAreaG = Integer.parseInt(p.getProperty("color.resultArea.g", "80"));
        resultAreaB = Integer.parseInt(p.getProperty("color.resultArea.b", "30"));
        resultAreaBgR = Integer.parseInt(p.getProperty("color.resultAreaBg.r", "255"));
        resultAreaBgG = Integer.parseInt(p.getProperty("color.resultAreaBg.g", "248"));
        resultAreaBgB = Integer.parseInt(p.getProperty("color.resultAreaBg.b", "245"));
        scrollBorderR = Integer.parseInt(p.getProperty("color.scrollBorder.r", "180"));
        scrollBorderG = Integer.parseInt(p.getProperty("color.scrollBorder.g", "200"));
        scrollBorderB = Integer.parseInt(p.getProperty("color.scrollBorder.b", "220"));
        String sserial = p.getProperty("serial", "1.1.2");
        String zhLog = "修复无法重启 \n\n 移除AI功能 \n\n 优化托盘 \n\n 添加终端操作 \n\n 优化随机数生成逻辑 \n\n 添加特效 \n\n 修复无法生成15号";
        String jpLog = "* 再起動できない問題を修正しました ~ \n\n * AI機能を削除しました ~ \n\n * トレイを最適化しました ~ \n\n * ターミナル操作を追加しました ~ \n\n * 乱数生成ロジックを最適化しました ~ \n\n * エフェクトを追加しました ~ \n\n * 15番が生成できない不具合を修正しました ~";

        int zhjpSize = 34;  //添加新文本；    zhjpSize++;
        zh = new String[zhjpSize];
        zh[0] = p.getProperty("zh.title", "G241随机学号生成器") + sserial;
        zh[1] = p.getProperty("zh.count", "抽取数量");
        zh[2] = p.getProperty("zh.start", "开始生成");
        zh[3] = p.getProperty("zh.langJa", "日语");
        zh[4] = p.getProperty("zh.langZh", "中文");
        zh[5] = p.getProperty("zh.error", "错误");
        zh[6] = p.getProperty("zh.inputInvalid", "请输入有效的正整数");
        zh[7] = p.getProperty("zh.configMissing", "配置文件 w.mx 不存在");
        zh[8] = p.getProperty("zh.exceedLimit", "抽取数量超过可用学号数");
        zh[9] = p.getProperty("zh.errorOccurred", "发生错误: ");
        zh[10] = p.getProperty("zh.muxin", "Muxin");
        zh[11] = p.getProperty("zh.saveConfigFail", "保存配置文件失败: ");
        zh[12] = p.getProperty("zh.loadConfigFail", "加载配置文件失败: ");
        zh[13] = p.getProperty("zh.settings", "设置");
        zh[14] = p.getProperty("zh.language", "语言:");
        zh[15] = p.getProperty("zh.stepNoRepeat", "不重复步数:");
        zh[16] = p.getProperty("zh.disclaimer", "免责声明");
        zh[17] = p.getProperty("zh.deleteHistory", "删除历史:");
        zh[18] = p.getProperty("zh.deleteBtn", "删除");
        zh[19] = p.getProperty("zh.followAuthor", "关注作者");
        zh[20] = p.getProperty("zh.changelog", "更新日志");
        zh[21] = zhLog;
        zh[22] = p.getProperty("zh.probQuery", "概率查询:");
        zh[23] = p.getProperty("zh.backgroundchooser", "选择图片");
        zh[24] = p.getProperty("zh.backgroundLabel", "切换背景");
        zh[25] = p.getProperty("zh.menuitemExit", "退出(^^)");
        zh[26] = p.getProperty("zh.menuItem", "打开菜单");
        zh[27] = p.getProperty("zh.saikidou", "重新启动");
        zh[28] = p.getProperty("zh.weatherTitle", "天气");
        zh[29] = p.getProperty("zh.searchWeatherLabel", "天气查询");
        zh[30] = p.getProperty("zh.cityName", "沈阳");
        zh[31] = p.getProperty("zh.today", "今天");
        zh[32] = p.getProperty("zh.tomorrow", "明天");
        zh[33] = p.getProperty("zh.dayaftertomorrow", "后天");
        ja = new String[zhjpSize];
        ja[0] = p.getProperty("jp.title", "G241学籍番号生成器") + sserial;
        ja[1] = p.getProperty("jp.count", "抽出数");
        ja[2] = p.getProperty("jp.start", "スタート!!!");
        ja[3] = p.getProperty("jp.langJa", "中文");
        ja[4] = p.getProperty("jp.langZh", "日本語");
        ja[5] = p.getProperty("jp.error", "エラー");
        ja[6] = p.getProperty("jp.inputInvalid", "有効な正の整数を入力してください");
        ja[7] = p.getProperty("jp.configMissing", "设定文件 w.mx が存在しません");
        ja[8] = p.getProperty("jp.exceedLimit", "抽出数量が利用可能な学籍番号数を超えています");
        ja[9] = p.getProperty("jp.errorOccurred", "エラーが発生しました: ");
        ja[10] = p.getProperty("jp.muxin", "おうもきん");
        ja[11] = p.getProperty("jp.saveConfigFail", "设定ファイルの保存に失敗しました: ");
        ja[12] = p.getProperty("jp.loadConfigFail", "设定ファイルの読み込みに失敗しました: ");
        ja[13] = p.getProperty("jp.settings", "设定");
        ja[14] = p.getProperty("jp.language", "言語:");
        ja[15] = p.getProperty("jp.stepNoRepeat", "重複不可ステップ数:");
        ja[16] = p.getProperty("jp.disclaimer", "免责事项");
        ja[17] = p.getProperty("jp.deleteHistory", "履歴削除:");
        ja[18] = p.getProperty("jp.deleteBtn", "削除します");
        ja[19] = p.getProperty("jp.followAuthor", "フォローしてください");
        ja[20] = p.getProperty("jp.changelog", "ログ");
        ja[21] = jpLog;
        ja[22] = p.getProperty("jp.probQuery", "確率クエリ:");
        ja[23] = p.getProperty("jp.backgroundchooser", "ピクチャーを選択");
        ja[24] = p.getProperty("jp.backgroundLabel", "背景スイッチ");
        ja[25] = p.getProperty("jp.menuitemExit", "さようなら(^^)");
        ja[26] = p.getProperty("jp.menuItem", "メニューオープン");
        ja[27] = p.getProperty("jp.saikidou", "リスタート");
        ja[28] = p.getProperty("jp.weatherTitle", "天気");
        ja[29] = p.getProperty("jp.searchWeatherLabel", "天気チェック");
        ja[30] = p.getProperty("jp.cityName", "瀋陽");
        ja[31] = p.getProperty("jp.today", "今日");
        ja[32] = p.getProperty("jp.tomorrow", "明日");
        ja[33] = p.getProperty("jp.dayaftertomorrow", "明後日");
        colorInternal = Integer.parseInt(p.getProperty("colorInternal"));

        settingIconPath = p.getProperty("settingPath", "resources/setting.png");
        settingLightIconPath = p.getProperty("setting_lightPath", "resources/setting_light.png");

        typetextinterval = Integer.parseInt(p.getProperty("typetextinterval", "40"));
        resultAreaDefaultFontSize = Integer.parseInt(p.getProperty("resultAreaDefaultFontSize", "32"));

        wocaonimaFont = getF(resultAreaDefaultFontSize, true);
        badappleFont = new Font(Font.MONOSPACED, Font.PLAIN, Integer.parseInt(p.getProperty("badapplefontsize", "8")));

        currentCityName = p.getProperty("cityName");
        trayIconPath = p.getProperty("trayIconPath");
    }
    //这里是历史遗留问题，也考虑到作者班级的需要，仅支持简体中文和日语两种语言，有其他语言需要可以考虑改properties
    public static String t(int index) {
        return currentLang == 1 ? zh[index] : ja[index];
    }

    public static ArrayList<Integer> tempPool = new ArrayList<>();
    /*---刷新文本---*/
    public static void refreshAllTexts() {
        mainFrame.setTitle(t(0));
        label.setText(t(1));
        generateButton.setText(t(2));

        menuDialog.setTitle(t(13));

        langPanelLabel.setText(t(14));
        langBtn.setText(t(3));

        stepPanelLabel.setText(t(15));

        removeHistoryLabel.setText(t(17));
        removeHistoryBtn.setText(t(18));

        mennsekiLabel.setText(t(16) + ":");
        linkLabel.setText("<html><font color='blue'><u>" + t(16) + "</u></font></html>");

        String htmlText = String.format("<html>%s<font color='#0366d6'><u>MMuXxin</u></font></html>", t(19));
        authorLink.setText(htmlText);

        updateLogLabel.setText(t(20) + ":");
        updateLogLink.setText(t(20) + ":");
        updateLogLink.setText("<html><font color='blue'><u>" + t(20) + "</u></font></html>");

        searchLabel.setText(t(22));
        try {   //初始化时menuFrame未被创建
            logDialog.setTitle(t(20));
            taLog.setText(t(21));
            weatherDisplayDialog.setTitle(t(30) + t(28));
        } catch (NullPointerException ignored) {}

        chooseBackgroundBtn.setText(t(23));
        chooseBackgroundLabel.setText(t(24) + ":");

        exitItem.setLabel(t(25));
        subscribeItem.setLabel(t(19));
        menunoItem.setLabel(t(26));
        saikidouItem.setLabel(t(27));

        simpleWeatherDisplayLabel.setText(t(29) + ":");
        todayLabel.setText("<html><font color='blue'><u>" + t(31) + "</u></font>&nbsp;&nbsp;&nbsp;&nbsp;</html>");
        tomorrowLabel.setText("<html><font color='blue'><u>" + t(32) + "</u></font>&nbsp;&nbsp;&nbsp;&nbsp;</html>");
        afterTomorrowLabel.setText("<html><font color='blue'><u>" + t(33) + "</u></font>&nbsp;&nbsp;&nbsp;&nbsp;</html>");

        SwingUtilities.updateComponentTreeUI(mainFrame);    //刷新
    }

    public static void main(String[] args) {
        startTerminalServer();  //伪终端
        int port;
        try {
            port = Integer.parseInt(p.getProperty("port", "18324"));    //冷门端口，默认18324
        } catch (NumberFormatException e) {
            port = 18324;
        }
        try {
            serverSocketLock = new java.net.ServerSocket(port);
            new Thread(() -> {
                while (true) {
                    try {
                        java.net.Socket socket = serverSocketLock.accept();
                        socket.close();
                        SwingUtilities.invokeLater(() -> {
                            if (mainFrame != null) {
                                mainFrame.setVisible(true);
                                mainFrame.setExtendedState(JFrame.NORMAL);
                                mainFrame.toFront();
                                mainFrame.requestFocus();
                            }
                        });
                    } catch (Exception e) {l.severe("mainFrame生成失败或端口挂了"+e.getMessage());}
                }
            }).start();
        } catch (Exception e) {
            try {
                //防止多开
                java.net.Socket clientSocket = new java.net.Socket("127.0.0.1", port);
                clientSocket.close();
            } catch (Exception e2) {l.severe("端口挂了"+e2.getMessage());}
            System.exit(0);
        }
        SwingUtilities.invokeLater(RT2::createAndShowGUI);
    }

    private static void createModifier() {
        JDialog modifierDialog = new JDialog(mainFrame, t(10), true);
        modifierDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        modifierDialog.setSize(450, 300);
        modifierDialog.setLocationRelativeTo(mainFrame);
        UIManager.put("Label.font", baseFont);
        UIManager.put("TextField.font", baseFont);
        UIManager.put("TextArea.font", baseFont);
        JTextField[] textFields = new JTextField[modifierStep];
        for (int i = 0; i < modifierStep; i++) {    //5个文本框
            textFields[i] = new JTextField();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (JTextField tf : textFields) {
            panel.add(tf);
        }
        modifierDialog.add(panel);
        loadConfig(textFields, modifierStep);
        modifierDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {  //关闭后自动保存
                saveConfig(textFields, modifierStep);
                try {
                    parseWmx = loadwmx();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                modifierDialog.dispose();
            }
        });
        modifierDialog.setVisible(true);
    }
    /*---主页面---*/
    private static void createAndShowGUI() {
        mainFrame = new JFrame(t(0));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(Integer.parseInt(p.getProperty("mainframe_width", "800")), Integer.parseInt(p.getProperty("mainframe_height", "500")));
        mainFrame.setLayout(new BorderLayout(15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        countField = new JTextField(initStr, 5);
        countField.setBackground(new Color(resultAreaBgR, resultAreaBgG, resultAreaBgB));
        countField.setFont(generateButtonPlainFont);
        countField.setHorizontalAlignment(JTextField.CENTER);
        PlainDocument doc = (PlainDocument) countField.getDocument();
        maskFilter = new PartialMaskFilter();
        doc.setDocumentFilter(maskFilter);

        generateButton = new JButton(t(2));
        generateButton.setPreferredSize(new Dimension(220, 70));
        generateButton.setFont(generateButtonFont);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        buttonPanel.add(generateButton);

        resultArea = new JTextArea();
        resultArea.setCaretColor(new Color(0, 0, 0, 0));
        resultArea.setFont(baseFont);
        resultArea.setEditable(false);
        resultArea.setFont(wocaonimaFont);
        resultArea.setForeground(new Color(resultAreaR, resultAreaG, resultAreaB));
        resultArea.setBackground(new Color(resultAreaBgR, resultAreaBgG, resultAreaBgB));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(700, 200));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                        .createLineBorder(new Color(scrollBorderR, scrollBorderG, scrollBorderB), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        label = new JLabel(t(1));
        label.setFont(generateButtonPlainFont);

        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout(15, 15));
        if (backgroundFile != null && backgroundFile.exists()) {
            System.out.println(backgroundFile.getAbsolutePath());
            boolean islight = backgroundPanel.setBackgroundImage(backgroundFile.getAbsolutePath());
            //深色浅色两个设置图标
            iconnoPath = islight ? settingLightIconPath : settingIconPath;

            if (islight) label.setForeground(Color.WHITE);
            else label.setForeground(Color.BLACK);
        } else iconnoPath = settingIconPath;

        settingsButton = new JButton();
        ImageIcon originalIcon = new ImageIcon(iconnoPath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon settingsIcon = new ImageIcon(scaledImage);
        settingsButton.setIcon(settingsIcon);
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setPreferredSize(new Dimension(40, 40));
        settingsButton.addActionListener(_ -> createMenu());

        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel leftSpacer = new JPanel();
        //设置按钮的northpanel使得中间的组件无法对齐，故添加空白的leftspacer使之对齐
        leftSpacer.setPreferredSize(new Dimension(67, 40));
        northPanel.add(leftSpacer, BorderLayout.WEST);

        topPanel.add(label);
        topPanel.add(countField);
        northPanel.add(topPanel, BorderLayout.CENTER);

        JPanel settingsBox = new JPanel(new BorderLayout());
        settingsBox.setOpaque(false);
        settingsBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 30));
        settingsBox.add(settingsButton, BorderLayout.CENTER);

        northPanel.add(settingsBox, BorderLayout.EAST);

        topPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        northPanel.setOpaque(false);
        leftSpacer.setOpaque(false);
        resultArea.setOpaque(false);
        scrollPane.setOpaque(false);

        backgroundPanel.add(northPanel, BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        backgroundPanel.add(scrollPane, BorderLayout.SOUTH);
        //让生成数字的resultArea上边框，永远在整个frame的3/7处
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int currentWindowHeight = mainFrame.getHeight();
                int targetHeight = currentWindowHeight * 3 / 7;
                scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, targetHeight));
                backgroundPanel.revalidate();
            }
        });

        mainFrame.setContentPane(backgroundPanel);
        loadHistory();

        wwwerStrategy(pf, runFile);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.setExtendedState(JFrame.NORMAL);
        mainFrame.toFront();
        mainFrame.requestFocus();
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        if (SystemTray.isSupported()) {
            Image image = Toolkit.getDefaultToolkit().createImage(trayIconPath);
            SystemTray tray = SystemTray.getSystemTray();
            TrayIcon trayIcon = new TrayIcon(image, "G241随机学号生成器" + sserial);
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
               l.severe("TrayIcon could not be added."+e.getMessage());
            }

            PopupMenu popup = new PopupMenu();

            Font tempFont = smallerFont;
            //退出
            exitItem = new MenuItem();
            exitItem.setLabel(t(25));
            exitItem.setFont(tempFont);
            exitItem.addActionListener(_ -> {
                System.exit(0);
            });
            //关注作者
            subscribeItem = new MenuItem();
            subscribeItem.setLabel(t(19));
            subscribeItem.setFont(tempFont);
            subscribeItem.addActionListener(_ -> {
                try {
                    Desktop.getDesktop().browse(URI.create("https://github.com/MMuXxin/"));
                } catch (IOException e) {
                    l.severe("Desktop could not be opened."+e.getMessage());
                }
            });
            //打开menu
            menunoItem = new MenuItem();
            menunoItem.setLabel(t(26));
            menunoItem.setFont(tempFont);
            menunoItem.addActionListener(_ -> {
                if (menuDialog == null) {
                    createMenu();
                } else {
                    menuDialog.setVisible(true);
                    menuDialog.toFront();
                }
            });
            //重启
            saikidouItem = new MenuItem();
            saikidouItem.setLabel(t(27));
            saikidouItem.setFont(tempFont);
            saikidouItem.addActionListener(_ -> {
                saikidou(runFile);
            });

            popup.add(menunoItem);
            popup.add(subscribeItem);
            popup.add(saikidouItem);
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            trayIcon.addActionListener(e -> {   //双击显示mainframe
                mainFrame.setVisible(true);
                mainFrame.setExtendedState(JFrame.NORMAL);
            });
        }

    }

    public static void saikidou(File runFile) {
        try {
            //这里不知道为什么，在seewo上就是重启不了，不管了，(●'◡'●)
            new ProcessBuilder("cmd", "/c", "start", "", runFile.getAbsolutePath()).start();
        } catch (IOException e) {
            l.severe("重启失败"+e.getMessage());
        }
        System.exit(0);
    }

    public static void wwwerStrategy(File propFile, File runFile) {
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wwwerNaibuStrategy(propFile, runFile);
            }
        });
    }
    /*---核心抽取逻辑---*/
    private static void wwwerNaibuStrategy(File propFile, File runFile) {
        EffectManager.stopCurrentEffect(resultArea, wocaonimaFont);
        resultArea.setForeground(new Color(resultAreaR, resultAreaG, resultAreaB));

        String input = maskFilter.getRealText().trim();
        if (input.equals("prop")) { //输入prop调出菜单，可以简单编辑配置文件
            editProp(propFile);
            return;
        }
        if (input.equals("5275")) { //输入5275重启
            saikidou(runFile);
            return;
        }
        if (input.equals("ter")) {  //输入ter打开伪终端
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(new File("resources/ter.bat"));
                return;
            } catch (IOException e) {
                l.severe("伪终端出错"+e.getMessage());
            }
        }
        int count;
        try {
            count = Integer.parseInt(input);
            if (count <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, t(6), t(5), JOptionPane.ERROR_MESSAGE);
            return;
        }
        wwwerCOUNT = count;
        if (count == getKey()) {
            tempKEY = getKey();
            SwingUtilities.invokeLater(() -> createModifier());
            return;
        }

        ArrayList<Integer> pool = new ArrayList<>(baseList);
        try {
            if (!file.exists()) {   //作弊配置菜单不存在
                JOptionPane.showMessageDialog(mainFrame, t(7), t(5), JOptionPane.ERROR_MESSAGE);
                return;
            }
            parseWmx = loadwmx();

            if (parseWmx.removedNums != null) {
                for (int num : parseWmx.removedNums) {
                    pool.remove((Integer) num);
                }
            }

            ArrayList<Integer> newPool1 = new ArrayList<>(pool);
            tempPool = pool;
            int maxHistorySize;

            maxHistorySize = stepNum;

            while (stepNum > 0 && recentHistory.size() > maxHistorySize) {
                recentHistory.removeFirst();
            }

            if (stepNum > 0) {
                int validHistoryCount = 0;
                for (int num : recentHistory) {
                    if (pool.contains(num)) validHistoryCount++;
                }
                int realStep = validHistoryCount;
                while (recentHistory.size() > stepNum) {
                    recentHistory.removeFirst();
                }
                if (count + realStep <= newPool1.size()) {      //抽取数量+历史数量<池子  正常从newPool1里面把recenthistory删除
                    for (int num : recentHistory) {
                        newPool1.remove((Integer) num);
                    }
                } else if (count + realStep > newPool1.size() && count > realStep) {    //抽取数量+历史数量>池子  先删recenthistory的一部分（needRelease个元素）
                    int needRelease = count - (newPool1.size() - realStep);
                    for (int i = 0; i < needRelease && !recentHistory.isEmpty(); i++) {
                        recentHistory.removeFirst();
                    }
                    newPool1 = new ArrayList<>(pool);
                    for (int num : recentHistory) {
                        newPool1.remove((Integer) num);
                    }
                } else if (count + realStep > newPool1.size() && count <= realStep) {
                    int needReleaseEffective = count - (newPool1.size() - realStep);
                    int releasedEffective = 0;
                    while (releasedEffective < needReleaseEffective && !recentHistory.isEmpty()) {
                        int removed = recentHistory.removeFirst();
                        if (pool.contains(removed)) {
                            releasedEffective++;
                        }
                    }
                    newPool1 = new ArrayList<>(pool);
                    for (int num : recentHistory) {
                        newPool1.remove((Integer) num);
                    }
                }
            }

            if (count <= baseList.size() && count >= newPool1.size()) {
                count = newPool1.size();
            }

            if (count > newPool1.size() && count != KEY) {
                JOptionPane.showMessageDialog(mainFrame, t(8) + baseList.size(), t(5), JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<Integer, Double> weightMap = new HashMap<>();

            for (int num : newPool1) {
                weightMap.put(num, 1.0);
            }

            if (parseWmx.amplifiedNums != null) {
                for (int num : parseWmx.amplifiedNums) {
                    if (weightMap.containsKey(num)) {
                        weightMap.put(num, parseWmx.amplifiedCount);
                    }
                }
            }

            Random rand = new Random();
            int[] result = new int[count];

            for (int i = 0; i < count; i++) {
                double totalWeight = 0;
                for (double w : weightMap.values()) {
                    totalWeight += w;
                }

                double r = rand.nextDouble() * totalWeight;
                double cumulative = 0;
                int selectedId = 0;

                for (Map.Entry<Integer, Double> entry : weightMap.entrySet()) {
                    cumulative += entry.getValue();
                    if (r <= cumulative) {
                        selectedId = entry.getKey();
                        break;
                    }
                }

                result[i] = selectedId;
                weightMap.remove(selectedId);
            }
            for (int id : result) {
                recentHistory.addLast(id);
            }

            while (stepNum > 0 && recentHistory.size() > maxHistorySize) {
                recentHistory.removeFirst();
            }

            saveHistory();
            if (count != tempKEY) {
                generateRound++;
                if (parseWmx.map != null && parseWmx.map.containsKey(generateRound)) {
                    int forceNum = parseWmx.map.get(generateRound);
                    if (newPool1.contains(forceNum)) {
                        boolean alreadyInResult = false;
                        for (int id : result) {
                            if (id == forceNum) {
                                alreadyInResult = true;
                                break;
                            }
                        }
                        if (!alreadyInResult && result.length > 0) {
                            result[result.length - 1] = forceNum;
                            recentHistory.addLast(forceNum);
                        }
                    }
                }
            }
            saveHistory();


            if (result.length == 1) {
                int gakuseki = result[0];
                HashMap<Integer, Integer> effectMap = parseWmx.effectMap;
                if (effectMap != null) {
                    if (effectMap.containsKey(gakuseki)) {
                        switch (effectMap.get(gakuseki)) {
                            case 0:
                                EffectManager.startBlinkEffect(resultArea, colorInternal);
                                break;
                            case 1:
                                EffectManager.startBreathSizeEffect(resultArea, 20, 50);
                                break;
                            case 2:
                                EffectManager.applyStaticColorAndSize(resultArea, Color.GREEN, 80);
                                break;
                            case 3:
                                EffectManager.startRandomFontEffect(resultArea, 60);
                        }
                    } else if (effectMap.containsKey(KEY)) {
                        Integer i = effectMap.get(KEY);
                        switch (i) {
                            case 0:
                                EffectManager.startBlinkEffect(resultArea, colorInternal);
                                break;
                            case 1:
                                EffectManager.startBreathSizeEffect(resultArea, 20, 50);
                                break;
                            case 2:
                                EffectManager.applyStaticColorAndSize(resultArea, Color.GREEN, 80);
                                break;
                            case 3:
                                EffectManager.startRandomFontEffect(resultArea, 60);
                        }
                    }
                }

            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(result[i]);
            }
            String str = sb.toString();
            resultArea.setText("");

            generateButton.setEnabled(false);
            typeText(str, typetextinterval, resultArea);
            resultArea.setVisible(true);
            generateButton.setEnabled(true);


        } catch (Exception ex) {
            if (count != tempKEY) {
                JOptionPane.showMessageDialog(mainFrame, t(9) + ex.getMessage(), t(5), JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    //menu
    //region
    private static void createMenu() {
        menuDialog = new JDialog(mainFrame, t(13), true);
        menuDialog.setSize(400, 330);
        menuDialog.setLocationRelativeTo(mainFrame);
        menuDialog.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(createLangPanel(menuDialog));
        mainPanel.add(settingStepPanel(menuDialog));
        mainPanel.add(searchHistory(menuDialog));
        mainPanel.add(removeHistoryPanel(menuDialog));
        mainPanel.add(chooseBackground(menuDialog));
        mainPanel.add(openAnnouncement(menuDialog));
        mainPanel.add(displayWeather(menuDialog, currentCityName));
        mainPanel.add(updateLog(menuDialog));
        mainPanel.add(subscribeAuthor(menuDialog));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        menuDialog.add(scrollPane, BorderLayout.CENTER);

        menuDialog.setVisible(true);
    }

    private static JPanel createLangPanel(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        langPanelLabel = new JLabel(t(14));
        langBtn = new JButton(t(3));
        langPanelLabel.setFont(smallerFont);
        langBtn.setFont(smallerFont);
        langBtn.setMargin(new Insets(1, 5, 1, 5));

        langBtn.addActionListener(_ -> {
            currentLang = (currentLang == 1) ? 0 : 1;
            refreshAllTexts();
        });
        panel.add(langPanelLabel);
        panel.add(langBtn);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private static JPanel settingStepPanel(JDialog dialog) {
        loadHistory();
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stepPanelLabel = new JLabel(t(15));
        stepPanelLabel.setFont(smallerFont);
        JTextField stepField = new JTextField(String.valueOf(stepNum), 3);
        stepField.setBackground(new Color(resultAreaBgR, resultAreaBgG, resultAreaBgB));
        stepField.setFont(smallerFont);
        stepField.setMargin(new Insets(1, 5, 1, 5));
        stepField.setHorizontalAlignment(JTextField.CENTER);

        stepField.addActionListener(_ -> {
            try {
                String text = stepField.getText();
                if (text == null || text.isEmpty()) {
                    return;
                }
                int newValue = Integer.parseInt(text);
                if (newValue <= baseList.size()) {
                    stepNum = newValue;
                    saveHistory();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, t(8) + baseList.size(), t(5), JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                stepField.setText(String.valueOf(stepNum));
            }
        });
        panel.add(stepPanelLabel);
        panel.add(stepField);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private static JPanel removeHistoryPanel(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        removeHistoryLabel = new JLabel(t(17));
        removeHistoryLabel.setFont(smallerFont);
        removeHistoryBtn = new JButton(t(18));
        removeHistoryBtn.setFont(smallerFont);
        removeHistoryBtn.setMargin(new Insets(1, 5, 1, 7));

        removeHistoryBtn.addActionListener(_ -> {
            Path path = Paths.get(HISTORY_FILE);
            try {
                Files.write(path, Files.lines(path).skip(1).collect(Collectors.joining("\n")).getBytes());
                recentHistory.clear();
                saveHistory();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, t(9) + ex.getMessage(), t(9), JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(removeHistoryLabel);
        panel.add(removeHistoryBtn);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private static JPanel openAnnouncement(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mennsekiLabel = new JLabel(t(16) + ":");
        mennsekiLabel.setFont(smallerFont);
        linkLabel = new JLabel("<html><font color='blue'><u>" + t(16) + "</u></font></html>");
        linkLabel.setFont(smallerFont);
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    File htmlFile = new File("resources/免责声明 免責事項 .html");
                    Desktop.getDesktop().browse(htmlFile.toURI());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "无法打开文件: " + ex.getMessage(), t(5), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(mennsekiLabel);
        panel.add(linkLabel);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private static JPanel subscribeAuthor(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel iconLabel;
        try {
            ImageIcon icon = new ImageIcon("resources/GitHub_Invertocat_Black.png");
            Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(scaledImage));
        } catch (Exception e) {
            iconLabel = new JLabel("[GitHub] ");
            iconLabel.setFont(smallerFont);
        }
        String htmlText = String.format("<html>%s<font color='#0366d6'><u>MMuXxin</u></font></html>", t(19));
        authorLink = new JLabel(htmlText);
        authorLink.setFont(smallerFont);
        authorLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        authorLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(URI.create("https://github.com/MMuXxin/"));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, t(9) + ex.getMessage(), t(5), JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(iconLabel);
        panel.add(authorLink);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

        return panel;
    }

    private static JPanel updateLog(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updateLogLabel = new JLabel(t(20) + ":");
        updateLogLabel.setFont(smallerFont);
        updateLogLink = new JLabel("<html><font color='blue'><u>" + t(20) + "</u></font></html>");
        updateLogLink.setFont(smallerFont);
        updateLogLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        updateLogLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logDialog = new JDialog(menuDialog, t(20), true);
                logDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                logDialog.setSize(780, 300);
                logDialog.setLocationRelativeTo(menuDialog);
                taLog = new JTextArea();
                taLog.setEditable(false);
                taLog.setFont(logButtonFont);
                logDialog.add(taLog);
                String content = t(21);
                typeText(content, typetextinterval, taLog);
                logDialog.setVisible(true);
            }
        });
        panel.add(updateLogLabel);
        panel.add(updateLogLink);
        return panel;
    }

    private static JPanel searchHistory(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchLabel = new JLabel(t(22));
        searchLabel.setFont(smallerFont);
        JTextField numberField = new JTextField(3);
        numberField.setFont(smallerFont);
        numberField.setMargin(new Insets(1, 5, 1, 5));
        numberField.setHorizontalAlignment(JTextField.CENTER);
        numberField.setBackground(new Color(resultAreaBgR, resultAreaBgG, resultAreaBgB));
        JLabel resultLabel = new JLabel();
        resultLabel.setFont(smallerFont);

        numberField.addActionListener(_ -> {
            String input = numberField.getText().trim();

            int num = Integer.parseInt(input);
            ArrayList<Integer> historyPool = null;
            if (loadHistory() != null) {
                historyPool = new ArrayList<>(loadHistory());
            }
            ArrayList<Integer> pool = new ArrayList<>(baseList);
            try {
                parseWmx = loadwmx();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int[] removedNums = parseWmx.removedNums;
            String trim = maskFilter.getRealText().trim();
            if (trim != null && trim.length() > 0) {
                int tempCount = Integer.parseInt(trim);
                int weight = getWeight(num, pool, tempCount);
                if (removedNums != null) {
                    pool.removeAll(historyPool);
                    boolean contains = Arrays.stream(removedNums).anyMatch(n -> n == num);
                    if (weight == -1) {
                        resultLabel.setText(t(5));
                    } else if (contains) {
                        weight = getWeight(pool.getLast(), pool, tempCount);
                        resultLabel.setText(weight + "%");
                    } else {
                        resultLabel.setText(weight + "%");
                    }
                } else {
                    if (weight == -1) {
                        resultLabel.setText(t(5));
                    } else {
                        resultLabel.setText(weight + "%");
                    }
                }
            } else resultLabel.setText(t(5));
        });
        panel.add(searchLabel);
        panel.add(numberField);
        panel.add(resultLabel);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private static JPanel chooseBackground(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chooseBackgroundLabel = new JLabel(t(24) + ":");
        chooseBackgroundLabel.setFont(smallerFont);
        chooseBackgroundBtn = new JButton(t(23));
        chooseBackgroundBtn.setFont(smallerFont);

        chooseBackgroundBtn.addActionListener(_ -> {
            FileDialog chooser = new FileDialog(mainFrame, "选择背景图片", FileDialog.LOAD);
            chooser.setLocationRelativeTo(mainFrame);
            File defaultDir = new File("resources/backgrounds");
            if (defaultDir.exists() && defaultDir.isDirectory()) {
                chooser.setDirectory(defaultDir.getAbsolutePath());
            }
            chooser.setFilenameFilter((_, name) -> {
                String lower = name.toLowerCase();
                return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".gif") || lower.endsWith(".webp");
            });
            chooser.setVisible(true);
            String dir = chooser.getDirectory();
            String fileStr = chooser.getFile();
            if (dir != null && fileStr != null) {
                File selectedFile = new File(dir, fileStr);
                backgroundFile = selectedFile;
                boolean islight = backgroundPanel.setBackgroundImage(backgroundFile.getAbsolutePath());
                iconnoPath = islight ? settingLightIconPath : settingIconPath;
                ImageIcon originalIcon = new ImageIcon(iconnoPath);
                Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                ImageIcon settingsIcon = new ImageIcon(scaledImage);
                settingsButton.setIcon(settingsIcon);
                if (islight) label.setForeground(Color.WHITE);
                else label.setForeground(Color.BLACK);

                p.setProperty("backgroundImage", backgroundFile.getAbsolutePath());
                try (FileOutputStream fos = new FileOutputStream(pPath)) {
                    p.store(fos, null);
                } catch (IOException e) {
                }
            }
        });
        panel.add(chooseBackgroundLabel);
        panel.add(chooseBackgroundBtn);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private static boolean kuwashi = false;
    private static int after = 0;

    private static JPanel displayWeather(JDialog dialog, String city) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        simpleWeatherDisplayLabel = new JLabel(t(29) + ":");
        simpleWeatherDisplayLabel.setFont(smallerFont);
        panel.add(simpleWeatherDisplayLabel);

        JCheckBox box = new JCheckBox();
        box.addActionListener(_ -> {
            kuwashi = box.isSelected();
        });

        todayLabel = createWeatherLinkLabel(t(31), city, 0);
        tomorrowLabel = createWeatherLinkLabel(t(32), city, 1);
        afterTomorrowLabel = createWeatherLinkLabel(t(33), city, 2);

        panel.add(todayLabel);
        panel.add(tomorrowLabel);
        panel.add(afterTomorrowLabel);
        panel.add(box);

        return panel;
    }

    //endregion
    public static LinkedList<Integer> loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null && !line.trim().isEmpty()) {
                String[] parts = line.split(",");
                recentHistory.clear();
                for (String part : parts) {
                    recentHistory.add(Integer.parseInt(part.trim()));
                }
            }
            String stepLine = br.readLine();
            if (stepLine != null && !stepLine.trim().isEmpty()) {
                stepNum = Integer.parseInt(stepLine.trim());
            } else {
                stepNum = 0;
            }
        } catch (Exception e) {
            recentHistory.clear();
        }
        return recentHistory;
    }

    public static void saveHistory() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < recentHistory.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(recentHistory.get(i));
            }
            bw.write(sb.toString());
            bw.newLine();
            bw.write(String.valueOf(stepNum));
        } catch (IOException e) {
            recentHistory.clear();
        }
    }

    private static void loadConfig(JTextField[] textFields, int step) {
        File file = new File("resources/w.mx");
        if (!file.exists()) {
            return;
        }
        try {
            //5275简单xor加密
            byte[] encrypted = readAllBytes(file);
            byte[] decrypted = xorCrypt(encrypted);
            String content = new String(decrypted, StandardCharsets.UTF_8);
            String[] parts = content.split(";", -1);
            for (int i = 0; i < step; i++) {
                textFields[i].setText(parts[i]);    //直接展示，严格按照规定格式读取
                textFields[i].setBackground(new Color(resultAreaBgR, resultAreaBgG, resultAreaBgB));
            }
        } catch (Exception e) {
            System.err.println(t(12) + e.getMessage());
        }
    }

    private static void saveConfig(JTextField[] textFields, int step) {
        String[] strs = new String[step];
        String raw;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < step; i++) {
            strs[i] = textFields[i].getText().trim();
        }
        sb.append(strs[0]);
        for (int i = 1; i < step; i++) {
            sb.append(';');
            sb.append(strs[i]);
        }
        raw = sb.toString();
        byte[] plainBytes = raw.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = xorCrypt(plainBytes);

        try (FileOutputStream fos = new FileOutputStream("resources/w.mx")) {
            fos.write(encrypted);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, t(11) + e.getMessage(), t(5), JOptionPane.ERROR_MESSAGE);
        }
    }

    public static byte[] xorCrypt(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ KEY_BYTES[i % KEY_BYTES.length]);
        }
        return result;
    }

    public static byte[] readAllBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int read = fis.read(buffer);
            if (read != buffer.length) {
                throw new IOException("读取文件失败");
            }
            return buffer;
        }
    }

    private static HashMap<Integer, Integer> strtoMap(String strs) {
        HashMap<Integer, Integer> result;
        try {
            result = new HashMap<>();
            String trimmed = strs.trim();
            if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                String[] pairs = trimmed.split("\\)\\(");
                for (int i = 0; i < pairs.length; i++) {
                    String pair = pairs[i];
                    if (i == 0) {
                        pair = pair.substring(1);
                    }
                    if (i == pairs.length - 1) {
                        pair = pair.substring(0, pair.length() - 1);
                    }
                    String[] nums = pair.split(",");
                    if (nums.length == 2) {
                        int key = Integer.parseInt(nums[0].trim());
                        int value = Integer.parseInt(nums[1].trim());
                        result.put(key, value);
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    private static int getKey() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int k2 = hour * 100 + minute;
        return KEY ^ k2;
    }

    public static int getWeight(int num, ArrayList<Integer> pool, int count) {
        try {
            if (!baseList.contains(num)) return -1;
            if (pool.contains(num)) {
                int i = (int) Math.round((double) count / pool.size() * 100);
                i = Math.min(i, 100);
                return i;
            } else return 0;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static ParseWmx loadwmx() throws Exception {
        byte[] encrypted = readAllBytes(file);
        byte[] decrypted = xorCrypt(encrypted);
        ParseWmx pwmx = new ParseWmx();
        String line = new String(decrypted, StandardCharsets.UTF_8);

        String[] sarr = line.split(";", -1);


        if (!sarr[0].isEmpty()) {   //删除的学号：标准格式：1,2
            System.out.println(sarr[0]);
            pwmx.removedNums = Arrays.stream(sarr[0].split(",")).mapToInt(Integer::parseInt).toArray();
        }
        if (!sarr[1].isEmpty()) {   //被放大权重的学号：标准格式：1,2
            pwmx.amplifiedNums = Arrays.stream(sarr[1].split(",")).mapToInt(Integer::parseInt).toArray();
        }
        String amplifiedStr = sarr[2];
        if (!sarr[2].isEmpty()) {   //放大倍数，可以是小数
            pwmx.amplifiedCount = Double.parseDouble(amplifiedStr);
        }

        String predictableStr;
        if (!sarr[3].isEmpty()) {   //强制制度次数展示指定学号：标准格式：(1,15)(3,19)
            predictableStr = sarr[3];
            pwmx.map = strtoMap(predictableStr);
        }
        String effectsStr;
        if (!sarr[4].isEmpty()) {   //指定学号启动有趣的文字特效
            effectsStr = sarr[4];
            switch (effectsStr) {
                case "*0":  //*【num】:全员特效
                case "*1":
                case "*2":
                case "*3":
                    int value = Integer.parseInt(effectsStr.substring(1));
                    HashMap<Integer, Integer> map = new HashMap<>();
                    map.put(KEY, value);
                    pwmx.effectMap = map;
                    break;
                default:
                    pwmx.effectMap = strtoMap(effectsStr);
                    break;
            }
        }
        return pwmx;
    }

    public static void editProp(File file) {
        UndoManager undoManager = new UndoManager();

        JFrame editPropFrame = new JFrame("编辑配置文件");
        editPropFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editPropFrame.setSize(700, 550);
        editPropFrame.setLocationRelativeTo(null);
        JTextArea textArea = new JTextArea();
        textArea.setFont(propTextAreaFont);
        JScrollPane scrollPane = new JScrollPane(textArea);
        editPropFrame.add(scrollPane, BorderLayout.CENTER);

        textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        KeyStroke undoKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z, mask);
        KeyStroke redoKey = KeyStroke.getKeyStroke(KeyEvent.VK_Y, mask);
        InputMap inputMap = textArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = textArea.getActionMap();

        inputMap.put(undoKey, "undo");
        actionMap.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        inputMap.put(redoKey, "redo");
        actionMap.put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });

        JButton saveBtn = new JButton("保存配置");
        saveBtn.setPreferredSize(new Dimension(110, 35));
        saveBtn.setFont(logButtonFont);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(saveBtn);
        editPropFrame.add(bottomPanel, BorderLayout.SOUTH);
        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.ISO_8859_1);
            String decoded = decodeUnicode(content);
            textArea.setText(decoded);
            undoManager.discardAllEdits();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(editPropFrame, "读取文件失败: " + e.getMessage());
            editPropFrame.dispose();
            return;
        }

        saveBtn.addActionListener(_ -> {
            String text = textArea.getText();
            String[] lines = text.split("\n");
            boolean formatOk = true;
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("#") && !trimmed.startsWith("!")) {
                    if (!trimmed.contains("=")) {
                        formatOk = false;
                        break;
                    }
                }
            }
            if (!formatOk) {
                int response = JOptionPane.showConfirmDialog(editPropFrame,
                        "部分行不是 key=value 格式，是否继续保存？",
                        "格式警告", JOptionPane.YES_NO_OPTION);
                if (response != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            String encoded = encodeUnicode(text);
            try {
                Files.writeString(file.toPath(), encoded, StandardCharsets.ISO_8859_1);
                JOptionPane.showMessageDialog(editPropFrame, "保存成功！");
                editPropFrame.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(editPropFrame, "保存失败: " + ex.getMessage());
            }
        });

        editPropFrame.setVisible(true);
    }

    private static String decodeUnicode(String input) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            int code = Integer.parseInt(matcher.group(1), 16);
            matcher.appendReplacement(sb, String.valueOf((char) code));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String encodeUnicode(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c > 127) {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void typeText(String text, int delay, JTextArea jf) {
        if (typingTimer != null && typingTimer.isRunning())
            typingTimer.stop();
        jf.setText("");
        final int[] index = {0};
        typingTimer = new Timer(delay, null);
        typingTimer.addActionListener(e -> {
            if (index[0] < text.length()) {
                jf.append(String.valueOf(text.charAt(index[0])));
                index[0]++;
                jf.setCaretPosition(jf.getDocument().getLength());
            } else {
                typingTimer.stop();
            }
        });
        typingTimer.start();
    }

    public static void startTerminalServer() {
        new Thread(() -> {
            int port = 5275;
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
                            writer.println("Ready. Available: /gb [num], /cls, /msg [text], /exit, /sai, /setb [num]");
                            writer.flush();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String fullCmd = line.trim();
                                if (fullCmd.isEmpty()) continue;
                                String[] parts = fullCmd.split("\\s+");
                                String mainCmd = parts[0].toLowerCase();
                                String arg = parts.length > 1 ? parts[1] : "";
                                if (BIGSWITCH(mainCmd, arg, fullCmd)) return;
                            }
                        } catch (Exception ignored) {
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static boolean BIGSWITCH(String mainCmd, String arg, String fullCmd) {
        try {
            switch (mainCmd) {
                case "/stop":
                    isStopRequested = true;
                    break;
                case "/gb":
                    int times = 1;
                    try {
                        if (!arg.isEmpty()) times = Integer.parseInt(arg);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        break;
                    }
                    final int loopTimes = times;
                    new Thread(() -> {
                        SwingUtilities.invokeLater(() -> {
                            resultArea.setFont(wocaonimaFont);
                        });
                        isStopRequested = false;
                        for (int i = 0; i < loopTimes; i++) {
                            if (isStopRequested) break;
                            SwingUtilities.invokeLater(() -> {
                                if (wwwerCOUNT == 0) {
                                    wwwerCOUNT++;
                                }
                                countField.setText(String.valueOf(wwwerCOUNT));
                                MouseEvent clickEvent = new MouseEvent(generateButton, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);
                                for (MouseListener ml : generateButton.getMouseListeners()) {
                                    ml.mouseClicked(clickEvent);
                                }
                            });
                            try {
                                if (wwwerCOUNT == 0) {
                                    wwwerCOUNT++;
                                }
                                Thread.sleep(Math.max(0, (3 * wwwerCOUNT - 1) * 40));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }).start();
                    break;

                case "/cls":
                    SwingUtilities.invokeLater(() -> resultArea.setText(""));
                    break;
                case "/msg":
                    final String textMsg = fullCmd.substring(mainCmd.length()).trim();
                    if (!textMsg.isEmpty()) {
                        SwingUtilities.invokeLater(() -> {
                            resultArea.setText("");
                            typeText(textMsg, typetextinterval, resultArea);
                        });
                    }
                    break;
                case "/edi":
                    new Thread(() -> {
                        try {
                            wmxkidou();
                            SwingUtilities.invokeLater(() -> {
                                frame.setVisible(true);
                                frame.setAlwaysOnTop(true);
                            });
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                    }).start();
                    break;
                case "/exit":
                    System.exit(0);
                    return true;
                case "/sai":
                    SwingUtilities.invokeLater(() -> saikidou(runFile));
                    return true;
                case "/badapple":
                    new Thread(() -> {
                        try {
                            SwingUtilities.invokeLater(() -> {
                                resultArea.setFont(badappleFont);
                                resultArea.setLineWrap(false);
                            });

                            Path path = Paths.get("resources/play.txt");
                            if (!Files.exists(path)) {
                                return;
                            }
                            byte[] bytes = Files.readAllBytes(path);
                            String allContent = new String(bytes, StandardCharsets.UTF_8);

                            String[] frames = allContent.split("SPLIT");
                            isStopRequested = false;
                            for (String frame : frames) {
                                if (isStopRequested) break;
                                SwingUtilities.invokeLater(() -> {
                                    resultArea.setText(frame);
                                });

                                Thread.sleep(Integer.parseInt(p.getProperty("badapplesleep", "75")));
                            }

                            SwingUtilities.invokeLater(() -> resultArea.setText("Bad Apple Playback Finished."));

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            resultArea.setFont(wocaonimaFont);
                        }
                    }).start();
                    break;
                case "/colorba":
                    new Thread(() -> {
                        try {
                            SwingUtilities.invokeLater(() -> {
                                resultArea.setFont(badappleFont);
                                resultArea.setLineWrap(false);
                                EffectManager.startBlinkEffect(resultArea, colorInternal);
                            });

                            Path path = Paths.get("resources/play.txt");
                            if (!Files.exists(path)) {
                                return;
                            }
                            byte[] bytes = Files.readAllBytes(path);
                            String allContent = new String(bytes, StandardCharsets.UTF_8);

                            String[] frames = allContent.split("SPLIT");
                            isStopRequested = false;
                            for (String frame : frames) {
                                if (isStopRequested) break;
                                SwingUtilities.invokeLater(() -> {
                                    resultArea.setText(frame);
                                });
                                Thread.sleep(Integer.parseInt(p.getProperty("badapplesleep", "75")));
                            }

                            if (!isStopRequested) {
                                SwingUtilities.invokeLater(() -> {
                                    resultArea.setText("Color Bad Apple Playback Finished.");
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            SwingUtilities.invokeLater(() -> {
                                try {
                                    EffectManager.stopCurrentEffect(resultArea, wocaonimaFont);
                                } catch (Exception ignored) {
                                }
                                resultArea.setFont(wocaonimaFont);
                                resultArea.setForeground(new Color(resultAreaR, resultAreaG, resultAreaB));
                                resultArea.setLineWrap(true);
                            });
                        }
                    }).start();
                    break;
                case "/setb":
                    if (arg.isEmpty()) break;
                    SwingUtilities.invokeLater(() -> {
                        File bgDir = new File("resources/backgrounds");
                        if (bgDir.exists() && bgDir.isDirectory()) {
                            File[] files = bgDir.listFiles((_, name) -> {
                                String lower = name.toLowerCase();
                                boolean isImage = lower.endsWith(".png") || lower.endsWith(".jpg") ||
                                        lower.endsWith(".jpeg") || lower.endsWith(".gif") ||
                                        lower.endsWith(".webp");
                                if (!isImage) return false;
                                int lastDot = lower.lastIndexOf('.');
                                String nameWithoutExtension = (lastDot - 1 >= 0) ? lower.substring(0, lastDot) : lower;
                                return nameWithoutExtension.equals(arg.toLowerCase()) ||
                                        nameWithoutExtension.startsWith(arg.toLowerCase() + "_");
                            });
                            if (files != null && files.length > 0) {
                                File selectedFile = files[0];
                                backgroundFile = selectedFile;
                                boolean islight = backgroundPanel.setBackgroundImage(backgroundFile.getAbsolutePath());
                                iconnoPath = islight ? settingLightIconPath : settingIconPath;
                                ImageIcon originalIcon = new ImageIcon(iconnoPath);
                                Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                                ImageIcon settingsIcon = new ImageIcon(scaledImage);
                                settingsButton.setIcon(settingsIcon);
                                if (islight) label.setForeground(Color.WHITE);
                                else label.setForeground(Color.BLACK);

                                p.setProperty("backgroundImage", backgroundFile.getAbsolutePath());
                                try (FileOutputStream fos = new FileOutputStream(pPath)) {
                                    p.store(fos, null);
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static Font getF(int size, boolean bold) {
        if (bold) {
            return new Font("微软雅黑", Font.BOLD, size);
        }
        return new Font("微软雅黑", Font.PLAIN, size);
    }

    private static JLabel createWeatherLinkLabel(String text, String city, int after) {
        JLabel label = new JLabel();
        label.setText("<html><font color='blue'><u>" + text + "</u></font>&nbsp;&nbsp;&nbsp;&nbsp;</html>");
        label.setFont(smallerFont);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showWeatherDialog(city, after);
            }
        });
        return label;
    }

    private static void showWeatherDialog(String city, int after) {
        weatherDisplayDialog = new JDialog(menuDialog, t(30) + t(28), true);
        weatherDisplayDialog.setResizable(true);
        weatherDisplayDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Map<String, String> m = getWeatherMapss(city, after);

        weatherDisplayTextArea = new JTextArea();
        weatherDisplayTextArea.setEditable(false);
        weatherDisplayTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        StringBuilder context = new StringBuilder();
        if (m == null) {
            JOptionPane.showMessageDialog(weatherDisplayDialog, "网络未连接", t(5), JOptionPane.ERROR_MESSAGE);
            weatherDisplayDialog.dispose();
        } else {
            if (kuwashi) {
                weatherDisplayDialog.setSize(330, 780);
                int targetWidth = 24;
                m.forEach((k, v) -> {
                    int currentWidth = 0;
                    for (char c : k.toCharArray()) {
                        if (c > 127) currentWidth += 2;
                        else currentWidth += 1;
                    }
                    int spaceCount = targetWidth - currentWidth;
                    if (spaceCount < 1) spaceCount = 1;
                    context.append(k).append(":").append(" ".repeat(spaceCount)).append(v).append("\n");
                });
            } else {
                weatherDisplayDialog.setSize(330, 300);
                if (currentLang == 1) {
                    context.append("日期:             ").append(m.getOrDefault("日期", "未知")).append("\n");
                    context.append("体感温度(℃):      ").append(m.getOrDefault("体感温度(℃)", "未知")).append("\n");
                    context.append("当前温度(℃):      ").append(m.getOrDefault("当前温度(℃)", "未知")).append("\n");
                    context.append("湿度(%):          ").append(m.getOrDefault("湿度(%)", "未知")).append("\n");
                    context.append("降雨概率(%):      ").append(m.getOrDefault("降雨概率(%)", "未知")).append("\n");
                } else {
                    context.append("日付:             ").append(m.getOrDefault("日付", "不明")).append("\n");
                    context.append("体感温度(℃):      ").append(m.getOrDefault("体感温度(℃)", "不明")).append("\n");
                    context.append("現在の気温(℃):    ").append(m.getOrDefault("現在の気温(℃)", "不明")).append("\n");
                    context.append("湿度(%):          ").append(m.getOrDefault("湿度(%)", "不明")).append("\n");
                    context.append("降雨確率(%):      ").append(m.getOrDefault("降雨確率(%)", "不明")).append("\n");

                }
            }
            typeText(context.toString(), 1, weatherDisplayTextArea);
        }
        weatherDisplayDialog.setLocationRelativeTo(menuDialog);
        JScrollPane scrollPane = new JScrollPane(weatherDisplayTextArea);
        weatherDisplayDialog.add(scrollPane, BorderLayout.CENTER);
        weatherDisplayDialog.setVisible(true);
    }
}

