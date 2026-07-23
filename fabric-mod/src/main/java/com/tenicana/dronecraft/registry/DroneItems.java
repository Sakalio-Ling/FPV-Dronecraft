package com.tenicana.dronecraft.registry;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import com.tenicana.dronecraft.FpvDronecraftMod;
import com.tenicana.dronecraft.item.DroneControllerItem;

public final class DroneItems {
	public static final Item PROPELLER = component("propeller");
	public static final Item BRUSHLESS_MOTOR = component("brushless_motor");
	public static final Item ESC = component("esc");
	public static final Item FLIGHT_CONTROLLER = component("flight_controller");
	public static final Item LIPO_BATTERY = component("lipo_battery");
	public static final Item CARBON_FRAME = component("carbon_frame");
	public static final Item FPV_DRONE = register(
			"fpv_drone",
			Item::new,
			new Item.Properties().stacksTo(1)
	);
	public static final Item DRONE_CONTROLLER = register(
			"drone_controller",
			DroneControllerItem::new,
			new Item.Properties().stacksTo(1)
	);

	public static final ResourceKey<CreativeModeTab> DRONE_TAB_KEY = ResourceKey.create(
			BuiltInRegistries.CREATIVE_MODE_TAB.key(),
			Identifier.fromNamespaceAndPath(FpvDronecraftMod.MOD_ID, "dronecraft")
	);

	public static final CreativeModeTab DRONE_TAB = FabricItemGroup.builder()
			.icon(() -> new ItemStack(DRONE_CONTROLLER))
			.title(Component.translatable("creativeTab.fpvdrone"))
			.displayItems((parameters, output) -> {
				output.accept(PROPELLER);
				output.accept(BRUSHLESS_MOTOR);
				output.accept(ESC);
				output.accept(FLIGHT_CONTROLLER);
				output.accept(LIPO_BATTERY);
				output.accept(CARBON_FRAME);
				output.accept(FPV_DRONE);
				output.accept(DRONE_CONTROLLER);
			})
			.build();

	private DroneItems() {
	}

	private static Item component(String name) {
		return register(name, Item::new, new Item.Properties());
	}

	public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(FpvDronecraftMod.MOD_ID, name));
		T item = itemFactory.apply(settings.setId(key));
		Registry.register(BuiltInRegistries.ITEM, key, item);
		return item;
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, DRONE_TAB_KEY, DRONE_TAB);
	}
}
