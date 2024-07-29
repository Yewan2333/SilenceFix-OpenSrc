package dev.xinxin.gui.verify;

import dev.xinxin.Client;
import dev.xinxin.utils.client.menu.BetterMainMenu;
import dev.xinxin.utils.render.RenderUtil;
import dev.xinxin.utils.render.RoundedUtils;
import dev.xinxin.utils.render.fontRender.FontManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.compatibility.display.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class GuiLogin extends GuiScreen {
    public GuiPasswordField passwordField;
    public GuiUserField usernameField;
    public String hwid = null;
    boolean exist = false;
    static String username = "";
    static String password = "";
    private final List<LoginButton> buttons = Arrays.asList(new LoginButton("Login"), new LoginButton("Exit"));
    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(mc);
        int textFieldWidth = 200;
        this.usernameField = new GuiUserField(0, FontManager.arial18, sr.getScaledWidth() - 420, 280, textFieldWidth, 20);
        this.passwordField = new GuiPasswordField(1, FontManager.arial18, sr.getScaledWidth() - 420, 340, textFieldWidth, 20);
        this.passwordField.setMaxStringLength(128);
        this.buttons.forEach(LoginButton::initGui);
    }
    public static String getUsername(){
        return username;
    }
    public static String getPassword(){
        return password;
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        /*if (typedChar == 9) {
            if (!usernameField.isFocused()) {
                usernameField.setFocused(true);
            } else {
                usernameField.setFocused(true);
                passwordField.setFocused(!usernameField.isFocused());
            }
        }*/
        usernameField.textboxKeyTyped(typedChar, keyCode);
        passwordField.textboxKeyTyped(typedChar, keyCode);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(mc);
        drawBackground();
        RoundedUtils.drawRound(sr.getScaledWidth() - 500, sr.getScaledHeight() - 620 , 340, 500, 2, new Color(255,255,255, 180));
        float x = sr.getScaledWidth() - 500;
        float y = sr.getScaledHeight() - 620;
        float width = 340;
        float height = 500;
        FontManager.Tahoma40.drawString("Welcome" + " " + Client.NAME, x + width / 2 - 65, y + 100, new Color(255,255,255,230).getRGB());
        FontManager.arial20.drawStringWithShadow("Username", sr.getScaledWidth() - 420, 260, Color.GRAY.getRGB());
        usernameField.drawTextBox();
        FontManager.arial20.drawStringWithShadow("Password", sr.getScaledWidth() - 420, 320, Color.GRAY.getRGB());
        passwordField.drawTextBox();
        int count = 0;
        for (LoginButton button : this.buttons) {
            button.x = sr.getScaledWidth() - 390 + count;
            button.y = 430/*- 25.0f + (float)count*/;
            button.width = FontManager.arial20.getStringWidth(button.text) + 26;
            button.height = FontManager.arial20.getHeight() + 12;
            button.clickAction = () -> {
                switch (button.text) {
                    case "Login":
                        //mc.displayGuiScreen(new BetterMainMenu());
                        StartVerify();
                        break;
                    case "Exit":
                        mc.shutdown();
                        break;
                }
            };
            button.drawScreen(mouseX, mouseY, partialTicks);
            count = 80;
        }
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        this.buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
    }
    public void drawBackground(){
        RenderUtil.drawImage(new ResourceLocation("express/bs.png"), 0, 0, this.width, this.height);
        RenderUtil.drawBlur(3, 1, () -> Gui.drawRect(0,0,width, height, -1));
    }
    public void StartVerify() {
        java.io.File directory = new java.io.File(Client.NAME);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        java.io.File credentialsFile = new java.io.File(directory, "ID.txt");
        if (credentialsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile))) {
                username = reader.readLine();
                password = reader.readLine();
                exist = true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading stored credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (exist) {
            usernameField.setText(username);
            passwordField.setText(password);
        }
        try {
            v1();
            if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && !hwid.isEmpty()) {
                username = usernameField.getText();
                password = passwordField.getText();
            }
            if (username != null && !username.isEmpty()) {
                String throwable;
                try {
                    throwable = "[" + username + "]" + HWIDUtil.getHWID() + ":" + password;
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                v2(throwable);
            }

        } catch (Throwable throwable1) {
            exist = false;
            throwable1.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERROR: " + throwable1.getMessage());
            mc.displayGuiScreen(new BetterMainMenu());
        }
        if (!exist) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFile))) {
                writer.write(username);
                writer.newLine();
                writer.write(password);
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private boolean containsInURL(String url, String text) {
        try {
            return get(url).contains(text);
        } catch (Exception e) {
            return false;
        }
    }
    private void v1(){
        try {
            hwid = HWIDUtil.getHWID();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    private void v2(String throwable){
        if (containsInURL("https://gitcode.net/qq_52488297/hwid/-/raw/master/HWID", throwable)) {
            Client.instance.setLogged(true);
            mc.displayGuiScreen(new BetterMainMenu());
            Display.setTitle(Client.NAME + "-" + usernameField.getText());
            exist = false;
        } else {
            Client.instance.setLogged(false);
            JOptionPane.showMessageDialog(null, "验证失败，请检查用户名和密码,HWID自动复制至剪贴板", "Error", JOptionPane.ERROR_MESSAGE);
            StringSelection stringSelection = new StringSelection(hwid);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }
    /*public void updateScreen() {
        usernameField.updateCursorCounter();
        passwordField.updateCursorCounter();
    }*/
    public static String get(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = in.readLine())!= null) {
            response.append(inputLine);
            response.append("\n");
        }

        in.close();

        return response.toString();
    }
}
