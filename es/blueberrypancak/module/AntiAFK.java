package es.blueberrypancak.module;

import java.util.Random;

import es.blueberrypancak.Client;
import es.blueberrypancak.event.EventRender;
import es.blueberrypancak.event.Subscribe;
import es.blueberrypancak.hook.EntityPlayerSPHook;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;

@RegisterModule(key=49,color=0x665EFF,listed=true)
public class AntiAFK extends Module {

	private long lastPacket;
	
	private int nextDelay = 60;
	
	@Subscribe
	public void onRender(EventRender e) {
		if(lastPacket == 0) lastPacket = System.currentTimeMillis();
		if(isEnabled()) {
			if(getElapsed() >= nextDelay) {
				lastPacket = System.currentTimeMillis();
				EntityPlayerSPHook player = (EntityPlayerSPHook) Client.getMinecraft().thePlayer;
				player.getConnection().sendPacket(new CPacketHeldItemChange(new Random().nextInt(9)));
				player.getConnection().sendPacket(new CPacketPlayer.Rotation(new Random().nextFloat()*360, new Random().nextFloat()*-40, true));
				player.getConnection().sendPacket(new CPacketPlayer.Rotation(player.rotationYaw, player.rotationPitch, true));
				player.getConnection().sendPacket(new CPacketHeldItemChange(player.inventory.currentItem));
			}
		}
	}
	
	private int getElapsed() {
		return (int)(System.currentTimeMillis()-lastPacket)/1000;
	}
	
	@Override
	public void onEnabled() {
		lastPacket = System.currentTimeMillis();
	}

	@Override
	public void onDisabled() {
		
	}

	@Override
	public String getName() {
		return "AntiAFK \2479" + (getElapsed());
	}
}