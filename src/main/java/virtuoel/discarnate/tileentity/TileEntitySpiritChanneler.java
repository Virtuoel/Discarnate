package virtuoel.discarnate.tileentity;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper.Action;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.block.BlockSpiritChanneler;
import virtuoel.discarnate.init.TaskRegistrar;

public class TileEntitySpiritChanneler extends TileEntity
{
	IItemHandlerModifiable itemHandler;
	
	public TileEntitySpiritChanneler(int inventorySize)
	{
		itemHandler = new ItemStackHandler(inventorySize)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				TileEntitySpiritChanneler.this.markDirty();
				
				World world = getWorld();
				if(world != null)
				{
					IBlockState state = world.getBlockState(getPos());
					world.notifyBlockUpdate(getPos(), state, state, 3);
				}
			}
		};
	}
	
	public TileEntitySpiritChanneler()
	{
		this(25);
	}
	
	@Override
	public void onLoad()
	{
		if(!getWorld().isRemote)
		{
			deactivate();
		}
	}
	
	@Override
	public void invalidate()
	{
		deactivate();
		super.invalidate();
	}
	
	@ObjectHolder(Discarnate.MOD_ID + ":reset_channeler_task")
	private static final Task RESET_CHANNELER_TASK = null;
	
	@Nullable
	EntityVex marker = null;
	
	@Nullable
	Thread taskThread = null;
	
	public boolean activate(EntityPlayer player)
	{
		synchronized(this)
		{
			if(taskThread == null)
			{
				if(player != null)
				{
					if(!player.capabilities.isCreativeMode && player.experienceLevel == 0)
					{
						return false;
					}
					player.addExperienceLevel(-1);
				}
				
				World w = getWorld();
				taskThread = new Thread(() ->
				{
					for(int i = 0; i < itemHandler.getSlots(); i++)
					{
						if(player != null && !player.isDead && isActive())
						{
							ItemStack stack = itemHandler.getStackInSlot(i);
							if(!stack.isEmpty())
							{
								Optional.ofNullable(TaskRegistrar.REGISTRY.getValue(stack.getItem().getRegistryName())).ifPresent(task -> task.accept(stack, player, this));
							}
						}
						else
						{
							break;
						}
					}
					
					if(RESET_CHANNELER_TASK != null)
					{
						RESET_CHANNELER_TASK.accept(ItemStack.EMPTY, player, this);
					}
					
					if(w != null)
					{
						Optional.ofNullable(w.getMinecraftServer()).ifPresent(s ->
						{
							s.addScheduledTask(() ->
							{
								deactivate();
							});
						});
					}
				}, "SpiritChannelerTasks");
				
				if(w != null)
				{
					marker = new EntityVex(w);
					setupMarkerVex(marker, w, getPos(), player);
					w.spawnEntity(marker);
					
					BlockPos pos = getPos();
					if(w.isBlockLoaded(pos))
					{
						IBlockState state = w.getBlockState(pos);
						if(state.getPropertyKeys().contains(BlockSpiritChanneler.ACTIVE) && !state.getValue(BlockSpiritChanneler.ACTIVE))
						{
							w.setBlockState(getPos(), state.withProperty(BlockSpiritChanneler.ACTIVE, true));
						}
						w.playSound(null, player == null ? getPos() : player.getPosition(), SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.BLOCKS, 0.5F, 1.0F);
					}
				}
				
				taskThread.start();
				return true;
			}
			return false;
		}
	}
	
	public boolean deactivate()
	{
		synchronized(this)
		{
			World w = getWorld();
			if(w != null)
			{
				if(marker != null)
				{
					marker = null;
				}
				
				BlockPos pos = getPos();
				if(w.isBlockLoaded(pos))
				{
					IBlockState state = w.getBlockState(pos);
					if(state.getPropertyKeys().contains(BlockSpiritChanneler.ACTIVE) && state.getValue(BlockSpiritChanneler.ACTIVE))
					{
						w.setBlockState(getPos(), state.withProperty(BlockSpiritChanneler.ACTIVE, false));
					}
				}
			}
			
			if(taskThread != null)
			{
				taskThread.interrupt();
				taskThread = null;
				return true;
			}
			return false;
		}
	}
	
	protected void setupMarkerVex(EntityVex marker, @Nonnull World w, BlockPos pos, EntityPlayer player)
	{
		marker.tasks.addTask(0, new EntityAIBase()
		{
			@Override
			public boolean shouldExecute()
			{
				return TileEntitySpiritChanneler.this.marker != null;
			}
			
			@Override
			public void updateTask()
			{
				if(marker != null)
				{
					marker.setLimitedLife(2);
					marker.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1, 5, false, false));
					marker.setCharging(marker.getMoveHelper().action == Action.MOVE_TO);
				}
			}
		});
		
		EntityAIBase follow = new EntityAIBase()
		{
			@Override
			public boolean shouldExecute()
			{
				return player != null;
			}
			
			@Override
			public void updateTask()
			{
				if(player != null && marker != null)
				{
					marker.faceEntity(player, 360, 360);
					marker.rotationYawHead = player.rotationYawHead;
					double yaw = -Math.toRadians(player.rotationYaw + 180D);
					marker.getMoveHelper().setMoveTo(player.posX + (Math.sin(yaw) * 1.25 * player.width), player.posY + player.eyeHeight + 0.5D, player.posZ + (Math.cos(yaw) * 1.25 * player.width), 1.0D);
				}
			}
		};
		
		follow.setMutexBits(3);
		marker.tasks.addTask(1, follow);
		marker.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1000000, 0, true, true));
		marker.setHealth(0.1F);
		marker.motionY = 0.25D;
		marker.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
		marker.onInitialSpawn(w.getDifficultyForLocation(pos), null);
		marker.targetTasks.taskEntries.clear();
		marker.setBoundOrigin(pos.up());
	}
	
	public boolean isActive()
	{
		synchronized(this)
		{
			return isActive(getWorld(), getPos());
		}
	}
	
	public static boolean isActive(@Nullable World w, BlockPos pos)
	{
		if(w != null)
		{
			if(w.isBlockLoaded(pos))
			{
				IBlockState state = w.getBlockState(pos);
				if(state.getPropertyKeys().contains(BlockSpiritChanneler.ACTIVE))
				{
					return state.getValue(BlockSpiritChanneler.ACTIVE);
				}
			}
		}
		return false;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(Discarnate.MOD_ID + ".spirit_channeler");
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		if(compound.hasKey("Items"))
		{
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(itemHandler, null, compound.getTagList("Items", Constants.NBT.TAG_COMPOUND));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		
		compound.setTag("Items", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(itemHandler, null));
		
		return compound;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return (facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
		}
		return super.getCapability(capability, facing);
	}
}
