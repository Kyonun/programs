/**
* Gabriella Garcia
* Program 4
**/

import org.junit.*;
public class Circle2Test 
{

    private Circle2 circle1;
    private Circle2 circle2;
    private Circle2 circle3;
   
	@Before
	public void setup()
	{
	   System.out.println("\nTest starting...");
	   circle1 = new Circle2(1,2,3);
	   circle2 = new Circle2(1,3,4);
	   circle3 = new Circle2(50,60,8);
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
	   p = circle1.moveBy(2,2);
	   Assert.assertTrue(p.x == 3 && p.y == 4);
	}
	
	@Test
	public void simpleMoveNeg()
	{
	   Point p;
	   System.out.println("Running test simpleMoveNeg.");
	   p = circle1.moveBy(-1,-1);
	   Assert.assertTrue(p.x == 0 && p.y == 1);
	}
	
	@Test
	public void scalePositive()
	{
	   System.out.println("Running test scalePositive.");
	   Assert.assertTrue(circle1.scale(10) == 30);
	}
	
	@Test
	public void scaleNegative()
	{
	   System.out.println("Running test scaleNegative.");
	   Assert.assertTrue(circle2.scale(0.5) == 2);
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
