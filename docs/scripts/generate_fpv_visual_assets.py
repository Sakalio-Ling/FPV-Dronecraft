#!/usr/bin/env python3
"""Generate the FPV component item textures and entity texture atlas."""

from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[2]
ITEM_TEXTURES = ROOT / "fabric-mod/src/main/resources/assets/fpvdrone/textures/item"
ENTITY_TEXTURE = ROOT / "fabric-mod/src/main/resources/assets/fpvdrone/textures/entity/drone.png"
PREVIEW = ROOT / "docs/assets/fpv-item-texture-preview.png"

TRANSPARENT = (0, 0, 0, 0)
BLACK = (17, 20, 22, 255)
CARBON = (35, 40, 43, 255)
CARBON_LIGHT = (57, 63, 66, 255)
STEEL = (139, 146, 148, 255)
STEEL_LIGHT = (215, 220, 217, 255)
RED = (174, 28, 35, 255)
RED_LIGHT = (231, 54, 57, 255)
COPPER = (190, 91, 26, 255)
GOLD = (240, 169, 46, 255)
YELLOW = (239, 185, 35, 255)
YELLOW_LIGHT = (255, 226, 91, 255)
CYAN = (49, 224, 226, 255)
PCB = (28, 48, 43, 255)


def canvas():
	return Image.new("RGBA", (16, 16), TRANSPARENT)


def propeller():
	image = canvas()
	draw = ImageDraw.Draw(image)
	draw.polygon([(7, 7), (2, 2), (1, 3), (5, 8)], fill=BLACK)
	draw.polygon([(8, 7), (13, 2), (15, 3), (10, 8)], fill=CARBON_LIGHT)
	draw.polygon([(8, 9), (9, 15), (7, 15), (6, 10)], fill=CARBON)
	draw.line([(3, 3), (6, 7)], fill=STEEL, width=1)
	draw.line([(13, 3), (10, 7)], fill=STEEL, width=1)
	draw.ellipse((5, 5, 10, 10), fill=RED, outline=RED_LIGHT)
	draw.rectangle((7, 7, 8, 8), fill=BLACK)
	return image


def brushless_motor():
	image = canvas()
	draw = ImageDraw.Draw(image)
	draw.ellipse((2, 4, 13, 14), fill=BLACK, outline=STEEL)
	draw.ellipse((3, 5, 12, 12), fill=RED)
	for box in [(4, 5, 5, 7), (8, 5, 9, 7), (10, 8, 12, 9), (4, 10, 5, 12), (8, 10, 9, 12), (2, 8, 4, 9)]:
		draw.rectangle(box, fill=COPPER)
	draw.ellipse((5, 6, 10, 11), fill=BLACK, outline=CARBON_LIGHT)
	draw.rectangle((7, 1, 8, 6), fill=STEEL_LIGHT)
	draw.point((8, 1), fill=(255, 255, 255, 255))
	draw.line([(4, 14), (2, 15)], fill=RED_LIGHT)
	draw.line([(7, 14), (6, 15)], fill=GOLD)
	draw.line([(10, 14), (11, 15)], fill=CARBON_LIGHT)
	return image


def esc():
	image = canvas()
	draw = ImageDraw.Draw(image)
	draw.polygon([(3, 3), (12, 2), (14, 11), (5, 14), (2, 11)], fill=RED)
	draw.polygon([(3, 2), (12, 1), (13, 10), (4, 12), (2, 10)], fill=BLACK, outline=STEEL)
	for offset in range(4):
		draw.line([(4 + offset * 2, 3), (5 + offset * 2, 10)], fill=CARBON_LIGHT)
	for point in [(4, 10), (11, 9), (4, 4), (11, 3)]:
		draw.rectangle((point[0], point[1], point[0] + 1, point[1] + 1), fill=GOLD)
	draw.point((8, 10), fill=CYAN)
	draw.line([(12, 4), (15, 3)], fill=RED_LIGHT)
	draw.line([(12, 6), (15, 6)], fill=BLACK)
	draw.line([(4, 11), (1, 14)], fill=GOLD)
	return image


def flight_controller():
	image = canvas()
	draw = ImageDraw.Draw(image)
	draw.polygon([(3, 2), (13, 3), (12, 13), (2, 12)], fill=PCB, outline=GOLD)
	for point in [(3, 3), (11, 4), (10, 11), (3, 10)]:
		draw.rectangle((point[0], point[1], point[0] + 1, point[1] + 1), fill=RED)
	draw.rectangle((5, 5, 9, 9), fill=BLACK, outline=STEEL)
	draw.line([(5, 4), (10, 5)], fill=GOLD)
	draw.line([(4, 10), (9, 11)], fill=GOLD)
	draw.point((10, 9), fill=CYAN)
	draw.rectangle((1, 6, 3, 8), fill=STEEL_LIGHT)
	return image


def lipo_battery():
	image = canvas()
	draw = ImageDraw.Draw(image)
	draw.polygon([(2, 5), (10, 2), (14, 5), (13, 12), (5, 14), (2, 11)], fill=YELLOW, outline=GOLD)
	draw.polygon([(10, 2), (14, 5), (13, 12), (10, 10)], fill=GOLD)
	draw.line([(3, 6), (10, 4)], fill=YELLOW_LIGHT)
	draw.polygon([(6, 4), (9, 3), (11, 11), (8, 12)], fill=BLACK)
	draw.line([(11, 3), (13, 1), (15, 1)], fill=RED_LIGHT)
	draw.line([(12, 4), (14, 3), (15, 3)], fill=BLACK)
	draw.rectangle((14, 0, 15, 2), fill=GOLD)
	return image


def carbon_frame():
	image = canvas()
	draw = ImageDraw.Draw(image)
	draw.line([(3, 3), (13, 13)], fill=BLACK, width=4)
	draw.line([(13, 3), (3, 13)], fill=BLACK, width=4)
	draw.line([(3, 3), (13, 13)], fill=CARBON_LIGHT)
	draw.line([(13, 3), (3, 13)], fill=CARBON_LIGHT)
	for x, y in [(2, 2), (13, 2), (2, 13), (13, 13)]:
		draw.ellipse((x - 1, y - 1, x + 2, y + 2), fill=CARBON, outline=STEEL)
		draw.point((x, y), fill=BLACK)
	draw.polygon([(5, 4), (11, 5), (10, 11), (4, 10)], fill=BLACK, outline=RED)
	draw.rectangle((6, 6, 9, 9), fill=CARBON_LIGHT)
	return image


def fpv_drone():
	image = carbon_frame()
	draw = ImageDraw.Draw(image)
	for x, y in [(2, 2), (13, 2), (2, 13), (13, 13)]:
		draw.ellipse((x - 1, y - 1, x + 2, y + 2), fill=RED, outline=BLACK)
		draw.line([(x - 3, y), (x + 3, y)], fill=CARBON_LIGHT)
		draw.line([(x, y - 3), (x, y + 3)], fill=CARBON)
	draw.polygon([(5, 4), (11, 5), (10, 11), (4, 10)], fill=BLACK, outline=RED_LIGHT)
	draw.polygon([(6, 4), (10, 4), (11, 8), (6, 9)], fill=YELLOW, outline=GOLD)
	draw.line([(8, 4), (9, 9)], fill=BLACK, width=2)
	draw.rectangle((6, 10, 9, 11), fill=STEEL)
	draw.point((7, 10), fill=CYAN)
	return image


def entity_atlas():
	image = Image.new("RGBA", (128, 64), CARBON)
	draw = ImageDraw.Draw(image)
	for y in range(64):
		for x in range(128):
			if ((x // 2) + (y // 2)) % 2 == 0:
				draw.point((x, y), fill=CARBON_LIGHT if y < 32 else CARBON)

	# Frame plates and arms.
	draw.rectangle((0, 0, 47, 23), fill=CARBON)
	for x in range(0, 48, 4):
		draw.line((x, 0, x + 20, 23), fill=CARBON_LIGHT)
	draw.rectangle((0, 32, 55, 63), fill=BLACK)
	for x in range(0, 56, 6):
		draw.line((x, 32, x + 24, 63), fill=CARBON_LIGHT)

	# Battery and strap.
	draw.rectangle((48, 0, 83, 23), fill=YELLOW)
	draw.line((48, 1, 83, 1), fill=YELLOW_LIGHT)
	draw.line((48, 22, 83, 22), fill=GOLD)
	draw.rectangle((84, 0, 95, 23), fill=BLACK)
	draw.line((87, 0, 87, 23), fill=CARBON_LIGHT)

	# Camera cage and red standoffs.
	draw.rectangle((96, 0, 127, 23), fill=RED)
	draw.rectangle((104, 3, 119, 17), fill=BLACK)
	draw.rectangle((109, 7, 115, 13), fill=(24, 55, 86, 255))
	draw.point((112, 9), fill=CYAN)

	# ESC and flight-controller board regions.
	draw.rectangle((48, 24, 71, 31), fill=BLACK)
	for x in range(49, 71, 4):
		draw.line((x, 24, x, 31), fill=STEEL)
	draw.rectangle((72, 24, 95, 31), fill=PCB)
	for x in range(74, 95, 5):
		draw.point((x, 26), fill=GOLD)
		draw.point((x, 29), fill=GOLD)
	draw.point((88, 28), fill=CYAN)

	# Motor bells, copper windings, and shafts.
	draw.rectangle((56, 32, 79, 47), fill=BLACK)
	for x in range(57, 79, 4):
		draw.rectangle((x, 35, x + 1, 43), fill=COPPER)
	draw.rectangle((72, 32, 79, 39), fill=STEEL)

	# Propeller hubs and blades.
	draw.rectangle((80, 32, 127, 63), fill=BLACK)
	for x in range(82, 128, 6):
		draw.line((x, 33, min(x + 18, 127), 62), fill=CARBON_LIGHT)
	draw.rectangle((80, 38, 87, 45), fill=RED)
	return image


def main():
	ITEM_TEXTURES.mkdir(parents=True, exist_ok=True)
	assets = {
		"propeller": propeller(),
		"brushless_motor": brushless_motor(),
		"esc": esc(),
		"flight_controller": flight_controller(),
		"lipo_battery": lipo_battery(),
		"carbon_frame": carbon_frame(),
		"fpv_drone": fpv_drone(),
	}
	for name, image in assets.items():
		image.save(ITEM_TEXTURES / f"{name}.png")

	ENTITY_TEXTURE.parent.mkdir(parents=True, exist_ok=True)
	entity_atlas().save(ENTITY_TEXTURE)

	preview = Image.new("RGBA", (640, 320), (25, 28, 31, 255))
	for index, image in enumerate(assets.values()):
		x = (index % 4) * 160
		y = (index // 4) * 160
		tile = Image.new("RGBA", (160, 160), (42, 46, 49, 255))
		for cy in range(0, 160, 20):
			for cx in range(0, 160, 20):
				if (cx // 20 + cy // 20) % 2 == 0:
					ImageDraw.Draw(tile).rectangle((cx, cy, cx + 19, cy + 19), fill=(50, 55, 58, 255))
		scaled = image.resize((144, 144), Image.Resampling.NEAREST)
		tile.alpha_composite(scaled, (8, 8))
		preview.alpha_composite(tile, (x, y))
	PREVIEW.parent.mkdir(parents=True, exist_ok=True)
	preview.save(PREVIEW)
	print(f"Generated {len(assets)} item textures, {ENTITY_TEXTURE.relative_to(ROOT)}, and {PREVIEW.relative_to(ROOT)}")


if __name__ == "__main__":
	main()
