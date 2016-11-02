package wheeloffortune.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

import wheeloffortune.engine.gui.ImageDisplay;
import wheeloffortune.engine.gui.Screen;
import wheeloffortune.engine.input.Mouse;
import wheeloffortune.game.Game;
import wheeloffortune.misc.Utils;

public class SpinnerScreen extends Screen {

	private static final int WHEEL_PADDING = 20;
	private static final float MIN_CLICK_RATIO = 0.7f;
	private static final float MAX_CLICK_RATIO = 1f;
	private static final int MIN_SPIN_SPEED = 45;

	private ImageDisplay wheel;
	private int wheelRadius;
	private int time;

	private EnumState state;

	private int wheelSpinSpeed;

	public SpinnerScreen() {
		setBackgroundColor(new Color(178, 238, 255));
	}

	@Override
	public void onScreenOpened() {
		state = EnumState.WAITING_SPIN;
		state.initialize(this);
	}

	@Override
	public void layout() {
		int wheelSize = Math.min(width, height) - WHEEL_PADDING;
		wheelRadius = wheelSize / 2;
		wheel = addImage(width / 2 - wheelSize / 2, height / 2 - wheelSize / 2, wheelSize, wheelSize, Images.wheel);
		wheel.setConstantScale(true);
		wheel.setRotation(Game.getLogic().getCurrentSpinnerAngle());

		state.layout(this);
	}

	@Override
	public void updateTick() {
		time++;

		state.updateState(this);

		wheel.setRotation(Game.getLogic().getCurrentSpinnerAngle());
	}

	private void changeState(EnumState state) {
		this.state.cleanUp(this);
		this.state = state;
		state.initialize(this);
	}

	private boolean isMouseOnSpinner(Point mouseLocation) {
		Point wheelCenter = new Point(width / 2, height / 2);
		int distSq = (int) mouseLocation.distanceSq(wheelCenter);
		if (distSq < Utils.square(wheelRadius * MIN_CLICK_RATIO)) {
			return false;
		} else if (distSq > Utils.square(wheelRadius * MAX_CLICK_RATIO)) {
			return false;
		} else {
			return true;
		}
	}

	private float getAngleFromSpinner(Point mouseLocation) {
		return (float) Math.toDegrees(Math.atan2(mouseLocation.y - height / 2d, mouseLocation.x - width / 2d));
	}

	private static enum EnumState {
		WAITING_SPIN {

			@Override
			public void updateState(SpinnerScreen screen) {
				if (Mouse.isButtonPressed(MouseEvent.BUTTON1) && screen.isMouseOnSpinner(Mouse.getMouseLocation())) {
					screen.changeState(DRAGGING);
				}
			}

			@Override
			public void layout(SpinnerScreen screen) {
			}

			@Override
			public void initialize(SpinnerScreen screen) {
			}

			@Override
			public void cleanUp(SpinnerScreen screen) {
			}

		},
		DRAGGING {

			private int gestureStartTime;
			private float lastGestureAngle;
			private float degreesSpun = 0;
			// -1 for counterclockwise, 1 for clockwise, 0 for unknown
			private int spinDirection = 0;

			@Override
			public void updateState(SpinnerScreen screen) {
				Point currentGesturePoint = Mouse.getMouseLocation();
				float currentGestureAngle = screen.getAngleFromSpinner(currentGesturePoint);

				// Check for wheel release
				if (Mouse.isButtonReleased(MouseEvent.BUTTON1) || !screen.isMouseOnSpinner(currentGesturePoint)) {
					// The wheel has been released
					float spinSpeed = degreesSpun / (screen.time - gestureStartTime);
					screen.wheelSpinSpeed = (int) Math.ceil(spinSpeed);
					if (screen.wheelSpinSpeed < MIN_SPIN_SPEED) {
						screen.changeState(SHOW_NOT_PROPER_SPIN);
					} else {
						screen.changeState(SPINNING);
					}
				} else {
					// Check for changing direction

					// The spin direction needs to have changed for the gesture
					// to be aborted, it's okay for it to stay the same
					if (currentGestureAngle != lastGestureAngle) {
						int spinDir;
						// -180 <= angle < 180
						if (lastGestureAngle >= 90 && currentGestureAngle < -90) {
							spinDir = 1;
							degreesSpun += (currentGestureAngle + 360) - lastGestureAngle;
						} else if (lastGestureAngle < -90 && currentGestureAngle >= 90) {
							spinDir = -1;
							degreesSpun += currentGestureAngle - (lastGestureAngle + 360);
						} else {
							spinDir = (int) Math.signum(currentGestureAngle - lastGestureAngle);
							degreesSpun += currentGestureAngle - lastGestureAngle;
						}

						// Check for spinning the opposite direction
						if (spinDir == -spinDirection) {
							screen.changeState(WAITING_SPIN);
						}

						spinDirection = spinDir;
					}
				}

				lastGestureAngle = currentGestureAngle;
			}

			@Override
			public void layout(SpinnerScreen screen) {
			}

			@Override
			public void initialize(SpinnerScreen screen) {
				gestureStartTime = screen.time;
				lastGestureAngle = screen.getAngleFromSpinner(Mouse.getMouseLocation());
			}

			@Override
			public void cleanUp(SpinnerScreen screen) {
			}

		},
		SPINNING {

			private static final int FRICTION = 2;

			@Override
			public void updateState(SpinnerScreen screen) {
				screen.wheelSpinSpeed -= FRICTION;
				if (screen.wheelSpinSpeed < 0) {
					screen.changeState(FINISHED_SPIN);
				}

				Game.getLogic()
						.setCurrentSpinnerAngle(Game.getLogic().getCurrentSpinnerAngle() + screen.wheelSpinSpeed);
			}

			@Override
			public void layout(SpinnerScreen screen) {
			}

			@Override
			public void initialize(SpinnerScreen screen) {
			}

			@Override
			public void cleanUp(SpinnerScreen screen) {
			}

		},
		FINISHED_SPIN {

			@Override
			public void updateState(SpinnerScreen screen) {
				// TODO Auto-generated method stub

			}

			@Override
			public void layout(SpinnerScreen screen) {
				// TODO Auto-generated method stub

			}

			@Override
			public void initialize(SpinnerScreen screen) {
				// TODO Auto-generated method stub

			}

			@Override
			public void cleanUp(SpinnerScreen screen) {
				// TODO Auto-generated method stub

			}

		},
		SHOW_NOT_PROPER_SPIN {

			@Override
			public void updateState(SpinnerScreen screen) {
				// TODO Auto-generated method stub

			}

			@Override
			public void layout(SpinnerScreen screen) {
				// TODO Auto-generated method stub

			}

			@Override
			public void initialize(SpinnerScreen screen) {
				// TODO Auto-generated method stub

			}

			@Override
			public void cleanUp(SpinnerScreen screen) {
				// TODO Auto-generated method stub

			}

		};

		public abstract void updateState(SpinnerScreen screen);

		public abstract void layout(SpinnerScreen screen);

		public abstract void initialize(SpinnerScreen screen);

		public abstract void cleanUp(SpinnerScreen screen);
	}

}
