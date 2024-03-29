package CISC190.bookClasses.shapeExample;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Class ShapeCanvas:  holds shapes in a custom drawn area
 *
 * @author Barb Ericson
 */
public class ShapeCanvas extends Canvas implements ShapeInterface {


	///////////////// Private Attributes /////////////////////////////

	private ArrayList<Shape> shapes = new ArrayList<Shape>(); // a vector of shapes
	private Shape currentShape = null; // current shape being dragged
	private String currShapeType = Shape.RECTANGLE; // default shape type
	private int width = 100;  // canvas width
	private int height = 100;  // canvas height
	private Color backgroundColor = Color.yellow;
	private Image backgroundImage = null;        // background image for double buffering
	private Graphics backgroundG = null;    // graphics context of background image

	/////////////////// Constructors //////////////////////////////////

	/**
	 * A constructor that uses the default size
	 */
	public ShapeCanvas() {
		init();
	}

	/**
	 * A constructor that takes the width and height
	 *
	 * @param width  the width of the canvas
	 * @param height the height of the canvas
	 */
	public ShapeCanvas(int width, int height) {
		// set the local variables
		this.width = width;
		this.height = height;

		init();
	}

	////////////////////// Private Methods ////////////////////////////////

	/* Method to initialize the shape canvas size and set the mouse listeners*/
	private void init() {
		// set the size of the canvas to the current width and height
		setSize(width, height);

		// add the mouse listener and mouse motion listener
		addMouseListener(new MyMouseAdapter());
		addMouseMotionListener(new MyMouseMotionAdapter());

		// add a component listener
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				backgroundImage = null;
			}
		});

		// set the background color
		setBackground(backgroundColor);
	}

	///////////////////////// Public Methods ///////////////////////////////

	/**
	 * Method to add a shape to the shape vector
	 *
	 * @param shape the shape to add
	 */
	public void add(Shape shape) {
		// add the shape to the vector of shapes
		shapes.add(shape);

		// force a repaint to show the new shape
		repaint();
	}

	/**
	 * Method to remove a shape from the shape vector
	 *
	 * @param shape the shape to remove
	 */
	public void remove(Shape shape) {
		// remove the shape from the vector of shapes
		shapes.remove(shape); // removes first one

		// force a repaint to show that it is gone
		repaint();
	}

	/**
	 * Method to remove a shape given the index
	 *
	 * @param index the index of the shape in the shape vector that you
	 *              wish to remove
	 */
	public void remove(int index) {
		// remove the shape at the given index
		shapes.remove(index);

		// force a repaint to show it is gone
		repaint();
	}

	/**
	 * Update normally clears the background and calls paint
	 * override it here to just call paint
	 *
	 * @param g the graphics context on which to draw
	 */
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * Method to paint the shape canvas and all objects in it
	 *
	 * @param g the graphic context on which to paint
	 */
	public void paint(Graphics g) {
		Shape currShape;
		int height = getSize().height;
		int width = getSize().width;

		// if background image not created then create it
		if (backgroundImage == null) {
			// create the background image for double buffering
			backgroundImage = createImage(width, height);

			// get the graphics context of the background image
			backgroundG = backgroundImage.getGraphics();
		}

		// clear the drawing area in the background image
		backgroundG.setColor(getBackground());
		backgroundG.clearRect(0, 0, width, height);

		// loop through the shape objects and draw each one
		for (int i = 0; i < shapes.size(); i++) {
			currShape = shapes.get(i);
			currShape.draw(backgroundG);
		}

		// when drawing to background is done display background
		// image
		g.drawImage(backgroundImage, 0, 0, this);
	}

	/**
	 * Set the type of the shape that will be created when the user
	 * clicks in the shape canvas.
	 *
	 * @param shapeType the name of the shape
	 */
	public void setShape(String shapeType) {
		currShapeType = shapeType;
	}

	/**
	 * Clear all shapes out of the shape vector
	 */
	public void clearShapes() {
		// remove all shapes from the shape vector
		shapes.clear();

		// repaint to show that they are gone
		repaint();
	}

	/////////////////// Main Method for Testing ///////////////////////////
	public static void main(String argv[]) {
		// create a frame
		Frame frame = new Frame();

		// create a ShapeCanvas
		ShapeCanvas shapeCanvas = new ShapeCanvas(500, 500);

		// create a rectangle shape
		Shape shape = new Rectangle(50, 50, 60, 60);

		// add the shape to the shape canvas
		shapeCanvas.add(shape);

		// create an oval
		shape = new Oval(10, 10, 30, 30);
		shapeCanvas.add(shape);

		// add the shape canvas to the frame
		frame.add(shapeCanvas);
		frame.pack(); // shrink to fit preferred size
		frame.setVisible(true); // show the frame
	}

	/**
	 * An inner class for handling the mouse listener interface
	 */
	class MyMouseAdapter extends MouseAdapter {
		/**
		 * Method to handle when the user presses down the button
		 */
		public void mousePressed(MouseEvent e) {
			int currX = e.getX();
			int currY = e.getY();

			// create an object of the current shape type
			try {
				Class shapeClass = Class.forName(currShapeType);
				currentShape = (Shape) shapeClass.newInstance();
			} catch (Exception ex) {
				System.err.println("Problem in creating a shape");
				ex.printStackTrace();
				System.exit(1);

			}

			// fill in point1 and point2 in the new shape
			currentShape.setPoint1Values(currX, currY);
			currentShape.setPoint2Values(currX + 1, currY + 1);

			// add the shape to the vector of shapes
			add(currentShape);

			// repaint all
			repaint();

		}

		/**
		 * Method to handle when the user releases the mouse
		 */
		public void mouseReleased(MouseEvent e) {
			int currX = e.getX();
			int currY = e.getY();

			// update the the point 2 values
			currentShape.setPoint2Values(currX, currY);

			// no current shape being dragged
			currentShape = null;

			// repaint
			repaint();

		}
	}

	/**
	 * Inner class for handling the mouse motion listener
	 */
	class MyMouseMotionAdapter extends MouseMotionAdapter {
		/**
		 * Method to handle the drag of a mouse
		 */
		public void mouseDragged(MouseEvent e) {
			int currX = e.getX();
			int currY = e.getY();

			// set the point 2 values
			currentShape.setPoint2Values(currX, currY);

			// repaint
			repaint();
		}
	}

}


