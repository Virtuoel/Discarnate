package virtuoel.discarnate.block.entity;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.DiscarnateConfig;
import virtuoel.discarnate.block.SpiritChannelerBlock;
import virtuoel.discarnate.init.BlockEntityRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;
import virtuoel.discarnate.mixin.MobEntityAccessor;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;

public class SpiritChannelerBlockEntity extends LockableContainerBlockEntity implements SidedInventory, ScreenHandlerFactory
{
	@Override
	public void markRemoved()
	{
		deactivate();
		super.markRemoved();
	}
	
	private static final Random RAND = new Random();
	
	@Nullable
	VexEntity marker = null;
	
	@Nullable
	Thread taskThread = null;
	
	public boolean activate(PlayerEntity player)
	{
		synchronized (this)
		{
			if (taskThread == null)
			{
				World w = getWorld();
				boolean hasWorld = w != null;
				if (player == null || !canPlayerStart(player))
				{
					if (hasWorld)
					{
						w.playSound(null, player == null ? getPos() : player.getBlockPos(), SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.BLOCKS, 0.5F, (RAND.nextFloat() - RAND.nextFloat()) * 0.2F + 1.0F);
					}
					
					propertyDelegate.set(0, 0);
					
					return false;
				}
				onPlayerStart(player);
				
				BlockPos pos = getPos();
				
				taskThread = new Thread(() ->
				{
					try
					{
						Thread.sleep(250);
					}
					catch (InterruptedException e)
					{
						
					}
					
					if (hasWorld)
					{
						Optional.ofNullable(w.getServer()).ifPresent(s ->
						{
							s.execute(() ->
							{
								w.spawnEntity(marker = setupMarkerVex(EntityType.VEX.create(w), w, pos, player));
								w.playSound(null, pos, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.BLOCKS, 0.5F, 1.0F);
								w.playSound(null, pos, SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.BLOCKS, 0.5F, (RAND.nextFloat() - RAND.nextFloat()) * 0.2F + 1.0F);
							});
						});
					}
					
					for (int i = 0; i < inventory.size(); i++)
					{
						if (player != null && canPlayerContinue(player) && isActive())
						{
							ItemStack stack = inventory.get(i);
							if (!stack.isEmpty())
							{
								Optional.ofNullable(TaskRegistrar.REGISTRY.get().getValue(stack.getItem().getRegistryName())).ifPresent(task -> task.accept(stack, player, this));
							}
						}
						else
						{
							break;
						}
					}
					
					onPlayerStop(player);
					
					if (hasWorld)
					{
						Optional.ofNullable(w.getServer()).ifPresent(s ->
						{
							s.execute(this::deactivate);
						});
					}
				}, "SpiritChannelerTasks");
				
				if (hasWorld)
				{
					w.playSound(null, pos, SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.BLOCKS, 0.5F, 1.0F);
					if (w.isChunkLoaded(pos))
					{
						BlockState state = w.getBlockState(pos);
						if (state.contains(SpiritChannelerBlock.ACTIVE) && !state.get(SpiritChannelerBlock.ACTIVE))
						{
							w.setBlockState(getPos(), state.with(SpiritChannelerBlock.ACTIVE, true));
						}
					}
				}
				
				taskThread.start();
				
				propertyDelegate.set(0, 1);
				
				return true;
			}
			
			propertyDelegate.set(0, 0);
			
			return false;
		}
	}
	
	public boolean deactivate()
	{
		synchronized (this)
		{
			World w = getWorld();
			if (w != null)
			{
				BlockPos pos = getPos();
				if (w.isChunkLoaded(pos))
				{
					BlockState state = w.getBlockState(pos);
					if (state.contains(SpiritChannelerBlock.ACTIVE) && state.get(SpiritChannelerBlock.ACTIVE))
					{
						w.setBlockState(getPos(), state.with(SpiritChannelerBlock.ACTIVE, false));
					}
				}
			}
			
			propertyDelegate.set(0, 0);
			
			if (taskThread != null)
			{
				taskThread.interrupt();
				taskThread = null;
				
				marker = null;
				
				return true;
			}
			
			marker = null;
			
			return false;
		}
	}
	
	public static boolean canPlayerStart(@NotNull PlayerEntity player)
	{
		return canPlayerContinue(player) && !player.isDead() && (player.isCreative() || (player.experienceLevel >= DiscarnateConfig.COMMON.minLevel.get() && player.experienceLevel >= DiscarnateConfig.COMMON.levelCost.get() && (!DiscarnateConfig.COMMON.pumpkinToStart.get() || isWearingPumpkin(player))));
	}
	
	public static boolean canPlayerContinue(@NotNull PlayerEntity player)
	{
		return !player.isDead() && (player.isCreative() || !DiscarnateConfig.COMMON.pumpkinToContinue.get() || isWearingPumpkin(player));
	}
	
	public static void onPlayerStart(@NotNull PlayerEntity player)
	{
		if (player instanceof ServerPlayerEntity)
		{
			((ServerPlayerEntity) player).closeHandledScreen();
		}
		
		player.addExperienceLevels(-DiscarnateConfig.COMMON.levelCost.get());
	}
	
	private static final Identifier RESET_CHANNELER_TASK_ID = Discarnate.id("reset_channeler_task");
	
	protected void onPlayerStop(@NotNull PlayerEntity player)
	{
		TaskRegistrar.REGISTRY.get().getValue(RESET_CHANNELER_TASK_ID).accept(ItemStack.EMPTY, player, this);
	}
	
	protected static boolean isWearingPumpkin(@NotNull PlayerEntity player)
	{
		return player.getEquippedStack(EquipmentSlot.HEAD).getItem() == Blocks.CARVED_PUMPKIN.asItem();
	}
	
	protected VexEntity setupMarkerVex(VexEntity marker, @NotNull World w, BlockPos pos, PlayerEntity player)
	{
		final Goal visuals = new Goal()
		{
			@Override
			public boolean canStart()
			{
				return SpiritChannelerBlockEntity.this.marker != null;
			}
			
			@Override
			public void tick()
			{
				if (marker != null)
				{
					if (marker.hasPassengers())
					{
						marker.removeAllPassengers();;
					}
					
					if (marker.hasVehicle())
					{
						marker.dismountVehicle();
					}
					
					marker.setLifeTicks(2);
					marker.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1, 10, false, false));
					marker.setCharging(marker.getMoveControl().isMoving());
				}
			}
		};
		
		final Goal follow = new Goal()
		{
			@Override
			public boolean canStart()
			{
				return player != null;
			}
			
			@Override
			public void tick()
			{
				if (player != null && marker != null)
				{
					marker.lookAtEntity(player, 360, 360);
					marker.setHeadYaw(player.getHeadYaw());
					double yaw = -Math.toRadians(player.getHeadYaw() + 180D);
					marker.getMoveControl().moveTo(player.getX() + (Math.sin(yaw) * 1.25 * player.getWidth()), player.getY() + player.getStandingEyeHeight() + 0.5D, player.getZ() + (Math.cos(yaw) * 1.25 * player.getWidth()), 1.0D);
				}
			}
		};
		
		MobEntityAccessor m = (MobEntityAccessor) marker;
		GoalSelector selector = m.getGoalSelector();
		
		marker.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1000000, 0, true, true));
		marker.setHealth(0.1F);
		marker.setLifeTicks(2);
		m.setExperiencePoints(0);
		final Vec3d vel = marker.getVelocity();
		marker.setVelocity(vel.getX(), 0.25D, vel.getZ());
		marker.refreshPositionAndAngles(pos, 0.0F, 0.0F);
		if (w instanceof ServerWorld)
		{
			marker.initialize((ServerWorld) w, w.getLocalDifficulty(pos), SpawnReason.MOB_SUMMONED, null, null);
		}
		marker.setBounds(pos.up());
		
		m.getTargetSelector().getGoals().clear();
		
		selector.clear();
		selector.add(0, visuals);
		selector.add(1, follow);
		
		return marker;
	}
	
	public boolean isActive()
	{
		synchronized (this)
		{
			return isActive(getCachedState());
		}
	}
	
	public static boolean isActive(@Nullable World w, BlockPos pos)
	{
		if (w != null && w.isChunkLoaded(pos))
		{
			return isActive(w.getBlockState(pos));
		}
		
		return false;
	}
	
	public static boolean isActive(BlockState state)
	{
		if (state.contains(SpiritChannelerBlock.ACTIVE))
		{
			return state.get(SpiritChannelerBlock.ACTIVE);
		}
		
		return false;
	}
	
	private static final int[] NO_SLOTS = new int[0];
	private DefaultedList<ItemStack> inventory;
	protected final PropertyDelegate propertyDelegate;
	
	public SpiritChannelerBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		super(BlockEntityRegistrar.SPIRIT_CHANNELER.get(), blockPos, blockState);
		this.inventory = DefaultedList.ofSize(25, ItemStack.EMPTY);
		this.propertyDelegate = new ArrayPropertyDelegate(1);
		this.propertyDelegate.set(0, isActive(blockState) ? 1 : 0);
	}
	
	@Override
	protected Text getContainerName()
	{
		return new TranslatableText("container." + Discarnate.MOD_ID + ".spirit_channeler");
	}
	
	@Override
	public int size()
	{
		return this.inventory.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		Iterator<ItemStack> var1 = this.inventory.iterator();
		
		ItemStack itemStack;
		do
		{
			if (!var1.hasNext())
			{
				return true;
			}
			
			itemStack = (ItemStack) var1.next();
		}
		while (itemStack.isEmpty());
		
		return false;
	}
	
	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory);
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.inventory);
		return nbt;
	}
	
	@Override
	public ItemStack getStack(int slot)
	{
		return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount)
	{
		return Inventories.splitStack(this.inventory, slot, amount);
	}
	
	@Override
	public ItemStack removeStack(int slot)
	{
		return Inventories.removeStack(this.inventory, slot);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack)
	{
		if (slot >= 0 && slot < this.inventory.size())
		{
			this.inventory.set(slot, stack);
		}
		
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player)
	{
		if (this.world.getBlockEntity(this.pos) != this)
		{
			return false;
		}
		else
		{
			return !(player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) > 64.0D);
		}
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack)
	{
		return false;
	}
	
	@Override
	public int[] getAvailableSlots(Direction side)
	{
		return NO_SLOTS;
	}
	
	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir)
	{
		return false;
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir)
	{
		return false;
	}
	
	@Override
	public void clear()
	{
		this.inventory.clear();
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory)
	{
		return new SpiritChannelerScreenHandler(syncId, playerInventory, this, propertyDelegate, ScreenHandlerContext.create(getWorld(), getPos()));
	}
}
