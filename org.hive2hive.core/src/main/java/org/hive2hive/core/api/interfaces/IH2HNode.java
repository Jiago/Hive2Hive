package org.hive2hive.core.api.interfaces;

import net.tomp2p.dht.PeerDHT;

/**
 * This interface represents the entry point of Hive2Hive and exposes the managers necessary for interaction.
 * 
 * @author Christian, Nico, Seppi
 * 
 */
public interface IH2HNode {

	/**
	 * Connect to the network. The connection kind is dependent on {@link INetworkConfiguration}.
	 * 
	 * @param networkConfiguration the network parameters, important to know how to bootstrap and which port
	 *            to listen to.
	 * @return <code>true</code> if the connection was successful, <code>false</code> otherwise
	 */
	boolean connect(INetworkConfiguration networkConfiguration);

	/**
	 * Disconnect the node from the network.
	 * 
	 * @return <code>true</code> if the disconnection was successful, <code>false</code> otherwise
	 */
	boolean disconnect();

	/**
	 * Checks whether this {@link IH2HNode} is connected.
	 * 
	 * @return <code>true</code> if connected, <code>false</code> otherwise
	 */
	boolean isConnected();

	/**
	 * Returns the user management. In the user management, the user can register himself, login and logout.
	 * 
	 * @return the user management
	 */
	IUserManager getUserManager();

	/**
	 * Returns the file management. In the file management, the user can (after successful login) add, update,
	 * remove files.
	 * 
	 * @return the file management
	 */
	IFileManager getFileManager();

	/**
	 * Returns the {@link IFileConfiguration} given at the node creation.
	 * 
	 * @return the file configuration
	 */
	IFileConfiguration getFileConfiguration();

	/**
	 * Returns the {@link PeerDHT} which can be used to implement custom actions.
	 * 
	 * @return the TomP2P peer. Before calling {@link IH2HNode#connect()}, this method returns null.
	 */
	PeerDHT getPeer();
}
