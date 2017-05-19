package com.myththewolf.MythBans.lib.player.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.myththewolf.MythBans.lib.feilds.ConfigProperties;

public class ChunkLoad implements Listener {

	public ChunkLoad() {
		// TODO Auto-generated constructor stub
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onChunkLoad(ChunkLoadEvent E) {
		Chunk C = E.getChunk();
		Entity[] ents = C.getEntities();

		List<LivingEntity> MOBS = new ArrayList<LivingEntity>();

		int numMobsFound = 0;

		idkwhatlabeltouse: for (Entity e : ents) {
			if (e instanceof LivingEntity && !(e instanceof Player) ) {
				if(e.getCustomName() != null){
					MOBS.add((LivingEntity) e);
					numMobsFound++;
					continue idkwhatlabeltouse;
				}
			} else {
				continue idkwhatlabeltouse;
			}
		}


		while(ConfigProperties.MAX_CHUNK_ENTITY_MOB > 0 && numMobsFound > ConfigProperties.MAX_CHUNK_ENTITY_MOB){
			int spot = ThreadLocalRandom.current().nextInt(0, MOBS.size())+1;
			MOBS.get(spot).remove();
			MOBS.remove(spot);
			Collections.shuffle(MOBS);
			numMobsFound--;
		}
	}
}
