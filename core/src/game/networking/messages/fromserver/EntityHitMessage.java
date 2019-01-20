package networking.messages.fromserver;

import entities.DamageSource;
import entities.Entity;
import networking.MessageType;
import networking.messages.Message;

public class EntityHitMessage extends Message
{
	private static final long serialVersionUID = -175246173202315055L;

	private final Entity entity;
	private final int damage;
	private final DamageSource source;
	
	public EntityHitMessage(Entity entity, int damage, DamageSource source)
	{
		super(MessageType.DAMAGE_ENTITY);
		this.entity = entity;
		this.damage = damage;
		this.source = source;
	}

	public Entity getEntity()
	{
		return entity;
	}

	public int getDamage()
	{
		return damage;
	}

	public DamageSource getSource()
	{
		return source;
	}
}