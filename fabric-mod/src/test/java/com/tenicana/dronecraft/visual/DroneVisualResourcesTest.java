package com.tenicana.dronecraft.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class DroneVisualResourcesTest {
	private static final Path RESOURCE_ROOT = Path.of("src/main/resources");
	private static final Path ASSET_ROOT = RESOURCE_ROOT.resolve("assets/fpvdrone");
	private static final Path RECIPE_ROOT = RESOURCE_ROOT.resolve("data/fpvdrone/recipe");
	private static final Set<String> COMPONENTS = Set.of(
			"propeller",
			"brushless_motor",
			"esc",
			"flight_controller",
			"lipo_battery",
			"carbon_frame"
	);

	@Test
	void everyComponentHasItemDefinitionModelAndPixelTexture() throws IOException {
		Set<String> items = new HashSet<>(COMPONENTS);
		items.add("fpv_drone");
		for (String item : items) {
			Path definition = ASSET_ROOT.resolve("items/" + item + ".json");
			Path model = ASSET_ROOT.resolve("models/item/" + item + ".json");
			Path texture = ASSET_ROOT.resolve("textures/item/" + item + ".png");
			assertTrue(Files.isRegularFile(definition), item + " item definition must exist");
			assertTrue(Files.isRegularFile(model), item + " item model must exist");
			assertTrue(Files.isRegularFile(texture), item + " item texture must exist");

			JsonObject definitionJson = parse(definition);
			assertEquals(
					"fpvdrone:item/" + item,
					definitionJson.getAsJsonObject("model").get("model").getAsString()
			);
			JsonObject modelJson = parse(model);
			assertEquals(
					"fpvdrone:item/" + item,
					modelJson.getAsJsonObject("textures").get("layer0").getAsString()
			);

			BufferedImage image = ImageIO.read(texture.toFile());
			assertNotNull(image, item + " texture must be a readable PNG");
			assertEquals(16, image.getWidth(), item + " texture width");
			assertEquals(16, image.getHeight(), item + " texture height");
			assertTrue(image.getColorModel().hasAlpha(), item + " texture must preserve transparent inventory corners");
		}
	}

	@Test
	void entityAtlasMatchesRemodeledLayerDefinition() throws IOException {
		BufferedImage image = ImageIO.read(ASSET_ROOT.resolve("textures/entity/drone.png").toFile());
		assertNotNull(image);
		assertEquals(128, image.getWidth());
		assertEquals(64, image.getHeight());
	}

	@Test
	void assembledDroneRecipeUsesEveryComponentAndNoComponentHasARecipe() throws IOException {
		Path droneRecipe = RECIPE_ROOT.resolve("fpv_drone.json");
		JsonObject recipe = parse(droneRecipe);
		assertEquals("minecraft:crafting_shapeless", recipe.get("type").getAsString());
		assertEquals("fpvdrone:fpv_drone", recipe.getAsJsonObject("result").get("id").getAsString());

		JsonArray ingredients = recipe.getAsJsonArray("ingredients");
		Set<String> ingredientIds = new HashSet<>();
		ingredients.forEach(element -> ingredientIds.add(element.getAsString()));
		Set<String> expectedIds = new HashSet<>();
		COMPONENTS.forEach(component -> expectedIds.add("fpvdrone:" + component));
		assertEquals(expectedIds, ingredientIds);

		for (String component : COMPONENTS) {
			assertFalse(
					Files.exists(RECIPE_ROOT.resolve(component + ".json")),
					component + " must not have a crafting recipe in this scope"
			);
		}
	}

	private static JsonObject parse(Path path) throws IOException {
		return JsonParser.parseString(Files.readString(path, StandardCharsets.UTF_8)).getAsJsonObject();
	}
}
