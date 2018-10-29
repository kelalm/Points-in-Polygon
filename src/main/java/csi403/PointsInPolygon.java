//Kelvin Almonte

package csi403;


// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

import java.awt.Polygon;


// Extend HttpServlet class
public class PointsInPolygon extends HttpServlet {

  // Standard servlet method
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
    try{
        doService(request, response);
    }   catch (Exception e){
            PrintWriter out = response.getWriter();
            out.println("{ " + "\"message\" : " + "\"Malformed JSON\"" + "}");
    }
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }


  // Our main worker method
  // Parses messages e.g. {"inList" : [5, 32, 3, 12]}
  // Returns the list reversed.
  private void doService(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }

      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);

      // Get the singular JSON object (name:value pair) in this message.
      JsonObject obj = reader.readObject();
      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");


      Boolean validInList = true;

      //store inArray size from inList

      int numberOfPoints = inArray.size();

      //used to check if there are at least 3 input points
      if(numberOfPoints < 3){
          validInList = false;
      }

      Point[] points = new Point[numberOfPoints];

      JsonObject line = inArray.getJsonObject(0);

      Polygon myShape  = new Polygon();

      int minX = 0;
      int minY = 0;
      int maxX = 0;
      int maxY = 0;

      int counter = 0;

      //copy inList into the Point array with x and y values,
      //as a point object.

      for(int i = 0; i < numberOfPoints; i++){

        //fetch the values associated with x and y
        //from the JsonObject called line.

        line = inArray.getJsonObject(i);
        int xPointEntered = line.getInt("x");
        int yPointEntered = line.getInt("y");

        //store it in the array as a Point object.
        points[i] = new Point(xPointEntered, yPointEntered);

      }

      //add the points to our polygon
      for(int i = 0; i < points.length; i++) {
          myShape.addPoint(points[i].x, points[i].y);
      }

      //set the min x and y values to the
      //first values in the point array

      minX = points[0].x;
      minY = points[0].y;

      //find min and max x and y values

      for(int m = 0; m < points.length; m++){
          //find the min X variable
          if(points[m].x < minX){
              minX = points[m].x;
          }

          //find the min Y variable
          if(points[m].y < minY){
              minY = points[m].y;
          }

          //find the max X variable
          if(points[m].x > maxX){
              maxX = points[m].x;
          }

          //find the max Y variable
          if(points[m].y > maxY){
              maxY = points[m].y;
          }
      }

      //X and Y values are bounded from 0 to 18 inclusive.

      if(minX < 0 || minY < 0){
          validInList = false;
      }

      if(maxX > 18 || maxY > 18){
          validInList = false;
      }

      //test all points 1-19 on (x,y)

      if(validInList){

          for(int i = 0; i < 19; i++) {
              for(int j = 0; j < 19; j++) {

                  Point currentTestPoint = new Point(i,j);

                  //use the contains method from the Polygon class as well as
                  //adding another filter to make sure that the point is inside
                  //of the polygon, using the min and max x and y values.

                  if(myShape.contains(currentTestPoint.x, currentTestPoint.y)){
                      if(currentTestPoint.x > minX && currentTestPoint.y > minY && currentTestPoint.x < maxX && currentTestPoint.y < maxY){
                          counter++;
                      }
                  }
              }
          }

          // Set response content type to be JSON
          response.setContentType("application/json");
          // Send back the response JSON message
          PrintWriter out = response.getWriter();
          out.println("{ \"count\" : " + counter + " }");

      } else {
        PrintWriter out = response.getWriter();
        out.println("{ " + "\"message\" : " + "\"Malformed JSON\"" + "}");
      }

  }


  // Standard Servlet method
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }

}

//Class for point objects, which hold x and y data.
class Point {

    public int x = 0;
    public int y = 0;

    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

}
