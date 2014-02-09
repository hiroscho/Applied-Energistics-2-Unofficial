package appeng.core.sync.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import appeng.core.WorldSettings;
import appeng.core.sync.AppEngPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class NetworkHandler
{

	public static NetworkHandler instance;

	final FMLEventChannel ec;
	final String myChannelName;

	final IPacketHandler clientHandler;
	final IPacketHandler serveHandler;

	public NetworkHandler(String channelName) {
		ec = NetworkRegistry.INSTANCE.newEventDrivenChannel( myChannelName = channelName );
		ec.register( this );

		clientHandler = new AppEngClientPacketHandler();
		serveHandler = new AppEngServerPacketHandler();
	}

	@SubscribeEvent
	public void newPlayer(PlayerLoggedInEvent ev)
	{
		WorldSettings.getInstance().sendToPlayer( ev.player );
	}

	@SubscribeEvent
	public void serverPacket(ServerCustomPacketEvent ev)
	{
		NetHandlerPlayServer srv = (NetHandlerPlayServer) ev.packet.handler();
		serveHandler.onPacketData( null, ev.packet, srv.playerEntity );
	}

	@SubscribeEvent
	public void clientPacket(ClientCustomPacketEvent ev)
	{
		clientHandler.onPacketData( null, ev.packet, null );
	}

	public String getChannel()
	{
		return myChannelName;
	}

	public void sendToAll(AppEngPacket message)
	{
		ec.sendToAll( message.getProxy() );
	}

	public void sendTo(AppEngPacket message, EntityPlayerMP player)
	{
		ec.sendTo( message.getProxy(), player );
	}

	public void sendToAllAround(AppEngPacket message, NetworkRegistry.TargetPoint point)
	{
		ec.sendToAllAround( message.getProxy(), point );
	}

	public void sendToDimension(AppEngPacket message, int dimensionId)
	{
		ec.sendToDimension( message.getProxy(), dimensionId );
	}

	public void sendToServer(AppEngPacket message)
	{
		ec.sendToServer( message.getProxy() );
	}

}