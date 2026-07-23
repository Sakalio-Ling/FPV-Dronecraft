package com.tenicana.dronecraft.client.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.tenicana.dronecraft.entity.RotorLayoutCodec;

class DroneEntityModelTest {
	@Test
	void remodeledLayerContainsSeparatedComponentsForEveryRotor() {
		var root = DroneEntityModel.createBodyLayer().bakeRoot();
		var body = root.getChild("body");

		assertDoesNotThrow(() -> body.getChild("frame"));
		assertDoesNotThrow(() -> body.getChild("esc"));
		assertDoesNotThrow(() -> body.getChild("flight_controller"));
		assertDoesNotThrow(() -> body.getChild("battery"));
		for (int index = 0; index < RotorLayoutCodec.MAX_RENDER_ROTORS; index++) {
			int rotorIndex = index;
			assertDoesNotThrow(() -> body.getChild("arm_" + rotorIndex));
			assertDoesNotThrow(() -> body.getChild("motor_" + rotorIndex));
			assertDoesNotThrow(() -> body.getChild("rotor_" + rotorIndex).getChild("blade_0"));
			assertDoesNotThrow(() -> body.getChild("rotor_" + rotorIndex).getChild("blade_1"));
			assertDoesNotThrow(() -> body.getChild("rotor_" + rotorIndex).getChild("blade_2"));
		}
	}

	@Test
	void positivePlayablePitchLowersVisibleNoseInLineOfSightView() {
		float playablePitch = (float) Math.toRadians(18.0);
		float modelPitch = DroneEntityModel.bodyPitchRotationRadians(playablePitch);
		float finalForwardYOffset = DroneEntityModel.renderedBodyForwardYOffsetAfterRendererTransform(playablePitch);

		assertTrue(modelPitch < 0.0f);
		assertEquals(-playablePitch, modelPitch, 1.0e-6f);
		assertTrue(finalForwardYOffset < 0.0f);
	}

	@Test
	void negativePlayablePitchRaisesVisibleNoseInLineOfSightView() {
		float playablePitch = (float) Math.toRadians(-18.0);
		float finalForwardYOffset = DroneEntityModel.renderedBodyForwardYOffsetAfterRendererTransform(playablePitch);

		assertTrue(finalForwardYOffset > 0.0f);
		assertEquals(
				-DroneEntityModel.renderedBodyForwardYOffsetAfterRendererTransform(-playablePitch),
				finalForwardYOffset,
				1.0e-6f
		);
	}
}
