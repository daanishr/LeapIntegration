package prototype;

import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

class SampleListener extends Listener {
	private String clockwiseness;
	public boolean rot;
	
	
	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}
	public String getClockwiseness(){
		return this.clockwiseness;
	}
	// long dTap[] = new long[100000000];
	int j;
	public void onConnect(Controller controller) {
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}

	public void onDisconnect(Controller controller) {
		System.out.println("Disconnected");
	}
	//PrototypeGLContext cube = new PrototypeGLContext();

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}
	
	
	
	public boolean getRotation(String rotation){
		//cube.camera.rotateY(-1f);

		if(rotation== "clockwise"){
			System.out.println("Clockwise Rotation");
			return true;
		}
		else {
			System.out.println("Anti Clockwise Rotation");
			return false;
		}
	}


	public void swipeRight(){
		System.out.println("\n\n\n\nSWIPE RIGHTTT\n\n\n\n\n");
	}

	public void swipeLeft(){
		System.out.println("\n\n\n\nSWIPE LEFT\n\n\n\n\n");
	}
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();
		System.out.println("Frame id: " + frame.id()
				+ ", timestamp: " + frame.timestamp()
				+ ", hands: " + frame.hands().count()
				+ ", fingers: " + frame.fingers().count()
				+ ", tools: " + frame.tools().count()
				+ ", gestures " + frame.gestures().count());


		if (frame.gestures().count()==1){
			System.out.println("\n\n\n\n\n\n\n\n");
		}
		if (!frame.hands().isEmpty()) {
			Hand hand = frame.hands().get(0);
			FingerList fingers = hand.fingers();
			if (!fingers.isEmpty()) {
				Vector avgPos = Vector.zero();
				for (Finger finger : fingers) {
					avgPos = avgPos.plus(finger.tipPosition());
				}
				avgPos = avgPos.divide(fingers.count());
				System.out.println("Hand has " + fingers.count()
						+ " fingers, average finger tip position: " + avgPos);
			}
			System.out.println("Hand sphere radius: " + hand.sphereRadius()
					+ " mm, palm position: " + hand.palmPosition());

			Vector normal = hand.palmNormal();
			Vector direction = hand.direction();

			System.out.println("Hand pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
					+ "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
					+ "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");
		}

		GestureList gestures = frame.gestures();
		
		for (int i = 0; i < gestures.count(); i++) {
			Gesture gesture = gestures.get(i);

			switch (gesture.type()) {
			case TYPE_CIRCLE:
				CircleGesture circle = new CircleGesture(gesture);
				
				if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/4) {
					this.clockwiseness = "clockwise";
				} else {
					this.clockwiseness = "counterclockwise";
				}

				double sweptAngle = 0;
				if (circle.state() != State.STATE_START) {
					CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
					sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
				}

				System.out.println("Circle id: " + circle.id()
						+ ", " + circle.state()
						+ ", progress: " + circle.progress()
						+ ", radius: " + circle.radius()
						+ ", angle: " + Math.toDegrees(sweptAngle)
						+ ", " + clockwiseness);


				rot =getRotation(clockwiseness);

				break;
			case TYPE_SWIPE:
				SwipeGesture swipe = new SwipeGesture(gesture);
				System.out.println("Swipe id: " + swipe.id()
						+ ", " + swipe.state()
						+ ", position: " + swipe.position()
						+ ", direction: " + swipe.direction()
						+ ", speed: " + swipe.speed());

				if((swipe.direction().getX() >0.5) && (swipe.direction().getY()>0)){
					swipeRight();
				}

				if((swipe.direction().getX() <-0.5) && (swipe.direction().getY()>0)){
					swipeLeft();
				}



				long x =frame.id();

				if((swipe.direction().getZ()<-0.7) && (frame.fingers().count() ==2 )){

					//dTap[j] = x;
					j++;
					System.out.println("\n\n\n\n INNNNNNNNNNNN \n\n\n\n\n");
					System.out.println(j);
					//&& (((dTap[j-3]) - (dTap[j-1])) < 10)
					if((j>2) ){
						System.out.println("\n\n\n\n DOUBLEEEE INNNNNNNNNNNN \n\n\n\n\n");
						j = 0;
					}
				}

				break;
			case TYPE_SCREEN_TAP:
				ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
				System.out.println("Screen Tap id: " + screenTap.id()
						+ ", " + screenTap.state()
						+ ", position: " + screenTap.position()
						+ ", direction: " + screenTap.direction());
				break;
			case TYPE_KEY_TAP:
				KeyTapGesture keyTap = new KeyTapGesture(gesture);
				System.out.println("Key Tap id: " + keyTap.id()
						+ ", " + keyTap.state()
						+ ", position: " + keyTap.position()
						+ ", direction: " + keyTap.direction());
				break;
			default:
				System.out.println("Unknown gesture type.");
				break;
			}
		}

		if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
			// System.out.println();
		}
		
	}
}

class Nilesh_Leap {
	public static void main(String[] args) {

	}


}

