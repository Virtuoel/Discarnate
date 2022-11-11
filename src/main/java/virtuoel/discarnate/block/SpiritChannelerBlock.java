package virtuoel.discarnate.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;
import virtuoel.discarnate.init.BlockEntityRegistrar;

public class SpiritChannelerBlock extends Block implements BlockEntityProvider
{
	public static final VoxelShape INDENT_SHAPE = VoxelShapes.union(
		Block.createCuboidShape(2, 0, 2, 14, 1, 14),
		Block.createCuboidShape(2, 15, 2, 14, 16, 14),
		Block.createCuboidShape(0, 2, 2, 1, 14, 14),
		Block.createCuboidShape(2, 2, 0, 14, 14, 1),
		Block.createCuboidShape(2, 2, 15, 14, 14, 16),
		Block.createCuboidShape(15, 2, 2, 16, 14, 14)
	);
	
	public static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(
		VoxelShapes.fullCube(), INDENT_SHAPE, BooleanBiFunction.NOT_SAME
	);
	
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
	
	public SpiritChannelerBlock(Block.Settings settings)
	{
		super(settings);
		
		setDefaultState(getDefaultState().with(ACTIVE, false));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder)
	{
		builder.add(ACTIVE);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return SHAPE;
	}
	
	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos)
	{
		return INDENT_SHAPE;
	}
	
	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos)
	{
		return SHAPE;
	}
	
	@Override
	public int getOpacity(BlockState state, BlockView world, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos)
	{
		return VoxelShapes.fullCube();
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if (!world.isClient)
		{
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
			return ActionResult.CONSUME;
		}
		
		return ActionResult.SUCCESS;
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		if (!state.isOf(newState.getBlock()))
		{
			final BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory)
			{
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
				world.updateComparators(pos, this);
			}
			
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos)
	{
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
	{
		if (itemStack.hasCustomName())
		{
			final BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SpiritChannelerBlockEntity)
			{
				((SpiritChannelerBlockEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}
	}
	
	@Override
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return BlockEntityRegistrar.SPIRIT_CHANNELER.instantiate(pos, state);
	}
	
	@Override
	@Nullable
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) blockEntity : null;
	}
}
