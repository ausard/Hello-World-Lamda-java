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
                output = output.concat("<h2>HI String Concatenation by concat() method1</h2><br>");
                output = output.concat("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum lacus libero, iaculis viverra tincidunt eget, interdum in sapien. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla scelerisque ligula nec varius hendrerit. Mauris augue dui, egestas eget sem ac, luctus consectetur ligula.<p>" .
                 "Sed congue metus nec turpis posuere consequat. In mollis sapien et accumsan auctor. Praesent elit massa, imperdiet eget massa quis, bibendum tincidunt nibh. Cras pharetra nibh ante, nec rutrum urna mattis sit amet. Duis eu ipsum metus. Integer porta lacinia risus. Maecenas ullamcorper ante eu faucibus pretium. Donec lacinia lectus nulla. Integer sagittis efficitur ligula, non luctus nisl condimentum id.                Phasellus eget tempor quam, id euismod felis. Morbi non pellentesque diam. Nulla non ultrices erat, nec ultrices libero. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Curabitur elementum luctus feugiat. In tempus tortor id felis lacinia vulputate. Vivamus laoreet tempus tempus. Praesent tincidunt scelerisque porta. Proin ultricies sit amet leo vel faucibus. Mauris nec tincidunt dolor. Quisque vehicula arcu ac iaculis consectetur. Donec malesuada nec velit convallis molestie.");
                
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
