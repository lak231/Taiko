import java.awt.Robot;
import com.leapmotion.leap.*;
import java.awt.event.*;


class CustomListener extends Listener {
  // The automator. More in the information document under "Leap Motion 101"
  public Robot robot;
  private static final float VER_THRESHOLD = -300;
  private static final float HOR_THRESHOLD = 200;
  // Executes once your program and detector are connected, are there any specific gestures you're going
  // to look for later on? Uncomment the ones you want to enable below
  public void onConnect(Controller controller) {
    System.out.println("Connected");
  //  controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
  //  controller.enableGesture(Gesture.Type.TYPE_SWIPE);
  //  controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
  //  controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    controller.setPolicy(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES); // allows the program to run in places other than the terminal
  }

  // Executes when detector stops working/is no longer connected to computer
  public void onDisconnect(Controller controller) {
    System.out.println("Disconnected");
  }

  // Executes when you manually stop running the detector/program
  public void onExit(Controller controller) {
    System.out.println("Exited");
  }

  public int count_fingers_extended(Hand hand) {
    int count = 0;
    for (Finger finger: hand.fingers()) {
      if (finger.isExtended()) {
        count += 1;
      }
    }
    return count;
  }
  // More info in the Leap Motion 101 section
  public void onFrame(Controller controller) {
    try {
      // Creating our robot friend. Feel free to name it anything you'd like
      robot = new Robot();
    } catch (Exception e) {
    }
    Frame frame = controller.frame();
    /*––––––––––––*/
    /* CODE HERE. */
    /*––––––––––––*/

    Hand left_hand = frame.hands().get(0);
    Hand right_hand = frame.hands().get(1);


    if (left_hand.isRight()) {
      left_hand = frame.hands().get(1);
      right_hand = frame.hands().get(0);
    }
    

    if (count_fingers_extended(right_hand) >= 3 && count_fingers_extended(left_hand) >= 3) {
      if (Math.abs(left_hand.palmPosition().getX() - right_hand.palmPosition().getX()) >= HOR_THRESHOLD) {
        if (left_hand.palmVelocity().getY() <= VER_THRESHOLD) {
          robot.keyPress(KeyEvent.VK_1);
          robot.delay(50);
          robot.keyRelease(KeyEvent.VK_1);
        }
        if (right_hand.palmVelocity().getY() <= VER_THRESHOLD) {
          robot.keyPress(KeyEvent.VK_4);
          robot.delay(50);
          robot.keyRelease(KeyEvent.VK_4);
        }
      } else {
        if (left_hand.palmVelocity().getY() <= VER_THRESHOLD) {
          robot.keyPress(KeyEvent.VK_2);
          robot.delay(50);
          robot.keyRelease(KeyEvent.VK_2);
        }
        if (right_hand.palmVelocity().getY() <= VER_THRESHOLD) {
          robot.keyPress(KeyEvent.VK_3);
          robot.delay(50);
          robot.keyRelease(KeyEvent.VK_3);
        }
      }
    }
  }

} // end of listener class



// The "main()" function
// NAME THIS CLASS WHATEVER YOUR FILE IS NAMED
class template {
  public static void main(String[] args){
    // initializes our detector
    CustomListener listener = new CustomListener();
    Controller controller = new Controller();
    controller.addListener(listener);
    System.out.println("Press Enter to quit...");

    try {
      System.in.read();
    } catch(Exception e) {}
    controller.removeListener(listener);
  }
}
