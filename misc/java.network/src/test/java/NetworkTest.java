import org.junit.Test;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by ziv on 2017/3/25.
 */
public class NetworkTest {

	@Test
	public void test_printAllIpv4() throws SocketException {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				InetAddress inetAddress = inetAddresses.nextElement();
				byte[] address = inetAddress.getAddress();
				if (null != address && address.length == 4) {
					System.out.println(String.format("%s: '%s'", networkInterface.getDisplayName(), inetAddress.getHostAddress()));
				}
			}
		}
	}

	@Test
	public void test_printAllInterface() throws SocketException {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		if (null != networkInterfaces) {
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				System.out.println("Name: " + networkInterface.getName());
				System.out.println("DisplayName: " + networkInterface.getDisplayName());
				System.out.println("HardwareAddress: " + Arrays.toString(networkInterface.getHardwareAddress()));
				System.out.println("index: " + networkInterface.getIndex());
				System.out.println("MTU: " + networkInterface.getMTU());
				System.out.println("Parent: " + networkInterface.getParent());

				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				System.out.println("InetAddresses: " + inetAddresses);
				while (inetAddresses.hasMoreElements()) {
					printInetAddress(inetAddresses.nextElement());
				}

				List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
				System.out.println("InterfaceAddresses: " + interfaceAddresses);
				if (null != interfaceAddresses) {
					for (InterfaceAddress interfaceAddress : interfaceAddresses) {
						InetAddress address = interfaceAddress.getAddress();
						InetAddress broadcast = interfaceAddress.getBroadcast();
						System.out.println("\tnetworkPrefixLength: " + interfaceAddress.getNetworkPrefixLength());
						printInetAddress(address);
						printInetAddress(broadcast);
					}
				}

				System.out.println("======================");
			}
		}
	}

	private void printInetAddress(InetAddress inetAddress) {
		if (null == inetAddress) {
			return;
		}
		String hostName = inetAddress.getHostName();
		String hostAddress = inetAddress.getHostAddress();
		String canonicalHostName = inetAddress.getCanonicalHostName();
		byte[] address = inetAddress.getAddress();
		System.out.println(String.format("\thostname: '%s', hostaddress: '%s', canonicalHostName: '%s', address: '%s'", hostName, hostAddress, canonicalHostName, Arrays.toString(address)));
	}

}
