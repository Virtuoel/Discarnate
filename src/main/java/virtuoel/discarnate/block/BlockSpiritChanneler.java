package virtuoel.discarnate.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.proxy.GuiProxy;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

public class BlockSpiritChanneler extends Block
{
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	public BlockSpiritChanneler(Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(blockMaterialIn, blockMapColorIn);
		setDefaultState(getBlockState().getBaseState().withProperty(ACTIVE, false));
	}
	
	public BlockSpiritChanneler(Material materialIn)
	{
		this(materialIn, materialIn.getMaterialMapColor());
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ACTIVE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(ACTIVE, meta == 1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ACTIVE) ? 1 : 0;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		playerIn.openGui(Discarnate.instance, GuiProxy.SPIRIT_CHANNELER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySpiritChanneler();
	}
}
