package virtuoel.discarnate.tileentity;

import java.util.Optional;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import virtuoel.discarnate.Discarnate;

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
				
				Optional.ofNullable(getWorld()).ifPresent(world ->
				{
					IBlockState state = world.getBlockState(getPos());
					getWorld().notifyBlockUpdate(getPos(), state, state, 3);
				});
			}
		};
	}
	
	public TileEntitySpiritChanneler()
	{
		this(25);
	}
	
	public boolean activate(EntityPlayer player)
	{
		// TODO do stuff
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
