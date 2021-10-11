package virtuoel.discarnate.tileentity;

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
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.block.BlockSpiritChanneler;
import virtuoel.discarnate.init.TaskRegistrar;
import virtuoel.discarnate.init.TileEntityRegistrar;
import virtuoel.discarnate.mixin.MobEntityAccessor;
import virtuoel.discarnate.reference.DiscarnateConfig;

public class TileEntitySpiritChanneler extends LockableContainerBlockEntity implements SidedInventory
{
	@Override
	public void cancelRemoval()
	{
		super.cancelRemoval();
		World w = getWorld();
		if(!w.isClient)
		{
			deactivate();
		}
	}
	
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
		synchronized(this)
		{
			if(taskThread == null)
			{
				World w = getWorld();
				if(player == null || !canPlayerStart(player))
				{
					w.playSound(null, player == null ? getPos() : player.getBlockPos(), SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.BLOCKS, 0.5F, (RAND.nextFloat() - RAND.nextFloat()) * 0.2F + 1.0F);
					return false;
				}
				onPlayerStart(player);
				
				taskThread = new Thread(() ->
				{
					for(int i = 0; i < inventory.size(); i++)
					{
						if(player != null && canPlayerContinue(player) && isActive())
						{
							ItemStack stack = inventory.get(i);
							if(!stack.isEmpty())
							{
								TaskRegistrar.REGISTRY.getOrEmpty(Registry.ITEM.getId(stack.getItem())).ifPresent(task -> task.accept(stack, player, this));
							}
						}
						else
						{
							break;
						}
					}
					
					onPlayerStop(player);
					
					if(w != null)
					{
						Optional.ofNullable(w.getServer()).ifPresent(s ->
						{
							s.execute(() ->
							{
								deactivate();
							});
						});
					}
				}, "SpiritChannelerTasks");
				
				if(w != null)
				{
					marker = EntityType.VEX.create(w);
					setupMarkerVex(marker, w, getPos(), player);
					w.spawnEntity(marker);
					
					BlockPos pos = getPos();
					if(w.isChunkLoaded(pos))
					{
						BlockState state = w.getBlockState(pos);
						if(state.contains(BlockSpiritChanneler.ACTIVE) && !state.get(BlockSpiritChanneler.ACTIVE))
						{
							w.setBlockState(getPos(), state.with(BlockSpiritChanneler.ACTIVE, true));
						}
						w.playSound(null, player == null ? getPos() : player.getBlockPos(), SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.BLOCKS, 0.5F, (RAND.nextFloat() - RAND.nextFloat()) * 0.2F + 1.0F);
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
				if(w.isChunkLoaded(pos))
				{
					BlockState state = w.getBlockState(pos);
					if(state.contains(BlockSpiritChanneler.ACTIVE) && state.get(BlockSpiritChanneler.ACTIVE))
					{
						w.setBlockState(getPos(), state.with(BlockSpiritChanneler.ACTIVE, false));
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
	
	protected boolean canPlayerStart(@NotNull PlayerEntity player)
	{
		return canPlayerContinue(player) && !player.isDead() && (player.isCreative() || (player.experienceLevel >= DiscarnateConfig.minExpLevel && player.experienceLevel >= DiscarnateConfig.expLevelCost && (!DiscarnateConfig.requirePumpkinToStart || isWearingPumpkin(player))));
	}
	
	protected boolean canPlayerContinue(@NotNull PlayerEntity player)
	{
		return !player.isDead() && (player.isCreative() || !DiscarnateConfig.requirePumpkinToContinue || isWearingPumpkin(player));
	}
	
	protected void onPlayerStart(@NotNull PlayerEntity player)
	{
		player.addExperienceLevels(-DiscarnateConfig.expLevelCost);
	}
	
	private static final Task RESET_CHANNELER_TASK = TaskRegistrar.REGISTRY.get(Discarnate.id("reset_channeler_task"));
	
	protected void onPlayerStop(@NotNull PlayerEntity player)
	{
		if(RESET_CHANNELER_TASK != null)
		{
			RESET_CHANNELER_TASK.accept(ItemStack.EMPTY, player, this);
		}
		
	}
	
	protected static boolean isWearingPumpkin(@NotNull PlayerEntity player)
	{
		return player.getEquippedStack(EquipmentSlot.HEAD).getItem() == Item.fromBlock(Blocks.PUMPKIN);
	}
	
	protected void setupMarkerVex(VexEntity marker, @NotNull World w, BlockPos pos, PlayerEntity player)
	{
		MobEntityAccessor m = (MobEntityAccessor) marker;
		GoalSelector selector = m.getGoalSelector();
		
		selector.add(0, new Goal()
		{
			@Override
			public boolean canStart()
			{
				return TileEntitySpiritChanneler.this.marker != null;
			}
			
			@Override
			public void start()
			{
				if(marker != null)
				{
					if(marker.hasPassengers())
					{
						marker.removeAllPassengers();;
					}
					
					if(marker.hasVehicle())
					{
						marker.dismountVehicle();
					}
					
					marker.setLifeTicks(2);
					marker.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1, 5, false, false));
					marker.setCharging(marker.getMoveControl().isMoving());
				}
			}
		});
		
		Goal follow = new Goal()
		{
			@Override
			public boolean canStart()
			{
				return player != null;
			}
			
			@Override
			public void tick()
			{
				if(player != null && marker != null)
				{
					marker.lookAtEntity(player, 360, 360);
					marker.setHeadYaw(player.getHeadYaw());
					double yaw = -Math.toRadians(player.getHeadYaw() + 180D);
					marker.getNavigation().startMovingTo(player.getX() + (Math.sin(yaw) * 1.25 * player.getWidth()), player.getY() + player.getStandingEyeHeight() + 0.5D, player.getZ() + (Math.cos(yaw) * 1.25 * player.getWidth()), 1.0D);
				}
			}
		};
		
		selector.add(1, follow);
		marker.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1000000, 0, true, true));
		marker.setHealth(0.1F);
		final Vec3d vel = marker.getVelocity();
		marker.setVelocity(vel.getX(), 0.25D, vel.getZ());
		marker.refreshPositionAndAngles(pos, 0.0F, 0.0F);
		m.callInitEquipment(w.getLocalDifficulty(pos));
		m.getTargetSelector().getGoals().clear();
		marker.setBounds(pos.up());
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
			if(w.isChunkLoaded(pos))
			{
				BlockState state = w.getBlockState(pos);
				if(state.contains(BlockSpiritChanneler.ACTIVE))
				{
					return state.get(BlockSpiritChanneler.ACTIVE);
				}
			}
		}
		return false;
	}
	
	private static final int[] NO_SLOTS = new int[]{};
	private DefaultedList<ItemStack> inventory;

	public TileEntitySpiritChanneler(BlockPos blockPos, BlockState blockState)
	{
		super(TileEntityRegistrar.SPIRIT_CHANNELER, blockPos, blockState);
		this.inventory = DefaultedList.ofSize(25, ItemStack.EMPTY);
	}
	
	@Override
	protected Text getContainerName() {
		return new TranslatableText("container." + Discarnate.MOD_ID + ".spirit_channeler");
	}

	@Override
	public int size() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		Iterator<ItemStack> var1 = this.inventory.iterator();

		ItemStack itemStack;
		do {
			if (!var1.hasNext()) {
				return true;
			}

			itemStack = (ItemStack)var1.next();
		} while(itemStack.isEmpty());

		return false;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.inventory);
		return nbt;
	}

	@Override
	public ItemStack getStack(int slot) {
		return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.inventory, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if (slot >= 0 && slot < this.inventory.size()) {
			this.inventory.set(slot, stack);
		}

	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return !(player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
		}
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return NO_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return false;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new BrewingStandScreenHandler(syncId, playerInventory, this, null);
	}
}
