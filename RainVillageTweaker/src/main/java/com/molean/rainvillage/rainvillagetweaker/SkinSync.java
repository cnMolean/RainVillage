package com.molean.rainvillage.rainvillagetweaker;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.mineskin.MineskinClient;
import org.mineskin.Model;
import org.mineskin.SkinOptions;
import org.mineskin.Visibility;
import org.mineskin.data.Skin;
import org.mineskin.data.SkinCallback;
import org.mineskin.data.Texture;
import skinsrestorer.bungee.SkinsRestorer;
import skinsrestorer.shared.storage.SkinStorage;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SkinSync implements Listener {
    private final MineskinClient skinClient;
    private final SkinsRestorer skinsRestorer;
    private final Map<String, String> skinValues = new HashMap<>();

    public SkinSync() {
        skinClient = new MineskinClient();
        skinsRestorer = (SkinsRestorer) ProxyServer.getInstance().getPluginManager().getPlugin("SkinsRestorer");
        ProxyServer.getInstance().getPluginManager().registerListener(RainVillageTweaker.getPlugin(), this);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxyServer.getInstance().getScheduler().runAsync(RainVillageTweaker.getPlugin(), () -> {
            String urlString = "https://skin.molean.com/skin/" + event.getPlayer().getName() + ".png";
            setSkin(event.getPlayer(), urlString, getSkinModule(event.getPlayer().getName()));
        });
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        sendSkinData(event.getPlayer(), skinValues.get(event.getPlayer().getName()));
    }

    public void sendSkinData(ProxiedPlayer player, String skinValue) {
        if (skinValue == null || skinValue.isEmpty()) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SkinValue");
        out.writeUTF(player.getName());
        out.writeUTF(skinValue);
        player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());

    }

    public static Model getSkinModule(String username) {
        try {
            String urlStr = "https://skin.molean.com/" + username + ".json";
            URL url = new URL(urlStr);
            InputStream inputStream = url.openStream();
            byte[] bytes = readInputStream(inputStream);
            int read = inputStream.read(bytes);
            String s = new String(bytes);
            int slimPos = s.indexOf("\"slim\"");
            int defaultPos = s.indexOf("\"default\"");
            if (slimPos < 0 && defaultPos < 0)
                return Model.DEFAULT;
            if (slimPos < 0)
                return Model.DEFAULT;
            if (defaultPos < 0)
                return Model.SLIM;
            if (slimPos < defaultPos)
                return Model.SLIM;
            else
                return Model.DEFAULT;
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    public void setSkin(ProxiedPlayer player, String urlString, Model model) {
        try {
            URL url = new URL(urlString);
            skinClient.generateUrl(url.toString(), SkinOptions.create(player.getName(), model, Visibility.PRIVATE), new SkinCallback() {

                @Override
                public void error(String errorMessage) {
                    noskin();
                }

                @Override
                public void exception(Exception exception) {
                    noskin();
                }

                @Override
                public void parseException(Exception exception, String body) {
                    noskin();
                }

                private void noskin() {
                    String filename = "plugins/SkinsRestorer/Skins/" + player.getName().toLowerCase().trim() + ".skin";
                    try {
                        File file = new File(filename);
                        FileInputStream fileInputStream = new FileInputStream(file);
                        Scanner scanner = new Scanner(fileInputStream);
                        String next = scanner.next();
                        synchronized (skinValues) {
                            skinValues.put(player.getName(), next);
                        }
                        sendSkinData(player, next);
                    } catch (IOException ignore) {

                    }
                }

                @Override
                public void done(Skin skin) {
                    synchronized (skinValues) {
                        skinValues.put(player.getName(), skin.data.texture.value);
                    }
                    sendSkinData(player, skin.data.texture.value);
                    SkinStorage skinStorage = skinsRestorer.getSkinStorage();
                    Texture texture = skin.data.texture;
                    Object property = skinStorage.createProperty(player.getName().toLowerCase(), texture.value, texture.signature);
                    skinStorage.setSkinData(player.getName().toLowerCase(), property);
                    skinsRestorer.getSkinsRestorerBungeeAPI().applySkin(player);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
