package org.hive2hive.core.test.network.messages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.tomp2p.peers.Number160;

import org.hive2hive.core.H2HConstants;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.model.UserPublicKey;
import org.hive2hive.core.network.NetworkManager;
import org.hive2hive.core.security.EncryptionUtil;
import org.hive2hive.core.test.H2HJUnitTest;
import org.hive2hive.core.test.network.NetworkTestUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Simple test to test message signatures.
 * 
 * @author Seppi, Nico
 */
public class MessageSignatureTest extends H2HJUnitTest {

	private List<NetworkManager> network;

	@BeforeClass
	public static void initTest() throws Exception {
		testClass = BaseMessageTest.class;
		beforeClass();
	}

	@Before
	public void createNetwork() {
		network = NetworkTestUtil.createNetwork(2);
	}

	@Test
	public void testMessageWithSignatureSameUser() throws NoPeerConnectionException {
		NetworkTestUtil.createSameKeyPair(network);
		NetworkManager sender = network.get(0);
		NetworkManager receiver = network.get(1);

		// putting of the public key is not necessary

		// location key is target node id
		String locationKey = receiver.getNodeId();

		// create a message with target node B
		TestSignedMessage message = new TestSignedMessage(locationKey);

		// send message
		assertTrue(sender.getMessageManager().send(message, receiver.getPublicKey()));
	}

	@Test
	public void testMessageWithSignatureDifferentUser() throws NoPeerConnectionException {
		NetworkTestUtil.createKeyPairs(network);
		NetworkManager sender = network.get(0);
		NetworkManager receiver = network.get(1);

		// put the public key of the sender into the network
		sender.getDataManager()
				.put(Number160.createHash(sender.getUserId()), H2HConstants.TOMP2P_DEFAULT_KEY,
						Number160.createHash(H2HConstants.USER_PUBLIC_KEY),
						new UserPublicKey(sender.getPublicKey()), null).awaitUninterruptibly();

		// location key is target node id
		String locationKey = receiver.getNodeId();

		// create a message with target node B
		TestSignedMessage message = new TestSignedMessage(locationKey);

		// send message
		assertTrue(sender.getMessageManager().send(message, receiver.getPublicKey()));
	}

	@Test
	public void testMessageWithWrongSignature1() throws NoPeerConnectionException {
		NetworkTestUtil.createKeyPairs(network);
		NetworkManager sender = network.get(0);
		NetworkManager receiver = network.get(1);

		// don't upload the sender public key

		// location key is target node id
		String locationKey = receiver.getNodeId();

		// create a message with target node B, assign random public key
		TestSignedMessage message = new TestSignedMessage(locationKey);

		// send message
		assertFalse(sender.getMessageManager().send(message, receiver.getPublicKey()));
	}

	@Test
	public void testMessageWithWrongSignature2() throws NoPeerConnectionException {
		NetworkTestUtil.createKeyPairs(network);
		NetworkManager sender = network.get(0);
		NetworkManager receiver = network.get(1);

		// put a wrong public key of the sender into the network
		sender.getDataManager()
				.put(Number160.createHash(sender.getUserId()),
						H2HConstants.TOMP2P_DEFAULT_KEY,
						Number160.createHash(H2HConstants.USER_PUBLIC_KEY),
						new UserPublicKey(EncryptionUtil.generateRSAKeyPair(H2HConstants.KEYLENGTH_USER_KEYS)
								.getPublic()), null).awaitUninterruptibly();

		// location key is target node id
		String locationKey = receiver.getNodeId();

		// create a message with target node B, assign random public key
		TestSignedMessage message = new TestSignedMessage(locationKey);

		// send message
		assertFalse(sender.getMessageManager().send(message, receiver.getPublicKey()));
	}

	@After
	public void shutdownNetwork() {
		NetworkTestUtil.shutdownNetwork(network);
	}

	@AfterClass
	public static void endTest() {
		afterClass();
	}
}
