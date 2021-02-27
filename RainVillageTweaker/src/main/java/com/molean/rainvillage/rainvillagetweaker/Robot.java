package com.molean.rainvillage.rainvillagetweaker;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.BotConfiguration;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Robot {
    private static Bot bot;

    public static Bot getBot() {
        return bot;
    }

    private static String getText(QuoteReply quoteReply) {
        MessageChain originalMessage = quoteReply.getSource().getOriginalMessage();
        String originText = originalMessage.contentToString();
        Pattern pattern = Pattern.compile("<([a-zA-Z0-9_]{3,18})> .*");
        Matcher matcher = pattern.matcher(originText);
        if (matcher.matches()) {
            String group = matcher.group(1);
            System.out.println(group);
            return "@" + group + " ";
        } else {
            return quoteReply.contentToString();
        }
    }

    public static int realLength(String string) {
        int length = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < string.length(); i++) {
            String single = string.substring(i, i + 1);
            if (single.matches(chinese)) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }

    public static String leftString(String string, int size) {
        if (size == 0)
            return "";
        int length = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < string.length(); i++) {
            String single = string.substring(i, i + 1);
            if (single.matches(chinese)) {
                length += 2;
            } else {
                length += 1;
            }
            if (length >= size) {
                return string.substring(0, i + 1);
            }
        }
        return string;
    }

    public Robot() {
        ProxyServer.getInstance().getScheduler().runAsync(RainVillageTweaker.getPlugin(), () -> {

            bot = Mirai.getInstance().getBotFactory().newBot(3215501892L, "x558545x55", new BotConfiguration() {
                {
                    fileBasedDeviceInfo("deviceInfo.json");
                }
            });
            bot.login();
            bot.getEventChannel().registerListenerHost(new SimpleListenerHost() {
                @EventHandler
                public void onGroupMessage(GroupMessageEvent event) {
                    if (event.getGroup().getId() != 865260261L) {
                        return;
                    }
                    if (Calendar.getInstance().getTimeInMillis() / 1000 - event.getTime() > 30) {
                        return;
                    }
                    StringBuilder plainMessage = new StringBuilder();
                    MessageChain rawMessage = event.getMessage();
                    for (SingleMessage singleMessage : rawMessage) {
                        String subMessage;
                        if (singleMessage instanceof QuoteReply) {
                            subMessage = getText((QuoteReply) singleMessage);
                        } else if (singleMessage instanceof At && ((At) singleMessage).getTarget() == bot.getId()) {
                            subMessage = "";
                        } else if (singleMessage instanceof At) {
                            subMessage = singleMessage.contentToString();
                        } else if (singleMessage instanceof PlainText) {
                            subMessage = singleMessage.contentToString();
                        } else if (singleMessage instanceof Image) {
                            subMessage = singleMessage.contentToString();
                        } else if (singleMessage instanceof Voice) {
                            subMessage = singleMessage.contentToString();
                        } else if (singleMessage instanceof Face) {
                            subMessage = singleMessage.contentToString();
                        } else {
                            subMessage = "";
                        }
                        plainMessage.append(subMessage);
                    }
                    //post handle
                    String p = event.getSender().getNameCard().replace('§', '&');
                    String m = plainMessage.toString().replace('§', '&');
                    if (realLength(p) > 16) {
                        p = leftString(p, 16) + "..";
                    }
                    if (realLength(m) > 256) {
                        m = leftString(m, 256) + "..";
                    }
                    if (m.trim().length() == 0)
                        return;
                    String finalMessage = "[茶馆]<§o" + p + "§r> " + m;
                    for (ProxiedPlayer player : RainVillageTweaker.getPlugin().getProxy().getPlayers()) {
                        new TextComponent();
                        player.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(finalMessage));
                    }
                }
            });
            bot.join();
        });
    }
}

