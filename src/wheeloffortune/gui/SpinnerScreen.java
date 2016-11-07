package wheeloffortune.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import wheeloffortune.engine.gui.Button;
import wheeloffortune.engine.gui.Component;
import wheeloffortune.engine.gui.GifDisplay;
import wheeloffortune.engine.gui.ImageDisplay;
import wheeloffortune.engine.gui.Label;
import wheeloffortune.engine.gui.Screen;
import wheeloffortune.engine.input.Mouse;
import wheeloffortune.game.Game;
import wheeloffortune.game.SpinnerAction;
import wheeloffortune.misc.Utils;

public class SpinnerScreen extends Screen {

	private static final int WHEEL_PADDING = 40;
	private static final float MIN_CLICK_RATIO = 0.6f;
	private static final float MAX_CLICK_RATIO = 1f;
	private static final int MIN_SPIN_SPEED = 10;

	private ImageDisplay wheel;
	private int wheelRadius;
	private int time;

	private EnumState state;

	private float wheelSpinSpeed;

	public SpinnerScreen() {
		setBackgroundColor(new Color(178, 238, 255));
	}

	@Override
	public void onScreenOpened() {
		state = EnumState.WAITING_SPIN;
		state.initialize(this);
	}

	@Override
	public void onScreenClosed() {
		state.cleanUp(this);
	}

	@Override
	public void layout() {
		int wheelSize = Math.min(width, height) - WHEEL_PADDING;
		wheelRadius = wheelSize / 2;
		wheel = addImage(width / 2 - wheelSize / 2, height / 2 - wheelSize / 2, wheelSize, wheelSize, Images.wheel);
		wheel.setConstantScale(true);
		wheel.setRotation(Game.getLogic().getCurrentSpinnerAngle());
		addImage(width / 2 - 32 / 2, WHEEL_PADDING / 2 - 32 / 2, 32, 32, Images.wheelPointer);

		state.layout(this);
	}

	@Override
	public void updateTick() {
		super.updateTick();

		time++;

		state.updateState(this);

		wheel.setRotation(Game.getLogic().getCurrentSpinnerAngle());
	}

	@Override
	public void onButtonPressed(String buttonId) {
		if ("nextEndTurn".equals(buttonId)) {
			Game.getLogic().nextPlayer();
			Game.getLogic().setGuessingPhrase(false);
			Game.openScreen(new GameScreen());
		} else if ("nextContinueTurn".equals(buttonId)) {
			Game.getLogic().setGuessingPhrase(true);
			Game.openScreen(new GameScreen());
		}
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

			private DonutComponent donutComponent;
			private GifDisplay wheelArrowGif;

			@Override
			public void updateState(SpinnerScreen screen) {
				if (Mouse.isButtonPressed(MouseEvent.BUTTON1) && screen.isMouseOnSpinner(Mouse.getMouseLocation())) {
					screen.changeState(DRAGGING);
				}
			}

			@Override
			public void layout(SpinnerScreen screen) {
				int x = screen.width / 2 - screen.wheelRadius;
				int y = screen.height / 2 - screen.wheelRadius;
				int wheelDimension = screen.wheelRadius * 2;

				donutComponent = new DonutComponent(screen, x, y, wheelDimension, wheelDimension);
				screen.addComponent(donutComponent);
				wheelArrowGif = new GifDisplay(x, y, wheelDimension, wheelDimension / 4, Images.wheelArrow);
				screen.addComponent(wheelArrowGif);
			}

			@Override
			public void initialize(SpinnerScreen screen) {
				layout(screen);
			}

			@Override
			public void cleanUp(SpinnerScreen screen) {
				screen.removeComponent(donutComponent);
				screen.removeComponent(wheelArrowGif);
			}

			class DonutComponent extends Component {

				private Shape donutShape;
				private float donutAlpha = 0.5f;
				private int donutAlphaDirection = -1;

				public DonutComponent(SpinnerScreen screen, int x, int y, int width, int height) {
					super(x, y, width, height);

					// build donut shape
					float radius = screen.wheelRadius * MAX_CLICK_RATIO;
					Shape shape = new Ellipse2D.Float(screen.width / 2 - radius, screen.height / 2 - radius, radius * 2,
							radius * 2);
					Area area = new Area(shape);

					radius = screen.wheelRadius * MIN_CLICK_RATIO;
					shape = new Ellipse2D.Float(screen.width / 2 - radius, screen.height / 2 - radius, radius * 2,
							radius * 2);
					area.subtract(new Area(shape));
					donutShape = area;
				}

				@Override
				public void updateTick() {
					donutAlpha += 0.02f * donutAlphaDirection;
					if (donutAlpha <= 0.3f) {
						donutAlphaDirection = 1;
					} else if (donutAlpha >= 0.5f) {
						donutAlphaDirection = -1;
					}
				}

				@Override
				public void draw(Graphics g) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(new Color(1f, 1f, 1f, donutAlpha));
					g2.fill(donutShape);
				}

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
					screen.wheelSpinSpeed = spinSpeed;
					if (Math.abs(spinSpeed) < MIN_SPIN_SPEED) {
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
					if (spinDirection == 0) {
						// The wheel isn't moving yet, this doesn't count to the speed of the spin
						gestureStartTime = screen.time;
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

			private static final float FRICTION = 0.2f;

			@Override
			public void updateState(SpinnerScreen screen) {
				if (screen.wheelSpinSpeed > 0) {
					screen.wheelSpinSpeed -= FRICTION;
					if (screen.wheelSpinSpeed < 0) {
						screen.wheelSpinSpeed = 0;
					}
				} else {
					screen.wheelSpinSpeed += FRICTION;
					if (screen.wheelSpinSpeed > 0) {
						screen.wheelSpinSpeed = 0;
					}
				}
				if (screen.wheelSpinSpeed == 0) {
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

			private final Font FONT = new Font(Game.FONT_NAME, Font.BOLD, 50);

			private SpinnerAction spinnerAction;
			private Label spinnerActionLabel;
			private Button button;

			@Override
			public void updateState(SpinnerScreen screen) {
			}

			@Override
			public void layout(SpinnerScreen screen) {
				spinnerActionLabel = screen.addLabel(screen.width / 2, screen.height / 2, spinnerAction.toString(),
						Color.YELLOW);
				spinnerActionLabel.setHAlignment(Label.TEXT_ALIGN_CENTER);
				spinnerActionLabel.setVAlignment(Label.TEXT_ALIGN_MIDDLE);
				spinnerActionLabel.setFont(FONT);
				button = screen.addButton(screen.width - 100, screen.height - 50, 90, 40, "Next",
						spinnerAction.endsTurn() ? "nextEndTurn" : "nextContinueTurn");
			}

			@Override
			public void initialize(SpinnerScreen screen) {
				spinnerAction = Game.getLogic().getCurrentSpinnerAction();
				spinnerAction.performAction();
				layout(screen);
			}

			@Override
			public void cleanUp(SpinnerScreen screen) {
				screen.removeComponent(spinnerActionLabel);
				screen.removeComponent(button);
			}

		},
		SHOW_NOT_PROPER_SPIN {

			private final Font FONT = new Font(Game.FONT_NAME, Font.BOLD, 33);

			private int cooldown;
			private Label spinProperlyLabel;

			@Override
			public void updateState(SpinnerScreen screen) {
				cooldown--;
				if (cooldown < 0) {
					screen.changeState(WAITING_SPIN);
				}
			}

			@Override
			public void layout(SpinnerScreen screen) {
				spinProperlyLabel = screen.addLabel(screen.width / 2, screen.height / 2, "Spin properly you stupid idiot!", Color.RED);
				spinProperlyLabel.setHAlignment(Label.TEXT_ALIGN_CENTER);
				spinProperlyLabel.setVAlignment(Label.TEXT_ALIGN_MIDDLE);
				spinProperlyLabel.setFont(FONT);
			}

			@Override
			public void initialize(SpinnerScreen screen) {
				cooldown = 50;
				layout(screen);
			}

			@Override
			public void cleanUp(SpinnerScreen screen) {
				screen.removeComponent(spinProperlyLabel);
			}

		};

		public abstract void updateState(SpinnerScreen screen);

		public abstract void layout(SpinnerScreen screen);

		public abstract void initialize(SpinnerScreen screen);

		public abstract void cleanUp(SpinnerScreen screen);
	}

}
