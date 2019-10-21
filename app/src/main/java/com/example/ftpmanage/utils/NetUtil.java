package com.example.ftpmanage.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import com.example.ftpmanage.entity.OperInfo;

public class NetUtil {

    /**
     * 将域名转换为IP
     *
     * @param domain
     * @return
     */
    public static String DomainToIp(String domain) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(domain);
        } catch (UnknownHostException e) {
            return "";
        }
        return address.getHostAddress().toString();
    }

    /**
     * 获取本地内网IP地址
     *
     * @return
     */
    public static String getLocalIpV4Address() {
        try {
            String ipv4;
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nilist) {
                ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address : ialist) {
                    if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
                        ipv4 = address.getHostAddress();
                        return ipv4;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 返回指定的ip是否与另一个ip在同一地址范围
     *
     * @param ip
     * @param tagIP
     * @return
     */
    public static boolean isIPRange(String ip, String tagIP) {
        if (AppRegExp.isReg(AppRegExp.RegExpStr_Ip, ip) && AppRegExp.isReg(AppRegExp.RegExpStr_Ip, tagIP)) {
            String ip1 = ip.substring(0, ip.lastIndexOf("."));
            String ip2 = tagIP.substring(0, tagIP.lastIndexOf("."));
            if (ip1.equals(ip2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据子网掩码和ip得到主机的广播地址
     *
     * @param ip
     * @param subnetMask
     * @return
     */
    public static String getBroadcastAddress(String ip, String subnetMask) {
        String ipBinary = toBinary(ip);
        String subnetBinary = toBinary(subnetMask);
        String broadcastBinary = getBroadcastBinary(ipBinary, subnetBinary);
        String wholeBroadcastBinary = spiltBinary(broadcastBinary);
        return binaryToDecimal(wholeBroadcastBinary);
    }

    /**
     * 二进制的ip字符串转十进制
     *
     * @param wholeBroadcastBinary
     * @return
     */
    public static String binaryToDecimal(String wholeBroadcastBinary) {
        String[] strings = wholeBroadcastBinary.split("\\.");
        StringBuilder sb = new StringBuilder(40);
        for (int j = 0; j < strings.length; j++) {
            String s = Integer.valueOf(strings[j], 2).toString();
            sb.append(s).append(".");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    /**
     * 按8位分割二进制字符串
     *
     * @param broadcastBinary
     * @return
     */
    public static String spiltBinary(String broadcastBinary) {
        StringBuilder stringBuilder = new StringBuilder(40);
        char[] chars = broadcastBinary.toCharArray();
        int count = 0;
        for (int j = 0; j < chars.length; j++) {
            if (count == 8) {
                stringBuilder.append(".");
                count = 0;
            }
            stringBuilder.append(chars[j]);
            count++;
        }
        return stringBuilder.toString();
    }

    /**
     * 得到广播地址的二进制码
     *
     * @param ipBinary
     * @param subnetBinary
     * @return
     */
    public static String getBroadcastBinary(String ipBinary, String subnetBinary) {
        int i = subnetBinary.lastIndexOf('1');
        String broadcastIPBinary = ipBinary.substring(0, i + 1);
        for (int j = broadcastIPBinary.length(); j < 32; j++) {
            broadcastIPBinary = broadcastIPBinary + "1";
        }
        return broadcastIPBinary;
    }

    /**
     * 转二进制
     *
     * @param content
     * @return
     */
    public static String toBinary(String content) {
        String binaryString = "";
        String[] ipSplit = content.split("\\.");
        for (String split : ipSplit) {
            String s = Integer.toBinaryString(Integer.valueOf(split));
            int length = s.length();
            for (int i = length; i < 8; i++) {
                s = "0" + s;
            }
            binaryString = binaryString + s;
        }
        return binaryString;
    }

    /**
     * 唤醒主机
     *
     * @param ip         主机ip
     * @param port       端口
     * @param mac        主机mac
     * @param subnetMask 主机子网掩码
     */
    public static OperInfo wakeUpDevice(String ip, int port, String mac, String subnetMask) {
        ip = ip.trim();
        mac = mac.trim();
        subnetMask = subnetMask.trim();
        String broadcastAddress = getBroadcastAddress(ip, subnetMask);
        mac = mac.replace("-", "");
        mac = mac.replace(":", "");
        return wakeBy(broadcastAddress, mac, port);
    }

    /**
     * 网络唤醒
     *
     * @param ip   主机ip
     * @param mac  主机mac
     * @param port 端口
     */
    public static OperInfo wakeBy(String ip, String mac, int port) {
        OperInfo oi = new OperInfo();
        //构建magic魔术包
        String MagicPacage = "FFFFFFFFFFFF";
        for (int i = 0; i < 16; i++) {
            MagicPacage += mac;
        }
        byte[] MPBinary = hexStr2BinArr(MagicPacage);
        try {
            InetAddress address = InetAddress.getByName(ip);
            DatagramSocket socket = new DatagramSocket(port);
            DatagramPacket packet = new DatagramPacket(MPBinary, MPBinary.length, address, port);
            //发送udp数据包到广播地址
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            oi.info = e.getMessage();
            oi.state = 1;
        }
        return oi;
    }

    /**
     * @param hexString
     * @return
     */
    public static byte[] hexStr2BinArr(String hexString) {
        String hexStr = "0123456789ABCDEF";
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;
        byte low = 0;
        for (int i = 0; i < len; i++) {
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);
        }
        return bytes;
    }
}