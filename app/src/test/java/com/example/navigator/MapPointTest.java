package com.example.navigator;


import com.example.navigator.utils.MapPoint;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;


import static org.junit.Assert.*;

public class MapPointTest {
    //Create all the map points
    private MapPoint root, almost_there, not_quite, midpoint, little_further, end, another_point, point_a, point_b, point_c, point_d;

    @Before
    public void makeMap()//Reset the map before each test
    {
        root = new MapPoint("STARTING POINT", "0");
        almost_there = new MapPoint("ALMOST THERE", "8");
        not_quite = new MapPoint("NOT QUITE", "7");
        midpoint = new MapPoint("MIDPOINT", "2");
        little_further = new MapPoint("LITTLE FURTHER", "6");
        end = new MapPoint("END", "1");
        another_point = new MapPoint("ANOTHER POINT", "3");
        point_a = new MapPoint("POINT A", "4");
        point_b = new MapPoint("POINT B", "5");
        point_c = new MapPoint("POINT C", "9");
        point_d = new MapPoint("POINT D", "10");

        //Add points to End
        end.addTwoWayPoint(not_quite, 9.5, 120);
        end.addTwoWayPoint(almost_there, 7.8, 240);

        //Add points to Almost_There
        almost_there.addTwoWayPoint(midpoint, 8.3, 150);

        //Add points to Midpoint
        midpoint.addTwoWayPoint(little_further, 9.6, 250);

        //Add points to Little Further
        little_further.addTwoWayPoint(another_point, 5.7, 250);

        //Add points to Another Point
        another_point.addTwoWayPoint(point_a, 8.6, 90);
        another_point.addTwoWayPoint(point_c, 10.1, 120);

        //Add points to Point A
        point_a.addTwoWayPoint(point_b, 9.3, 90);

        //Add points to Point C
        point_c.addTwoWayPoint(point_d, 20.2, 90);

        //Add points to Point B
        point_b.addTwoWayPoint(point_d, 3.5, 180);

        //Add points to root
        root.addTwoWayPoint(almost_there, 21.5, 30);
        root.addTwoWayPoint(not_quite, 10.5, 330);
        root.addTwoWayPoint(midpoint, 7.2, 120);
        root.addTwoWayPoint(little_further, 30.6, 180);
//        System.out.println("Setup Done!");

    }

    @Test
    public void distanceTo_GivenTwoPoints_ReturnsDistance()
    {
        assertWithMessage("DistanceFrom Root To AT is 21.5").that(root.distanceTo("8")).isEqualTo(21.5);
        assertWithMessage("DistanceFrom AT To Root is 21.5").that(almost_there.distanceTo("0")).isEqualTo(21.5);

    }

    @Test
    public void getDirectionsTo_GivenTwoPoints_ReturnsDirectionArray()
    {
        MapPoint[] directions = root.getDirectionsTo("2", 10);
        System.out.println("Test is: getDirectionsTo_GivenTwoPoints_ReturnsDirectionArray");
        System.out.println(MapPoint.flattenDirections(directions));
        System.out.println("");
        assertWithMessage("NumberPointsFrom Root to Midpoint").that(directions.length).isEqualTo(2);
        assertWithMessage("NextPointFrom Root to Midpoint Index = 0").that(directions[0].getName()).isEqualTo("STARTING POINT");
        assertWithMessage("NextPointFrom Root to Midpoint Index = 1").that(directions[1].getName()).isEqualTo("MIDPOINT");
    }

    @Test
    public void getDirectionsTo_GivenThreePointsWithLongCircuit_ReturnsDirectionArray()
    {
        MapPoint[] directions = root.getDirectionsTo("6", 10);
        System.out.println("Test is: getDirectionsTo_GivenThreePointsWithLongCircuit_ReturnsDirectionArray");
        System.out.println("Directions to 6 :" + MapPoint.flattenDirections(directions));
//        System.out.println(root.getNearbyLocations());
        System.out.println("");
        assertWithMessage("NumberPointsFrom Root to Little Further").that(directions.length).isEqualTo(3);
        assertWithMessage("NextPointFrom Root to Little Further Index = 0").that(directions[0].getName()).isEqualTo("STARTING POINT");
        assertWithMessage("NextPointFrom Root to Little Further Index = 1").that(directions[1].getName()).isEqualTo("MIDPOINT");
        assertWithMessage("NextPointFrom Root to Little Further Index = 2").that(directions[2].getName()).isEqualTo("LITTLE FURTHER");
    }
    @Test
    public void getDirectionsTo_GivenFivePoints_ReturnsDirectionArray()
    {
        MapPoint[] directions = root.getDirectionsTo("5", 10);
        System.out.println("Test is: getDirectionsTo_GivenFivePoints_ReturnsDirectionArray");
        System.out.println("Directions to 5 :" + MapPoint.flattenDirections(directions));
//        System.out.println(root.getNearbyLocations());
        System.out.println("");
        assertWithMessage("NumberPointsFrom Root to Point B").that(directions.length).isEqualTo(6);
        assertWithMessage("NextPointFrom Root to Point B Index = 0").that(directions[0].getName()).isEqualTo("STARTING POINT");
        assertWithMessage("NextPointFrom Root to Point B Index = 1").that(directions[1].getName()).isEqualTo("MIDPOINT");
        assertWithMessage("NextPointFrom Root to Point B Index = 2").that(directions[2].getName()).isEqualTo("LITTLE FURTHER");
        assertWithMessage("NextPointFrom Root to Point B Index = 3").that(directions[3].getName()).isEqualTo("ANOTHER POINT");
        assertWithMessage("NextPointFrom Root to Point B Index = 4").that(directions[4].getName()).isEqualTo("POINT A");
        assertWithMessage("NextPointFrom Root to Point B Index = 5").that(directions[5].getName()).isEqualTo("POINT B");
    }
    @Test
    public void getDirectionsTo_GivenNonExistent_ReturnsNull()
    {
        MapPoint[] directions = root.getDirectionsTo("30", 10);
        assertWithMessage("Directions to 30 are null").that(directions).isEqualTo(null);

    }
    @Test
    public void getDirectionsTo_GivenFullTree_ReturnsDirectionArray()
    {
        MapPoint[] directions = end.getDirectionsTo("10", 10);
        System.out.println("Test is: getDirectionsTo_GivenFullTree_ReturnsDirectionArray");
        System.out.println("Directions to 10 :" + MapPoint.flattenDirections(directions));
//        System.out.println(end.getNearbyLocations());
        System.out.println("");
        assertWithMessage("Directions to 10 contains 8 nodes").that(directions.length).isEqualTo(8);

    }
    @Test
    public void bearingTo_GivenTwoPoints_ReturnsBearing()
    {
        assertWithMessage("BearingFrom Root To AT is 21.5").that(root.getBearingTo("8")).isEqualTo(30);
        assertWithMessage("BearingFrom AT To Root is 21.5").that(almost_there.getBearingTo("0")).isEqualTo(210);

    }


}
