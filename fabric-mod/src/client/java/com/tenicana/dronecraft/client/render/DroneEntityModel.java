package com.tenicana.dronecraft.client.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import com.tenicana.dronecraft.entity.RotorLayoutCodec;

public class DroneEntityModel extends EntityModel<DroneEntityRenderState> {
	private static final String BODY = "body";
	private static final String FRAME = "frame";
	private static final String ESC = "esc";
	private static final String FLIGHT_CONTROLLER = "flight_controller";
	private static final String BATTERY = "battery";
	private static final String ARM_PREFIX = "arm_";
	private static final String MOTOR_PREFIX = "motor_";
	private static final String ROTOR_PREFIX = "rotor_";
	private static final float RPM_TO_RADIANS_PER_TICK = (float) (Math.PI * 2.0 / 1200.0);
	private static final float ARM_BASE_LENGTH = 24.0f;
	private static final float ARM_Y = 0.4f;
	private static final float MOTOR_Y = -0.8f;
	private static final float ROTOR_Y = -2.2f;
	private static final float BODY_FORWARD_Z = 6.0f;

	private final ModelPart body;
	private final ModelPart[] arms = new ModelPart[RotorLayoutCodec.MAX_RENDER_ROTORS];
	private final ModelPart[] motors = new ModelPart[RotorLayoutCodec.MAX_RENDER_ROTORS];
	private final ModelPart[] rotors = new ModelPart[RotorLayoutCodec.MAX_RENDER_ROTORS];

	public DroneEntityModel(ModelPart root) {
		super(root);
		this.body = root.getChild(BODY);
		for (int i = 0; i < RotorLayoutCodec.MAX_RENDER_ROTORS; i++) {
			this.arms[i] = body.getChild(ARM_PREFIX + i);
			this.motors[i] = body.getChild(MOTOR_PREFIX + i);
			this.rotors[i] = body.getChild(ROTOR_PREFIX + i);
		}
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		PartDefinition body = root.addOrReplaceChild(
				BODY,
				CubeListBuilder.create(),
				PartPose.offset(0.0f, 20.0f, 0.0f)
		);
		body.addOrReplaceChild(
				FRAME,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0f, 0.0f, -6.0f, 10.0f, 0.8f, 12.0f)
						.texOffs(0, 14).addBox(-4.0f, -3.2f, -5.0f, 8.0f, 0.7f, 10.0f)
						.texOffs(96, 0).addBox(-3.8f, -2.8f, -4.8f, 0.7f, 3.0f, 0.7f)
						.texOffs(96, 0).addBox(3.1f, -2.8f, -4.8f, 0.7f, 3.0f, 0.7f)
						.texOffs(96, 0).addBox(-3.8f, -2.8f, 4.1f, 0.7f, 3.0f, 0.7f)
						.texOffs(96, 0).addBox(3.1f, -2.8f, 4.1f, 0.7f, 3.0f, 0.7f)
						.texOffs(104, 0).addBox(-2.0f, -2.4f, -7.2f, 4.0f, 2.8f, 2.4f),
				PartPose.ZERO
		);
		body.addOrReplaceChild(
				ESC,
				CubeListBuilder.create()
						.texOffs(48, 24).addBox(-3.0f, -1.1f, -3.0f, 6.0f, 1.0f, 6.0f),
				PartPose.ZERO
		);
		body.addOrReplaceChild(
				FLIGHT_CONTROLLER,
				CubeListBuilder.create()
						.texOffs(72, 24).addBox(-2.5f, -2.2f, -2.5f, 5.0f, 0.8f, 5.0f),
				PartPose.ZERO
		);
		body.addOrReplaceChild(
				BATTERY,
				CubeListBuilder.create()
						.texOffs(48, 0).addBox(-4.0f, -6.1f, -4.6f, 8.0f, 3.0f, 9.2f)
						.texOffs(84, 0).addBox(-4.2f, -6.25f, -0.8f, 8.4f, 3.2f, 1.6f),
				PartPose.ZERO
		);
		for (int i = 0; i < RotorLayoutCodec.MAX_RENDER_ROTORS; i++) {
			body.addOrReplaceChild(ARM_PREFIX + i, arm(), PartPose.offset(0.0f, ARM_Y, 0.0f));
			body.addOrReplaceChild(MOTOR_PREFIX + i, motor(), PartPose.offset(0.0f, MOTOR_Y, 0.0f));
			PartDefinition propeller = body.addOrReplaceChild(
					ROTOR_PREFIX + i,
					CubeListBuilder.create().texOffs(80, 38).addBox(-1.25f, -0.35f, -1.25f, 2.5f, 0.7f, 2.5f),
					PartPose.offset(0.0f, ROTOR_Y, 0.0f)
			);
			for (int blade = 0; blade < 3; blade++) {
				propeller.addOrReplaceChild(
						"blade_" + blade,
						propellerBlade(),
						PartPose.rotation(0.0f, blade * (Mth.TWO_PI / 3.0f), 0.0f)
				);
			}
		}
		return LayerDefinition.create(mesh, 128, 64);
	}

	private static CubeListBuilder arm() {
		return CubeListBuilder.create()
				.texOffs(0, 32).addBox(-0.65f, -0.4f, 0.0f, 1.3f, 0.8f, ARM_BASE_LENGTH);
	}

	private static CubeListBuilder motor() {
		return CubeListBuilder.create()
				.texOffs(56, 32).addBox(-1.8f, -1.1f, -1.8f, 3.6f, 2.2f, 3.6f)
				.texOffs(72, 32).addBox(-0.55f, -1.8f, -0.55f, 1.1f, 0.8f, 1.1f);
	}

	private static CubeListBuilder propellerBlade() {
		return CubeListBuilder.create()
				.texOffs(88, 38).addBox(-0.65f, -0.18f, 0.8f, 1.3f, 0.36f, 5.2f);
	}

	@Override
	public void setupAnim(DroneEntityRenderState state) {
		super.setupAnim(state);
		body.xRot = bodyPitchRotationRadians(state.pitchRadians);
		body.yRot = state.yawRadians;
		body.zRot = state.rollRadians;

		int rotorCount = Math.max(1, Math.min(RotorLayoutCodec.MAX_RENDER_ROTORS, state.rotorCount));
		for (int i = 0; i < RotorLayoutCodec.MAX_RENDER_ROTORS; i++) {
			boolean visible = i < rotorCount;
			arms[i].visible = visible;
			motors[i].visible = visible;
			rotors[i].visible = visible;
			if (!visible) {
				continue;
			}

			float x = state.rotorXModelUnits[i];
			float y = state.rotorYModelUnits[i];
			float z = state.rotorZModelUnits[i];
			float radius = (float) Math.sqrt(x * x + z * z);
			float yaw = (float) Math.atan2(x, z);
			arms[i].setPos(0.0f, ARM_Y + y, 0.0f);
			arms[i].setRotation(0.0f, yaw, 0.0f);
			arms[i].xScale = 1.0f;
			arms[i].yScale = 1.0f;
			arms[i].zScale = Math.max(0.10f, radius / ARM_BASE_LENGTH);

			motors[i].setPos(x, MOTOR_Y + y, z);
			motors[i].setRotation(0.0f, yaw, 0.0f);
			motors[i].xScale = 1.0f;
			motors[i].yScale = 1.0f;
			motors[i].zScale = 1.0f;

			rotors[i].setPos(x, ROTOR_Y + y, z);
			rotors[i].xScale = 1.0f;
			rotors[i].yScale = 1.0f;
			rotors[i].zScale = 1.0f;
			rotors[i].setRotation(0.0f, rotorAngle(state.ageInTicks, state.rotorRpm[i], state.rotorSpinDirection[i], phaseOffset(i)), 0.0f);
		}
	}

	private static float rotorAngle(float ageInTicks, float rpm, int spinDirection, float phaseOffset) {
		return spinDirection * ageInTicks * Math.max(0.0f, rpm) * RPM_TO_RADIANS_PER_TICK + phaseOffset;
	}

	static float bodyPitchRotationRadians(float pitchRadians) {
		return -pitchRadians;
	}

	static float renderedBodyForwardYOffset(float pitchRadians) {
		return -BODY_FORWARD_Z * (float) Math.sin(bodyPitchRotationRadians(pitchRadians));
	}

	static float renderedBodyForwardYOffsetAfterRendererTransform(float pitchRadians) {
		return -renderedBodyForwardYOffset(pitchRadians);
	}

	private static float phaseOffset(int index) {
		return (index & 1) == 0 ? 0.0f : Mth.HALF_PI;
	}
}
