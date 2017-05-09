package org.organet.commons.gabriel;

import java.net.*;
import java.util.*;

public class Helper {
  public static final int ELECTION_SCORE_THRESHOLD = 5;

  private static final NetworkInterface adhocInterface;
  private static final byte[] rawMACAddress; // The MAC address of the ad-hoc network interface card
  private static final Inet4Address rawIPAddress; // The IP address of this very node on the ad-hoc network
  private static final InetAddress rawBroadcastAddress;
  private static final String macAddress;
  private static final String ipAddress;
  private static final String broadcastAddress;

  static {
    adhocInterface = obtainAdhocInterface();
    rawMACAddress = obtainMACAddress();
    rawIPAddress = obtainIPAddress();
    rawBroadcastAddress = obtainBroadcastAddress();

    macAddress = convertMACAddress(rawMACAddress);
    ipAddress = stringifyInetAddress(rawIPAddress);
    broadcastAddress = stringifyInetAddress(rawBroadcastAddress);
  }

  private static NetworkInterface obtainAdhocInterface() {
    Enumeration<NetworkInterface> interfaces;
    List<NetworkInterface> adhocInterfaceCandidates = new ArrayList<>();

    try {
      interfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      System.exit(1);
      return null;
    }

    int interfacesCount = 0;

    while (interfaces.hasMoreElements()) {
      NetworkInterface theInterface = interfaces.nextElement();

      interfacesCount += 1;

      // The interface MUST NOT be loopback
      try {
        if (theInterface.isLoopback()) {
          continue; // Continue with the next interface since this one is loopback
        }
      } catch (SocketException e) {
        continue; // Continue with the next interface since this interface is erroneous
      }

      // The interface MUST be a physical one
      if (theInterface.isVirtual()) {
        continue;
      }

      adhocInterfaceCandidates.add(theInterface);
    }

    if (adhocInterfaceCandidates.isEmpty()) {
      System.exit(2);
      return null;
    }

    return electAdhocInterface(adhocInterfaceCandidates);
  }

  private static NetworkInterface electAdhocInterface(List<NetworkInterface> candidateInterfaces) {
    SortedMap<Integer, NetworkInterface> interfacesAndScores = new TreeMap<>();

    for (NetworkInterface candidate : candidateInterfaces) {
      int score = 0;

      char[] candidateNameCharacters = candidate.getName().toCharArray();
      if (candidateNameCharacters[0] == 'w') {
        score += 1;
      }
      if (candidateNameCharacters[1] == 'l') {
        score += 1;
      }
      if (candidateNameCharacters[2] == 'a' || candidateNameCharacters[2] == 'p') {
        score += 1;
      }
      if (candidateNameCharacters[2] == 'n') {
        score += 1;
      }

      if (candidateNameCharacters[candidateNameCharacters.length - 1] >= '0' &&
        candidateNameCharacters[candidateNameCharacters.length - 1] <= '9') {
        score += 1;
      }

      if (candidate.getInterfaceAddresses().size() > 0) {
        score += 1;
      }

      // Check if the interface has IPv4 address bound
      Enumeration<InetAddress> addresses = candidate.getInetAddresses();

      while (addresses.hasMoreElements()) {
        InetAddress theAddress = addresses.nextElement();

        if (theAddress instanceof Inet4Address) {
          score += 1;

          // TODO Check if the address starts with "192.168"
          String[] theAddressOctets = stringifyInetAddress(theAddress).split("\\.");

          if (theAddressOctets[0].equals("192")) {
            score += 1;
          }
          if (theAddressOctets[0].equals("168")) {
            score += 1;
          }
        }
      }

      interfacesAndScores.put(score, candidate);
    }

    if (((Integer) ((TreeMap) interfacesAndScores).lastEntry().getKey()) <= ELECTION_SCORE_THRESHOLD) {

      System.exit(5);
    }

    NetworkInterface electedInterface = (NetworkInterface) ((TreeMap) interfacesAndScores).lastEntry().getValue();

    // Last entry has the highest score
    return (electedInterface);
  }

  private static byte[] obtainMACAddress() {
    try {
      return adhocInterface.getHardwareAddress();
    } catch (SocketException e) {
      System.exit(3);
      return null;
    }
  }

  private static Inet4Address obtainIPAddress() {
    Enumeration<InetAddress> addresses = adhocInterface.getInetAddresses();

    while (addresses.hasMoreElements()) {
      InetAddress theAddress = addresses.nextElement();

      if (theAddress instanceof Inet4Address) {
        // TODO Do more checks on the addresses if necessary

        return (Inet4Address) theAddress;
      }
    }

    return null;
  }

  private static InetAddress obtainBroadcastAddress() {
    List<InterfaceAddress> addresses = adhocInterface.getInterfaceAddresses();

    for (InterfaceAddress theAddress : addresses) {
      if (theAddress.getAddress().equals(rawIPAddress)) {
        return theAddress.getBroadcast();
      }
    }

    return null;
  }

  private static String convertMACAddress(byte[] macAddress) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0, len = macAddress.length; i < len; i++) {
      sb.append(String.format("%02X%s", macAddress[i], (i < len - 1) ? "-" : ""));
    }

    return sb.toString();
  }

  private static String stringifyInetAddress(InetAddress ipAddress) {
    if (ipAddress == null) {
      return null;
    }

    if (ipAddress instanceof Inet6Address) {
      return ipAddress.getHostAddress();
    } else {
      return ipAddress.toString().replaceAll("^/+", "");
    }
  }

  public static String getMACAddress() {
    return macAddress;
  }

  public static String getIPAddress() {
    return ipAddress;
  }

  public static String getBroadcastAddress() {
    return broadcastAddress;
  }
}
