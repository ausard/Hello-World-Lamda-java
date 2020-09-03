package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import helloworldlib.Library;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            String output = "";
            boolean check = new Library().someLibraryMethod();
            if (!check) {
                output = "<h1>Hello from Aliaksei Sabetski</h1>";
            } else {
                output = "<h1>Hello from AWS Lambda Function and aws-sam-cli</h1><br>";
                output = output.concat("<h2>HI String Concatenation by concat() method</h2>");
                output = output + "<div class=\"boxed\"><!-- 


                If you want to use Lorem Ipsum within another program please contact us for details
                on our API rather than parse the HTML below, we have XML and JSON available.
                
                
                 --><div id=\"lipsum\">
                <p>
                <ul>
                <li>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</li>
                <li>Vivamus porttitor nisi id velit facilisis, at tempus purus congue.</li>
                <li>Cras id augue imperdiet, sollicitudin dui sagittis, auctor est.</li>
                <li>Maecenas ut neque a risus fringilla pellentesque eu eget nunc.</li>
                <li>Phasellus at nulla iaculis ex semper ornare.</li>
                </ul>
                </p>
                <p>
                <ul>
                <li>Aliquam pretium ante sit amet est dignissim, fermentum varius magna pulvinar.</li>
                <li>Duis eleifend felis non viverra sodales.</li>
                </ul>
                </p>
                <p>
                <ul>
                <li>Donec lobortis ipsum a dictum bibendum.</li>
                <li>Maecenas vestibulum massa nec dolor interdum, in porttitor mauris dignissim.</li>
                </ul>
                </p>
                <p>
                <ul>
                <li>Quisque ut orci elementum, ullamcorper augue vitae, semper sapien.</li>
                <li>Mauris ullamcorper lectus nec augue viverra, sit amet pellentesque velit congue.</li>
                <li>Maecenas et lectus ac diam sollicitudin scelerisque.</li>
                </ul>
                </p>
                <p>
                <ul>
                <li>Maecenas sed felis nec tellus interdum consequat.</li>
                <li>Vivamus pellentesque dolor sed purus pulvinar commodo.</li>
                <li>Mauris varius mi nec ex dignissim auctor.</li>
                <li>Fusce pulvinar sem nec nunc convallis, in lacinia mauris ornare.</li>
                <li>Quisque tristique quam eu augue rhoncus, at cursus massa pellentesque.</li>
                </ul>
                </p></div>";
            }

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            return response
                    .withBody("<b>Error</b>")
                    .withStatusCode(500);
        }
    }
}
