package nebulous.entity;

import nebulous.Game;

public class Instance {

	protected EntitySystem entitySystem;
	protected Game game;
	
	public Instance() {
		entitySystem = new EntitySystem();
	}
	
	public void add(Entity entity) {
		entity.setInstance(this);
		entity.init(game);
		getEntitySystem().addEntity(entity, entity.getComponents());
	}
	
	public void load(Game game) {
		this.game = game;
		EntitySystem.setInstance(this);
	}
	
	public EntitySystem getEntitySystem() {
		return entitySystem;
	}
	
}