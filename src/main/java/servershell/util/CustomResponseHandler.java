package servershell.util;


import java.io.IOException;

import org.apache.http.annotation.Immutable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.EntityUtils;

/**
 * A {@link ResponseHandler} that returns the response body as a String
 * for successful (2xx) responses. If the response code was >= 300, the response
 * body is consumed and an {@link HttpResponseException} is thrown.
 *
 * If this is used with
 * {@link org.apache.http.client.HttpClient#execute(
 *  org.apache.http.client.methods.HttpUriRequest, ResponseHandler)},
 * HttpClient may handle redirects (3xx responses) internally.
 *
 *
 * @since 4.0
 */
@Immutable
public class CustomResponseHandler  implements ResponseHandler<String> {

	/**
	 * Returns the response body as a String if the response was successful (a
	 * 2xx status code). If no response body exists, this returns null. If the
	 * response was unsuccessful (>= 300 status code), throws an
	 * {@link HttpResponseException}.
	 */
	public String handleResponse(final HttpResponse response)
			throws HttpResponseException, IOException {
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		String entityString = (entity == null) ? null : EntityUtils.toString(entity);
		
		if (statusLine.getStatusCode() >= 300) {
			String serverStack = "";
			if(entityString.length() > 1450){
				serverStack = entityString.substring(0, 1450);
			}else{
				serverStack = entityString;
			}
			throw new HttpResponseException(statusLine.getStatusCode(),
					statusLine.getReasonPhrase()+"    "+serverStack);
		}

		return entityString;
	}

}