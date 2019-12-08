/**
* Gabriella Garcia
* Program 4 
*
* Example JUnit testing class for Circle1 (and Circle)
*
* - must have your classpath set to include the JUnit jarfiles
* - to run the test do:
*     java org.junit.runner.JUnitCore Circle1Test
* - note that the commented out main is another way to run tests
* - note that normally you would not have print statements in
*   a JUnit testing class; they are here just so you see what is
*   happening. You should not have them in your test cases.
**/

import org.junit.*;

public class Circle2Test
{
    private Circle1 circle1;
    private Circle1 circle2;
    private Circle1 circle3;
   

	@Before
	public void setup()
	{
	   System.out.println("\nTest starting...");
	   circle1 = new Circle1(5,6,7);
	   circle2 = new Circle1(5,7,8);
	   circle3 = new Circle1(45,20,6);
	}
	
	@After
	public void teardown()
	{
	   System.out.println("\nTest finished.");
	}
	
	@Test
	public void simpleMove()
	{
	   Point p;
	   System.out.println("Running test simpleMove.");
	   p = circle1.moveBy(1,1);
	   Assert.assertTrue(p.x == 6 && p.y == 7);
	}
	
	@Test
	public void simpleMoveNeg()
	{
	   Point p;
	   System.out.println("Running test simpleMoveNeg.");
	   p = circle1.moveBy(-1,-1);
	   Assert.assertTrue(p.x == 4 && p.y == 5);
	}
	
	@Test
	public void scalePositive()
	{
	   System.out.println("Running test scalePositive.");
	   Assert.assertTrue(circle1.scale(10) == 70);
	}
	
	@Test
	public void scaleNegative()
	{
	   System.out.println("Running test scaleNegative.");
	   Assert.assertTrue(circle2.scale(0.5) == 4);
	}
	
	@Test
	public void intersect()
	{
		System.out.println("Running intersect test");
		System.out.println(circle1.intersects(circle2));
		Assert.assertTrue(circle1.intersects(circle2));
	}
	
	@Test
	public void noIntersect() 
	{
		System.out.println("Running intersect test");
		Assert.assertFalse(circle1.intersects(circle3));
	}

}
