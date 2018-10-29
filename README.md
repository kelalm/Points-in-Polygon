# Points in Polygon

This is a RESTful service which accepts a POST request of JSON. The JSON entered here would be the (x,y) coordinates to define the perimeter of the polygon. Points on the polygon itself are not included in the count.

The points must be entered in order in (x,y) format. This program finds the number of (x,y) points inside of the polygon.

This program deals with a grid of at max 19x19, and the points entered must be 0 to 18 inclusive.

Erroneous input/malformed JSON is handled in this program
